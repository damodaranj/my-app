package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x164, CharSet = CharSet.Unicode)]*/
public class AccessRec extends FromBufReader {
  public static final int Size = 356;
  /*[FieldOffset(0)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/
  public String ServerName;
  /*[FieldOffset(64)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 128)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s40;
  public byte[] s40;
  /*[FieldOffset(192)]*/ public int sC0;
  /*[FieldOffset(196)]*/ public int sC4;
  /*[FieldOffset(200)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 156)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] sC8;
  public byte[] sC8;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 356;
    AccessRec st = new AccessRec();
    st.ServerName = GetString(buf.Bytes(64));
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s40 = new byte[128];
    st.s40 = new byte[128];
    for (int i = 0; i < 128; i++) {
      st.s40[i] = buf.Byte();
    }
    st.sC0 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sC4 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.sC8 = new byte[156];
    st.sC8 = new byte[156];
    for (int i = 0; i < 156; i++) {
      st.sC8[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
