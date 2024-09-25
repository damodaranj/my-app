package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0xE84, CharSet = CharSet.Unicode)]*/
public class SymBaseInfo extends FromBufReader {
  /*[FieldOffset(0)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 16)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s0;
  public byte[] s0;
  /*[FieldOffset(16)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String s10;
  /*[FieldOffset(144)]*/ public int s90;
  /*[FieldOffset(148)]*/ public long s94;
  /*[FieldOffset(156)]*/ public long s9C;
  /*[FieldOffset(164)]*/ public long sA4;
  /*[FieldOffset(172)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 64)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] sAC;
  public byte[] sAC;
  /*[FieldOffset(236)]*/ public int sEC;
  /*[FieldOffset(240)]*/ public long sF0;
  /*[FieldOffset(248)]*/ public int sF8;
  /*[FieldOffset(252)]*/ public int sFC;
  /*[FieldOffset(256)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 60)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s100;
  public byte[] s100;
  /*[FieldOffset(316)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 128)]*/ public String CompanyName;
  /*[FieldOffset(572)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 256)]*/ public String s23C;
  /*[FieldOffset(1084)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String s43C;
  /*[FieldOffset(1212)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 256)]*/ public String s4BC;
  /*[FieldOffset(1724)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String s6BC;
  /*[FieldOffset(1852)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String s73C;
  /*[FieldOffset(1980)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String s7BC;
  /*[FieldOffset(2108)]*/ public int s83C;
  /*[FieldOffset(2112)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String s840;
  /*[FieldOffset(2240)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String s8C0;
  /*[FieldOffset(2368)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String s940;
  /*[FieldOffset(2496)]*/ public long s9C0;
  /*[FieldOffset(2504)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 64)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s9C8;
  public byte[] s9C8;
  /*[FieldOffset(2568)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Currency;
  /*[FieldOffset(2632)]*/ public int Digits;
  /*[FieldOffset(2636)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 128)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] sA4C;
  public byte[] sA4C;
  /*[FieldOffset(2764)]*/ public int sACC;
  /*[FieldOffset(2768)]*/ public int sAD0;
  /*[FieldOffset(2772)]*/ public int sAD4;
  /*[FieldOffset(2776)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String sAD8;
  /*[FieldOffset(2904)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 8)]*/ public int[] sB58;
  /*[FieldOffset(2936)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 32)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] sB78;
  public byte[] sB78;
  /*[FieldOffset(2968)]*/ public int ReceiveUserMsg;
  /*[FieldOffset(2972)]*/ public int sB9C;
  /*[FieldOffset(2976)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 64)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] sBA0;
  public byte[] sBA0;
  /*[FieldOffset(3040)]*/ public io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.AccMethod
      AccMethod;
  /*[FieldOffset(3044)]*/ public int sBE4;
  /*[FieldOffset(3048)]*/ public int sBE8;
  /*[FieldOffset(3052)]*/ public MarginMode MarginMode;
  /*[FieldOffset(3056)]*/ public double sBF0;
  /*[FieldOffset(3064)]*/ public double sBF8;
  /*[FieldOffset(3072)]*/ public int sC00;
  /*[FieldOffset(3076)]*/ public double sC04;
  /*[FieldOffset(3084)]*/ public int sC0C;
  /*[FieldOffset(3088)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 60)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] sC10;
  public byte[] sC10;
  /*[FieldOffset(3148)]*/ public int sC4C;
  /*[FieldOffset(3152)]*/ public double sC50;
  /*[FieldOffset(3160)]*/ public int sC58;
  /*[FieldOffset(3164)]*/ public int sC5C;
  /*[FieldOffset(3168)]*/ public int sC60;
  /*[FieldOffset(3172)]*/ public double sC64;
  /*[FieldOffset(3180)]*/ public long sC6C;
  /*[FieldOffset(3188)]*/ public int sC74;
  /*[FieldOffset(3192)]*/ public int sC78;
  /*[FieldOffset(3196)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 248)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] sC7C;
  public byte[] sC7C;
  /*[FieldOffset(3444)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 16)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] SymbolsHash;
  public byte[] SymbolsHash;
  /*[FieldOffset(3460)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 32)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] sD84;
  public byte[] sD84;
  /*[FieldOffset(3492)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 16)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] SpreadsHash;
  public byte[] SpreadsHash;
  /*[FieldOffset(3508)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 208)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] sDB4;
  public byte[] sDB4;
  private String s8BC;
  private String s9BC;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd;
    if (buf.SymBuild <= 2097) endInd = buf.Ind + 3716;
    else endInd = buf.Ind + 4228;
    SymBaseInfo st = new SymBaseInfo();
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s0 = new byte[16];
    st.s0 = new byte[16];
    for (int i = 0; i < 16; i++) {
      st.s0[i] = buf.Byte();
    }
    st.s10 = GetString(buf.Bytes(128));
    st.s90 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s94 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s9C = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.sA4 = BitConverter.ToInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.sAC = new byte[64];
    st.sAC = new byte[64];
    for (int i = 0; i < 64; i++) {
      st.sAC[i] = buf.Byte();
    }
    st.sEC = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sF0 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.sF8 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sFC = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s100 = new byte[60];
    st.s100 = new byte[60];
    for (int i = 0; i < 60; i++) {
      st.s100[i] = buf.Byte();
    }
    st.CompanyName = GetString(buf.Bytes(256));
    st.s23C = GetString(buf.Bytes(512));
    st.s43C = GetString(buf.Bytes(128));
    st.s4BC = GetString(buf.Bytes(512));
    st.s6BC = GetString(buf.Bytes(128));
    st.s73C = GetString(buf.Bytes(128));
    if (buf.SymBuild <= 2097) {
      st.s7BC = GetString(buf.Bytes(128));
    } else {
      st.s7BC = GetString(buf.Bytes(256));
      st.s8BC = GetString(buf.Bytes(256));
      st.s9BC = GetString(buf.Bytes(128));
    }
    st.s83C = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s840 = GetString(buf.Bytes(128));
    st.s8C0 = GetString(buf.Bytes(128));
    st.s940 = GetString(buf.Bytes(128));
    st.s9C0 = BitConverter.ToInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s9C8 = new byte[64];
    st.s9C8 = new byte[64];
    for (int i = 0; i < 64; i++) {
      st.s9C8[i] = buf.Byte();
    }
    st.Currency = GetString(buf.Bytes(64));
    if (st.Currency == "") throw new RuntimeException("Account currency is not correct");
    st.Digits = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.sA4C = new byte[128];
    st.sA4C = new byte[128];
    for (int i = 0; i < 128; i++) {
      st.sA4C[i] = buf.Byte();
    }
    st.sACC = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sAD0 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sAD4 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sAD8 = GetString(buf.Bytes(128));
    st.sB58 = new int[8];
    for (int i = 0; i < 8; i++) {
      st.sB58[i] = BitConverter.ToInt32(buf.Bytes(4), 0);
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.sB78 = new byte[32];
    st.sB78 = new byte[32];
    for (int i = 0; i < 32; i++) {
      st.sB78[i] = buf.Byte();
    }
    st.ReceiveUserMsg = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sB9C = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.sBA0 = new byte[64];
    st.sBA0 = new byte[64];
    for (int i = 0; i < 64; i++) {
      st.sBA0[i] = buf.Byte();
    }
    st.AccMethod =
        io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.AccMethod.forValue(
            BitConverter.ToInt32(buf.Bytes(4), 0));
    st.sBE4 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sBE8 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.MarginMode = MarginMode.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.sBF0 = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.sBF8 = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.sC00 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sC04 = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.sC0C = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.sC10 = new byte[60];
    st.sC10 = new byte[60];
    for (int i = 0; i < 60; i++) {
      st.sC10[i] = buf.Byte();
    }
    st.sC4C = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sC50 = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.sC58 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sC5C = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sC60 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sC64 = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.sC6C = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.sC74 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sC78 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.sC7C = new byte[248];
    st.sC7C = new byte[248];
    for (int i = 0; i < 248; i++) {
      st.sC7C[i] = buf.Byte();
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.SymbolsHash = new byte[16];
    st.SymbolsHash = new byte[16];
    for (int i = 0; i < 16; i++) {
      st.SymbolsHash[i] = buf.Byte();
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.sD84 = new byte[32];
    st.sD84 = new byte[32];
    for (int i = 0; i < 32; i++) {
      st.sD84[i] = buf.Byte();
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.SpreadsHash = new byte[16];
    st.SpreadsHash = new byte[16];
    for (int i = 0; i < 16; i++) {
      st.SpreadsHash[i] = buf.Byte();
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.sDB4 = new byte[208];
    st.sDB4 = new byte[208];
    for (int i = 0; i < 208; i++) {
      st.sDB4[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
