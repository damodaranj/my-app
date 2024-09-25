package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.util.List;

/** Quote history event args. */
// C# TO JAVA CONVERTER WARNING: Java does not allow user-defined value types. The behavior of this
// class will differ from the original:
// ORIGINAL LINE: public struct QuoteHistoryEventArgs
public final class QuoteHistoryEventArgs {
  /** Instrument. */
  public String Symbol;

  /** History bars. */
  public List<Bar> Bars;
}
