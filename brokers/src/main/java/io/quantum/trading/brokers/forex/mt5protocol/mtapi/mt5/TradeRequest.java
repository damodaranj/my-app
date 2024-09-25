package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Size = 0x320, CharSet = CharSet.Unicode)]*/
public class TradeRequest extends FromBufReader {
  // <summary>
  /** Request id */
  /*[FieldOffset(0)]*/ public int RequestId;

  /*[FieldOffset(4)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 64)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s4;
  public byte[] s4;

  /** Trade type */
  /*[FieldOffset(68)]*/ public TradeType TradeType;

  /** Account login */
  /*[FieldOffset(72)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong Login;
  public long Login;

  /*[FieldOffset(80)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 72)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s50;
  public byte[] s50;

  /** Transfer login */
  /*[FieldOffset(152)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong TransferLogin;
  public long TransferLogin;

  /*[FieldOffset(160)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String sA0;

  /** Symbol currency */
  /*[FieldOffset(288)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 16)]*/ public String Currency;

  /** Lots */
  /*[FieldOffset(352)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong Lots;
  public long Lots;

  /*[FieldOffset(360)]*/ public long s168;

  /** Significant digits */
  /*[FieldOffset(368)]*/ public int Digits;

  /*[FieldOffset(372)]*/ public long s174;
  /*[FieldOffset(380)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 20)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s17C;
  public byte[] s17C;

  /** Order ticket */
  /*[FieldOffset(400)]*/ public long OrderTicket;

  /*[FieldOffset(408)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 64)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s198;
  public byte[] s198;
  /*[FieldOffset(472)]*/ public long s1D8;

  /** Expiration time */
  /*[FieldOffset(480)]*/ public long ExpirationTime;

  /** Order type */
  /*[FieldOffset(488)]*/ public OrderType OrderType;

  /** Fill policy */
  /*[FieldOffset(492)]*/ public FillPolicy FillPolicy;

  /** Expiration type */
  /*[FieldOffset(496)]*/ public ExpirationType ExpirationType =
      io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.ExpirationType.values()[0];

  /** Request flags */
  /*[FieldOffset(500)]*/ public long Flags;

  /** Placed type */
  /*[FieldOffset(508)]*/ public PlacedType PlacedType;

  /*[FieldOffset(512)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 16)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s200;
  public byte[] s200;

  /** Price */
  /*[FieldOffset(528)]*/ public double Price;

  /** Order price */
  /*[FieldOffset(536)]*/ public double OrderPrice;

  /** Stop loss */
  /*[FieldOffset(544)]*/ public double StopLoss;

  /** Take profit */
  /*[FieldOffset(552)]*/ public double TakeProfit;

  /** Deviation */
  /*[FieldOffset(560)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong Deviation;
  public long Deviation;

  /*[FieldOffset(568)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 32)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s238;
  public byte[] s238;

  /** Expert id */
  /*[FieldOffset(600)]*/ public long ExpertId;

  /** Text comment */
  /*[FieldOffset(608)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 16)]*/ public String Comment;

  /** Deal ticket */
  /*[FieldOffset(672)]*/ public long DealTicket;

  /** By close deal ticket */
  /*[FieldOffset(680)]*/ public long ByCloseTicket;

  /*[FieldOffset(688)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 112)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s2B0;
  public byte[] s2B0;

  // new internal string GetString(byte[] buf)
  // {
  //	int count = 0;
  //	for (int i = 0; i < buf.Length; i += 2)
  //	{
  //		if (buf[i] == 0 && buf[i + 1] == 0)
  //			break;
  //		count++;
  //	}
  //	byte[] res = new byte[count * 2];
  //	for (int i = 0; i < count * 2; i++)
  //		res[i] = buf[i];
  //	string result = Encoding.Unicode.GetString(res);
  //	return result;
  // }

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 800;
    TradeRequest st = new TradeRequest();
    st.RequestId = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s4 = new byte[64];
    st.s4 = new byte[64];
    for (int i = 0; i < 64; i++) {
      st.s4[i] = buf.Byte();
    }
    st.TradeType = TradeType.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.Login = BitConverter.ToUInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s50 = new byte[72];
    st.s50 = new byte[72];
    for (int i = 0; i < 72; i++) {
      st.s50[i] = buf.Byte();
    }
    st.TransferLogin = BitConverter.ToUInt64(buf.Bytes(8), 0);
    st.sA0 = GetString(buf.Bytes(128));
    st.Currency = GetString(buf.Bytes(64));
    st.Lots = BitConverter.ToUInt64(buf.Bytes(8), 0);
    st.s168 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Digits = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s174 = BitConverter.ToInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s17C = new byte[20];
    st.s17C = new byte[20];
    for (int i = 0; i < 20; i++) {
      st.s17C[i] = buf.Byte();
    }
    st.OrderTicket = BitConverter.ToInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s198 = new byte[64];
    st.s198 = new byte[64];
    for (int i = 0; i < 64; i++) {
      st.s198[i] = buf.Byte();
    }
    st.s1D8 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.ExpirationTime = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.OrderType = OrderType.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.FillPolicy = FillPolicy.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.ExpirationType = ExpirationType.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.Flags = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.PlacedType = PlacedType.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s200 = new byte[16];
    st.s200 = new byte[16];
    for (int i = 0; i < 16; i++) {
      st.s200[i] = buf.Byte();
    }
    st.Price = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.OrderPrice = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.StopLoss = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.TakeProfit = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Deviation = BitConverter.ToUInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s238 = new byte[32];
    st.s238 = new byte[32];
    for (int i = 0; i < 32; i++) {
      st.s238[i] = buf.Byte();
    }
    st.ExpertId = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Comment = GetString(buf.Bytes(64));
    st.DealTicket = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.ByCloseTicket = BitConverter.ToInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s2B0 = new byte[112];
    st.s2B0 = new byte[112];
    for (int i = 0; i < 112; i++) {
      st.s2B0[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }

  public final void WriteToBuf(OutBuf buf) {
    buf.Add(RequestId);
    buf.Add(s4, 64);
    buf.Add(TradeType.getValue());
    buf.Add(Login);
    buf.Add(s50, 72);
    buf.Add(TransferLogin);
    buf.Add(sA0, 32);
    buf.Add(Currency, 16);
    buf.Add(Lots);
    buf.Add(s168);
    buf.Add(Digits);
    buf.Add(s174);
    buf.Add(s17C, 20);
    buf.Add(OrderTicket);
    buf.Add(s198, 64);
    buf.Add(s1D8);
    buf.Add(ExpirationTime);
    buf.Add(OrderType.getValue());
    buf.Add(FillPolicy.getValue());
    buf.Add(ExpirationType.getValue());
    buf.Add(Flags);
    buf.Add(PlacedType.getValue());
    buf.Add(s200, 16);
    buf.Add(Price);
    buf.Add(OrderPrice);
    buf.Add(StopLoss);
    buf.Add(TakeProfit);
    buf.Add(Deviation);
    buf.Add(s238, 32);
    buf.Add(ExpertId);
    buf.Add(Comment, 16);
    buf.Add(DealTicket);
    buf.Add(ByCloseTicket);
    buf.Add(s2B0, 112);
  }
}
