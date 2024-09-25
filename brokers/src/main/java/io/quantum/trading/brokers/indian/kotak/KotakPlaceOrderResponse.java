package io.quantum.trading.brokers.indian.kotak;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class KotakPlaceOrderResponse {
  @SerializedName("NSE")
  private ExchangeResponse exchangeResponse;

  @Data
  public static class ExchangeResponse {
    private String message;
    private long orderId;
    private double price;
    private int quantity;
    private String tag;
  }
}
