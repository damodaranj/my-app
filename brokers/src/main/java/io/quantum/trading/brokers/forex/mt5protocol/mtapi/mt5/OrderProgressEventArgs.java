package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/** Order progress event arguments. */
// C# TO JAVA CONVERTER WARNING: Java does not allow user-defined value types. The behavior of this
// class will differ from the original:
// ORIGINAL LINE: public struct OrderProgressEventArgs
public final class OrderProgressEventArgs {
  /** Temporary ID. Useful until server assign ticket number. */
  public int TempID;

  /** Stage of order processing by server. */
  public ProgressType Type = ProgressType.values()[0];

  /** Opened/closed order. */
  // public Order Order;
  /** Exception during processing of the order. Could be ServerException or RequoteException. */
  public RuntimeException Exception;

  /**
   * Converts to string.
   *
   * @return "TempID Type Exception"
   */
  @Override
  public String toString() {
    String res = TempID + " " + Type + " " + Exception;
    if (Exception != null) {
      res += " " + Exception.getMessage();
    }
    return res;
  }

  public OrderProgressEventArgs clone() {
    OrderProgressEventArgs varCopy = new OrderProgressEventArgs();

    varCopy.TempID = this.TempID;
    varCopy.Type = this.Type;
    varCopy.Exception = this.Exception;

    return varCopy;
  }
}
