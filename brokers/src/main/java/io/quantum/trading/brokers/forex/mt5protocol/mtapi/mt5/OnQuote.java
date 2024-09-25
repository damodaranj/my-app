package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/**
 * New quote
 *
 * @param api Sender</paerram>
 * @param quote Quote
 */
@FunctionalInterface
public interface OnQuote {
  void invoke(MT5API sender, Quote quote);
}
