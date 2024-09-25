package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x5A, CharSet = CharSet.Unicode)]*/
public class Ticker extends FromBufReader {
  /*[FieldOffset(0)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Name;
  /*[FieldOffset(64)]*/ public short BankId;
  /*[FieldOffset(66)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 24)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s42;
  public byte[] s42;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 90;
    Ticker st = new Ticker();
    st.Name = GetString(buf.Bytes(64));
    st.BankId = BitConverter.ToInt16(buf.Bytes(2), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s42 = new byte[24];
    st.s42 = new byte[24];
    for (int i = 0; i < 24; i++) {
      st.s42[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
