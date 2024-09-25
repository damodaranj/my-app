package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum Direction {
  In(0),
  Out(1),
  InOut(2),
  OutBy(3);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, Direction> mappings;

  private static java.util.HashMap<Integer, Direction> getMappings() {
    if (mappings == null) {
      synchronized (Direction.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, Direction>();
        }
      }
    }
    return mappings;
  }

  private Direction(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static Direction forValue(int value) {
    return getMappings().get(value);
  }
}
