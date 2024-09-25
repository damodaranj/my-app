package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public class ServerInfo extends FromBufReader {
  public static final int Size = 0x294;
  /*[FieldOffset(0)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/
  public String ServerName;
  /*[FieldOffset(128)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 128)]*/
  public String CompanyName;
  /*[FieldOffset(384)]*/
  public int s180;
  /*[FieldOffset(388)]*/
  public int s184;
  /*[FieldOffset(392)]*/
  public int DST;
  /*[FieldOffset(396)]*/
  public int TimeZone;
  /*[FieldOffset(400)]*/
  public int s190;
  /*[FieldOffset(404)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/
  public String Address;
  /*[FieldOffset(532)]*/
  public int PingTime;
  /*[FieldOffset(536)]*/
  public int s218;
  /*[FieldOffset(540)]*/
  public int s21C;
  /*[FieldOffset(544)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 116)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s220;
  public byte[] s220;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + Size;
    ServerInfo st = new ServerInfo();
    st.ServerName = GetString(buf.Bytes(128));
    st.CompanyName = GetString(buf.Bytes(256));
    st.s180 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s184 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.DST = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.TimeZone = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s190 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.Address = GetString(buf.Bytes(128));
    st.PingTime = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s218 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s21C = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s220 = new byte[116];
    st.s220 = new byte[116];
    for (int i = 0; i < 116; i++) {
      st.s220[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
