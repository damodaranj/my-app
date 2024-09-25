package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public class ConnectException extends Exception {
  public ConnectException() {}

  public ConnectException(String message) {
    super(message);
  }

  public ConnectException(Throwable cause) {
    super(cause);
  }

  public ConnectException(String message, Throwable cause) {
    super(message, cause);
  }
}
