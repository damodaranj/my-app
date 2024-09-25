package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum SwapType {
  SwapNone(0),
  InPoints(1),
  SymInfo_s408(2), // ???
  MarginCurrency(3),
  Currency(4),
  PercCurPrice(5), // In percentage terms, using current price
  PercOpenPrice(6), // In percentage terms, using open price
  PointClosePrice(7), // In points, reopen position by close price
  PointBidPrice(8); // In points, reopen position by bid price

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, SwapType> mappings;

  private static java.util.HashMap<Integer, SwapType> getMappings() {
    if (mappings == null) {
      synchronized (SwapType.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, SwapType>();
        }
      }
    }
    return mappings;
  }

  private SwapType(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static SwapType forValue(int value) {
    return getMappings().get(value);
  }
}
