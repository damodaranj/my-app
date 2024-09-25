package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum FillPolicy {
  FillOrKill(0),
  ImmediateOrCancel(1),
  FlashFill(2);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, FillPolicy> mappings;

  private static java.util.HashMap<Integer, FillPolicy> getMappings() {
    if (mappings == null) {
      synchronized (FillPolicy.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, FillPolicy>();
        }
      }
    }
    return mappings;
  }

  private FillPolicy(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static FillPolicy forValue(int value) {
    return getMappings().get(value);
  }
}
