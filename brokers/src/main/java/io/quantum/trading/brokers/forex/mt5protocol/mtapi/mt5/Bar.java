package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

// C# TO JAVA CONVERTER WARNING: Java does not allow user-defined value types. The behavior of this
// class will differ from the original:
// ORIGINAL LINE: public struct Bar
public final class Bar // sizeof 0x3C d
 {
  public java.time.LocalDateTime Time = java.time.LocalDateTime.MIN; // 0
  public double OpenPrice; // 8
  public double HighPrice; // 10
  public double LowPrice; // 18
  public double ClosePrice; // 20
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong TickVolume;
  public long TickVolume; // 28
  public int Spread; // 30
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong Volume;
  public long Volume; // 34

  public Bar clone() {
    Bar varCopy = new Bar();

    varCopy.Time = this.Time;
    varCopy.OpenPrice = this.OpenPrice;
    varCopy.HighPrice = this.HighPrice;
    varCopy.LowPrice = this.LowPrice;
    varCopy.ClosePrice = this.ClosePrice;
    varCopy.TickVolume = this.TickVolume;
    varCopy.Spread = this.Spread;
    varCopy.Volume = this.Volume;

    return varCopy;
  }
}
