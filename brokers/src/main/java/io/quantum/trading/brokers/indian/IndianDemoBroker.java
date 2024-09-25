package io.quantum.trading.brokers.indian;

import io.quantum.trading.brokers.*;
import io.quantum.trading.entities.*;
import io.quantum.trading.exception.ApiError;
import io.quantum.trading.patterns.Candle;
import io.quantum.trading.redis.RedisCrudService;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

@Slf4j
public class IndianDemoBroker extends NseBroker implements AllowBacktesting {

  private final RedisCrudService redisCrudService;
  private final Map<String, BrokerOrderDetail> brokerOrderDetails;
  private final Map<String, Set<String>> ordersByScript;
  private final DemoBrokerRepository demoBrokerRepository;
  private final BrokerConfig brokerConfig;
  private final boolean backtestMode;
  private final boolean utMode;

  public IndianDemoBroker(
      BrokerConfig brokerConfig,
      RedisCrudService redisCrudService,
      DemoBrokerRepository demoBrokerRepository) {
    this.redisCrudService = redisCrudService;
    this.demoBrokerRepository = demoBrokerRepository;
    this.brokerConfig = brokerConfig;
    backtestMode =
        brokerConfig.getBrokerConfigDetails() instanceof BrokerConfigDetails.ZerodhaBackTestBroker;
    this.brokerOrderDetails = new ConcurrentHashMap<>();
    ordersByScript = new ConcurrentHashMap<>();
    utMode = false;
  }

  public IndianDemoBroker(
      BrokerConfig brokerConfig,
      RedisCrudService redisCrudService,
      DemoBrokerRepository demoBrokerRepository,
      boolean utMode) {
    this.redisCrudService = redisCrudService;
    this.demoBrokerRepository = demoBrokerRepository;
    this.brokerConfig = brokerConfig;
    backtestMode =
        brokerConfig.getBrokerConfigDetails() instanceof BrokerConfigDetails.ZerodhaBackTestBroker;
    this.brokerOrderDetails = new ConcurrentHashMap<>();
    ordersByScript = new ConcurrentHashMap<>();
    this.utMode = utMode;
  }

  private Candle getCurrentCandle(String exchangeScript) {
    if (backtestMode) {
      return redisCrudService.getKeyValue(exchangeScript, Candle.class).orElseThrow();
    } else {
      var liveTick = redisCrudService.getKeyValue(exchangeScript, LiveTick.class).orElseThrow();
      return liveTick.toCandle();
    }
  }

  @Override
  public double getBalance() throws BrokerException {
    return 0;
  }

  @Override
  public boolean testConnection() throws BrokerException {
    return false;
  }

  @Override
  public void disConnect() throws BrokerException {}

  @Override
  public BrokerOrderDetail placeOrder(BrokerPlaceOrderRequest brokerPlaceOrderRequest)
      throws BrokerException {
    var currentCandle = getCurrentCandle(brokerPlaceOrderRequest.getExchangeScript());
    var orderTime =
        backtestMode
            ? new DateTime(currentCandle.getEndTs()).plusMillis(1).getMillis()
            : System.currentTimeMillis();
    var brokerOrderDetail =
        switch (brokerPlaceOrderRequest.getType()) {
          case BrokerOrderType.REGULAR_BUY_NSE -> {
            var orderRequest = (BrokerPlaceOrderRequest.RegularBuyNse) brokerPlaceOrderRequest;
            yield BrokerOrderDetail.RegularBuyNse.builder()
                .price(orderRequest.getPrice())
                .productType(orderRequest.getProductType())
                .type(orderRequest.getType())
                .exchangeScript(orderRequest.getExchangeScript())
                .quantity(orderRequest.getQuantity())
                .filledQuantity(orderRequest.getQuantity())
                .stopLoss(0)
                .takeProfit(0)
                .orderId(UUID.randomUUID().toString())
                .orderStatus(BrokerOrderStatus.COMPLETE)
                .tag(orderRequest.getTag())
                .openTs(orderTime)
                .closeTs(orderTime)
                .orderUpdateTs(0)
                .avgOpenPrice(currentCandle.getClose())
                .avgClosePrice(0.0)
                .profitAndLoss(BigDecimal.valueOf(0.0))
                .build();
          }

          case BrokerOrderType.REGULAR_SELL_NSE -> {
            var orderRequest = (BrokerPlaceOrderRequest.RegularSellNse) brokerPlaceOrderRequest;
            yield BrokerOrderDetail.RegularSellNse.builder()
                .price(orderRequest.getPrice())
                .productType(orderRequest.getProductType())
                .type(orderRequest.getType())
                .exchangeScript(orderRequest.getExchangeScript())
                .quantity(orderRequest.getQuantity())
                .filledQuantity(orderRequest.getQuantity())
                .stopLoss(0)
                .takeProfit(0)
                .orderId(UUID.randomUUID().toString())
                .orderStatus(BrokerOrderStatus.COMPLETE)
                .tag(orderRequest.getTag())
                .openTs(orderTime)
                .closeTs(orderTime)
                .orderUpdateTs(0)
                .avgOpenPrice(currentCandle.getClose())
                .avgClosePrice(0.0)
                .profitAndLoss(BigDecimal.valueOf(0.0))
                .build();
          }
          default ->
              throw new IllegalArgumentException(
                  brokerPlaceOrderRequest.getType() + " orderType not available");
        };

    var orderId = brokerOrderDetail.getOrderId();
    var exchangeScript = brokerPlaceOrderRequest.getExchangeScript();
    brokerOrderDetails.put(orderId, brokerOrderDetail);
    var demoBrokerOrderDetail =
        new DemoBrokerOrderDetails(
            new DemoBrokerOrderDetails.DemoBrokerOrderId(orderId, brokerConfig.getId()),
            brokerOrderDetail);
    if (!backtestMode && !utMode) {
      demoBrokerRepository.save(demoBrokerOrderDetail);
    }
    if (!ordersByScript.containsKey(exchangeScript)) {
      ordersByScript.put(exchangeScript, new ConcurrentSkipListSet<>());
    }
    ordersByScript.get(exchangeScript).add(orderId);

    return brokerOrderDetail;
  }

  @Override
  public BrokerOrderDetail modifyOrder(BrokerModifyOrderRequest brokerModifyOrderRequest)
      throws BrokerException {
    var orderId = brokerModifyOrderRequest.getOrderId();
    DemoBrokerOrderDetails dbOrderDetail = null;
    if (!backtestMode && !utMode) {
      dbOrderDetail = demoBrokerRepository.findById_OrderId(orderId).get();
    }
    var orderDetail =
        backtestMode ? brokerOrderDetails.get(orderId) : dbOrderDetail.getBrokerOrderDetail();
    if (orderDetail.getOrderStatus() != BrokerOrderStatus.COMPLETE
        || orderDetail.getOrderStatus() != BrokerOrderStatus.CANCELLED
        || orderDetail.getOrderStatus() != BrokerOrderStatus.REJECTED) {
      brokerOrderDetails.put(orderId, orderDetail);
      if (!backtestMode && !utMode) {
        demoBrokerRepository.save(dbOrderDetail);
      }
      return orderDetail;
    } else {
      throw new BrokerException(ApiError.BROKER_MODIFY_ORDER, orderDetail.getOrderStatus());
    }
  }

  @Override
  public void cancelOrder(String orderId) throws BrokerException {
    DemoBrokerOrderDetails dbOrderDetail = null;
    if (!backtestMode && !utMode) {
      dbOrderDetail = demoBrokerRepository.findById_OrderId(orderId).get();
    }
    var orderDetail =
        backtestMode ? brokerOrderDetails.get(orderId) : dbOrderDetail.getBrokerOrderDetail();
    if (orderDetail.getOrderStatus() == BrokerOrderStatus.PLACED) {
      orderDetail.setOrderStatus(BrokerOrderStatus.CANCELLED);
      if (!backtestMode && !utMode) {
        demoBrokerRepository.save(dbOrderDetail);
      }
      brokerOrderDetails.remove(orderId);
      ordersByScript.get(orderDetail.getExchangeScript()).remove(orderId);
    } else {
      throw new BrokerException(
          ApiError.ORDER_CANNOT_CANCEL, orderId, orderDetail.getOrderStatus());
    }
  }

  @Override
  public void closeOrder(String orderId) throws BrokerException {}

  @Override
  public BrokerOrderDetail getOrderDetails(String orderId) throws BrokerException {
    if (brokerOrderDetails.containsKey(orderId)) {
      return brokerOrderDetails.get(orderId);
    } else {
      var brokerOrder = demoBrokerRepository.findById_OrderId(orderId);
      if (!backtestMode && !utMode && brokerOrder.isPresent()) {
        brokerOrderDetails.put(orderId, brokerOrder.get().getBrokerOrderDetail());
        return brokerOrder.get().getBrokerOrderDetail();
      } else {
        throw new BrokerException(ApiError.ORDER_NOT_FOUND, orderId);
      }
    }
  }

  @Override
  public String getBrokerToken(NseScriptMetadata nseScriptMetadata) {
    return null;
  }

  @Override
  public String getBrokerScriptName(NseScriptMetadata nseScriptMetadata) {
    return null;
  }

  @Override
  public void synchronizeOrders() {}

  @Override
  public boolean isInBacktestMode() {
    return backtestMode;
  }

  @Override
  public boolean isUtMode() {
    return this.utMode;
  }
}
