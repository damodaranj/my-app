package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum TradeType {
  TradePrice(0),
  RequestExecution(1),
  InstantExecution(2),
  MarketExecution(3),
  ExchangeExecution(4),
  SetOrder(5),
  ModifyDeal(6),
  ModifyOrder(7),
  CancelOrder(8),
  Transfer(9),
  ClosePosition(10),
  ActivateOrder(100),
  ActivateStopLoss(101),
  ActivateTakeProfit(102),
  ActivateStopLimitOrder(103),
  ActivateStopOutOrder(104),
  ActivateStopOutPosition(105),
  ExpireOrder(106),
  ForSetOrder(200),
  ForOrderPrice(201),
  ForModifyDeal(202),
  ForModifyOrder(203),
  ForCancelOrder(204),
  ForActivateOrder(205),
  ForBalance(206),
  ForActivateStopLimitOrder(207),
  ForClosePosition(208);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, TradeType> mappings;

  private static java.util.HashMap<Integer, TradeType> getMappings() {
    if (mappings == null) {
      synchronized (TradeType.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, TradeType>();
        }
      }
    }
    return mappings;
  }

  private TradeType(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static TradeType forValue(int value) {
    return getMappings().get(value);
  }
}
