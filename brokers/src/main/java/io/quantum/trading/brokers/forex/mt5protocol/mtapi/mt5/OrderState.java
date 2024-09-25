package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum OrderState {
  Started(0),
  Placed(1),
  Cancelled(2),
  Partial(3),
  Filled(4),
  Rejected(5),
  Expired(6),
  RequestAdding(7),
  RequestModifying(8),
  RequestCancelling(9);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, OrderState> mappings;

  private static java.util.HashMap<Integer, OrderState> getMappings() {
    if (mappings == null) {
      synchronized (OrderState.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, OrderState>();
        }
      }
    }
    return mappings;
  }

  private OrderState(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static OrderState forValue(int value) {
    return getMappings().get(value);
  }
}
