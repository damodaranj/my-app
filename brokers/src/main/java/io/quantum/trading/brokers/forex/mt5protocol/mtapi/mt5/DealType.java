package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/** Deal type */
public enum DealType {
  DealBuy(0),
  DealSell(1),
  Balance(2),
  Credit(3),
  Charge(4),
  Correction(5),
  Bonus(6),
  Commission(7),
  DailyCommission(8),
  MonthlyCommission(9),
  DailyAgentCommission(10),
  MonthlyAgentCommission(11),
  InterestRate(12),
  CanceledBuy(13),
  CanceledSell(14),
  Dividend(15),
  Tax(17);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, DealType> mappings;

  private static java.util.HashMap<Integer, DealType> getMappings() {
    if (mappings == null) {
      synchronized (DealType.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, DealType>();
        }
      }
    }
    return mappings;
  }

  private DealType(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static DealType forValue(int value) {
    return getMappings().get(value);
  }
}
