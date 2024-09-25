package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/** Stage of order processing by server. */
public enum ProgressType {
  /** Order was rejected. */
  Rejected,
  /** Order was accepted by server. */
  Accepted,
  /** Server started to execute the order. */
  InProcess,
  /** Order was opened. */
  Opened,
  /** Order was closed. */
  Closed,
  /** Order was modified. */
  Modified,
  /** Pending order was deleted. */
  PendingDeleted,
  /** Closed of pair of opposite orders. */
  ClosedBy,
  /** Closed of multiple orders. */
  MultipleClosedBy,
  /** Trade timeout. */
  Timeout,
  /** Price data. */
  Price,
  /** Exception. */
  Exception;

  public static final int SIZE = java.lang.Integer.SIZE;

  public int getValue() {
    return this.ordinal();
  }

  public static ProgressType forValue(int value) {
    return values()[value];
  }
}
