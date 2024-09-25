package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x80, CharSet = CharSet.Unicode)]*/
public class SpreadData extends FromBufReader {
  /*[FieldOffset(0)]*/ public int s0;
  /*[FieldOffset(4)]*/ public int s4;
  /*[FieldOffset(8)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Currency;
  /*[FieldOffset(72)]*/ public long s48;
  /*[FieldOffset(80)]*/ public long s50;
  /*[FieldOffset(88)]*/ public double s58;
  /*[FieldOffset(96)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 32)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s60;
  public byte[] s60;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 128;
    SpreadData st = new SpreadData();
    st.s0 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s4 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.Currency = GetString(buf.Bytes(64));
    st.s48 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s50 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s58 = BitConverter.ToDouble(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s60 = new byte[32];
    st.s60 = new byte[32];
    for (int i = 0; i < 32; i++) {
      st.s60[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
