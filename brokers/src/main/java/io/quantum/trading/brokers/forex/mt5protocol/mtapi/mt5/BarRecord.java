package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

// C# TO JAVA CONVERTER WARNING: Java does not allow user-defined value types. The behavior of this
// class will differ from the original:
// ORIGINAL LINE: internal struct BarRecord
public final class BarRecord // sizeof 0x30 d
 {
  public long Time; // 0
  public long OpenPrice; // 8
  public int High; // 10
  public int Low; // 14
  public int Close; // 18
  public int Spread; // 1C
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong TickVolume;
  public long TickVolume; // 20
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong Volume;
  public long Volume; // 28

  public BarRecord clone() {
    BarRecord varCopy = new BarRecord();

    varCopy.Time = this.Time;
    varCopy.OpenPrice = this.OpenPrice;
    varCopy.High = this.High;
    varCopy.Low = this.Low;
    varCopy.Close = this.Close;
    varCopy.Spread = this.Spread;
    varCopy.TickVolume = this.TickVolume;
    varCopy.Volume = this.Volume;

    return varCopy;
  }
}
