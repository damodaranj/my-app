package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/** New quote event arguments. */
public class Quote {
  /** Trading instrument. */
  public String Symbol;

  /** Bid. */
  public double Bid;

  /** Ask. */
  public double Ask;

  /** Server time. */
  public java.time.LocalDateTime Time = java.time.LocalDateTime.MIN;

  /** Last deal price. */
  public double Last;

  /** Volume */
  public long Volume;

  /** Spread */
  public int Spread;

  public long UpdateMask;
  public short BankId;

  public Quote() {}

  public Quote(String symbol) {
    Symbol = symbol;
  }

  /**
   * Convert to string.
   *
   * @return "Symbol Bid Ask"
   */
  @Override
  public String toString() {
    return Symbol + " " + Bid + " " + Ask;
  }
}
