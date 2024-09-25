package io.quantum.trading.brokers.indian;

import static com.zerodhatech.kiteconnect.utils.Constants.*;
import static java.lang.Double.parseDouble;

import com.neovisionaries.ws.client.OpeningHandshakeException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.PermissionException;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.TokenException;
import com.zerodhatech.models.*;
import com.zerodhatech.ticker.KiteTicker;
import com.zerodhatech.ticker.OnError;
import com.zerodhatech.ticker.OnOrderUpdate;
import io.quantum.trading.brokers.*;
import io.quantum.trading.entities.BrokerConfig;
import io.quantum.trading.entities.BrokerConfigDetails.ZerodhaBrokerConfigDetails;
import io.quantum.trading.entities.NseScriptMetadata;
import io.quantum.trading.entities.NseScriptMetadata.Segment;
import io.quantum.trading.enums.TimeFrame;
import io.quantum.trading.exception.ApiError;
import io.quantum.trading.patterns.Candle;
import io.quantum.trading.provider.DataProvidable;
import io.quantum.trading.provider.HistoricalDataProvidable;
import io.quantum.trading.provider.NseMetadataProvidable;
import io.quantum.trading.util.OmsUtil;
import io.quantum.trading.util.RetryUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/*When websocket is disconnected it will reconnect automatically & subscribe to the scripts
 *, a method will fetch balance for period of every minute to check if the connection is alive
 *& reconnect on need basis*/
@Slf4j
public class ZerodhaBroker extends NseBroker
    implements NseMetadataProvidable, DataProvidable, HistoricalDataProvidable {
  private static final String TAG_PREFIX = "QUANTUM_BOT";
  private static final String DUMMY_TRADE = "NO_TRADE";

  // TODO Track this for random AOT issue https://github.com/oracle/graal/issues/6456
  public static final int TOO_MANY_REQUESTS_WAIT_TIME =
      1000; // getDefault().create().nextInt(100, 1000);
  private KiteConnect kite;
  private KiteTicker tickerProvider;
  private final Supplier<BrokerConfig> brokerConfigSupplier;
  private BrokerConfig brokerConfig;
  private OnOrderUpdate onOrderUpdateHandler;
  private final ScheduledExecutorService reconnectTimer =
      Executors.newSingleThreadScheduledExecutor(Thread.ofPlatform().factory());
  private List<Instrument> instruments;
  private Map<String, Map<String, String>> exchangeTokenBrokerMetadataCache;
  private Map<String, Long> subscribedScripts = new HashMap<>();
  private Function<List<LiveTick>, Void> liveTickConsumer;
  private final String orderTypeSuffix = "Nse";
  private boolean kiteConnected = false;
  private final BlockingQueue<List<Tick>> liveTicksHolder = new LinkedBlockingQueue<>(1000);
  private Thread liveTicksAsyncPublisher;
  private static final String LIVE_FEED_LOCK = "LIVE_FEED_LOCK";
  private static final String LIVE_FEED_SUBSCRIBE_LOCK = "LIVE_FEED_SUBSCRIBE_LOCK";

  public ZerodhaBroker(Supplier<BrokerConfig> brokerConfigSupplier) {
    this.brokerConfigSupplier = brokerConfigSupplier;
    init();
    initReconnect();
  }

  public static String generateAccessToken(String requestToken, String apiKey, String apiSecret)
      throws IOException, KiteException {
    var kiteConnect = new KiteConnect(apiKey);
    return kiteConnect.generateSession(requestToken, apiSecret).accessToken;
  }

  private void init() {
    brokerConfig = brokerConfigSupplier.get();
    var brokerConfigDetails = (ZerodhaBrokerConfigDetails) brokerConfig.getBrokerConfigDetails();
    kite = new KiteConnect(brokerConfigDetails.getApiKey());
    kite.setAccessToken(brokerConfigDetails.getAccessToken());

    var connectionInProgress = new AtomicBoolean(true);
    // Set session expiry callback.
    kite.setSessionExpiryHook(
        () -> {
          if (!connectionInProgress.get()) {
            log.warn(
                "[{} - {}] Zerodha session has expired. Please update the database and login again",
                brokerConfig.getDisplayName(),
                brokerConfigDetails.getUserId());
            kiteConnected = false;
            init();
          }
        });
    checkReconnectTask(false);
    connectionInProgress.set(false);
  }

  private void initReconnect() {
    reconnectTimer.scheduleAtFixedRate(
        () -> checkReconnectTask(true), 0, 60_000, TimeUnit.MILLISECONDS);
  }

  private void checkReconnectTask(boolean autoRetry) {
    if (kite == null) {
      return;
    }

    while (true) {
      try {
        getBalance();
        if (!kiteConnected) {
          kiteConnected = true;
          log.info(
              "[{} - {}] Kite connected",
              brokerConfig.getDisplayName(),
              ((ZerodhaBrokerConfigDetails) brokerConfig.getBrokerConfigDetails()).getUserId());
        }
        return;
      } catch (Throwable t) {
        if (t instanceof BrokerException brokerException) {
          if ((brokerException.getCause() instanceof TokenException tokenException
                  && tokenException.code == 403)
              || (brokerException.getCause() instanceof PermissionException permissionException
                  && permissionException.code == 403)) {
            kiteConnected = false;
            if (autoRetry) {
              init();
            }
            return;
          } else {
            log.error("", t);
            try {
              Thread.sleep(500);
            } catch (InterruptedException e) {
              // ignore
            }
            continue;
          }
        }
      }
    }
  }

  private List<Instrument> getInstruments() {
    if (instruments == null) {
      try {
        instruments = kite.getInstruments();
      } catch (KiteException | IOException e) {
        throw new RuntimeException(e);
      }
    }
    return instruments;
  }

  @Override
  public List<NseScriptMetadata> getMetadata() {
    return getInstruments().stream().map(this::getNseScriptMetadata).toList();
  }

  private NseScriptMetadata getNseScriptMetadata(Instrument instrument) {
    var segment = getSegment(instrument);
    return new NseScriptMetadata(
        instrument.getName(),
        instrument.getTradingsymbol(),
        instrument.getExchange(),
        segment,
        Double.parseDouble(instrument.getStrike()),
        instrument.getExpiry() == null
            ? null
            : new DateTime(instrument.getExpiry()).minusHours(5).minusMinutes(30).toDate(),
        String.valueOf(instrument.getExchange_token()),
        instrument.getLot_size(),
        getFreezeLimit(instrument.getName()));
  }

  private Segment getSegment(Instrument instrument) {
    var segment = instrument.getSegment();
    var instrumentType = instrument.getInstrument_type();

    return switch (segment) {
      case "INDICES" -> Segment.INDICES;
      case "BSE" -> Segment.BSE;
      case "NSE" -> Segment.NSE;
      case "NFO-FUT" -> Segment.NFO_FUT;
      case "NFO-OPT" -> instrumentType.equals("CE") ? Segment.NFO_CE : Segment.NFO_PE;
      case "BCD-OPT" -> instrumentType.equals("CE") ? Segment.BCD_CE : Segment.BCD_PE;
      case "BCD-FUT" -> Segment.BCD_FUT;
      case "BFO-OPT" -> instrumentType.equals("CE") ? Segment.BFO_CE : Segment.BFO_PE;
      case "BFO-FUT" -> Segment.BFO_FUT;
      case "CDS-FUT" -> Segment.CDS_FUT;
      case "CDS-OPT" -> instrumentType.equals("CE") ? Segment.CDS_CE : Segment.CDS_PE;
      case "MCX-FUT" -> Segment.MCX_FUT;
      case "MCX-OPT" -> instrumentType.equals("CE") ? Segment.MCX_CE : Segment.MCX_PE;
      case "NCO" -> Segment.NCO;
      case "NCO-FUT" -> Segment.NCO_FUT;
      case "NCO-OPT" -> instrumentType.equals("CE") ? Segment.NCO_CE : Segment.NCO_PE;
      default ->
          throw new IllegalStateException(
              String.format(
                  "Unexpected value: %s for script, %s", segment, instrument.getTradingsymbol()));
    };
  }

  @Override
  public String getBrokerToken(NseScriptMetadata nseScriptMetadata) {
    loadBrokerMetadata();
    var segment = nseScriptMetadata.getSegment();
    var exchange = nseScriptMetadata.getId().getExchange();

    var metadataMapperKey = nseScriptMetadata.getExchangeToken();
    if (segment.equals(Segment.INDICES)) metadataMapperKey = metadataMapperKey + "_" + segment;
    else metadataMapperKey = metadataMapperKey + "_" + exchange;

    if (!exchangeTokenBrokerMetadataCache.containsKey(metadataMapperKey)) {
      throw new IllegalArgumentException("Token not found for script, " + metadataMapperKey);
    }
    return exchangeTokenBrokerMetadataCache.get(metadataMapperKey).get("token");
  }

  private void loadBrokerMetadata() {
    if (exchangeTokenBrokerMetadataCache == null || exchangeTokenBrokerMetadataCache.isEmpty()) {
      exchangeTokenBrokerMetadataCache = new HashMap<>();
      getInstruments()
          .forEach(
              instrument -> {
                Map<String, String> brokerConfigsMap = new HashMap<>();
                brokerConfigsMap.put("token", String.valueOf(instrument.getInstrument_token()));
                brokerConfigsMap.put("script_name", instrument.getTradingsymbol());
                if (instrument.segment.equals(Segment.INDICES.name()))
                  exchangeTokenBrokerMetadataCache.put(
                      instrument.getExchange_token() + "_" + Segment.INDICES, brokerConfigsMap);
                else
                  exchangeTokenBrokerMetadataCache.put(
                      String.valueOf(instrument.getExchange_token()) + "_" + instrument.exchange,
                      brokerConfigsMap);
              });
    }
  }

  @Override
  public String getBrokerScriptName(NseScriptMetadata nseScriptMetadata) {
    loadBrokerMetadata();
    var segment = nseScriptMetadata.getSegment();
    var exchange = nseScriptMetadata.getId().getExchange();

    var metadataMapperKey = nseScriptMetadata.getExchangeToken();
    if (segment.equals(Segment.INDICES)) metadataMapperKey = metadataMapperKey + "_" + segment;
    else metadataMapperKey = metadataMapperKey + "_" + exchange;
    if (!exchangeTokenBrokerMetadataCache.containsKey(metadataMapperKey)) {
      throw new IllegalArgumentException("ScriptName not found for script, " + metadataMapperKey);
    }
    return exchangeTokenBrokerMetadataCache.get(metadataMapperKey).get("script_name");
  }

  @Override
  public void startFeed(Function<List<LiveTick>, Void> liveTickConsumer) {
    this.liveTickConsumer = liveTickConsumer;
    // Kite Ticker class uses e.printstacktrace() which writes to stderr. Yet we catch these
    // exceptions properly. So, skipping the noises
    System.setErr(
        new PrintStream(
            new OutputStream() {
              public void write(int b) {}
            }));
    initLiveFeed();
  }

  @Override
  public void disconnectFeed() {
    if (tickerProvider != null) {
      tickerProvider.setOnConnectedListener(() -> tickerProvider.disconnect());
      tickerProvider.setOnTickerArrivalListener(ticks -> {});
      tickerProvider.setOnDisconnectedListener(
          () -> log.info("Websocket disconnected and won't re-connect automatically"));
      tickerProvider.setOnErrorListener(
          new OnError() {
            @Override
            public void onError(Exception exception) {
              log.error("Exception. But already disconnected", exception);
            }

            @Override
            public void onError(KiteException kiteException) {
              log.error("KiteException. But already disconnected", kiteException);
            }

            @Override
            public void onError(String error) {
              log.error("Error - {}. But already disconnected", error);
            }
          });
      tickerProvider.disconnect();
    }
  }

  private void initLiveFeed() {
    synchronized (LIVE_FEED_LOCK) {
      var connectionInProgress = new AtomicBoolean(false);
      if (tickerProvider == null || !tickerProvider.isConnectionOpen()) {
        // Websocket
        tickerProvider = new KiteTicker(kite.getAccessToken(), kite.getApiKey());
        try {
          tickerProvider.setMaximumRetries(Integer.MAX_VALUE);
          tickerProvider.setMaximumRetryInterval(5);
          tickerProvider.setTryReconnection(true);
          if (onOrderUpdateHandler != null) {
            tickerProvider.setOnOrderUpdateListener(onOrderUpdateHandler);
          }

          setupLiveTickListener();
          tickerProvider.setOnConnectedListener(
              () -> {
                synchronized (LIVE_FEED_SUBSCRIBE_LOCK) {
                  log.info("Websocket connected");
                  var tokens = new ArrayList<>(subscribedScripts.values());
                  tickerProvider.unsubscribe(tokens);
                  tickerProvider.subscribe(tokens);
                  tickerProvider.setMode(tokens, KiteTicker.modeFull);
                }
              });
          tickerProvider.setOnDisconnectedListener(
              () -> {
                log.info("Websocket disconnected. Re-connecting...");
                disconnectFeed();
                initLiveFeed();
              });
          tickerProvider.setOnErrorListener(
              new OnError() {
                @Override
                public void onError(Exception e) {
                  if (e instanceof OpeningHandshakeException openingHandshakeException) {
                    if (openingHandshakeException.getStatusLine().getStatusCode() == 403) {
                      if (connectionInProgress.get()) {
                        // KiteTicker.connect() method calls onError() and the Timer thread calls
                        // onError(). We have to ignore the KiteTicker.connect() onError event.
                        log.debug("Received 403 on websocket. Ignoring as connection in progress");
                        return;
                      }
                      if (kiteConnected) {
                        log.error("Received 403 on websocket. Re-connecting...");
                        disconnectFeed();
                        initLiveFeed();
                      } else {
                        log.error(
                            "Received 403 on websocket. Since Kite api is not connected, ignoring");
                      }
                    } else {
                      log.error("Received openingHandshakeException", e);
                    }
                  } else {
                    log.error("Exception in socket listener", e);
                  }
                }

                @Override
                public void onError(KiteException kiteException) {
                  log.error(
                      "Kite Exception in socket listener - {} with code - {}",
                      kiteException.message,
                      kiteException.code);
                }

                @Override
                public void onError(String error) {
                  log.error("Error in socket listener: {}", error);
                }
              });
          connectionInProgress.set(true);
          tickerProvider.connect();
          connectionInProgress.set(false);
          DataProvidable.super.reconnectFeed();
        } catch (KiteException e) {
          log.error(getErrMsg(e), e);
        }
      }
    }
  }

  private void setupLiveTickListener() {
    tickerProvider.setOnTickerArrivalListener(
        ticks -> {
          try {
            if (!ticks.isEmpty()) {
              liveTicksHolder.put(ticks);
            } else {
              log.debug("Ticks is empty");
            }
          } catch (InterruptedException e) {
            // ignore
            log.error("", e);
          }
        });

    if (liveTicksAsyncPublisher == null) {
      liveTicksAsyncPublisher =
          Thread.ofPlatform()
              .name("live-ticks-async-publisher")
              .start(
                  () -> {
                    while (true) {
                      try {
                        var currLiveTicks = liveTicksHolder.take();
                        var liveTicks =
                            currLiveTicks.stream()
                                .filter(tick -> tick.getTickTimestamp() != null)
                                .map(
                                    tick -> {
                                      try {
                                        var ltp = tick.getLastTradedPrice();
                                        var exchangeScripts =
                                            scriptsTokenSymbolMap.get(tick.getInstrumentToken());
                                        return exchangeScripts.stream()
                                            .map(
                                                exchangeScript ->
                                                    new LiveTick(
                                                        exchangeScript.getExchange()
                                                            + ":"
                                                            + exchangeScript.getScript(),
                                                        tick.getTickTimestamp().getTime(),
                                                        new Date(),
                                                        ltp,
                                                        ltp,
                                                        ltp,
                                                        tick.getVolumeTradedToday()))
                                            .toList();
                                      } catch (Throwable t) {
                                        log.error("", t);
                                        throw t;
                                      }
                                    })
                                .flatMap(Collection::stream)
                                .toList();
                        liveTickConsumer.apply(liveTicks);
                      } catch (InterruptedException e) {
                        log.error("live-ticks-async-publisher thread interrupted");
                      }
                    }
                  });
    }
  }

  @Override
  public void subscribe(String exchangeScript) {
    synchronized (LIVE_FEED_SUBSCRIBE_LOCK) {
      var script = OmsUtil.getScript(exchangeScript);
      var exchange = OmsUtil.getExchange(exchangeScript);
      if (!scriptMetadataMap.containsKey(
          new NseScriptMetadata.NseMetadataId(script, Exchange.valueOf(exchange)))) {
        log.error("{} is not found in metadata", exchangeScript);
        return;
      }
      var scriptToken =
          Long.parseLong(
              scriptMetadataMap
                  .get(new NseScriptMetadata.NseMetadataId(script, Exchange.valueOf(exchange)))
                  .getBrokerScriptIdMap()
                  .get(BrokerConfig.BrokerType.ZERODHA));
      var tokensToSubscribe = new ArrayList<Long>();
      tokensToSubscribe.add(scriptToken);
      if (tickerProvider.isConnectionOpen()) {
        tickerProvider.subscribe(tokensToSubscribe);
        tickerProvider.setMode(tokensToSubscribe, KiteTicker.modeFull);
      }
      subscribedScripts.put(exchangeScript, scriptToken);
    }
  }

  @Override
  public void unSubscribe(String script) {
    synchronized (LIVE_FEED_SUBSCRIBE_LOCK) {
      if (subscribedScripts.containsKey(script)) {
        // Do not get from scriptMetadataMap bcoz in case the metadata changes, the old scripts and
        // it's tokens will not be available
        var scriptToken = subscribedScripts.get(script);
        var tokensToUnSubscribe = new ArrayList<Long>();
        tokensToUnSubscribe.add(scriptToken);
        if (tickerProvider.isConnectionOpen()) {
          tickerProvider.unsubscribe(tokensToUnSubscribe);
        }
        subscribedScripts.remove(script);
      }
    }
  }

  @Override
  public void reconnectFeed() {
    init();
    initLiveFeed();
  }

  @Override
  public boolean testConnection() throws BrokerException {
    try {
      getBalance();
      return true;
    } catch (Throwable t) {
      log.warn(
          "Getting balance failed with exception. Concluding that the broker is not logged in", t);
      return false;
    }
  }

  @Override
  public void closeOrder(String orderId) throws BrokerException {}

  @Override
  public BrokerOrderDetail getOrderDetails(String orderId) throws BrokerException {
    return getBrokerOrderDetail(getOrderDetail(orderId), 0);
  }

  @Override
  public BrokerOrderDetail placeOrder(BrokerPlaceOrderRequest brokerPlaceOrderRequest)
      throws BrokerException {
    switch (brokerPlaceOrderRequest.getType()) {
      case BrokerOrderType.REGULAR_BUY_NSE -> {
        var orderRequest = (BrokerPlaceOrderRequest.RegularBuyNse) brokerPlaceOrderRequest;
        var script = OmsUtil.getScript(orderRequest.getExchangeScript());
        var exchange = OmsUtil.getExchange(orderRequest.getExchangeScript());
        var res =
            placeOrder(
                script,
                orderRequest.getQuantity(),
                "BUY",
                orderRequest.getPrice(),
                exchange,
                orderRequest.getProductType());
        return getBrokerOrderDetail(getOrderDetail(res), orderRequest.getPrice());
      }
      case BrokerOrderType.REGULAR_SELL_NSE -> {
        var orderRequest = (BrokerPlaceOrderRequest.RegularSellNse) brokerPlaceOrderRequest;
        var script = OmsUtil.getScript(orderRequest.getExchangeScript());
        var exchange = OmsUtil.getExchange(orderRequest.getExchangeScript());
        //        if(!orderRequest.getProductType().equals("CNC")){
        var res =
            placeOrder(
                script,
                orderRequest.getQuantity(),
                "SELL",
                orderRequest.getPrice(),
                exchange,
                orderRequest.getProductType());
        return getBrokerOrderDetail(getOrderDetail(res), orderRequest.getPrice());
        //      }
        //        else throw new BrokerException(ApiError.INVALID_PLACE_REQUEST);
      }
      default ->
          throw new IllegalArgumentException(
              brokerPlaceOrderRequest.getType() + " orderType not available");
    }
  }

  private BrokerOrderDetail getBrokerOrderDetail(Order order, double requestedPrice) {
    try {
      switch (order.transactionType) {
        case "BUY" -> {
          return BrokerOrderDetail.RegularBuyNse.builder()
              .quantity(Double.parseDouble(order.quantity))
              .stopLoss(0)
              .takeProfit(0)
              .openTs(new DateTime(order.orderTimestamp).minusHours(5).minusMinutes(30).getMillis())
              .closeTs(
                  (order.orderTimestamp != null)
                      ? new DateTime(order.orderTimestamp)
                          .minusHours(5)
                          .minusMinutes(30)
                          .getMillis()
                      : 0)
              .orderUpdateTs(0)
              .type(BrokerOrderType.findEnumByValue("RegularBuy" + orderTypeSuffix))
              .exchangeScript(order.exchange + ":" + order.tradingSymbol)
              .orderId(order.orderId)
              .orderStatus(BrokerOrderStatus.findEnumByValue(order.status))
              .avgOpenPrice(parseDouble(order.averagePrice))
              .price(requestedPrice)
              .productType(order.product)
              .avgClosePrice(0)
              .profitAndLoss(BigDecimal.valueOf(0))
              .filledQuantity(Double.parseDouble(order.filledQuantity))
              .tag(order.tag)
              .build();
        }
        case "SELL" -> {
          return BrokerOrderDetail.RegularSellNse.builder()
              .quantity(Double.parseDouble(order.quantity))
              .stopLoss(0)
              .takeProfit(0)
              .openTs(new DateTime(order.orderTimestamp).minusHours(5).minusMinutes(30).getMillis())
              .closeTs(
                  (order.orderTimestamp != null)
                      ? new DateTime(order.orderTimestamp)
                          .minusHours(5)
                          .minusMinutes(30)
                          .getMillis()
                      : 0)
              .orderUpdateTs(0)
              .type(BrokerOrderType.findEnumByValue("RegularSell" + orderTypeSuffix))
              .exchangeScript(order.exchange + ":" + order.tradingSymbol)
              .orderId(order.orderId)
              .orderStatus(BrokerOrderStatus.findEnumByValue(order.status))
              .avgOpenPrice(parseDouble(order.averagePrice))
              .price(requestedPrice)
              .productType(order.product)
              .avgClosePrice(0)
              .profitAndLoss(BigDecimal.valueOf(0))
              .filledQuantity(Double.parseDouble(order.filledQuantity))
              .tag(order.tag)
              .build();
        }
        default -> throw new RuntimeException("Error in mapping.." + order.transactionType);
      }
    } catch (Exception e) {
      throw new RuntimeException("Error in formatting - " + order.orderId, e);
    }
  }

  @Override
  public BrokerOrderDetail modifyOrder(BrokerModifyOrderRequest brokerModifyOrderRequest)
      throws BrokerException {
    return null;
  }

  private List<Candle> getHistoricalData(
      String instrumentToken, DateTime from, DateTime to, TimeFrame timeFrame) {
    var interval =
        switch (timeFrame) {
          case TimeFrame.MINUTE_1 -> "minute";
          case TimeFrame.MINUTE_3 -> "3minute";
          case TimeFrame.MINUTE_5 -> "5minute";
          case TimeFrame.MINUTE_10 -> "10minute";
          case TimeFrame.MINUTE_15 -> "15minute";
          case TimeFrame.MINUTE_30 -> "30minute";
          case TimeFrame.HOUR_1 -> "60minute";
          default -> throw new RuntimeException("Timeframe, " + timeFrame + " is not supported");
        };
    HistoricalData todayData;
    while (true) {
      try {
        log.debug("Getting data from, {} to, {}", from, to);
        todayData =
            kite.getHistoricalData(
                from.toDate(), to.toDate(), instrumentToken, interval, false, true);
        return todayData.dataArrayList.stream()
            .map(historicalData -> createCandleFromHistoricalData(historicalData, timeFrame))
            .collect(Collectors.toList());
      } catch (Throwable e) {
        if (e instanceof KiteException k) {
          log.error(
              "Failed to get historical data for token - {}; from - {}; to - {} with error, {}",
              instrumentToken,
              from,
              to,
              k.message,
              e);
        } else {
          log.error(
              "Failed to get historical data for token - {}; from - {}; to - {}",
              instrumentToken,
              from,
              to,
              e);
        }
      }
    }
  }

  @Override
  public double getBalance() {
    try {
      var marginVal =
          kite.getMargins().values().stream().mapToDouble(margin -> parseDouble(margin.net)).sum();
      var posVal = kite.getPositions().get("net").stream().mapToDouble(pos -> pos.unrealised).sum();
      var holdingsVal =
          kite.getHoldings().stream().mapToDouble(holding -> holding.averagePrice).sum();
      return marginVal + posVal + holdingsVal;
    } catch (Throwable e) {
      throw new BrokerException(e, ApiError.BROKER_GET_BALANCE_FAILURE, getErrMsg(e));
    }
  }

  public String buyShares(
      String tradingSymbol,
      double quantity,
      double limitPrice,
      String exchange,
      String productType) {
    if (quantity == 0) return DUMMY_TRADE;
    return placeOrder(
        tradingSymbol, quantity, TRANSACTION_TYPE_BUY, limitPrice, exchange, productType);
  }

  public String sellShares(
      String tradingSymbol,
      double quantity,
      double limitPrice,
      String exchange,
      String productType) {
    if (quantity == 0) return DUMMY_TRADE;
    return placeOrder(
        tradingSymbol, quantity, TRANSACTION_TYPE_SELL, limitPrice, exchange, productType);
  }

  public String modifyOrder(
      String orderId, double limitPrice, double triggerPrice, String orderType, double qty) {
    var orderDetail = getOrderDetail(orderId);
    var orderStatus = BrokerOrderStatus.findEnumByValue(orderDetail.status);
    if (orderStatus == BrokerOrderStatus.CANCELLED
        || orderStatus == BrokerOrderStatus.REJECTED
        || orderStatus == BrokerOrderStatus.COMPLETE) {
      throw new BrokerException(ApiError.ORDER_CANNOT_MODIFY, orderId, orderStatus);
    }
    OrderParams orderParams = new OrderParams();
    orderParams.price = limitPrice;
    orderParams.triggerPrice = triggerPrice;
    orderParams.orderType = orderType;
    orderParams.quantity = (int) qty;
    Function<Void, RetryUtils.Output<String>> modifyOrderFunc =
        (unused) -> {
          try {
            kite.modifyOrder(orderId, orderParams, VARIETY_REGULAR);
            return new RetryUtils.Output<>(false, orderId);
          } catch (Throwable t) {
            if (t instanceof KiteException ke && ke.code == 429) {
              try {
                Thread.sleep(1000);
              } catch (InterruptedException ex) {
                // ignore
              }
              log.warn(
                  "Modify order received 429 from Zerodha. Cancelling and placing the order again");
              cancelOrder(orderId);
              return new RetryUtils.Output<>(
                  false,
                  placeOrder(
                      orderDetail.tradingSymbol,
                      qty,
                      orderDetail.transactionType,
                      limitPrice,
                      orderDetail.exchange,
                      orderDetail.product));
            }
            throw new BrokerException(t, ApiError.BROKER_MODIFY_ORDER, getErrMsg(t));
          }
        };
    return RetryUtils.runWithRetries(
        modifyOrderFunc, null, 10, this::getErrMsg, "Zerodha modify order");
  }

  /**
   * Always send a tag with a unique identifier
   *
   * @param tradingSymbol This is the script name
   * @param quantity Quantity of shares to buy
   * @param transactionType Buy/Sell
   * @param tag Some random comments for future reference
   * @param limitPrice Value of limit price
   * @param exchange NSE/BSE
   * @param productType MIS/CNC/LONG TERM
   * @return orderId
   */
  public Order placeOrderBroker(
      String tradingSymbol,
      double quantity,
      String transactionType,
      String tag,
      double limitPrice,
      String exchange,
      String productType) {
    OrderParams orderParams = new OrderParams();
    orderParams.exchange = exchange;
    orderParams.tradingsymbol = tradingSymbol;
    orderParams.transactionType = transactionType;
    orderParams.quantity = (int) quantity;
    orderParams.product = productType;
    orderParams.tag = tag;
    orderParams.price = limitPrice;
    var orderType = limitPrice == 0.0 ? ORDER_TYPE_MARKET : ORDER_TYPE_LIMIT;
    orderParams.orderType = orderType;
    var variety = VARIETY_REGULAR;

    Function<Void, RetryUtils.Output<Order>> placeOrderFunction =
        (unused) -> {
          try {
            log.info(
                "Placing order for tradingSymbol - {}; transactionType - {}; quantity - {}; tag - {}",
                tradingSymbol,
                transactionType,
                quantity,
                tag);
            return new RetryUtils.Output<>(false, kite.placeOrder(orderParams, variety));
          } catch (Throwable t) {
            if (t instanceof KiteException ke && ke.code == 429) {
              try {
                Thread.sleep(1000);
              } catch (InterruptedException ex) {
                // ignore
              }
              log.warn(
                  "Place order received 429 from Zerodha. Cancelling and placing the order again");
              return new RetryUtils.Output<>(true, null);
            }
            var errMsg = getErrMsg(t);
            try {
              var orderOpt =
                  getOrders().stream()
                      .filter(
                          order ->
                              order.exchange.equals(exchange)
                                  && order.tradingSymbol.equals(tradingSymbol)
                                  && order.transactionType.equals(transactionType)
                                  && parseDouble(order.quantity) == quantity
                                  && order.product.equals(productType)
                                  && (order.tag == null || order.tag.equals(tag))
                                  && parseDouble(order.price) == limitPrice
                                  && order.orderType.equals(orderType)
                                  && order.orderVariety.equals(variety))
                      .findAny();
              if (orderOpt.isPresent()) {
                log.error(
                    "Failed while placing order. But found similar order in the broker. ErrMsg - {}",
                    errMsg,
                    t);
                return new RetryUtils.Output<>(false, orderOpt.get());
              } else {
                // This is thrown when order is not found in broker. RetryUtils will retry to place
                // the order again
                throw new BrokerException(t, ApiError.ORDER_CANNOT_PLACE, errMsg);
              }
            } catch (BrokerException ex) {
              throw new BrokerException(ex, ex.getApiError(), false);
            }
          }
        };

    return RetryUtils.runWithRetries(
        placeOrderFunction,
        null,
        10,
        this::getErrMsg,
        "Zerodha place order",
        (t) -> {
          if (t instanceof BrokerException be) {
            return be.isRetryable();
          } else return true;
        });
  }

  /**
   * Always send a tag with a unique identifier
   *
   * @param tradingSymbol This is the script name
   * @param quantity Quantity of shares to buy
   * @param transactionType Buy/Sell
   * @param limitPrice Value of limit price
   * @param exchange NSE/BSE
   * @param productType MIS/CNC/LONG TERM
   * @return orderId
   */
  public String placeOrder(
      String tradingSymbol,
      double quantity,
      String transactionType,
      double limitPrice,
      String exchange,
      String productType) {
    var tag = UUID.randomUUID().toString().replace("-", "").substring(0, 15);
    OrderParams orderParams = new OrderParams();
    orderParams.exchange = exchange;
    orderParams.tradingsymbol = tradingSymbol;
    orderParams.transactionType = transactionType;
    orderParams.quantity = (int) quantity;
    orderParams.product = productType;
    orderParams.tag = tag;
    orderParams.price = limitPrice;
    orderParams.disclosedQuantity = 0;
    var orderType = limitPrice == 0.0 ? ORDER_TYPE_MARKET : ORDER_TYPE_LIMIT;
    orderParams.orderType = orderType;
    var variety = VARIETY_REGULAR;

    Function<Void, RetryUtils.Output<String>> placeOrderFunction =
        (unused) -> {
          try {
            log.info(
                "Placing order for tradingSymbol - {}; transactionType - {}; quantity - {}; tag - {}",
                tradingSymbol,
                transactionType,
                quantity,
                tag);
            return new RetryUtils.Output<>(false, kite.placeOrder(orderParams, variety).orderId);
          } catch (Throwable t) {
            if (t instanceof KiteException ke && ke.code == 429) {
              try {
                Thread.sleep(TOO_MANY_REQUESTS_WAIT_TIME);
              } catch (InterruptedException ex) {
                // ignore
              }
              log.warn(
                  "Place order received 429 from Zerodha. Cancelling and placing the order again");
              return new RetryUtils.Output<>(true, null);
            }
            var errMsg = getErrMsg(t);
            try {
              var orderOpt =
                  getOrders().stream()
                      .filter(
                          order ->
                              order.exchange.equals(exchange)
                                  && order.tradingSymbol.equals(tradingSymbol)
                                  && order.transactionType.equals(transactionType)
                                  && parseDouble(order.quantity) == quantity
                                  && order.product.equals(productType)
                                  && (order.tag != null && order.tag.equals(tag))
                                  && parseDouble(order.price) == limitPrice
                                  && order.orderType.equals(orderType)
                                  && order.orderVariety.equals(variety))
                      .findAny();
              if (orderOpt.isPresent()) {
                log.error(
                    "Failed while placing order. But found similar order in the broker. ErrMsg - {}",
                    errMsg,
                    t);
                return new RetryUtils.Output<>(false, orderOpt.get().orderId);
              } else {
                // This is thrown when order is not found in broker. RetryUtils will retry to place
                // the order again
                throw new BrokerException(t, ApiError.ORDER_CANNOT_PLACE, errMsg);
              }
            } catch (BrokerException ex) {
              throw new BrokerException(false, ex);
            }
          }
        };

    return RetryUtils.runWithRetries(
        placeOrderFunction,
        null,
        10,
        this::getErrMsg,
        "Zerodha place order",
        (t) -> {
          if (t instanceof BrokerException be) {
            return be.isRetryable();
          } else return true;
        });
  }

  private String getErrMsg(Throwable t) {
    if (t instanceof KiteException ke) {
      return String.format(
          "{\"class\":\"%s\",\"err\":\"%s\",\"code\":\"%s\",\"reference\":\"https://kite.trade/docs/connect/v3/exceptions/#common-http-error-codes\"}",
          ke.getClass().getName(), ke.message, ke.code);
    } else {
      return t.getMessage();
    }
  }

  public Order getOrderDetail(String orderId) {
    log.info("Getting details for orderId, {}", orderId);
    Function<Void, RetryUtils.Output<Order>> getOrderFunc =
        (unused) -> {
          try {
            var orders = kite.getOrderHistory(orderId);
            return new RetryUtils.Output<>(false, orders.get(orders.size() - 1));
          } catch (Throwable e) {
            if (e instanceof KiteException ke && ke.code == 429) {
              try {
                Thread.sleep(TOO_MANY_REQUESTS_WAIT_TIME);
              } catch (InterruptedException ex) {
                // ignore
              }
              log.warn("Get order received 429 from Zerodha.");
              return new RetryUtils.Output<>(true, null);
            }
            throw new BrokerException(e, ApiError.BROKER_GET_ORDER_DETAILS_FAILURE, getErrMsg(e));
          }
        };
    return RetryUtils.runWithRetries(getOrderFunc, null, 10, this::getErrMsg, "Zerodha get order");
  }

  @Override
  public void cancelOrder(String orderId) {
    if (orderId.equals(DUMMY_TRADE)) return;
    log.info("Cancelling order, {}", orderId);
    var orderDetail = getOrderDetail(orderId);
    var orderStatus = BrokerOrderStatus.findEnumByValue(orderDetail.status);
    if (orderStatus == BrokerOrderStatus.CANCELLED) {
      log.warn("Order, {} is already cancelled", orderId);
      return;
    } else if (orderStatus == BrokerOrderStatus.REJECTED
        || orderStatus == BrokerOrderStatus.COMPLETE) {
      throw new BrokerException(ApiError.ORDER_CANNOT_CANCEL, orderId, orderStatus);
    }
    Function<Void, RetryUtils.Output<Void>> cancelOrder =
        (unused) -> {
          try {
            kite.cancelOrder(orderId, VARIETY_REGULAR);
            return null;
          } catch (Throwable t) {
            if (t instanceof KiteException ke && ke.code == 429) {
              try {
                Thread.sleep(TOO_MANY_REQUESTS_WAIT_TIME);
              } catch (InterruptedException ex) {
                // ignore
              }
              log.warn(
                  "Cancel order received 429 from Zerodha. Cancelling and placing the order again");
              return new RetryUtils.Output<>(true, null);
            }
            throw new BrokerException(
                t, ApiError.BROKER_CANCEL_ORDER, orderId + ". " + getErrMsg(t));
          }
        };

    RetryUtils.runWithRetries(cancelOrder, null, 10, this::getErrMsg, "Zerodha cancel order");
  }

  public List<Order> getOrders() {
    Function<Void, RetryUtils.Output<List<Order>>> getOrdersFunc =
        (unused) -> {
          try {
            return new RetryUtils.Output<>(false, kite.getOrders());
          } catch (KiteException | IOException e) {
            if (e instanceof KiteException ke && ke.code == 429) {
              try {
                Thread.sleep(TOO_MANY_REQUESTS_WAIT_TIME);
              } catch (InterruptedException ex) {
                // ignore
              }
              log.warn("Get orders received 429 from Zerodha.");
              return new RetryUtils.Output<>(true, null);
            }
            throw new BrokerException(e, ApiError.BROKER_GET_ORDER_DETAILS_FAILURE, getErrMsg(e));
          }
        };

    return RetryUtils.runWithRetries(
        getOrdersFunc, null, 10, this::getErrMsg, "Zerodha get orders");
  }

  @Override
  public void disConnect() {
    this.reconnectTimer.shutdown();
    this.reconnectTimer.close();
    this.kite = null;
    disconnectFeed();
  }

  private static Candle createCandleFromHistoricalData(HistoricalData candle, TimeFrame timeFrame) {
    return new Candle(
        DateTime.parse(candle.timeStamp).toDate(),
        candle.open,
        candle.high,
        candle.low,
        candle.close,
        candle.volume,
        candle.oi,
        timeFrame);
  }

  /**
   * This is used only by Shruthi Copy trading Poc feature
   *
   * @param handler This lambda is invoked on any order update
   */
  public void setOnOrderUpdateHandler(OnOrderUpdate handler) {
    onOrderUpdateHandler = handler;
    initLiveFeed();
    tickerProvider.setOnOrderUpdateListener(onOrderUpdateHandler);
  }

  @Override
  public List<Candle> getHistoricalDataFromProvider(
      NseScriptMetadata nseScriptMetadata, DateTime from, DateTime to, TimeFrame timeFrame) {
    String token = nseScriptMetadata.getBrokerScriptIdMap().get(BrokerConfig.BrokerType.ZERODHA);
    if (token == null || token.isEmpty()) {
      return new ArrayList<>();
    } else {
      var candles = new ArrayList<Candle>();
      var start = from;
      while (start.isBefore(to)) {
        if (start.plusDays(30).isBefore(to)) {
          candles.addAll(getHistoricalData(token, start, start.plusDays(30), timeFrame));
          start = start.plusDays(31);
        } else {
          candles.addAll(getHistoricalData(token, start, to, timeFrame));
          start = to;
        }
      }
      return candles;
    }
  }
}
