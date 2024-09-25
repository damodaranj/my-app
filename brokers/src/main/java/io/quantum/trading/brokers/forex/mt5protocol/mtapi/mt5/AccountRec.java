package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0xAB4, CharSet = CharSet.Unicode)]*/
public class AccountRec extends FromBufReader {
  /*[FieldOffset(0)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong Login;
  public long Login;
  /*[FieldOffset(8)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 8)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s8;
  public byte[] s8;
  /*[FieldOffset(16)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 16)]*/ public String Type;
  /*[FieldOffset(48)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 152)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s30;
  public byte[] s30;
  /*[FieldOffset(200)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 128)]*/ public String UserName;
  // #define vDISABLE_ON_SERVER			4
  // #define vINVESTOR_MODE				8
  // #define vALLOWED_TRAILING			0x20
  // #define vALLOWED_AUTOTRADE			0x40
  // #define vACCOUNT_NOT_CONFIRMED		0x200
  // #define vPASS_MUST_BY_CHANGED		0x400
  /*[FieldOffset(456)]*/ public int TradeFlags;
  /*[FieldOffset(460)]*/ public int s1CC;
  /*[FieldOffset(464)]*/ public int s1D0;
  /*[FieldOffset(468)]*/ public int s1D4;
  /*[FieldOffset(472)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 56)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s1D8;
  public byte[] s1D8;
  /*[FieldOffset(528)]*/ public int s210;
  /*[FieldOffset(532)]*/ public int s214;
  /*[FieldOffset(536)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 8)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s218;
  public byte[] s218;
  /*[FieldOffset(544)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String s220;
  /*[FieldOffset(608)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String s260;
  /*[FieldOffset(672)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Country;
  /*[FieldOffset(736)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String City;
  /*[FieldOffset(800)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String State;
  /*[FieldOffset(864)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 16)]*/ public String ZipCode;
  /*[FieldOffset(896)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 128)]*/ public String UserAddress;
  /*[FieldOffset(1152)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Phone;
  /*[FieldOffset(1216)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String Email;
  /*[FieldOffset(1344)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String s540;
  /*[FieldOffset(1472)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String s5C0;
  /*[FieldOffset(1536)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 16)]*/ public String s600;
  /*[FieldOffset(1568)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String s620;
  /*[FieldOffset(1696)]*/ public int s6A0;
  /*[FieldOffset(1700)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 136)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s6A4;
  public byte[] s6A4;
  /*[FieldOffset(1836)]*/ public double Balance;
  /*[FieldOffset(1844)]*/ public double Credit;
  /*[FieldOffset(1852)]*/ public double s73C;
  /*[FieldOffset(1860)]*/ public double s744;
  /*[FieldOffset(1868)]*/ public double s74C;
  /*[FieldOffset(1876)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 52)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s754;
  public byte[] s754;
  /*[FieldOffset(1928)]*/ public double Blocked;
  /*[FieldOffset(1936)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 84)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s790;
  public byte[] s790;
  /*[FieldOffset(2020)]*/ public int Leverage;
  /*[FieldOffset(2024)]*/ public int s7E8;
  /*[FieldOffset(2028)]*/ public int s7EC;
  /*[FieldOffset(2032)]*/ public int s7F0;
  /*[FieldOffset(2036)]*/ public double s7F4;
  /*[FieldOffset(2044)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 312)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s7FC;
  public byte[] s7FC;
  /*[FieldOffset(2356)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 128)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s934;
  public byte[] s934;
  /*[FieldOffset(2484)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 256)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s9B4;
  public byte[] s9B4;
  private byte[] sAB4;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 0xBB4;
    AccountRec st = new AccountRec();
    st.Login = BitConverter.ToInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s8 = new byte[8];
    st.s8 = new byte[8];
    for (int i = 0; i < 8; i++) {
      st.s8[i] = buf.Byte();
    }
    st.Type = GetString(buf.Bytes(32));
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s30 = new byte[152];
    st.s30 = new byte[152];
    for (int i = 0; i < 152; i++) {
      st.s30[i] = buf.Byte();
    }
    st.UserName = GetString(buf.Bytes(256));
    st.TradeFlags = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s1CC = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s1D0 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s1D4 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s1D8 = new byte[56];
    st.s1D8 = new byte[56];
    for (int i = 0; i < 56; i++) {
      st.s1D8[i] = buf.Byte();
    }
    st.s210 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s214 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s218 = new byte[8];
    st.s218 = new byte[8];
    for (int i = 0; i < 8; i++) {
      st.s218[i] = buf.Byte();
    }
    st.s220 = GetString(buf.Bytes(64));
    st.s260 = GetString(buf.Bytes(64));
    st.Country = GetString(buf.Bytes(64));
    st.City = GetString(buf.Bytes(64));
    st.State = GetString(buf.Bytes(64));
    st.ZipCode = GetString(buf.Bytes(32));
    st.UserAddress = GetString(buf.Bytes(256));
    st.Phone = GetString(buf.Bytes(64));
    st.Email = GetString(buf.Bytes(128));
    st.s540 = GetString(buf.Bytes(128));
    st.s5C0 = GetString(buf.Bytes(64));
    st.s600 = GetString(buf.Bytes(32));
    st.s620 = GetString(buf.Bytes(128));
    st.s6A0 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s6A4 = new byte[136];
    st.s6A4 = new byte[136];
    for (int i = 0; i < 136; i++) {
      st.s6A4[i] = buf.Byte();
    }
    st.Balance = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Credit = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s73C = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s744 = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s74C = BitConverter.ToDouble(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s754 = new byte[52];
    st.s754 = new byte[52];
    for (int i = 0; i < 52; i++) {
      st.s754[i] = buf.Byte();
    }
    st.Blocked = BitConverter.ToDouble(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s790 = new byte[84];
    st.s790 = new byte[84];
    for (int i = 0; i < 84; i++) {
      st.s790[i] = buf.Byte();
    }
    st.Leverage = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s7E8 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s7EC = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s7F0 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s7F4 = BitConverter.ToDouble(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s7FC = new byte[312];
    st.s7FC = new byte[312];
    for (int i = 0; i < 312; i++) {
      st.s7FC[i] = buf.Byte();
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s934 = new byte[128];
    st.s934 = new byte[128];
    for (int i = 0; i < 128; i++) {
      st.s934[i] = buf.Byte();
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s9B4 = new byte[256];
    st.s9B4 = new byte[256];
    for (int i = 0; i < 256; i++) {
      st.s9B4[i] = buf.Byte();
    }
    st.sAB4 = new byte[256];
    for (int i = 0; i < 256; i++) st.sAB4[i] = buf.Byte();
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
