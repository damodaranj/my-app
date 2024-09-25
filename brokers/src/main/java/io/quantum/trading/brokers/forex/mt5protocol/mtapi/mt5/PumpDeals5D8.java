package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0xC0, CharSet = CharSet.Unicode)]*/
public class PumpDeals5D8 extends FromBufReader {
  /*[FieldOffset(0)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 8)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s0;
  public byte[] s0;
  /*[FieldOffset(8)]*/ public double Balance;
  /*[FieldOffset(16)]*/ public double Credit;
  /*[FieldOffset(24)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 8)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s18;
  public byte[] s18;
  /*[FieldOffset(32)]*/ public double s20;
  /*[FieldOffset(40)]*/ public double s28;
  /*[FieldOffset(48)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 52)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s30;
  public byte[] s30;
  /*[FieldOffset(100)]*/ public double Blocked;
  /*[FieldOffset(108)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 84)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s6C;
  public byte[] s6C;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 192;
    PumpDeals5D8 st = new PumpDeals5D8();
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s0 = new byte[8];
    st.s0 = new byte[8];
    for (int i = 0; i < 8; i++) {
      st.s0[i] = buf.Byte();
    }
    st.Balance = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Credit = BitConverter.ToDouble(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s18 = new byte[8];
    st.s18 = new byte[8];
    for (int i = 0; i < 8; i++) {
      st.s18[i] = buf.Byte();
    }
    st.s20 = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s28 = BitConverter.ToDouble(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s30 = new byte[52];
    st.s30 = new byte[52];
    for (int i = 0; i < 52; i++) {
      st.s30[i] = buf.Byte();
    }
    st.Blocked = BitConverter.ToDouble(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s6C = new byte[84];
    st.s6C = new byte[84];
    for (int i = 0; i < 84; i++) {
      st.s6C[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
