package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/**
 * Connect progress.
 *
 * @param sender Object that sent event
 * @param args Event arguments
 */
@FunctionalInterface
public interface OnConnectProgress {
  void invoke(MT5API sender, ConnectEventArgs args);
}
