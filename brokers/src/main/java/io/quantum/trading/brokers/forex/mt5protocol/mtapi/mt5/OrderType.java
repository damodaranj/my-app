package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum OrderType {
  Buy(0),
  Sell(1),
  BuyLimit(2),
  SellLimit(3),
  BuyStop(4),
  SellStop(5),
  BuyStopLimit(6),
  SellStopLimit(7),
  CloseBy(8);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, OrderType> mappings;

  private static java.util.HashMap<Integer, OrderType> getMappings() {
    if (mappings == null) {
      synchronized (OrderType.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, OrderType>();
        }
      }
    }
    return mappings;
  }

  private OrderType(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static OrderType forValue(int value) {
    return getMappings().get(value);
  }
}
