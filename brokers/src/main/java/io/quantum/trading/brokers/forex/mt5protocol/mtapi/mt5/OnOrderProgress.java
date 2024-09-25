package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/**
 * Order update notification from server
 *
 * @param sender Sender</paerram> /// @param update Progress details
 * @param order Order
 */
@FunctionalInterface
public interface OnOrderProgress {
  void invoke(MT5API sender, OrderProgress progress);
}
