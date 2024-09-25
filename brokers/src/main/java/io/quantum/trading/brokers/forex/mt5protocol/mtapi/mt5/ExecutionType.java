package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum ExecutionType {
  Request(0),
  Instant(1),
  Market(2),
  Exchange(3);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, ExecutionType> mappings;

  private static java.util.HashMap<Integer, ExecutionType> getMappings() {
    if (mappings == null) {
      synchronized (ExecutionType.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, ExecutionType>();
        }
      }
    }
    return mappings;
  }

  private ExecutionType(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static ExecutionType forValue(int value) {
    return getMappings().get(value);
  }
}
