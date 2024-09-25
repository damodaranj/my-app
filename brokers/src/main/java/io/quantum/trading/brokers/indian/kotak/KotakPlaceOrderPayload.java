package io.quantum.trading.brokers.indian.kotak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KotakPlaceOrderPayload {
  private int instrumentToken;
  private String transactionType;
  private int quantity;
  private double price; // limit order
  private String product; // NORMAL,MIS
  private String validity; // GFD,IOC
  private String variety; // REGULAR
  private int disclosedQuantity;
  private double triggerPrice;
  private String tag;
}
