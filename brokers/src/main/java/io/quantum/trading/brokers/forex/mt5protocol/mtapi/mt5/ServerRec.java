package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x214, CharSet = CharSet.Unicode)]*/
public class ServerRec extends FromBufReader {
  /*[FieldOffset(0)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String ServerName;
  /*[FieldOffset(128)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 128)]*/ public String CompanyName;
  /*[FieldOffset(384)]*/ public int s180;
  /*[FieldOffset(388)]*/ public int s184;
  /*[FieldOffset(392)]*/ public int DST;
  /*[FieldOffset(396)]*/ public int TimeZone;
  /*[FieldOffset(400)]*/ public int s190;
  /*[FieldOffset(404)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 128)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s194;
  public byte[] s194;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 532;
    ServerRec st = new ServerRec();
    st.ServerName = GetString(buf.Bytes(128));
    st.CompanyName = GetString(buf.Bytes(256));
    st.s180 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s184 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.DST = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.TimeZone = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s190 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s194 = new byte[128];
    st.s194 = new byte[128];
    for (int i = 0; i < 128; i++) {
      st.s194[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
