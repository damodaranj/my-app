package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x6B4, CharSet = CharSet.Unicode)]*/
public class ServerInfoEx extends FromBufReader {
  public static final int Size = 1716;
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
  /*[FieldOffset(660)]*/
  public int s294;
  /*[FieldOffset(664)]*/
  public int s298;
  /*[FieldOffset(668)]*/
  public long s29C;
  /*[FieldOffset(676)]*/
  public long s2A4;
  /*[FieldOffset(684)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 256)]*/
  public String CompanyLink;
  /*[FieldOffset(1196)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 256)]*/
  public String s4AC;
  /*[FieldOffset(1708)]*/
  public long s6AC;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 1716;
    ServerInfoEx st = new ServerInfoEx();
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
    st.s294 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s298 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s29C = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s2A4 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.CompanyLink = GetString(buf.Bytes(512));
    st.s4AC = GetString(buf.Bytes(512));
    st.s6AC = BitConverter.ToInt64(buf.Bytes(8), 0);
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
