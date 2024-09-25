package io.quantum.trading.brokers;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.quantum.trading.brokers.forex.mt5.MT5Broker;
import io.quantum.trading.entities.BrokerConfigDetails;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(
      value = BrokerPlaceOrderRequest.BuyMarketForex.class,
      name = "BUY_MARKET_FOREX"),
  @JsonSubTypes.Type(
      value = BrokerPlaceOrderRequest.SellMarketForex.class,
      name = "SELL_MARKET_FOREX"),
  @JsonSubTypes.Type(value = BrokerPlaceOrderRequest.BuyLimitForex.class, name = "BUY_LIMIT_FOREX"),
  @JsonSubTypes.Type(
      value = BrokerPlaceOrderRequest.SellLimitForex.class,
      name = "SELL_LIMIT_FOREX"),
  @JsonSubTypes.Type(value = BrokerPlaceOrderRequest.BuyStopForex.class, name = "BUY_STOP_FOREX"),
  @JsonSubTypes.Type(value = BrokerPlaceOrderRequest.SellStopForex.class, name = "SELL_STOP_FOREX"),
  @JsonSubTypes.Type(value = BrokerPlaceOrderRequest.RegularBuyNse.class, name = "REGULAR_BUY_NSE"),
  @JsonSubTypes.Type(
      value = BrokerPlaceOrderRequest.RegularSellNse.class,
      name = "REGULAR_SELL_NSE"),
  @JsonSubTypes.Type(
      value = BrokerPlaceOrderRequest.RegularBuyLimitNse.class,
      name = "REGULAR_BUY_LIMIT_NSE"),
  @JsonSubTypes.Type(
      value = BrokerPlaceOrderRequest.RegularSellLimitNse.class,
      name = "REGULAR_SELL_LIMIT_NSE"),
  @JsonSubTypes.Type(
      value = BrokerPlaceOrderRequest.RegularBuySLNse.class,
      name = "REGULAR_BUY_SL_NSE"),
  @JsonSubTypes.Type(
      value = BrokerPlaceOrderRequest.RegularSellSLNse.class,
      name = "REGULAR_SELL_SL_NSE"),
  @JsonSubTypes.Type(
      value = BrokerPlaceOrderRequest.RegularBuySLMNse.class,
      name = "REGULAR_BUY_SLM_NSE"),
  @JsonSubTypes.Type(
      value = BrokerPlaceOrderRequest.RegularSellSLMNse.class,
      name = "REGULAR_SELL_SLM_NSE")
  //        @JsonSubTypes.Type(value = BrokerPlaceOrderRequest.BuyStopLimitForex.class, name =
  // "BUY_STOP_LIMIT_FOREX"),
  //        @JsonSubTypes.Type(value = BrokerPlaceOrderRequest.SellStopLimitForex.class, name =
  // "SELL_STOP_LIMIT_FOREX"),
})
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
public class BrokerPlaceOrderRequest {
  @NonNull private BrokerOrderType type;
  @NonNull private String exchangeScript;
  private double quantity;
  @NonNull private String tag;

  @EqualsAndHashCode(callSuper = true)
  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @ToString(callSuper = true)
  public static class BuyMarketForex extends BrokerPlaceOrderRequest {
    private double stopLoss;
    private double takeProfit;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @ToString(callSuper = true)
  public static class SellMarketForex extends BrokerPlaceOrderRequest {
    private double stopLoss;
    private double takeProfit;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @ToString(callSuper = true)
  public static class BuyLimitForex extends BrokerPlaceOrderRequest {
    private double stopLoss;
    private double takeProfit;
    private double price;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @ToString(callSuper = true)
  public static class SellLimitForex extends BrokerPlaceOrderRequest {
    private double stopLoss;
    private double takeProfit;
    private double price;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @ToString(callSuper = true)
  public static class BuyStopForex extends BrokerPlaceOrderRequest {
    private double stopLoss;
    private double takeProfit;
    private double price;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @ToString(callSuper = true)
  public static class SellStopForex extends BrokerPlaceOrderRequest {
    private double stopLoss;
    private double takeProfit;
    private double price;
  }

  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class RegularBuyNse extends BrokerPlaceOrderRequest {
    private double price;
    @NonNull private String productType;
  }

  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class RegularSellNse extends BrokerPlaceOrderRequest {
    private double price;
    @NonNull private String productType;
  }

  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class RegularBuyLimitNse extends BrokerPlaceOrderRequest {
    private double price;
    @NonNull private String productType;
  }

  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class RegularSellLimitNse extends BrokerPlaceOrderRequest {
    private double price;
    @NonNull private String productType;
  }

  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class RegularBuySLNse extends BrokerPlaceOrderRequest {
    private double price;
    private double triggerPrice;
    @NonNull private String productType;
  }

  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class RegularSellSLNse extends BrokerPlaceOrderRequest {
    private double price;
    private double triggerPrice;
    @NonNull private String productType;
  }

  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class RegularBuySLMNse extends BrokerPlaceOrderRequest {
    private double price;
    private double triggerPrice;
    @NonNull private String productType;
  }

  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class RegularSellSLMNse extends BrokerPlaceOrderRequest {
    private double price;
    private double triggerPrice;
    @NonNull private String productType;
  }

  public static void main(String[] args) {
    var mt5BrokerConfigDetails = new BrokerConfigDetails.Mt5BrokerConfigDetails();
    mt5BrokerConfigDetails.setUsername("212129991");
    mt5BrokerConfigDetails.setPassword("y4umEXA$");
    mt5BrokerConfigDetails.setHost("d51a2s.octanetwork.net");
    mt5BrokerConfigDetails.setPort(443);
    MT5Broker mt5Broker = new MT5Broker(mt5BrokerConfigDetails);
    //    BrokerPlaceOrderRequest brokerPlaceOrderRequest = new BuyMarketForex(0, 0);
    //    brokerPlaceOrderRequest.setScript("EURUSD");
    //    brokerPlaceOrderRequest.setType(BrokerOrderType.BUY_MARKET_FOREX);
    //    brokerPlaceOrderRequest.setQuantity(2);
    //    System.out.println(mt5Broker.placeOrder(brokerPlaceOrderRequest));
    System.out.println(mt5Broker.getOrderDetails("5050105719"));
    //        mt5Broker.cancelOrder("5049561821");
    ////    BrokerModifyOrderRequest brokerModifyOrderRequest =
    ////        new BrokerModifyOrderRequest.BuyLimitForex(142.717, 0, 142.753);
    //    brokerModifyOrderRequest.setOrderId("5049562761");
    //    brokerModifyOrderRequest.setType(BrokerOrderType.BUY_LIMIT_FOREX);
    //        mt5Broker.modifyOrder(brokerModifyOrderRequest);
  }

  //    @Data
  //    @AllArgsConstructor
  //    public static class BuyStopLimitForex extends BrokerPlaceOrderRequest{
  //        private double quantity;
  //        private double price;
  //        private double stopLoss;
  //        private double takeProfit;
  //        private double stopLimitPrice;
  //        private String comment;
  //    }
  //
  //    @Data
  //    @AllArgsConstructor
  //    public static class SellStopLimitForex extends BrokerPlaceOrderRequest{
  //        private double quantity;
  //        private double price;
  //        private double stopLoss;
  //        private double takeProfit;
  //        private double stopLimitPrice;
  //        private String comment;
  //    }
}
