package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum ExpirationDate {
  GTC(0),
  Today(1),
  Specified(2),
  SpecifiedDay(3);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, ExpirationDate> mappings;

  private static java.util.HashMap<Integer, ExpirationDate> getMappings() {
    if (mappings == null) {
      synchronized (ExpirationDate.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, ExpirationDate>();
        }
      }
    }
    return mappings;
  }

  private ExpirationDate(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static ExpirationDate forValue(int value) {
    return getMappings().get(value);
  }
}
