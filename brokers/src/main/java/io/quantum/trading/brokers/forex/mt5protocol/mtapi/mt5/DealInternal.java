package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x2A0, CharSet = CharSet.Unicode)]*/
public class DealInternal extends FromBufReader {
  /** Deal ticket */
  /*[FieldOffset(0)]*/ public long TicketNumber;

  /** Text id */
  /*[FieldOffset(8)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Id;

  /** Account login */
  /*[FieldOffset(72)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong Login;
  public long Login;

  /** History time (as FileTime format) */
  /*[FieldOffset(80)]*/ public long HistoryTime;

  /** Order ticket */
  /*[FieldOffset(88)]*/ public long OrderTicket;

  /*[FieldOffset(96)]*/ public long s60;

  /** Open time */
  /*[FieldOffset(104)]*/ public long OpenTime;

  /*[FieldOffset(112)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 8)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s70;
  public byte[] s70;

  /** Symbol currency */
  /*[FieldOffset(120)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Symbol;

  /** Deal type */
  /*[FieldOffset(184)]*/ public DealType Type;

  /** Deal direction */
  /*[FieldOffset(188)]*/ public Direction Direction;

  /** Open price */
  /*[FieldOffset(192)]*/ public double OpenPrice;

  /** Price */
  /*[FieldOffset(200)]*/ public double Price;

  /** Stop loss */
  /*[FieldOffset(208)]*/ public double StopLoss;

  /** Take profit */
  /*[FieldOffset(216)]*/ public double TakeProfit;

  /** Volume */
  /*[FieldOffset(224)]*/ public double Lots;

  /** Profit (money digits) */
  /*[FieldOffset(232)]*/ public double Profit;

  /** Profit rate */
  /*[FieldOffset(240)]*/ public double ProfitRate;

  /** Volume rate */
  /*[FieldOffset(248)]*/ public double VolumeRate;

  /** Commission (money digits) */
  /*[FieldOffset(256)]*/ public double Commission;

  /*[FieldOffset(264)]*/ public double s108;

  /** Swap */
  /*[FieldOffset(272)]*/ public double Swap;

  /** Expert id */
  /*[FieldOffset(280)]*/ public long ExpertId;

  /** Position ticket */
  /*[FieldOffset(288)]*/ public long PositionTicket;

  /** Text comment */
  /*[FieldOffset(296)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Comment;

  /** Lots */
  /*[FieldOffset(360)]*/ public double ContractSize;

  /** Significant digits */
  /*[FieldOffset(368)]*/ public int Digits;

  /** Money significant digits */
  /*[FieldOffset(372)]*/ public int MoneyDigits;

  /*[FieldOffset(376)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 132)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s178;
  public byte[] s178;

  /** Free profit */
  /*[FieldOffset(508)]*/ public double FreeProfit;

  /*[FieldOffset(516)]*/ public long s204;
  /*[FieldOffset(524)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 8)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s20C;
  public byte[] s20C;

  /** Trail rounder */
  /*[FieldOffset(532)]*/ public double TrailRounder;

  /*[FieldOffset(540)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 8)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s21C;
  public byte[] s21C;

  /** Open time (ms) */
  /*[FieldOffset(548)]*/ public long OpenTimeMs;

  /*[FieldOffset(556)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 8)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s22C;
  public byte[] s22C;
  /*[FieldOffset(564)]*/ public long s234;
  /*[FieldOffset(572)]*/ public long s23C;
  /*[FieldOffset(580)]*/ public long s244;
  /*[FieldOffset(588)]*/ public long s24C;
  /*[FieldOffset(596)]*/ public long s254;

  /** Placed type */
  /*[FieldOffset(604)]*/ public PlacedType PlacedType;

  /*[FieldOffset(608)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 64)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s260;
  public byte[] s260;

  /** Volume */
  public long Volume;

  public int PlacedTypeInt;
  public int TypeInt;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 672;
    DealInternal st = new DealInternal();
    st.TicketNumber = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Id = GetString(buf.Bytes(64));
    st.Login = BitConverter.ToUInt64(buf.Bytes(8), 0);
    st.HistoryTime = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.OrderTicket = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s60 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.OpenTime = BitConverter.ToInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s70 = new byte[8];
    st.s70 = new byte[8];
    for (int i = 0; i < 8; i++) {
      st.s70[i] = buf.Byte();
    }
    st.Symbol = GetString(buf.Bytes(64));
    st.TypeInt = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.Type = DealType.forValue(st.TypeInt);
    st.Direction = Direction.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.OpenPrice = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Price = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.StopLoss = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.TakeProfit = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Volume = BitConverter.ToUInt64(buf.Bytes(8), 0);
    st.Lots = BigDecimal.valueOf(st.Volume).divide(BigDecimal.valueOf(100000000)).doubleValue();
    st.Profit = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.ProfitRate = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.VolumeRate = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Commission = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s108 = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Swap = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.ExpertId = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.PositionTicket = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Comment = GetString(buf.Bytes(64));
    st.ContractSize = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Digits = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.MoneyDigits = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s178 = new byte[132];
    st.s178 = new byte[132];
    for (int i = 0; i < 132; i++) {
      st.s178[i] = buf.Byte();
    }
    st.FreeProfit = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s204 = BitConverter.ToInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s20C = new byte[8];
    st.s20C = new byte[8];
    for (int i = 0; i < 8; i++) {
      st.s20C[i] = buf.Byte();
    }
    st.TrailRounder = BitConverter.ToDouble(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s21C = new byte[8];
    st.s21C = new byte[8];
    for (int i = 0; i < 8; i++) {
      st.s21C[i] = buf.Byte();
    }
    st.OpenTimeMs = BitConverter.ToInt64(buf.Bytes(8), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s22C = new byte[8];
    st.s22C = new byte[8];
    for (int i = 0; i < 8; i++) {
      st.s22C[i] = buf.Byte();
    }
    st.s234 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s23C = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s244 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s24C = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s254 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.PlacedTypeInt = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.PlacedType = PlacedType.forValue(st.PlacedTypeInt);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s260 = new byte[64];
    st.s260 = new byte[64];
    for (int i = 0; i < 64; i++) {
      st.s260[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }

  public LocalDateTime OpenTimeAsDateTime() {
    return ConvertTo.DateTime(OpenTime);
  }
}
