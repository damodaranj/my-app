package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x18B, CharSet = CharSet.Unicode)]*/
public class DealsResult extends FromBufReader {
  /*[FieldOffset(0)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 395)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s0;
  public byte[] s0;
  private int Size = 396;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + Size;
    DealsResult st = new DealsResult();
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s0 = new byte[Size];
    st.s0 = new byte[Size];
    for (int i = 0; i < Size; i++) {
      st.s0[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
