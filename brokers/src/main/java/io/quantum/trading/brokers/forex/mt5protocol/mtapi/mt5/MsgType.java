package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/** Message type. */
public enum MsgType {
  /** Trace. */
  Trace,
  /** Debug. */
  Debug,
  /** Information. */
  Info,
  /** Warning. */
  Warn,
  /** Error. */
  Error,
  /** Exception. */
  Exception;

  public static final int SIZE = java.lang.Integer.SIZE;

  public int getValue() {
    return this.ordinal();
  }

  public static MsgType forValue(int value) {
    return values()[value];
  }
}
