package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x27C, CharSet = CharSet.Unicode)]*/
public class OrderInternal extends FromBufReader {
  /** Ticket number */
  /*[FieldOffset(0)]*/ public long TicketNumber;

  /** Text id */
  /*[FieldOffset(8)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Id;

  /** Account login */
  /*[FieldOffset(72)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong Login;
  public long Login;

  /*[FieldOffset(80)]*/ public long s50;

  /** Symbol currency */
  /*[FieldOffset(88)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Symbol;

  /** History time (as FileTime format) */
  /*[FieldOffset(152)]*/ public long HistoryTime;

  /** Open time */
  /*[FieldOffset(160)]*/ public long OpenTime;

  /** Expiration time */
  /*[FieldOffset(168)]*/ public long ExpirationTime;

  /** Execution time */
  /*[FieldOffset(176)]*/ public long ExecutionTime;

  /** Order type */
  /*[FieldOffset(184)]*/ public OrderType Type = OrderType.values()[0];

  /** Fill policy */
  /*[FieldOffset(188)]*/ public FillPolicy FillPolicy;

  /** Expiration type */
  /*[FieldOffset(192)]*/ public ExpirationDate ExpirationType = ExpirationDate.values()[0];

  /*[FieldOffset(196)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 8)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] sC4;
  public byte[] sC4;

  /** Placed type */
  /*[FieldOffset(204)]*/ public PlacedType PlacedType;

  /*[FieldOffset(208)]*/ public int sD0;

  /** Open price */
  /*[FieldOffset(212)]*/ public double OpenPrice;

  /** Order price */
  /*[FieldOffset(220)]*/ public double OrderPrice;

  /** Price */
  /*[FieldOffset(228)]*/ public double Price;

  /** Stop loss */
  /*[FieldOffset(236)]*/ public double StopLoss;

  /** Take profit */
  /*[FieldOffset(244)]*/ public double TakeProfit;

  /** Cover volume */
  /*[FieldOffset(252)]*/ public double Lots;

  /** Request volume */
  /*[FieldOffset(260)]*/ public long RequestVolume;

  /** Order state */
  /*[FieldOffset(268)]*/ public OrderState State = OrderState.values()[0];

  /** Expert id */
  /*[FieldOffset(272)]*/ public long ExpertId;

  /** Associative deal ticket */
  /*[FieldOffset(280)]*/ public long DealTicket;

  /** Comment text */
  /*[FieldOffset(288)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Comment;

  /** Lots */
  /*[FieldOffset(352)]*/ public double ContractSize;

  /** Significant digits */
  /*[FieldOffset(360)]*/ public int Digits;

  /** Symbols base significant digits */
  /*[FieldOffset(364)]*/ public int BaseDigits;

  /*[FieldOffset(368)]*/ public double s170;
  /*[FieldOffset(376)]*/ public double s178;
  /*[FieldOffset(384)]*/ public long s180;

  /** Profit rate */
  /*[FieldOffset(392)]*/ public double ProfitRate;

  /** Open time (ms) */
  /*[FieldOffset(400)]*/ public long OpenTimeMs;

  /*[FieldOffset(408)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 48)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s198;
  public byte[] s198;
  /*[FieldOffset(456)]*/ public int s1C8;
  /*[FieldOffset(460)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 176)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s1CC;
  public byte[] s1CC;

  /** Volume */
  private long Volume;

  /** Request lots */
  private double RequestLots;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 636;
    OrderInternal st = new OrderInternal();
    st.TicketNumber = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Id = GetString(buf.Bytes(64));
    st.Login = BitConverter.ToUInt64(buf.Bytes(8), 0);
    st.s50 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Symbol = GetString(buf.Bytes(64));
    st.HistoryTime = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.OpenTime = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.ExpirationTime = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.ExecutionTime = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Type = OrderType.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.FillPolicy = FillPolicy.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.ExpirationType = ExpirationDate.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.sC4 = new byte[8];
    st.sC4 = new byte[8];
    for (int i = 0; i < 8; i++) {
      st.sC4[i] = buf.Byte();
    }
    st.PlacedType = PlacedType.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.sD0 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.OpenPrice = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.OrderPrice = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Price = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.StopLoss = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.TakeProfit = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Volume = BitConverter.ToUInt64(buf.Bytes(8), 0);
    st.Lots = BigDecimal.valueOf(st.Volume).divide(BigDecimal.valueOf(100000000)).doubleValue();
    st.RequestVolume = BitConverter.ToUInt64(buf.Bytes(8), 0);
    st.RequestLots = (double) st.RequestVolume / 100000000;
    st.State = OrderState.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.ExpertId = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.DealTicket = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Comment = GetString(buf.Bytes(64));
    st.ContractSize = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Digits = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.BaseDigits = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s170 = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s178 = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s180 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.ProfitRate = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.OpenTimeMs = BitConverter.ToInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s198 = new byte[48];
    st.s198 = new byte[48];
    for (int i = 0; i < 48; i++) {
      st.s198[i] = buf.Byte();
    }
    st.s1C8 = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s1CC = new byte[176];
    st.s1CC = new byte[176];
    for (int i = 0; i < 176; i++) {
      st.s1CC[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }

  public boolean IsAssociativeDealOrder() {
    return IsTradeOrder() && DealTicket != 0 && (TicketNumber == 0 || (DealTicket != TicketNumber));
  }

  public boolean IsTradeOrder() {
    return (Type == OrderType.Buy) || (Type == OrderType.Sell);
  }

  public boolean IsLimitOrder() {
    return (Type == OrderType.BuyLimit) || (Type == OrderType.SellLimit);
  }

  public boolean IsStopOrder() {
    return (Type == OrderType.BuyStop) || (Type == OrderType.SellStop);
  }

  public boolean IsBuyOrder() {
    return (Type == OrderType.Buy)
        || (Type == OrderType.BuyStop)
        || (Type == OrderType.BuyLimit)
        || (Type == OrderType.BuyStopLimit);
  }

  public boolean IsStopLimitOrder() {
    return (Type == OrderType.BuyStopLimit) || (Type == OrderType.SellStopLimit);
  }

  public LocalDateTime OpenTimeAsDateTime() {
    return ConvertTo.DateTime(OpenTime);
  }
}
