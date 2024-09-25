package io.quantum.trading.brokers;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(
      value = BrokerModifyOrderRequest.BuyMarketForex.class,
      name = "BUY_MARKET_FOREX"),
  @JsonSubTypes.Type(
      value = BrokerModifyOrderRequest.SellMarketForex.class,
      name = "SELL_MARKET_FOREX"),
  @JsonSubTypes.Type(
      value = BrokerModifyOrderRequest.BuyLimitForex.class,
      name = "BUY_LIMIT_FOREX"),
  @JsonSubTypes.Type(
      value = BrokerModifyOrderRequest.SellLimitForex.class,
      name = "SELL_LIMIT_FOREX"),
  @JsonSubTypes.Type(value = BrokerModifyOrderRequest.BuyStopForex.class, name = "BUY_STOP_FOREX"),
  @JsonSubTypes.Type(value = BrokerModifyOrderRequest.SellStopForex.class, name = "SELL_STOP_FOREX")
  //        @JsonSubTypes.Type(value = BrokerModifyOrderRequest.BuyStopLimitForex.class, name =
  // "BUY_STOP_LIMIT_FOREX"),
  //        @JsonSubTypes.Type(value = BrokerModifyOrderRequest.SellStopLimitForex.class, name =
  // "SELL_STOP_LIMIT_FOREX")
})
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
public class BrokerModifyOrderRequest {
  public BrokerOrderType type;
  @NonNull private String orderId;

  @EqualsAndHashCode(callSuper = true)
  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @ToString(callSuper = true)
  public static class BuyMarketForex extends BrokerModifyOrderRequest {
    private double stopLoss;
    private double takeProfit;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @ToString(callSuper = true)
  public static class SellMarketForex extends BrokerModifyOrderRequest {
    private double stopLoss;
    private double takeProfit;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @ToString(callSuper = true)
  public static class BuyLimitForex extends BrokerModifyOrderRequest {
    private double stopLoss;
    private double takeProfit;
    private double price;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @ToString(callSuper = true)
  public static class SellLimitForex extends BrokerModifyOrderRequest {
    private double stopLoss;
    private double takeProfit;
    private double price;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @ToString(callSuper = true)
  public static class BuyStopForex extends BrokerModifyOrderRequest {
    private double stopLoss;
    private double takeProfit;
    private double price;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  @Jacksonized
  @SuperBuilder(toBuilder = true)
  @ToString(callSuper = true)
  public static class SellStopForex extends BrokerModifyOrderRequest {
    private double stopLoss;
    private double takeProfit;
    private double price;
  }

  //    @Data
  //    @AllArgsConstructor
  //    public static class BuyStopLimitForex extends BrokerModifyOrderRequest{
  //        private double price;
  //        private double stopLoss;
  //        private double stopLimitPrice;
  //        private double takeProfit;
  //        private String comment;
  //    }
  //
  //    @Data
  //    @AllArgsConstructor
  //    public static class SellStopLimitForex extends BrokerModifyOrderRequest{
  //        private double price;
  //        private double stopLoss;
  //        private double stopLimitPrice;
  //        private double takeProfit;
  //        private String comment;
  //    }
}
