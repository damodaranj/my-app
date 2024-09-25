package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x6A0, CharSet = CharSet.Unicode)]*/
public class SymbolInfo extends FromBufReader {
  /** Update time */
  /*[FieldOffset(0)]*/ public long UpdateTime;

  /** Symbol currency */
  /*[FieldOffset(8)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Name;

  /** Symbol ISIN */
  /*[FieldOffset(72)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 16)]*/ public String ISIN;

  /** Description */
  /*[FieldOffset(104)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String Description;

  /*[FieldOffset(232)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String sE8;
  /*[FieldOffset(360)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String s168;
  /*[FieldOffset(424)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String s1A8;

  /** Reference to site */
  /*[FieldOffset(488)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 256)]*/ public String RefToSite;

  /*[FieldOffset(1000)]*/ public int Custom;
  /*[FieldOffset(1004)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 28)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s3EC;
  public byte[] s3EC;
  /*[FieldOffset(1032)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 16)]*/ public String s408;

  /** Currency for profit */
  /*[FieldOffset(1064)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 16)]*/ public String ProfitCurrency;

  /** Currency for margin */
  /*[FieldOffset(1096)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 16)]*/ public String MarginCurrency;

  /*[FieldOffset(1128)]*/ public int s468;
  /*[FieldOffset(1132)]*/ public int Precision;
  /*[FieldOffset(1136)]*/ public int s470;
  /*[FieldOffset(1140)]*/ public int s474;
  /*[FieldOffset(1144)]*/ public int s478;

  /** Background color */
  /*[FieldOffset(1148)]*/ public int BkgndColor;

  /*[FieldOffset(1152)]*/ public int s480;
  /*[FieldOffset(1156)]*/ public int s484;
  /*[FieldOffset(1160)]*/ public int s488;
  /*[FieldOffset(1164)]*/ public int s48C;

  /** Significant digits */
  /*[FieldOffset(1168)]*/ public int Digits;

  /** Symbol points */
  /*[FieldOffset(1172)]*/ public double Points;

  /** Symbol limit points */
  /*[FieldOffset(1180)]*/ public double LimitPoints;

  /** Symbol id */
  /*[FieldOffset(1188)]*/ public int Id;

  /*[FieldOffset(1192)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 32)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s4A8;
  public byte[] s4A8;
  /*[FieldOffset(1224)]*/ public long s4C8;

  /** Depth of market */
  /*[FieldOffset(1232)]*/ public int DepthOfMarket;

  /*[FieldOffset(1236)]*/ public int s4D4;
  /*[FieldOffset(1240)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 36)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s4D8;
  public byte[] s4D8;
  /*[FieldOffset(1276)]*/ public int s4FC;
  /*[FieldOffset(1280)]*/ public int s500;
  /*[FieldOffset(1284)]*/ public int s504;
  /*[FieldOffset(1288)]*/ public int s508;
  /*[FieldOffset(1292)]*/ public int s50C;
  /*[FieldOffset(1296)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 64)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s510;
  public byte[] s510;
  /*[FieldOffset(1360)]*/ public long s550;

  /** Spread */
  /*[FieldOffset(1368)]*/ public int Spread;

  /*[FieldOffset(1372)]*/ public int s55C;

  /** Tick value */
  /*[FieldOffset(1376)]*/ public double TickValue;

  /** Tick size */
  /*[FieldOffset(1384)]*/ public double TickSize;

  /** Contract size */
  /*[FieldOffset(1392)]*/ public double ContractSize;

  /** Good till canceled mode */
  /*[FieldOffset(1400)]*/ public GTCMode GTCMode;

  /** Calculation mode */
  /*[FieldOffset(1404)]*/ public CalculationMode CalcMode = CalculationMode.values()[0];

  /*[FieldOffset(1408)]*/ public int s580;
  /*[FieldOffset(1412)]*/ public int s584;

  /** Settlement price */
  /*[FieldOffset(1416)]*/ public double SettlementPrice;

  /** Lower limit */
  /*[FieldOffset(1424)]*/ public double LowerLimit;

  /** Upper limit */
  /*[FieldOffset(1432)]*/ public double UpperLimit;

  /*[FieldOffset(1440)]*/ public double s5A0;
  /*[FieldOffset(1448)]*/ public int s5A8;

  /** Face value */
  /*[FieldOffset(1452)]*/ public double FaceValue;

  /** Accuired interest */
  /*[FieldOffset(1460)]*/ public double AccruedInterest;

  /*[FieldOffset(1468)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 12)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s5BC;
  public byte[] s5BC;
  /*[FieldOffset(1480)]*/ public long s5C8;

  /** First trade time */
  /*[FieldOffset(1488)]*/ public long FirstTradeTime;

  /** Last trade time */
  /*[FieldOffset(1496)]*/ public long LastTradeTime;

  /*[FieldOffset(1504)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 64)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s5E0;
  public byte[] s5E0;
  /*[FieldOffset(1568)]*/ public int s620;
  /*[FieldOffset(1572)]*/ public int s624;
  /*[FieldOffset(1576)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 120)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s628;
  public byte[] s628;

  public double bid_tickvalue;
  public double ask_tickvalue;
  public double tick_value;
  public double tick_size;
  private String s68;
  private String _sE8;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 1696;
    if (buf.SymBuild > 2097) endInd += 256;
    SymbolInfo st = new SymbolInfo();
    st.UpdateTime = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Name = GetString(buf.Bytes(64));
    st.ISIN = GetString(buf.Bytes(32));
    if (buf.SymBuild > 2097) {
      st.s68 = GetString(buf.Bytes(128));
      st._sE8 = GetString(buf.Bytes(128));
    }
    st.Description = GetString(buf.Bytes(128));
    st.sE8 = GetString(buf.Bytes(128));
    st.s168 = GetString(buf.Bytes(64));
    st.s1A8 = GetString(buf.Bytes(64));
    st.RefToSite = GetString(buf.Bytes(512));
    st.Custom = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s3EC = new byte[28];
    st.s3EC = new byte[28];
    for (int i = 0; i < 28; i++) {
      st.s3EC[i] = buf.Byte();
    }
    st.s408 = GetString(buf.Bytes(32));
    st.ProfitCurrency = GetString(buf.Bytes(32));
    st.MarginCurrency = GetString(buf.Bytes(32));
    st.s468 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.Precision = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s470 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s474 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s478 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.BkgndColor = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s480 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s484 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s488 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s48C = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.Digits = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.Points = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.LimitPoints = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Id = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s4A8 = new byte[32];
    st.s4A8 = new byte[32];
    for (int i = 0; i < 32; i++) {
      st.s4A8[i] = buf.Byte();
    }
    st.s4C8 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.DepthOfMarket = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s4D4 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s4D8 = new byte[36];
    st.s4D8 = new byte[36];
    for (int i = 0; i < 36; i++) {
      st.s4D8[i] = buf.Byte();
    }
    st.s4FC = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s500 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s504 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s508 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s50C = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s510 = new byte[64];
    st.s510 = new byte[64];
    for (int i = 0; i < 64; i++) {
      st.s510[i] = buf.Byte();
    }
    st.s550 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Spread = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s55C = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.TickValue = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.TickSize = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.ContractSize = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.GTCMode = GTCMode.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.CalcMode = CalculationMode.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.s580 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s584 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.SettlementPrice = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.LowerLimit = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.UpperLimit = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s5A0 = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s5A8 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.FaceValue = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.AccruedInterest = BitConverter.ToDouble(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s5BC = new byte[12];
    st.s5BC = new byte[12];
    for (int i = 0; i < 12; i++) {
      st.s5BC[i] = buf.Byte();
    }
    st.s5C8 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.FirstTradeTime = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.LastTradeTime = BitConverter.ToInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s5E0 = new byte[64];
    st.s5E0 = new byte[64];
    for (int i = 0; i < 64; i++) {
      st.s5E0[i] = buf.Byte();
    }
    st.s620 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s624 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s628 = new byte[120];
    st.s628 = new byte[120];
    for (int i = 0; i < 120; i++) {
      st.s628[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
