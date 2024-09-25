package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x10C, CharSet = CharSet.Unicode)]*/
public class AccessInfo extends FromBufReader {
  /*[FieldOffset(0)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String ServerName;
  /*[FieldOffset(64)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 36)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s40;
  public byte[] s40;
  /*[FieldOffset(100)]*/ public int s64;
  /*[FieldOffset(104)]*/ public int s68;
  /*[FieldOffset(108)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 160)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s6C;
  public byte[] s6C;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 268;
    AccessInfo st = new AccessInfo();
    st.ServerName = GetString(buf.Bytes(64));
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s40 = new byte[36];
    st.s40 = new byte[36];
    for (int i = 0; i < 36; i++) {
      st.s40[i] = buf.Byte();
    }
    st.s64 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s68 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s6C = new byte[160];
    st.s6C = new byte[160];
    for (int i = 0; i < 160; i++) {
      st.s6C[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
