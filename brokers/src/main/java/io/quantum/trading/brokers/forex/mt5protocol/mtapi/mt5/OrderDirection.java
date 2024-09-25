package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum OrderDirection {
  In(0),
  Out(1),
  InOut(2);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, OrderDirection> mappings;

  private static java.util.HashMap<Integer, OrderDirection> getMappings() {
    if (mappings == null) {
      synchronized (OrderDirection.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, OrderDirection>();
        }
      }
    }
    return mappings;
  }

  private OrderDirection(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static OrderDirection forValue(int value) {
    return getMappings().get(value);
  }
}
