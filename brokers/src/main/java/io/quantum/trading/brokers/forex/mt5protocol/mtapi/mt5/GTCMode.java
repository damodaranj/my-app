package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum GTCMode // Good till
{
  Cancelled(0),
  TodayIncludeSL_TP(1),
  TodayExcludeSL_TP(2);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, GTCMode> mappings;

  private static java.util.HashMap<Integer, GTCMode> getMappings() {
    if (mappings == null) {
      synchronized (GTCMode.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, GTCMode>();
        }
      }
    }
    return mappings;
  }

  private GTCMode(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static GTCMode forValue(int value) {
    return getMappings().get(value);
  }
}
