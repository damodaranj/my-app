package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x64, CharSet = CharSet.Unicode)]*/
public class SpreadInfo extends FromBufReader {
  /*[FieldOffset(0)]*/ public long Time;
  /*[FieldOffset(8)]*/ public int Id;
  /*[FieldOffset(12)]*/ public int sC;
  /*[FieldOffset(16)]*/ public double s10;
  /*[FieldOffset(24)]*/ public double s18;
  /*[FieldOffset(32)]*/ public int s20;
  /*[FieldOffset(36)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 64)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s24;
  public byte[] s24;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 100;
    SpreadInfo st = new SpreadInfo();
    st.Time = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Id = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sC = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s10 = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s18 = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s20 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s24 = new byte[64];
    st.s24 = new byte[64];
    for (int i = 0; i < 64; i++) {
      st.s24[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
