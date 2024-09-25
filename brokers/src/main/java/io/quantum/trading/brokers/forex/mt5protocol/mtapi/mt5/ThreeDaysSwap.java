package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum ThreeDaysSwap {
  vSunday(0),
  vMonday(1),
  vTuesday(2),
  vWednesday(3),
  vThursday(4),
  vFriday(5),
  vSaturday(6);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, ThreeDaysSwap> mappings;

  private static java.util.HashMap<Integer, ThreeDaysSwap> getMappings() {
    if (mappings == null) {
      synchronized (ThreeDaysSwap.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, ThreeDaysSwap>();
        }
      }
    }
    return mappings;
  }

  private ThreeDaysSwap(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static ThreeDaysSwap forValue(int value) {
    return getMappings().get(value);
  }
}
