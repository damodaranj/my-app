package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum ExpirationType {
  GTC(0),
  Today(1),
  Specified(2),
  SpecifiedDay(3);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, ExpirationType> mappings;

  private static java.util.HashMap<Integer, ExpirationType> getMappings() {
    if (mappings == null) {
      synchronized (ExpirationType.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, ExpirationType>();
        }
      }
    }
    return mappings;
  }

  private ExpirationType(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static ExpirationType forValue(int value) {
    return getMappings().get(value);
  }
}
