package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum CalculationMode {
  Forex(0),
  Futures(1),
  CFD(2),
  CFDIndex(3),
  CFDLeverage(4),
  CalcMode5(5),
  ExchangeStocks(32),
  ExchangeFutures(33),
  FORTSFutures(34),
  ExchangeOption(35),
  ExchangeMarginOption(36),
  ExchangeBounds(37),
  Collateral(64);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, CalculationMode> mappings;

  private static java.util.HashMap<Integer, CalculationMode> getMappings() {
    if (mappings == null) {
      synchronized (CalculationMode.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, CalculationMode>();
        }
      }
    }
    return mappings;
  }

  private CalculationMode(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static CalculationMode forValue(int value) {
    return getMappings().get(value);
  }
}
