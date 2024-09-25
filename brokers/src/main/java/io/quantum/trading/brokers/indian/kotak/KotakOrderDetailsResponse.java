package io.quantum.trading.brokers.indian.kotak;

import lombok.Data;

@Data
public class KotakOrderDetailsResponse {
  private String orderTimestamp;
  private long orderId;
  private double price;
  private String status;
}
