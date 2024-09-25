package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum V3DaysSwap {
  Sunday(0),
  Monday(1),
  Tuesday(2),
  Wednesday(3),
  Thursday(4),
  Friday(5),
  Saturday(6);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, V3DaysSwap> mappings;

  private static java.util.HashMap<Integer, V3DaysSwap> getMappings() {
    if (mappings == null) {
      synchronized (V3DaysSwap.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, V3DaysSwap>();
        }
      }
    }
    return mappings;
  }

  private V3DaysSwap(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static V3DaysSwap forValue(int value) {
    return getMappings().get(value);
  }
}
