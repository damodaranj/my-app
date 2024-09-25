package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/** Trade timeout exception. */
public class TimeoutException extends Exception {
  /** */
  private static final long serialVersionUID = 1L;

  /**
   * Initialize TimeoutException.
   *
   * @param message Exception message.
   */
  public TimeoutException(String message, Logger log) {
    super(message);
    log.warn(getMessage());
  }
}
