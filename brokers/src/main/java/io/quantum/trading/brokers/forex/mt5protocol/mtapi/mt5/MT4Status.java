package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public enum MT4Status {
  OK_ANSWER(0),
  OK_REQUEST(1),
  COMMON_ERROR(2),
  INVALID_PARAM(3),
  SERVER_BUSY(4),
  OLD_VERSION(5),
  NO_CONNECT(6),
  NOT_ENOUGH_RIGHTS(7),
  TOO_FREQUENT_REQUEST(8),
  SECRET_KEY_REQUIRED(0xD),
  INVALID_ONETIME_PASSWORD(0xE),
  ACCOUNT_DISABLED(0x40),
  INVALID_ACCOUNT(0x41),
  TRADE_TIMEOUT(0x80),
  INVALID_PRICES(0x81),
  INVALID_SL_TP(0x82),
  INVALID_VOLUME(0x83),
  MARKET_CLOSED(0x84),
  TRADE_DISABLED(0x85),
  NOT_MONEY(0x86),
  PRICE_CHANGED(0x87),
  OFF_QUOTES(0x88),
  BROKER_BUSY(0x89),
  REQUOTE(0x8A),
  ORDER_LOCKED(0x8B),
  LONG_POS_ALLOWED(0x8C),
  TOO_MANY_REQUESTS(0x8D),
  ORDER_ACCEPTED(0x8E),
  ORDER_IN_PROCESS(0x8F),
  REQUEST_CANCELLED(0x90),
  MODIFICATIONS_DENIED(0x91),
  TRADE_CONTEXT_BUSY(0x92),
  EXPIRATION_DISABLED(0x93),
  TOO_MANY_ORDERS(0x94),
  HEDGE_PROHIBITED(0x95),
  RPROHIBITED_FIFO(0x96);

  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static java.util.HashMap<Integer, MT4Status> mappings;

  private static java.util.HashMap<Integer, MT4Status> getMappings() {
    if (mappings == null) {
      synchronized (MT4Status.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<Integer, MT4Status>();
        }
      }
    }
    return mappings;
  }

  private MT4Status(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static MT4Status forValue(int value) {
    return getMappings().get(value);
  }
}
