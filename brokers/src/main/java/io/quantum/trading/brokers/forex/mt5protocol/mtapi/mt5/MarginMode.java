package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum MarginMode {
  MarginForex(0),
  MarginFutures(1),
  vMarginCFD(2),
  MarginCFDIndex(3);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, MarginMode> mappings;

  private static java.util.HashMap<Integer, MarginMode> getMappings() {
    if (mappings == null) {
      synchronized (MarginMode.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, MarginMode>();
        }
      }
    }
    return mappings;
  }

  private MarginMode(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static MarginMode forValue(int value) {
    return getMappings().get(value);
  }
}
