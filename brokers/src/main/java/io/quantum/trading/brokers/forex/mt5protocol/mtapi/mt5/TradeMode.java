package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum TradeMode {
  Disabled(0),
  LongOnly(1),
  ShortOnly(2),
  CloseOnly(3),
  FullAccess(4);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, TradeMode> mappings;

  private static java.util.HashMap<Integer, TradeMode> getMappings() {
    if (mappings == null) {
      synchronized (TradeMode.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, TradeMode>();
        }
      }
    }
    return mappings;
  }

  private TradeMode(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static TradeMode forValue(int value) {
    return getMappings().get(value);
  }
}
