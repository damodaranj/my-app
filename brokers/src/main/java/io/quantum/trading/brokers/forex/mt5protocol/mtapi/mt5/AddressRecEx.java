package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x504, CharSet = CharSet.Unicode)]*/
public class AddressRecEx extends FromBufReader {
  public static final int Size = 1284;
  /*[FieldOffset(0)]*/
  public int s0;
  /*[FieldOffset(4)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 256)]*/ public String s4;
  /*[FieldOffset(516)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 256)]*/ public String s204;
  /*[FieldOffset(1028)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 256)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s404;
  public byte[] s404;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 1284;
    AddressRecEx st = new AddressRecEx();
    st.s0 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s4 = GetString(buf.Bytes(512));
    st.s204 = GetString(buf.Bytes(512));
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s404 = new byte[256];
    st.s404 = new byte[256];
    for (int i = 0; i < 256; i++) {
      st.s404[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
