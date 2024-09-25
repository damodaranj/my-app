package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0xC58, CharSet = CharSet.Unicode)]*/
public class AccessRecEx extends FromBufReader {
  public static final int Size = 3160;
  /*[FieldOffset(0)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/
  public String s0;
  /*[FieldOffset(128)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String s80;
  /*[FieldOffset(256)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 128)]*/ public String s100;
  /*[FieldOffset(512)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String s200;
  /*[FieldOffset(576)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 24)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s240;
  public byte[] s240;
  /*[FieldOffset(600)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 128)]*/ public String s258;
  /*[FieldOffset(856)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 1024)]*/ public String s358;
  /*[FieldOffset(2904)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 256)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] sB58;
  public byte[] sB58;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 3160;
    AccessRecEx st = new AccessRecEx();
    st.s0 = GetString(buf.Bytes(128));
    st.s80 = GetString(buf.Bytes(128));
    st.s100 = GetString(buf.Bytes(256));
    st.s200 = GetString(buf.Bytes(64));
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s240 = new byte[24];
    st.s240 = new byte[24];
    for (int i = 0; i < 24; i++) {
      st.s240[i] = buf.Byte();
    }
    st.s258 = GetString(buf.Bytes(256));
    st.s358 = GetString(buf.Bytes(2048));
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.sB58 = new byte[256];
    st.sB58 = new byte[256];
    for (int i = 0; i < 256; i++) {
      st.sB58[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
