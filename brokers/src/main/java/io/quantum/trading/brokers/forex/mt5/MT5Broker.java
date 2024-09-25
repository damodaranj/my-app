package io.quantum.trading.brokers.forex.mt5;

import io.quantum.trading.brokers.*;
import io.quantum.trading.brokers.Currency;
import io.quantum.trading.brokers.forex.ForexBroker;
import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.ConnectEventArgs;
import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.ConnectException;
import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.FillPolicy;
import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.MT5API;
import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.OnQuoteHistory;
import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.Order;
import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.OrderState;
import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.OrderType;
import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.ServerException;
import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.SymbolInfo;
import io.quantum.trading.entities.BrokerConfigDetails;
import io.quantum.trading.entities.BrokerConfigDetails.Mt5BrokerConfigDetails;
import io.quantum.trading.entities.NseScriptMetadata;
import io.quantum.trading.enums.TimeFrame;
import io.quantum.trading.exception.ApiError;
import io.quantum.trading.patterns.Candle;
import io.quantum.trading.provider.DataProvidable;
import io.quantum.trading.provider.ForexMetadataProvidable;
import io.quantum.trading.provider.HistoricalDataProvidable;
import io.quantum.trading.util.CandleBucketingUtil;
import io.quantum.trading.util.OmsUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Days;

@Slf4j
public class MT5Broker extends ForexBroker
    implements DataProvidable, HistoricalDataProvidable, ForexMetadataProvidable {
  private MT5API api;
  private int offsetFromUTC;
  private final String orderTypeSuffix = "Forex";
  final Set<String> apiOrderTypes =
      Arrays.stream(OrderType.values())
          .map(e -> e.name() + orderTypeSuffix)
          .collect(Collectors.toSet());

  // Closed orders alone are stored in this map since we don't have any straight method in mt5 api
  // to get it by passing orderId
  private final HashMap<String, BrokerOrderDetail> orderHistory = new HashMap<>();
  private boolean flag = false;
  private boolean dealHistoryFlag = false;

  private boolean disconnected = false;
  private final Mt5BrokerConfigDetails mt5BrokerConfigDetails;
  private Function<List<LiveTick>, Void> liveTickConsumer;

  public MT5Broker(Mt5BrokerConfigDetails mt5BrokerConfigDetails) {
    this.mt5BrokerConfigDetails = mt5BrokerConfigDetails;
    try {
      this.initialize();
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  private void initialize() throws ConnectException {
    this.api =
        new MT5API(
            Long.parseLong(mt5BrokerConfigDetails.getUsername()),
            mt5BrokerConfigDetails.getPassword(),
            mt5BrokerConfigDetails.getHost(),
            mt5BrokerConfigDetails.getPort());
    api.OnConnectProgress.addListener(this::reconnect);
    log.info("Connecting {}...", mt5BrokerConfigDetails.getUsername());
    api.Connect();
    this.disconnected = false;

    var tz = api.ServerDetails.getKey().TimeZone;
    var dst = api.ServerDetails.getKey().DST;
    var serverTzInMins = api.ServrTimeZoneInMinutes();

    log.info(
        "{} - Broker TZ - {}; Broker DST - {}, ServerTimeZoneInMinutes - {}",
        mt5BrokerConfigDetails.getUsername(),
        tz,
        dst,
        api.ServrTimeZoneInMinutes());
    this.offsetFromUTC = serverTzInMins * 60_000;

    this.addLiveListener();
    this.addHistoryListener();
    this.addOrderUpdateListener();
  }

  private void addOrderUpdateListener() {
    this.api.OnOrderUpdate.addListener(
        (sender, args) -> {
          {
            var order = args.Order;
            try {
              if (orderHistory.containsKey(String.valueOf(order.Ticket))) {
                var oldOrder = orderHistory.get(String.valueOf(order.Ticket));
                oldOrder.setAvgClosePrice(order.ClosePrice);
                oldOrder.setAvgOpenPrice(order.OpenPrice);
                oldOrder.setProfitAndLoss(BigDecimal.valueOf(order.Profit));
                oldOrder.setQuantity(order.Lots);
                oldOrder.setFilledQuantity(order.CloseVolume);
                oldOrder.setCloseTs(
                    (order.CloseTime.isAfter(order.OpenTime))
                        ? new DateTime(
                                ZonedDateTime.of(order.CloseTime, ZoneId.of("UTC"))
                                    .toInstant()
                                    .toEpochMilli())
                            .minusMillis(offsetFromUTC)
                            .getMillis()
                        : 0);
                log.debug("Update received for account {}, orderId - {}", order.Ticket, oldOrder);
              } else {
                var res = getBrokerOrderDetail(order, order.OpenPrice, false);
                orderHistory.put(String.valueOf(order.Ticket), res);
                log.debug("New Update received for account {}, orderId - {}", order.Ticket, res);
              }
            } catch (Exception e) {
              log.error(
                  "Error in loading update of order for account {}, orderId {}, {}",
                  sender.User,
                  order.Ticket,
                  order.OrderType.name(),
                  e);
            }
          }
        });
  }

  // TODO: This listener should be registered only when load history is been called, else its a
  // unwanted process
  private void addHistoryListener() {
    this.api.OnTradeHistory.addListener(
        (sender, args) -> {
          args.Orders.forEach(
              order -> {
                try {
                  if (orderHistory.containsKey(String.valueOf(order.Ticket))) {
                    var oldOrder = orderHistory.get(String.valueOf(order.Ticket));
                    oldOrder.setAvgClosePrice(order.ClosePrice);
                    oldOrder.setAvgOpenPrice(order.OpenPrice);
                    oldOrder.setProfitAndLoss(BigDecimal.valueOf(order.Profit));
                    oldOrder.setQuantity(order.Lots);
                    oldOrder.setFilledQuantity(order.CloseVolume);
                    oldOrder.setCloseTs(
                        (order.CloseTime.isAfter(order.OpenTime))
                            ? new DateTime(
                                    ZonedDateTime.of(order.CloseTime, ZoneId.of("UTC"))
                                        .toInstant()
                                        .toEpochMilli())
                                .minusMillis(offsetFromUTC)
                                .getMillis()
                            : 0);
                    log.debug(
                        "Update in history received for account {}, orderId - {}",
                        order.Ticket,
                        oldOrder);
                  } else {
                    var res = getBrokerOrderDetail(order, order.OpenPrice, false);
                    orderHistory.put(String.valueOf(order.Ticket), res);
                    log.debug(
                        "New Update in history received for account {}, orderId - {}",
                        order.Ticket,
                        res);
                  }
                } catch (Exception e) {
                  log.error(
                      "Error in loading history orders for account {}, orderId - {}, {}",
                      sender.User,
                      order.Ticket,
                      order.OrderType.name(),
                      e);
                }
              });
          flag = true;
          dealHistoryFlag = true;
        });
  }

  private void addLiveListener() {
    if (liveTickConsumer != null) {
      api.OnQuote.addListener(
          (sender, quote) -> {
            try {
              var script = quote.Symbol;
              var bid = quote.Bid;
              var ask = quote.Ask;
              var ltp = (bid + ask) / 2.0;
              var ts =
                  new DateTime(quote.Time.toInstant(ZoneOffset.UTC).toEpochMilli())
                      .minusMillis(offsetFromUTC)
                      .getMillis();
              var liveTick =
                  new LiveTick(
                      mt5BrokerConfigDetails.getExchange() + ":" + script,
                      ts,
                      new Date(),
                      ltp,
                      bid,
                      ask,
                      quote.Volume);
              liveTickConsumer.apply(List.of(liveTick));
            } catch (Throwable t) {
              log.error("Error while ingesting candles {}", sender.User, t);
            }
          });
    }
  }

  // TODO: This method has to be synchronised, else its a risk wen used outside without synchronized
  // block. Now its been used under synchronized block so ignoring for now. Listener can be added
  // and removed within the scope of this method and the flag can be passed to the listener method
  // instead of having it as an instance variable. When we keep it as an instance variable there is
  // a risk.
  private void loadHistoryOrders() {
    try {
      flag = false;
      // This from & to dates are not considered in picking the history orders right now by mt5 api
      log.info(
          "Started loading history orders for account {}", mt5BrokerConfigDetails.getUsername());
      this.api.RequestOrderHistory(LocalDateTime.now().minusYears(5), LocalDateTime.now());
      while (!flag) {
        log.debug("Waiting for historical orders to complete..");
      }
      log.info(
          "Completed loading history orders for account {}", mt5BrokerConfigDetails.getUsername());
      loadDealHistoryOrders();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // TODO: This method has to be synchronised, else its a risk wen used outside without synchronized
  // block. Now its been used under synchronized block so ignoring for now. Listener can be added
  // and removed within the scope of this method and the flag can be passed to the listener method
  // instead of having it as an instance variable. When we keep it as an instance variable there is
  // a risk
  public void loadDealHistoryOrders() {
    try {
      dealHistoryFlag = false;
      // This from & to dates are not considered in picking the history orders right now by mt5 api
      log.info(
          "Started loading deal history orders for account {}",
          mt5BrokerConfigDetails.getUsername());
      this.api.RequestDealHistory(LocalDateTime.now().minusYears(5), LocalDateTime.now());
      while (!dealHistoryFlag) {
        log.debug("Waiting for deal historical orders to complete..");
      }
      log.info(
          "Completed loading deal history orders for account {}",
          mt5BrokerConfigDetails.getUsername());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void reconnect(MT5API sender, ConnectEventArgs args) {
    var reConnectWaitTime = 10_000;
    if (disconnected) {
      log.info(
          "Disconnect called no further reconnect will be attempted on this thread for account {}",
          sender.User);
      return;
    }
    switch (args.Progress) {
      case Disconnect -> {
        log.warn("Try reconnect {}", sender.User);
        int i = 0;
        while (true) {
          try {
            log.warn("Reconnect attempt #{} for account {}", i, sender.User);
            this.disConnect();
            initialize();
            break;
          } catch (Exception e) {
            log.warn("Reconnect attempt #{} fail for account {}", i, sender.User, e);
          }
          i++;
          try {
            log.warn("Waiting for {} secs before attempting re-connect", reConnectWaitTime / 1000);
            Thread.sleep(reConnectWaitTime);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
      case Connected -> {
        log.info("{} connected", sender.User);
        reconnectFeed();
      }
      case Exception -> {
        log.error("", args.Exception);
        this.api.Disconnect();
        try {
          this.api.Connect();
        } catch (ConnectException e) {
          throw new RuntimeException(e);
        }
      }
      case SendLogin, SendAccountPassword, AcceptAuthorized, RequestTradeInfo ->
          log.info("Received event for account {}, {}", sender.User, args.Progress);
      default -> log.error("Unhandled event for account {}, {}", sender.User, args.Progress);
    }
  }

  @Override
  public double getBalance() {
    return this.api.AccountBalance();
  }

  @Override
  public Currency getCurrency() throws BrokerException {
    return mt5BrokerConfigDetails.getCurrency();
  }

  @Override
  public boolean testConnection() throws BrokerException {
    return this.api.Connected();
  }

  @Override
  public void cancelOrder(String orderId) {
    // TODO implement status check - isStarted
    try {
      var existingOrder = getOrderDetails(orderId);
      this.api.OrderClose(
          Long.parseLong(existingOrder.getOrderId()),
          existingOrder.getExchangeScript(),
          0,
          existingOrder.getQuantity(),
          OrderType.valueOf(
              BrokerOrderType.findValueByEnum(existingOrder.getType(), apiOrderTypes)
                  .replace(orderTypeSuffix, "")),
          0,
          FillPolicy.FillOrKill);
      log.info("Closed " + orderId);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void closeOrder(String orderId) throws BrokerException {
    // TODO implement status check - isNotStarted
    var existingOrder = getOrderDetails(orderId);
    try {
      this.api.OrderClose(
          Long.parseLong(existingOrder.getOrderId()),
          existingOrder.getExchangeScript(),
          0,
          existingOrder.getQuantity(),
          OrderType.valueOf(
              BrokerOrderType.findValueByEnum(existingOrder.getType(), apiOrderTypes)
                  .replace(orderTypeSuffix, "")),
          0,
          FillPolicy.FillOrKill);
      log.info("Closed " + orderId);
    } catch (Exception e) {
      // If order is in PLACED state we are getting INVALID_REQUEST, hence handled.
      if (e instanceof ServerException && e.getMessage().equals("POSITION_NOT_EXISTS")
          || e.getMessage().equals("INVALID_REQUEST")) {
        log.warn("OrderId, {} is already closed", orderId);
      } else {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public BrokerOrderDetail getOrderDetails(String orderId) throws BrokerException {
    Order[] openOrders = this.api.GetOpenedOrders();
    var opened =
        Arrays.stream(openOrders)
            .filter(order -> String.valueOf(order.Ticket).equals(orderId))
            .findFirst();

    if (opened.isPresent()) {
      log.debug("Got from open order..");
      return getBrokerOrderDetail(opened.get(), 0, true);
    }
    if (!orderHistory.isEmpty()
        && orderHistory.containsKey(orderId)
        && orderHistory.get(orderId).getCloseTs() != 0) {
      log.info(
          "Got from history for account {}, orderId - {}",
          mt5BrokerConfigDetails.getUsername(),
          orderId);

      return orderHistory.get(orderId);
    } else {
      // Its is synchronized to make the loadHistoryOrders method thread safe due to global flag
      synchronized (this) {
        if (orderHistory.isEmpty() || !orderHistory.containsKey(orderId)) loadHistoryOrders();
      }
      if (!orderHistory.isEmpty()
          && orderHistory.containsKey(orderId)
          && orderHistory.get(orderId).getCloseTs() != 0) {
        log.info(
            "Got from history after loading for account {}, orderId - {}",
            mt5BrokerConfigDetails.getUsername(),
            orderId);
        return orderHistory.get(orderId);
      }
    }
    throw new RuntimeException("Order details not available for account - " + orderId);
  }

  @Override
  public void disConnect() {
    disconnected = true;
    try {
      api.Disconnect();
    } catch (Exception e) {
      // ignore
    }
  }

  @Override
  public BrokerOrderDetail placeOrder(BrokerPlaceOrderRequest brokerPlaceOrderRequest)
      throws BrokerException {
    try {
      Order order = null;
      double requestedPrice = 0;
      var script = OmsUtil.getScript(brokerPlaceOrderRequest.getExchangeScript());
      switch (brokerPlaceOrderRequest.getType()) {
        case BrokerOrderType.BUY_MARKET_FOREX -> {
          var orderRequest = (BrokerPlaceOrderRequest.BuyMarketForex) brokerPlaceOrderRequest;
          order =
              this.api.OrderSend(
                  script,
                  orderRequest.getQuantity(),
                  0,
                  OrderType.Buy,
                  orderRequest.getStopLoss(),
                  orderRequest.getTakeProfit(),
                  0,
                  null,
                  0,
                  FillPolicy.FillOrKill);
          requestedPrice = 0;
        }
        case BrokerOrderType.BUY_LIMIT_FOREX -> {
          var orderRequest = (BrokerPlaceOrderRequest.BuyLimitForex) brokerPlaceOrderRequest;
          order =
              this.api.OrderSend(
                  script,
                  orderRequest.getQuantity(),
                  orderRequest.getPrice(),
                  OrderType.BuyLimit,
                  orderRequest.getStopLoss(),
                  orderRequest.getTakeProfit(),
                  0,
                  null,
                  0,
                  FillPolicy.FlashFill);
          requestedPrice = orderRequest.getPrice();
        }
        case BrokerOrderType.BUY_STOP_FOREX -> {
          var orderRequest = (BrokerPlaceOrderRequest.BuyStopForex) brokerPlaceOrderRequest;
          order =
              this.api.OrderSend(
                  script,
                  orderRequest.getQuantity(),
                  orderRequest.getPrice(),
                  OrderType.BuyStop,
                  orderRequest.getStopLoss(),
                  orderRequest.getTakeProfit(),
                  0,
                  null,
                  0,
                  FillPolicy.FlashFill);
          requestedPrice = orderRequest.getPrice();
        }
        case BrokerOrderType.SELL_MARKET_FOREX -> {
          var orderRequest = (BrokerPlaceOrderRequest.SellMarketForex) brokerPlaceOrderRequest;
          order =
              this.api.OrderSend(
                  script,
                  orderRequest.getQuantity(),
                  0,
                  OrderType.Sell,
                  orderRequest.getStopLoss(),
                  orderRequest.getTakeProfit(),
                  0,
                  null,
                  0,
                  FillPolicy.FillOrKill);
          requestedPrice = 0;
        }
        case BrokerOrderType.SELL_LIMIT_FOREX -> {
          var orderRequest = (BrokerPlaceOrderRequest.SellLimitForex) brokerPlaceOrderRequest;
          order =
              this.api.OrderSend(
                  script,
                  orderRequest.getQuantity(),
                  orderRequest.getPrice(),
                  OrderType.SellLimit,
                  orderRequest.getStopLoss(),
                  orderRequest.getTakeProfit(),
                  0,
                  null,
                  0,
                  FillPolicy.FlashFill);
          requestedPrice = orderRequest.getPrice();
        }
        case BrokerOrderType.SELL_STOP_FOREX -> {
          var orderRequest = (BrokerPlaceOrderRequest.SellStopForex) brokerPlaceOrderRequest;
          order =
              this.api.OrderSend(
                  script,
                  orderRequest.getQuantity(),
                  orderRequest.getPrice(),
                  OrderType.SellStop,
                  orderRequest.getStopLoss(),
                  orderRequest.getTakeProfit(),
                  0,
                  null,
                  0,
                  FillPolicy.FlashFill);
          requestedPrice = orderRequest.getPrice();
        }
        default ->
            throw new IllegalArgumentException(
                brokerPlaceOrderRequest.getType() + " orderType not available");
      }
      log.info("Placed order successfully - {}", String.valueOf(order.Ticket));
      return getBrokerOrderDetail(order, requestedPrice, false);
    } catch (Throwable t) {
      throw new BrokerException(t, ApiError.BROKER_PLACE_ORDER);
    }
  }

  private BrokerOrderDetail getBrokerOrderDetail(
      Order order, double requestedPrice, boolean isPositionOpen) {
    try {
      switch (order.OrderType.name()) {
        case "Buy" -> {
          return BrokerOrderDetail.BuyMarketForex.builder()
              .quantity((Math.round(order.Lots * 100.0) / 100.0))
              .stopLoss(order.StopLoss)
              .takeProfit(order.TakeProfit)
              .openTs(
                  new DateTime(
                          ZonedDateTime.of(order.OpenTime, ZoneId.of("UTC"))
                              .toInstant()
                              .toEpochMilli())
                      .minusMillis(offsetFromUTC)
                      .getMillis())
              .closeTs(
                  (order.CloseTime.isAfter(order.OpenTime))
                      ? (order.CloseTime.isAfter(order.OpenTime))
                          ? new DateTime(
                                  ZonedDateTime.of(order.CloseTime, ZoneId.of("UTC"))
                                      .toInstant()
                                      .toEpochMilli())
                              .minusMillis(offsetFromUTC)
                              .getMillis()
                          : 0
                      : 0)
              .orderUpdateTs(0)
              .type(BrokerOrderType.findEnumByValue(order.OrderType.name() + orderTypeSuffix))
              .exchangeScript(order.Symbol)
              .orderId(String.valueOf(order.Ticket))
              .orderStatus(
                  (isPositionOpen && order.State.name().equals(OrderState.Filled.name()))
                      ? BrokerOrderStatus.POSITION_OPEN
                      : BrokerOrderStatus.findEnumByValue(order.State.name()))
              .avgOpenPrice(order.OpenPrice)
              .avgClosePrice(order.ClosePrice)
              .profitAndLoss(BigDecimal.valueOf(order.Profit))
              .filledQuantity(order.CloseVolume)
              .tag(order.Comment)
              .build();
        }
        case "Sell" -> {
          return BrokerOrderDetail.SellMarketForex.builder()
              .quantity((Math.round(order.Lots * 100.0) / 100.0))
              .stopLoss(order.StopLoss)
              .takeProfit(order.TakeProfit)
              .openTs(
                  new DateTime(
                          ZonedDateTime.of(order.OpenTime, ZoneId.of("UTC"))
                              .toInstant()
                              .toEpochMilli())
                      .minusMillis(offsetFromUTC)
                      .getMillis())
              .closeTs(
                  (order.CloseTime.isAfter(order.OpenTime))
                      ? (order.CloseTime.isAfter(order.OpenTime))
                          ? new DateTime(
                                  ZonedDateTime.of(order.CloseTime, ZoneId.of("UTC"))
                                      .toInstant()
                                      .toEpochMilli())
                              .minusMillis(offsetFromUTC)
                              .getMillis()
                          : 0
                      : 0)
              .orderUpdateTs(0)
              .type(BrokerOrderType.findEnumByValue(order.OrderType.name() + orderTypeSuffix))
              .exchangeScript(order.Symbol)
              .orderId(String.valueOf(order.Ticket))
              .orderStatus(
                  (isPositionOpen && order.State.name().equals(OrderState.Filled.name()))
                      ? BrokerOrderStatus.POSITION_OPEN
                      : BrokerOrderStatus.findEnumByValue(order.State.name()))
              .avgOpenPrice(order.OpenPrice)
              .avgClosePrice(order.ClosePrice)
              .profitAndLoss(BigDecimal.valueOf(order.Profit))
              .filledQuantity(order.CloseVolume)
              .tag(order.Comment)
              .build();
        }
        case "BuyLimit" -> {
          return BrokerOrderDetail.BuyLimitForex.builder()
              .quantity((Math.round(order.Lots * 100.0) / 100.0))
              .stopLoss(order.StopLoss)
              .takeProfit(order.TakeProfit)
              .openTs(
                  new DateTime(
                          ZonedDateTime.of(order.OpenTime, ZoneId.of("UTC"))
                              .toInstant()
                              .toEpochMilli())
                      .minusMillis(offsetFromUTC)
                      .getMillis())
              .closeTs(
                  (order.CloseTime.isAfter(order.OpenTime))
                      ? new DateTime(
                              ZonedDateTime.of(order.CloseTime, ZoneId.of("UTC"))
                                  .toInstant()
                                  .toEpochMilli())
                          .minusMillis(offsetFromUTC)
                          .getMillis()
                      : 0)
              .orderUpdateTs(0)
              .type(BrokerOrderType.findEnumByValue(order.OrderType.name() + orderTypeSuffix))
              .exchangeScript(order.Symbol)
              .orderId(String.valueOf(order.Ticket))
              .orderStatus(
                  (isPositionOpen && order.State.name().equals(OrderState.Filled.name()))
                      ? BrokerOrderStatus.POSITION_OPEN
                      : BrokerOrderStatus.findEnumByValue(order.State.name()))
              .price(requestedPrice)
              .avgOpenPrice(order.OpenPrice)
              .avgClosePrice(order.ClosePrice)
              .profitAndLoss(BigDecimal.valueOf(order.Profit))
              .filledQuantity(order.CloseVolume)
              .tag(order.Comment)
              .build();
        }
        case "SellLimit" -> {
          return BrokerOrderDetail.SellLimitForex.builder()
              .quantity((Math.round(order.Lots * 100.0) / 100.0))
              .stopLoss(order.StopLoss)
              .takeProfit(order.TakeProfit)
              .openTs(
                  new DateTime(
                          ZonedDateTime.of(order.OpenTime, ZoneId.of("UTC"))
                              .toInstant()
                              .toEpochMilli())
                      .minusMillis(offsetFromUTC)
                      .getMillis())
              .closeTs(
                  (order.CloseTime.isAfter(order.OpenTime))
                      ? new DateTime(
                              ZonedDateTime.of(order.CloseTime, ZoneId.of("UTC"))
                                  .toInstant()
                                  .toEpochMilli())
                          .minusMillis(offsetFromUTC)
                          .getMillis()
                      : 0)
              .orderUpdateTs(0)
              .type(BrokerOrderType.findEnumByValue(order.OrderType.name() + orderTypeSuffix))
              .exchangeScript(order.Symbol)
              .orderId(String.valueOf(order.Ticket))
              .orderStatus(
                  (isPositionOpen && order.State.name().equals(OrderState.Filled.name()))
                      ? BrokerOrderStatus.POSITION_OPEN
                      : BrokerOrderStatus.findEnumByValue(order.State.name()))
              .price(requestedPrice)
              .avgOpenPrice(order.OpenPrice)
              .avgClosePrice(order.ClosePrice)
              .profitAndLoss(BigDecimal.valueOf(order.Profit))
              .filledQuantity(order.CloseVolume)
              .tag(order.Comment)
              .build();
        }
        case "BuyStop" -> {
          return BrokerOrderDetail.BuyStopForex.builder()
              .quantity((Math.round(order.Lots * 100.0) / 100.0))
              .stopLoss(order.StopLoss)
              .takeProfit(order.TakeProfit)
              .openTs(
                  new DateTime(
                          ZonedDateTime.of(order.OpenTime, ZoneId.of("UTC"))
                              .toInstant()
                              .toEpochMilli())
                      .minusMillis(offsetFromUTC)
                      .getMillis())
              .closeTs(
                  (order.CloseTime.isAfter(order.OpenTime))
                      ? new DateTime(
                              ZonedDateTime.of(order.CloseTime, ZoneId.of("UTC"))
                                  .toInstant()
                                  .toEpochMilli())
                          .minusMillis(offsetFromUTC)
                          .getMillis()
                      : 0)
              .orderUpdateTs(0)
              .type(BrokerOrderType.findEnumByValue(order.OrderType.name() + orderTypeSuffix))
              .exchangeScript(order.Symbol)
              .orderId(String.valueOf(order.Ticket))
              .orderStatus(
                  (isPositionOpen && order.State.name().equals(OrderState.Filled.name()))
                      ? BrokerOrderStatus.POSITION_OPEN
                      : BrokerOrderStatus.findEnumByValue(order.State.name()))
              .price(requestedPrice)
              .avgOpenPrice(order.OpenPrice)
              .avgClosePrice(order.ClosePrice)
              .profitAndLoss(BigDecimal.valueOf(order.Profit))
              .filledQuantity(order.CloseVolume)
              .tag(order.Comment)
              .build();
        }
        case "SellStop" -> {
          return BrokerOrderDetail.SellStopForex.builder()
              .quantity((Math.round(order.Lots * 100.0) / 100.0))
              .stopLoss(order.StopLoss)
              .takeProfit(order.TakeProfit)
              .openTs(
                  new DateTime(
                          ZonedDateTime.of(order.OpenTime, ZoneId.of("UTC"))
                              .toInstant()
                              .toEpochMilli())
                      .minusMillis(offsetFromUTC)
                      .getMillis())
              .closeTs(
                  (order.CloseTime.isAfter(order.OpenTime))
                      ? new DateTime(
                              ZonedDateTime.of(order.CloseTime, ZoneId.of("UTC"))
                                  .toInstant()
                                  .toEpochMilli())
                          .minusMillis(offsetFromUTC)
                          .getMillis()
                      : 0)
              .orderUpdateTs(0)
              .type(BrokerOrderType.findEnumByValue(order.OrderType.name() + orderTypeSuffix))
              .exchangeScript(order.Symbol)
              .orderId(String.valueOf(order.Ticket))
              .orderStatus(
                  (isPositionOpen && order.State.name().equals(OrderState.Filled.name()))
                      ? BrokerOrderStatus.POSITION_OPEN
                      : BrokerOrderStatus.findEnumByValue(order.State.name()))
              .price(requestedPrice)
              .avgOpenPrice(order.OpenPrice)
              .avgClosePrice(order.ClosePrice)
              .profitAndLoss(BigDecimal.valueOf(order.Profit))
              .filledQuantity(order.CloseVolume)
              .tag(order.Comment)
              .build();
        }
        case "CloseBy" -> {
          return BrokerOrderDetail.CloseByForex.builder()
              .quantity((Math.round(order.Lots * 100.0) / 100.0))
              .stopLoss(order.StopLoss)
              .takeProfit(order.TakeProfit)
              .openTs(
                  new DateTime(
                          ZonedDateTime.of(order.OpenTime, ZoneId.of("UTC"))
                              .toInstant()
                              .toEpochMilli())
                      .minusMillis(offsetFromUTC)
                      .getMillis())
              .closeTs(
                  (order.CloseTime.isAfter(order.OpenTime))
                      ? new DateTime(
                              ZonedDateTime.of(order.CloseTime, ZoneId.of("UTC"))
                                  .toInstant()
                                  .toEpochMilli())
                          .minusMillis(offsetFromUTC)
                          .getMillis()
                      : 0)
              .orderUpdateTs(0)
              .type(BrokerOrderType.findEnumByValue(order.OrderType.name() + orderTypeSuffix))
              .exchangeScript(order.Symbol)
              .orderId(String.valueOf(order.Ticket))
              .orderStatus(
                  (isPositionOpen && order.State.name().equals(OrderState.Filled.name()))
                      ? BrokerOrderStatus.POSITION_OPEN
                      : BrokerOrderStatus.findEnumByValue(order.State.name()))
              .price(requestedPrice)
              .avgOpenPrice(order.OpenPrice)
              .avgClosePrice(order.ClosePrice)
              .profitAndLoss(BigDecimal.valueOf(order.Profit))
              .filledQuantity(order.CloseVolume)
              .tag(order.Comment)
              .build();
        }
        default -> throw new RuntimeException("Error in mapping.." + order.OrderType.name());
      }
    } catch (Exception e) {
      throw new RuntimeException("Error in formatting - " + order.Ticket, e);
    }
  }

  @Override
  protected long getServerTimeZoneInMillis() {
    return ZonedDateTime.of(this.api.ServerTime(), ZoneId.of("UTC")).toInstant().toEpochMilli();
  }

  @Override
  protected String getAccountType() {
    return this.api.Account.Type;
  }

  @Override
  public void partialCloseOrder(String orderId, double qty) throws BrokerException {
    // TODO implement status check - isNotStarted
    var existingOrder = getOrderDetails(orderId);
    try {
      this.api.OrderClose(
          Long.parseLong(existingOrder.getOrderId()),
          existingOrder.getExchangeScript(),
          0,
          qty,
          OrderType.valueOf(
              BrokerOrderType.findValueByEnum(existingOrder.getType(), apiOrderTypes)
                  .replace(orderTypeSuffix, "")),
          0,
          FillPolicy.FillOrKill);
      log.info("Partially Closed {} with {} qty", orderId, qty);
    } catch (Exception e) {
      // If order is in PLACED state we are getting INVALID_REQUEST, hence handled.
      if (e instanceof ServerException && e.getMessage().equals("POSITION_NOT_EXISTS")
          || e.getMessage().equals("INVALID_REQUEST")) {
        log.warn("OrderId, {} is already closed", orderId);
      } else {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public BrokerOrderDetail modifyOrder(BrokerModifyOrderRequest brokerModifyOrderRequest)
      throws BrokerException {
    try {
      var orderDetails = getOrderDetails(brokerModifyOrderRequest.getOrderId());
      switch (brokerModifyOrderRequest.getType()) {
        case BrokerOrderType.BUY_MARKET_FOREX -> {
          var orderRequest = (BrokerModifyOrderRequest.BuyMarketForex) brokerModifyOrderRequest;
          this.api.OrderModify(
              Long.parseLong(orderRequest.getOrderId()),
              orderDetails.getExchangeScript(),
              orderDetails.getQuantity(),
              0,
              OrderType.Buy,
              orderRequest.getStopLoss(),
              orderRequest.getTakeProfit(),
              100,
              "",
              0,
              FillPolicy.FillOrKill);
        }
        case BrokerOrderType.BUY_LIMIT_FOREX -> {
          var orderRequest = (BrokerModifyOrderRequest.BuyLimitForex) brokerModifyOrderRequest;
          this.api.OrderModify(
              Long.parseLong(orderRequest.getOrderId()),
              orderDetails.getExchangeScript(),
              orderDetails.getQuantity(),
              orderRequest.getPrice(),
              OrderType.BuyLimit,
              orderRequest.getStopLoss(),
              orderRequest.getTakeProfit(),
              0,
              "",
              0,
              FillPolicy.FlashFill);
        }
        case BrokerOrderType.BUY_STOP_FOREX -> {
          var orderRequest = (BrokerModifyOrderRequest.BuyStopForex) brokerModifyOrderRequest;
          this.api.OrderModify(
              Long.parseLong(orderRequest.getOrderId()),
              orderDetails.getExchangeScript(),
              orderDetails.getQuantity(),
              orderRequest.getPrice(),
              OrderType.BuyStop,
              orderRequest.getStopLoss(),
              orderRequest.getTakeProfit(),
              0,
              "",
              0,
              FillPolicy.FlashFill);
        }
        case BrokerOrderType.SELL_MARKET_FOREX -> {
          var orderRequest = (BrokerModifyOrderRequest.SellMarketForex) brokerModifyOrderRequest;
          this.api.OrderModify(
              Long.parseLong(orderRequest.getOrderId()),
              orderDetails.getExchangeScript(),
              orderDetails.getQuantity(),
              0,
              OrderType.Sell,
              orderRequest.getStopLoss(),
              orderRequest.getTakeProfit(),
              0,
              "",
              0,
              FillPolicy.FillOrKill);
        }
        case BrokerOrderType.SELL_LIMIT_FOREX -> {
          var orderRequest = (BrokerModifyOrderRequest.SellLimitForex) brokerModifyOrderRequest;
          this.api.OrderModify(
              Long.parseLong(orderRequest.getOrderId()),
              orderDetails.getExchangeScript(),
              orderDetails.getQuantity(),
              orderRequest.getPrice(),
              OrderType.SellLimit,
              orderRequest.getStopLoss(),
              orderRequest.getTakeProfit(),
              0,
              "",
              0,
              FillPolicy.FlashFill);
        }
        case BrokerOrderType.SELL_STOP_FOREX -> {
          var orderRequest = (BrokerModifyOrderRequest.SellStopForex) brokerModifyOrderRequest;
          this.api.OrderModify(
              Long.parseLong(orderRequest.getOrderId()),
              orderDetails.getExchangeScript(),
              orderDetails.getQuantity(),
              orderRequest.getPrice(),
              OrderType.SellStop,
              orderRequest.getStopLoss(),
              orderRequest.getTakeProfit(),
              0,
              "",
              0,
              FillPolicy.FlashFill);
        }
        default ->
            throw new IllegalArgumentException(
                brokerModifyOrderRequest.getType() + " orderType not available");
      }
      log.info("Succesfully modifed " + brokerModifyOrderRequest.getOrderId());
      return getOrderDetails(brokerModifyOrderRequest.getOrderId());
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  @Override
  public void startFeed(Function<List<LiveTick>, Void> liveTickConsumer) {
    this.liveTickConsumer = liveTickConsumer;
    this.addLiveListener();
  }

  @Override
  public void disconnectFeed() {
    this.liveTickConsumer = null;
    api.OnQuote.listeners().clear();
  }

  @Override
  public void subscribe(String exchangeScript) {
    try {
      var script = OmsUtil.getScript(exchangeScript);
      api.Subscribe(script);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void unSubscribe(String exchangeScript) {
    try {
      var script = OmsUtil.getScript(exchangeScript);
      api.Unsubscribe(script);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Candle> getHistoricalDataFromProvider(
      NseScriptMetadata nseScriptMetadata, DateTime from, DateTime to, TimeFrame timeFrame) {
    List<Candle> result = new ArrayList<>();
    if (Days.daysBetween(from, to).getDays() <= 25) {
      try {
        result.addAll(getCandlesForMonth(nseScriptMetadata.getId().getScript(), from, to));
      } catch (IOException e) {
        log.error("Exception getting historical data", e);
        throw new RuntimeException(e);
      }
    } else {
      var start = from;
      do {
        try {
          result.addAll(
              getCandlesForMonth(nseScriptMetadata.getId().getScript(), start, start.plusDays(25)));
          start = start.plusDays(25);
        } catch (IOException e) {
          log.error("Exception getting historical data", e);
          throw new RuntimeException(e);
        }
      } while (start.isBefore(to));
    }
    result.sort(Comparator.comparing(Candle::getBeginTs));
    return CandleBucketingUtil.bucketCandles(
        nseScriptMetadata.getId().getScript(), result, timeFrame);
  }

  public synchronized List<Candle> getCandlesForMonth(
      String instrumentToken, DateTime from, DateTime to) throws IOException {
    var candles = Collections.synchronizedList(new ArrayList<Candle>());
    var countDownLatch = new CountDownLatch(1);
    var serverTzInMillis = api.ServrTimeZoneInMinutes() * 60 * 1000;
    OnQuoteHistory quoteListener =
        (mt5API, args) -> {
          if (!args.Bars.isEmpty()) {
            log.info(
                "{}: {} - {}; Spread - {}",
                args.Symbol,
                args.Bars.get(0).Time,
                args.Bars.get(args.Bars.size() - 1).Time,
                args.Bars.get(0).Spread);
          }
          for (int i = 0; i < args.Bars.size(); i++) {
            var bar = args.Bars.get(i);
            var epochTs =
                bar.Time.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli() - serverTzInMillis;
            log.debug("{}", to);
            if (new DateTime(epochTs).isAfter(from)) {
              var newCandle =
                  new Candle(
                      new Date(epochTs),
                      bar.OpenPrice,
                      bar.HighPrice,
                      bar.LowPrice,
                      bar.ClosePrice,
                      0.0,
                      bar.Spread,
                      TimeFrame.MINUTE_1);
              candles.add(newCandle);
            }
          }
          countDownLatch.countDown();
        };
    api.OnQuoteHistory.addListener(quoteListener);

    api.RequestQuoteHistoryMonth(
        instrumentToken, to.getYear(), to.getMonthOfYear(), to.getDayOfMonth());
    try {
      countDownLatch.await();
      api.OnQuoteHistory.listeners.clear();
    } catch (InterruptedException e) {
      log.error("Exception while waiting for candle fetch", e);
    }
    candles.sort(Comparator.comparing(Candle::getBeginTs));
    return candles;
  }

  @Override
  public List<SymbolInfo> getSymbols() {
    return List.of(this.api.Symbols.Infos);
  }

  @Override
  public String getGroupName(String symbol) {
    return this.api.Symbols.GetGroup(symbol).GroupName.split("\\\\")[0];
  }

  public static void main(String[] args) throws ConnectException {
    var mt5BrokerConfigDetails = new BrokerConfigDetails.Mt5BrokerConfigDetails();
    mt5BrokerConfigDetails.setUsername("210527124");
    mt5BrokerConfigDetails.setPassword("Dy3YPa9E");
    mt5BrokerConfigDetails.setHost("34.105.200.212");
    mt5BrokerConfigDetails.setPort(443);
    MT5Broker mt5Broker = new MT5Broker(mt5BrokerConfigDetails);
    mt5Broker.loadHistoryOrders();
    //    System.out.println(orderHistory.size());
  }
}
