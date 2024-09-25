package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/**
 * Order update notification from server
 *
 * @param sender Sender
 * @param update Update details
 * @param order Order
 */
@FunctionalInterface
public interface OnQuoteHistory {
  void invoke(MT5API sender, QuoteHistoryEventArgs args);
}
