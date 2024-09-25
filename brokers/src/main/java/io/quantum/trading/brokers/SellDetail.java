package io.quantum.trading.brokers;

import lombok.Data;

@Data
public class SellDetail {
  private double sellLtp;
  private double parentMin;
  private double parentMax;
  private double childMin;
  private double childMax;
  private Double newSL = null;
}
