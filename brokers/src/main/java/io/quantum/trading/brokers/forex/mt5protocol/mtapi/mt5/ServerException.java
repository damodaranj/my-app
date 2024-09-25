package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/** Error reply from server on login and order opening/closing/modifying. */
public class ServerException extends RuntimeException {
  /** Error code. */
  public Msg Code = Msg.values()[0];

  /**
   * Initialize ServerException.
   *
   * @param code Exception code.
   */
  public ServerException(Msg code) {
    super(code.toString());
    Code = code;
  }
}
