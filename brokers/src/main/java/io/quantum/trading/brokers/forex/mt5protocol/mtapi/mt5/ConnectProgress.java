package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum ConnectProgress {
  SendLogin,
  SendAccountPassword,
  AcceptAuthorized,
  RequestTradeInfo,
  Connected,
  Exception,
  Disconnect;

  public static final int SIZE = java.lang.Integer.SIZE;

  public int getValue() {
    return this.ordinal();
  }

  public static ConnectProgress forValue(int value) {
    return values()[value];
  }
}
