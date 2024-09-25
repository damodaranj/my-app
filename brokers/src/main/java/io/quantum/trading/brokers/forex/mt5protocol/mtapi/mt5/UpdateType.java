package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum UpdateType {
  Unknown,
  PendingClose,
  MarketOpen,
  PendingOpen,
  MarketClose,
  PartialClose,
  Started,
  Filled,
  Cancelling,
  MarketModify,
  PendingModify,
  OnStopLoss,
  OnTakeProfit,
  OnStopOut,
  Balance,
  Expired,
  Rejected;

  public static final int SIZE = java.lang.Integer.SIZE;

  public int getValue() {
    return this.ordinal();
  }

  public static UpdateType forValue(int value) {
    return values()[value];
  }
}
