package io.quantum.trading.brokers.forex.mt5;

import static io.quantum.trading.brokers.BrokerOrderStatus.*;
import static io.quantum.trading.exception.ApiError.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quantum.trading.brokers.*;
import io.quantum.trading.brokers.Currency;
import io.quantum.trading.brokers.forex.ForexBroker;
import io.quantum.trading.entities.BrokerConfigDetails;
import io.quantum.trading.enums.TimeFrame;
import io.quantum.trading.exception.ApiError;
import io.quantum.trading.patterns.Candle;
import io.quantum.trading.redis.RedisCrudService;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

@Slf4j
public class Mt5BacktestBroker extends ForexBroker implements AllowBacktesting {
  private final RedisCrudService redisCrudService;
  private final Map<String, BrokerOrderDetail> brokerOrderDetails;
  private final Map<String, Set<String>> ordersByScript;

  public Mt5BacktestBroker(
      BrokerConfigDetails.Mt5BacktestBrokerConfigDetails mt5BacktestBrokerConfigDetails,
      RedisCrudService redisCrudService) {
    this.redisCrudService = redisCrudService;
    this.brokerOrderDetails = new HashMap<>();
    ordersByScript = new HashMap<>();
  }

  @Override
  public double getBalance() throws BrokerException {
    return 0;
  }

  @Override
  public Currency getCurrency() throws BrokerException {
    return Currency.USD;
  }

  @Override
  public boolean testConnection() throws BrokerException {
    // No-op
    return true;
  }

  @Override
  public void disConnect() throws BrokerException {
    // No-op
  }

  private Candle getCurrentCandle(String script) {
    return redisCrudService.getKeyValue(script, Candle.class).orElseThrow();
  }

  @Override
  public BrokerOrderDetail placeOrder(BrokerPlaceOrderRequest brokerPlaceOrderRequest)
      throws BrokerException {
    var currentCandle = getCurrentCandle(brokerPlaceOrderRequest.getExchangeScript());
    var orderTime = new DateTime(currentCandle.getEndTs()).plusMillis(1).getMillis();
    var brokerOrderDetail =
        switch (brokerPlaceOrderRequest.getType()) {
          case BrokerOrderType.BUY_MARKET_FOREX -> {
            var orderRequest = (BrokerPlaceOrderRequest.BuyMarketForex) brokerPlaceOrderRequest;
            if (currentCandle.getClose() <= orderRequest.getStopLoss()) {
              throw new BrokerException(
                  ApiError.BROKER_ORDER_INVALID,
                  String.format(
                      "Stoploss, %s should be lesser than current market price, %s",
                      orderRequest.getStopLoss(), currentCandle.getClose()));
            }
            yield BrokerOrderDetail.BuyMarketForex.builder()
                .type(orderRequest.getType())
                .exchangeScript(brokerPlaceOrderRequest.getExchangeScript())
                .quantity(brokerPlaceOrderRequest.getQuantity())
                .stopLoss(orderRequest.getStopLoss())
                .takeProfit(orderRequest.getTakeProfit())
                .orderId(UUID.randomUUID().toString())
                .orderStatus(BrokerOrderStatus.POSITION_OPEN)
                .openTs(orderTime)
                .orderUpdateTs(orderTime)
                .avgOpenPrice(currentCandle.getClose())
                .avgClosePrice(0.0)
                .closeTs(0)
                .profitAndLoss(BigDecimal.valueOf(0.0))
                .filledQuantity(0)
                .tag(brokerPlaceOrderRequest.getTag())
                .build();
          }
          case BrokerOrderType.BUY_LIMIT_FOREX -> {
            var orderRequest = (BrokerPlaceOrderRequest.BuyLimitForex) brokerPlaceOrderRequest;
            if (currentCandle.getClose() <= orderRequest.getPrice()) {
              throw new BrokerException(
                  ApiError.BROKER_ORDER_INVALID,
                  String.format(
                      "Limit price, %s should be lesser than current market price, %s",
                      orderRequest.getPrice(), currentCandle.getClose()));
            }
            yield BrokerOrderDetail.BuyLimitForex.builder()
                .type(orderRequest.getType())
                .exchangeScript(brokerPlaceOrderRequest.getExchangeScript())
                .quantity(brokerPlaceOrderRequest.getQuantity())
                .stopLoss(orderRequest.getStopLoss())
                .takeProfit(orderRequest.getTakeProfit())
                .orderId(UUID.randomUUID().toString())
                .orderStatus(BrokerOrderStatus.PLACED)
                .openTs(0)
                .closeTs(0)
                .orderUpdateTs(orderTime)
                .avgOpenPrice(orderRequest.getPrice())
                .avgClosePrice(0.0)
                .profitAndLoss(BigDecimal.valueOf(0.0))
                .price(orderRequest.getPrice())
                .filledQuantity(0)
                .tag(brokerPlaceOrderRequest.getTag())
                .build();
          }
          case BrokerOrderType.BUY_STOP_FOREX -> {
            var orderRequest = (BrokerPlaceOrderRequest.BuyStopForex) brokerPlaceOrderRequest;
            if (currentCandle.getClose() >= orderRequest.getPrice()) {
              throw new BrokerException(
                  ApiError.BROKER_ORDER_INVALID,
                  String.format(
                      "Limit price, %s should be greater than current market price, %s",
                      orderRequest.getPrice(), currentCandle.getClose()));
            }
            yield BrokerOrderDetail.BuyStopForex.builder()
                .type(orderRequest.getType())
                .exchangeScript(brokerPlaceOrderRequest.getExchangeScript())
                .quantity(brokerPlaceOrderRequest.getQuantity())
                .stopLoss(orderRequest.getStopLoss())
                .takeProfit(orderRequest.getTakeProfit())
                .orderId(UUID.randomUUID().toString())
                .orderStatus(BrokerOrderStatus.PLACED)
                .openTs(0)
                .closeTs(0)
                .orderUpdateTs(orderTime)
                .avgOpenPrice(orderRequest.getPrice())
                .avgClosePrice(0.0)
                .profitAndLoss(BigDecimal.valueOf(0.0))
                .price(orderRequest.getPrice())
                .filledQuantity(0)
                .tag(orderRequest.getTag())
                .build();
          }
          case BrokerOrderType.SELL_MARKET_FOREX -> {
            var orderRequest = (BrokerPlaceOrderRequest.SellMarketForex) brokerPlaceOrderRequest;
            if (currentCandle.getClose() >= orderRequest.getStopLoss()) {
              throw new BrokerException(
                  ApiError.BROKER_ORDER_INVALID,
                  String.format(
                      "Stoploss, %s should be greater than current market price, %s",
                      orderRequest.getStopLoss(), currentCandle.getClose()));
            }
            yield BrokerOrderDetail.SellMarketForex.builder()
                .type(orderRequest.getType())
                .exchangeScript(brokerPlaceOrderRequest.getExchangeScript())
                .quantity(brokerPlaceOrderRequest.getQuantity())
                .stopLoss(orderRequest.getStopLoss())
                .takeProfit(orderRequest.getTakeProfit())
                .orderId(UUID.randomUUID().toString())
                .orderStatus(BrokerOrderStatus.POSITION_OPEN)
                .openTs(orderTime)
                .closeTs(0)
                .orderUpdateTs(orderTime)
                .avgOpenPrice(currentCandle.getClose())
                .avgClosePrice(0.0)
                .profitAndLoss(BigDecimal.valueOf(0.0))
                .filledQuantity(0)
                .tag(orderRequest.getTag())
                .build();
          }
          case BrokerOrderType.SELL_LIMIT_FOREX -> {
            var orderRequest = (BrokerPlaceOrderRequest.SellLimitForex) brokerPlaceOrderRequest;
            if (currentCandle.getClose() >= orderRequest.getStopLoss()) {
              throw new BrokerException(
                  ApiError.BROKER_ORDER_INVALID,
                  String.format(
                      "Stoploss, %s should be greater than current market price, %s",
                      orderRequest.getStopLoss(), currentCandle.getClose()));
            }
            yield BrokerOrderDetail.SellLimitForex.builder()
                .type(orderRequest.getType())
                .exchangeScript(brokerPlaceOrderRequest.getExchangeScript())
                .quantity(brokerPlaceOrderRequest.getQuantity())
                .stopLoss(orderRequest.getStopLoss())
                .takeProfit(orderRequest.getTakeProfit())
                .orderId(UUID.randomUUID().toString())
                .orderStatus(BrokerOrderStatus.PLACED)
                .openTs(0)
                .closeTs(0)
                .orderUpdateTs(orderTime)
                .avgOpenPrice(orderRequest.getPrice())
                .avgClosePrice(0.0)
                .profitAndLoss(BigDecimal.valueOf(0.0))
                .price(orderRequest.getPrice())
                .filledQuantity(0)
                .tag(orderRequest.getTag())
                .build();
          }
          case BrokerOrderType.SELL_STOP_FOREX -> {
            var orderRequest = (BrokerPlaceOrderRequest.SellStopForex) brokerPlaceOrderRequest;
            if (currentCandle.getClose() <= orderRequest.getPrice()) {
              throw new BrokerException(
                  ApiError.BROKER_ORDER_INVALID,
                  String.format(
                      "Limit price, %s should be lesser than current market price, %s",
                      orderRequest.getPrice(), currentCandle.getClose()));
            }
            yield BrokerOrderDetail.SellStopForex.builder()
                .type(orderRequest.getType())
                .exchangeScript(brokerPlaceOrderRequest.getExchangeScript())
                .quantity(brokerPlaceOrderRequest.getQuantity())
                .stopLoss(orderRequest.getStopLoss())
                .takeProfit(orderRequest.getTakeProfit())
                .orderId(UUID.randomUUID().toString())
                .orderStatus(BrokerOrderStatus.PLACED)
                .openTs(0)
                .closeTs(0)
                .orderUpdateTs(orderTime)
                .avgOpenPrice(orderRequest.getPrice())
                .avgClosePrice(0.0)
                .profitAndLoss(BigDecimal.valueOf(0.0))
                .price(orderRequest.getPrice())
                .filledQuantity(0)
                .tag(orderRequest.getTag())
                .build();
          }
          default ->
              throw new IllegalArgumentException(
                  brokerPlaceOrderRequest.getType() + " orderType not available");
        };

    var orderId = brokerOrderDetail.getOrderId();
    var script = brokerPlaceOrderRequest.getExchangeScript();
    brokerOrderDetails.put(orderId, brokerOrderDetail);
    if (!ordersByScript.containsKey(script)) {
      ordersByScript.put(script, new ConcurrentSkipListSet<>());
    }
    ordersByScript.get(script).add(orderId);

    return brokerOrderDetail;
  }

  @Override
  public BrokerOrderDetail modifyOrder(BrokerModifyOrderRequest brokerModifyOrderRequest)
      throws BrokerException {
    var orderDetail = getOrderDetails(brokerModifyOrderRequest.getOrderId());
    switch (brokerModifyOrderRequest.getType()) {
      case BrokerOrderType.BUY_MARKET_FOREX -> {
        var orderRequest = (BrokerModifyOrderRequest.BuyMarketForex) brokerModifyOrderRequest;
        orderDetail.setStopLoss(orderRequest.getStopLoss());
        orderDetail.setTakeProfit(orderRequest.getTakeProfit());
      }
      case BrokerOrderType.BUY_LIMIT_FOREX -> {
        var orderRequest = (BrokerModifyOrderRequest.BuyLimitForex) brokerModifyOrderRequest;
        orderDetail.setStopLoss(orderRequest.getStopLoss());
        orderDetail.setTakeProfit(orderRequest.getTakeProfit());
        ((BrokerOrderDetail.BuyLimitForex) orderDetail).setPrice(orderRequest.getPrice());
      }
      case BrokerOrderType.BUY_STOP_FOREX -> {
        var orderRequest = (BrokerModifyOrderRequest.BuyStopForex) brokerModifyOrderRequest;
        orderDetail.setStopLoss(orderRequest.getStopLoss());
        orderDetail.setTakeProfit(orderRequest.getTakeProfit());
        ((BrokerOrderDetail.BuyStopForex) orderDetail).setPrice(orderRequest.getPrice());
      }
      case BrokerOrderType.SELL_MARKET_FOREX -> {
        var orderRequest = (BrokerModifyOrderRequest.SellMarketForex) brokerModifyOrderRequest;
        orderDetail.setStopLoss(orderRequest.getStopLoss());
        orderDetail.setTakeProfit(orderRequest.getTakeProfit());
      }
      case BrokerOrderType.SELL_LIMIT_FOREX -> {
        var orderRequest = (BrokerModifyOrderRequest.SellLimitForex) brokerModifyOrderRequest;
        orderDetail.setStopLoss(orderRequest.getStopLoss());
        orderDetail.setTakeProfit(orderRequest.getTakeProfit());
        ((BrokerOrderDetail.SellLimitForex) orderDetail).setPrice(orderRequest.getPrice());
      }
      case BrokerOrderType.SELL_STOP_FOREX -> {
        var orderRequest = (BrokerModifyOrderRequest.SellStopForex) brokerModifyOrderRequest;
        orderDetail.setStopLoss(orderRequest.getStopLoss());
        orderDetail.setTakeProfit(orderRequest.getTakeProfit());
        ((BrokerOrderDetail.SellStopForex) orderDetail).setPrice(orderRequest.getPrice());
      }
      default ->
          throw new IllegalArgumentException(
              brokerModifyOrderRequest.getType() + " orderType not available");
    }
    ;

    return orderDetail;
  }

  @Override
  public void cancelOrder(String orderId) throws BrokerException {
    var orderDetail = brokerOrderDetails.get(orderId);
    if (orderDetail.getOrderStatus() == BrokerOrderStatus.PLACED) {
      orderDetail.setOrderStatus(BrokerOrderStatus.CANCELLED);
      ordersByScript.get(orderDetail.getExchangeScript()).remove(orderId);
    } else {
      throw new BrokerException(
          ApiError.ORDER_CANNOT_CANCEL, orderId, orderDetail.getOrderStatus());
    }
  }

  @Override
  public void closeOrder(String orderId) throws BrokerException {
    var orderDetail = brokerOrderDetails.get(orderId);
    var currentCandle = getCurrentCandle(orderDetail.getExchangeScript());
    if (orderDetail.getOrderStatus() == BrokerOrderStatus.PLACED) {
      orderDetail.setOrderStatus(BrokerOrderStatus.CANCELLED);
      orderDetail.setAvgClosePrice(0);
      orderDetail.setFilledQuantity(0);
      orderDetail.setCloseTs(0);
      orderDetail.setProfitAndLoss(BigDecimal.valueOf(0));
    } else if (orderDetail.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN) {
      orderDetail.setOrderStatus(BrokerOrderStatus.COMPLETE);
      orderDetail.setAvgClosePrice(currentCandle.getClose());
      orderDetail.setFilledQuantity(orderDetail.getQuantity());
      orderDetail.setCloseTs(currentCandle.getBeginTs().getTime());
      orderDetail.setProfitAndLoss(
          BigDecimal.valueOf(orderDetail.getAvgClosePrice())
              .subtract(BigDecimal.valueOf(orderDetail.getAvgOpenPrice())));
    }
    ordersByScript.get(orderDetail.getExchangeScript()).remove(orderId);
  }

  private void closeOrder(String orderId, double closePrice) throws BrokerException {
    var orderDetail = brokerOrderDetails.get(orderId);
    var currentCandle = getCurrentCandle(orderDetail.getExchangeScript());
    if (orderDetail.getOrderStatus() == BrokerOrderStatus.PLACED) {
      orderDetail.setOrderStatus(BrokerOrderStatus.CANCELLED);
      orderDetail.setAvgClosePrice(0);
      orderDetail.setFilledQuantity(0);
      orderDetail.setCloseTs(0);
      orderDetail.setProfitAndLoss(BigDecimal.valueOf(0));
    } else if (orderDetail.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN) {
      orderDetail.setOrderStatus(BrokerOrderStatus.COMPLETE);
      orderDetail.setAvgClosePrice(closePrice);
      orderDetail.setFilledQuantity(orderDetail.getQuantity());
      orderDetail.setCloseTs(currentCandle.getBeginTs().getTime());
      orderDetail.setProfitAndLoss(
          BigDecimal.valueOf(orderDetail.getAvgClosePrice())
              .subtract(BigDecimal.valueOf(orderDetail.getAvgOpenPrice())));
    }
    ordersByScript.get(orderDetail.getExchangeScript()).remove(orderId);
  }

  @Override
  public BrokerOrderDetail getOrderDetails(String orderId) throws BrokerException {
    if (brokerOrderDetails.containsKey(orderId)) {
      return brokerOrderDetails.get(orderId);
    } else {
      throw new BrokerException(ApiError.ORDER_NOT_FOUND, orderId);
    }
  }

  @Override
  protected long getServerTimeZoneInMillis() {
    return 0;
  }

  @Override
  protected String getAccountType() {
    return null;
  }

  @Override
  public void partialCloseOrder(String orderId, double qty) throws BrokerException {}

  @Override
  public boolean isDemoAcc() {
    return true;
  }

  @Override
  public synchronized void synchronizeOrders() {
    for (var script : ordersByScript.keySet()) {
      Candle currCandle;
      var currCandleOpt = redisCrudService.getKeyValue(script, Candle.class);
      if (currCandleOpt.isEmpty()) continue;
      currCandle = currCandleOpt.get();

      for (var orderId : ordersByScript.get(script)) {
        var order = brokerOrderDetails.get(orderId);
        if (order != null) {
          switch (order.getType()) {
            case BrokerOrderType.BUY_MARKET_FOREX -> {
              if (order.getTakeProfit() != 0.0 && currCandle.getHigh() >= order.getTakeProfit()) {
                closeOrder(orderId, order.getTakeProfit());
              }

              if (order.getStopLoss() != 0.0 && currCandle.getLow() <= order.getStopLoss()) {
                closeOrder(orderId, order.getStopLoss());
              }
            }
            case BrokerOrderType.SELL_MARKET_FOREX -> {
              if (order.getTakeProfit() != 0.0 && currCandle.getLow() <= order.getTakeProfit()) {
                closeOrder(orderId, order.getTakeProfit());
              }

              if (order.getStopLoss() != 0.0 && currCandle.getHigh() >= order.getStopLoss()) {
                closeOrder(orderId, order.getStopLoss());
              }
            }
            case BrokerOrderType.BUY_LIMIT_FOREX -> {
              // Order triggered
              if (order.getOrderStatus() == BrokerOrderStatus.PLACED
                  && currCandle.getLow() <= order.getAvgOpenPrice()) {
                order.setOrderStatus(BrokerOrderStatus.POSITION_OPEN);
                order.setOpenTs(currCandle.getBeginTs().getTime());
                order.setFilledQuantity(order.getQuantity());

                // SL triggered
                if (order.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN
                    && order.getStopLoss() != 0.0
                    && currCandle.getLow() <= order.getStopLoss()) {
                  closeOrder(orderId, order.getStopLoss());
                }
                continue;
              }

              // SL triggered
              if (order.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN
                  && order.getStopLoss() != 0.0
                  && currCandle.getLow() <= order.getStopLoss()) {
                closeOrder(orderId, order.getStopLoss());
                continue;
              }

              // TGT triggered
              if (order.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN
                  && order.getTakeProfit() != 0.0
                  && currCandle.getHigh() >= order.getTakeProfit()) {
                closeOrder(orderId, order.getTakeProfit());
                continue;
              }
            }
            case BrokerOrderType.SELL_LIMIT_FOREX -> {
              // Order triggered
              if (order.getOrderStatus() == BrokerOrderStatus.PLACED
                  && currCandle.getHigh() >= order.getAvgOpenPrice()) {
                order.setOrderStatus(BrokerOrderStatus.POSITION_OPEN);
                order.setOpenTs(currCandle.getBeginTs().getTime());
                order.setFilledQuantity(order.getQuantity());

                // SL triggered
                if (order.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN
                    && order.getStopLoss() != 0.0
                    && currCandle.getHigh() >= order.getStopLoss()) {
                  closeOrder(orderId, order.getStopLoss());
                }
                continue;
              }

              // SL triggered
              if (order.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN
                  && order.getStopLoss() != 0.0
                  && currCandle.getHigh() >= order.getStopLoss()) {
                closeOrder(orderId, order.getStopLoss());
                continue;
              }

              // TGT triggered
              if (order.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN
                  && order.getTakeProfit() != 0.0
                  && currCandle.getLow() <= order.getTakeProfit()) {
                closeOrder(orderId, order.getTakeProfit());
                continue;
              }
            }
            case BrokerOrderType.BUY_STOP_FOREX -> {
              // Order triggered
              if (order.getOrderStatus() == BrokerOrderStatus.PLACED
                  && currCandle.getLow() >= order.getAvgOpenPrice()) {
                order.setOrderStatus(BrokerOrderStatus.POSITION_OPEN);
                order.setOpenTs(currCandle.getBeginTs().getTime());
                order.setFilledQuantity(order.getQuantity());

                // SL triggered
                if (order.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN
                    && order.getStopLoss() != 0.0
                    && currCandle.getLow() <= order.getStopLoss()) {
                  closeOrder(orderId, order.getStopLoss());
                }
                continue;
              }

              // SL triggered
              if (order.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN
                  && order.getStopLoss() != 0.0
                  && currCandle.getLow() <= order.getStopLoss()) {
                closeOrder(orderId, order.getStopLoss());
                continue;
              }

              // TGT triggered
              if (order.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN
                  && order.getTakeProfit() != 0.0
                  && currCandle.getHigh() >= order.getTakeProfit()) {
                closeOrder(orderId, order.getTakeProfit());
                continue;
              }
            }
            case BrokerOrderType.SELL_STOP_FOREX -> {
              // Order triggered
              if (order.getOrderStatus() == BrokerOrderStatus.PLACED
                  && currCandle.getLow() <= order.getAvgOpenPrice()) {
                order.setOrderStatus(BrokerOrderStatus.POSITION_OPEN);
                order.setOpenTs(currCandle.getBeginTs().getTime());
                order.setFilledQuantity(order.getQuantity());

                // SL triggered
                if (order.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN
                    && order.getStopLoss() != 0.0
                    && currCandle.getHigh() >= order.getStopLoss()) {
                  closeOrder(orderId, order.getStopLoss());
                }
                continue;
              }

              // SL triggered
              if (order.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN
                  && order.getStopLoss() != 0.0
                  && currCandle.getHigh() >= order.getStopLoss()) {
                closeOrder(orderId, order.getStopLoss());
                continue;
              }

              // TGT triggered
              if (order.getOrderStatus() == BrokerOrderStatus.POSITION_OPEN
                  && order.getTakeProfit() != 0.0
                  && currCandle.getLow() <= order.getTakeProfit()) {
                closeOrder(orderId, order.getTakeProfit());
                continue;
              }
            }
          }
        }
      }
    }
  }

  @Override
  public boolean isInBacktestMode() {
    return true;
  }

  @Override
  public boolean isUtMode() {
    return false;
  }

  public static void main(String[] args) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    var data =
        objectMapper.writeValueAsString(
            new Candle(
                new Date(), 1.34974, 1.34984, 1.34954, 1.34964, 0.0, 0.0, TimeFrame.MINUTE_1));
    System.out.println(data);
  }
}
