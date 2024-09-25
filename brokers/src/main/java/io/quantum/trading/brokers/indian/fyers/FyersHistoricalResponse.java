package io.quantum.trading.brokers.indian.fyers;

import java.util.List;
import lombok.Data;

@Data
public class FyersHistoricalResponse {
  private String s;
  private List<List<String>> candles;
}

@Data
class FyerHistoricalCandles {
  private long beginTs;
  private double open;
  private double high;
  private double low;
  private double close;
  private double volume;
}
