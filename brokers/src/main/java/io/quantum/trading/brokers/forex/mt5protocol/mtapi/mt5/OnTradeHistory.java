package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.IOException;

/**
 * Order history
 *
 * @param api Sender</paerram>
 * @param orders Orders
 */
@FunctionalInterface
public interface OnTradeHistory {
  void invoke(MT5API sender, OrderHistoryEventArgs args)
      throws IOException, TimeoutException, ConnectException;
}
