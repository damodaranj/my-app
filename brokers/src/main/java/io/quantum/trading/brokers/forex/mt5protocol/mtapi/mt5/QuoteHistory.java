package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.IOException;
import java.util.*;

public class QuoteHistory {
  private MT5API QuoteClient;

  public QuoteHistory(MT5API qc) {
    QuoteClient = qc;
  }

  public static void ReqProcess(Connection connection, String symbol) throws IOException {
    OutBuf buf = new OutBuf();
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: buf.Add((byte)0xE);
    buf.Add((byte) (0xE & 0xFF));
    // C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
    byte[] bytes = symbol.getBytes(java.nio.charset.StandardCharsets.UTF_16LE);
    buf.Add(bytes);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: buf.Add(new byte[32 * 2 - bytes.Length]);
    buf.Add(new byte[32 * 2 - bytes.length]);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: buf.Add((ushort)1);
    buf.Add((short) (1 & 0xFFFF)); // size
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: buf.Add((byte)0);
    buf.Add((byte) (0 & 0xFF)); // year
    buf.Add((int) 0); // time
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: buf.Add((ushort)0);
    buf.Add((short) (0 & 0xFFFF)); // CheckDate
    connection.SendPacket((byte) 0x66, buf);
  }

  public static void ReqStart(Connection connection, String symbol, short date) throws IOException {
    OutBuf buf;
    buf = new OutBuf();
    buf.Add((byte) 0xE);
    byte[] bytes = symbol.getBytes(java.nio.charset.StandardCharsets.UTF_16LE);
    buf.Add(bytes);
    buf.Add(new byte[32 * 2 - bytes.length]);
    buf.Add((short) 0); // size
    // buf.Add((byte)0); //year
    // buf.Add((int)0); //time
    buf.Add(date); // CheckDate
    connection.SendPacket((byte) 0x66, buf);
  }

  public static void ReqSend(Connection connection, String symbol, short a, short b)
      throws IOException {
    OutBuf buf = new OutBuf();
    buf.Add((byte) 9);
    byte[] bytes = symbol.getBytes(java.nio.charset.StandardCharsets.UTF_16LE);
    buf.Add(bytes);
    buf.Add(new byte[32 * 2 - bytes.length]);
    buf.Add((short) a); // begin
    buf.Add((short) b); // firstDate
    connection.SendPacket((byte) 0x66, buf);
  }

  public static void ReqSelect(Connection connection, String symbol) throws IOException {
    OutBuf buf = new OutBuf();
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: buf.Add((byte)9);
    buf.Add((byte) (9 & 0xFF));
    // C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
    byte[] bytes = symbol.getBytes(java.nio.charset.StandardCharsets.UTF_16LE);
    buf.Add(bytes);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: buf.Add(new byte[32 * 2 - bytes.Length]);
    buf.Add(new byte[32 * 2 - bytes.length]);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: buf.Add((ushort)1);
    buf.Add((short) (1 & 0xFFFF)); // begin
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: buf.Add((ushort)1);
    buf.Add((short) (1 & 0xFFFF)); // firstDate
    connection.SendPacket((byte) 0x66, buf);
  }

  public final void Parse(InBuf buf) {
    byte cmd = buf.Byte();
    String symbol = ConvertBytes.ToUnicode(buf.Bytes(64));
    switch ((cmd & 0xFF)) {
      case 0x0E:
        ParseStart(buf, symbol);
        break;
      case 9:
        QuoteClient.OnQuoteHistory(symbol, ParseSelect(buf, symbol));
        break;
      default:
        throw new UnsupportedOperationException("Parse quote hist cmd = " + (cmd & 0xFF));
    }
  }

  public final List<Bar> ParseSelect(InBuf buf, String symbol) {
    int status = buf.Int();
    if (status != 0) {
      throw new RuntimeException((Msg.forValue(status)).toString());
    }
    short num = buf.UShort();
    LinkedList<Bar> bars = new LinkedList<>();
    for (int i = 0; i < (num & 0xFFFF); i++) bars.addAll(0, ParseContainer(buf, symbol));
    return bars;
  }

  public final void ParseStart(InBuf buf, String symbol) {
    int status = buf.Int();
    if (status != 0) {
      throw new RuntimeException((Msg.forValue(status)).toString());
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: var day = buf.UShort();
    short day = buf.UShort();
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: var num = buf.UShort();
    short num = buf.UShort();
    if ((num & 0xFFFF) == 0) {
      ParseContainer(buf, symbol);
    } else {
      throw new UnsupportedOperationException("num != 0");
    }
  }

  private LinkedList<Bar> ParseContainer(InBuf buf, String symbol) {
    HistHeader hdr = UDT.ReadStruct(buf.Bytes(0x81), 0, 0x81, new HistHeader());
    if ((hdr.Flags & 1) != 0) {
      throw new RuntimeException("Compressed");
    }
    byte[] data = buf.Bytes(hdr.DataSize);
    return ReadBarRecords(new BitReader(data, hdr), hdr, symbol);
  }

  private LinkedList<Bar> ReadBarRecords(BitReader btr, HistHeader hdr, String symbol) {
    long flags = 0;
    boolean bSpread = false;
    boolean bVolume = false;
    int numBars = 0;
    LinkedList<Bar> bars = new LinkedList<Bar>();
    BarRecord rec = new BarRecord();
    while ((btr.BitPos <= btr.BitSize) && (numBars < hdr.NumberBars)) {
      long type = BitConverter.ToInt64(btr.GetRecord(8), 0);
      if (type == 0) {
        flags = BitConverter.ToUInt64(btr.GetRecord(8), 0);
        rec.Time = BitConverter.ToInt64(btr.GetRecord(8), 0);
        rec.OpenPrice = BitConverter.ToInt32(btr.GetSignRecord(8), 0);
        rec.High = BitConverter.ToInt32(btr.GetRecord(4), 0);
        rec.Low = BitConverter.ToInt32(btr.GetRecord(4), 0);
        rec.Close = BitConverter.ToInt32(btr.GetSignRecord(4), 0);
        rec.TickVolume = BitConverter.ToUInt64(btr.GetRecord(8), 0);
        if ((flags & 1) != 0) {
          bSpread = true;
          rec.Spread = BitConverter.ToInt32(btr.GetSignRecord(4), 0);
        }
        if ((flags & 2) != 0) {
          bVolume = true;
          rec.Volume = BitConverter.ToUInt64(btr.GetRecord(8), 0);
        }
        btr.SkipRecords(flags, 4);
        bars.add(RecordToBar(rec.clone(), hdr.Digits).clone());
        numBars++;
      } else if (type == 1) {
        long num = btr.GetLong();
        for (long i = 0; i < num; i++) {
          rec.Time += 60;
          long value = btr.GetSignLong();
          rec.OpenPrice += (hdr.LimitPoints & 0xFFFFFFFFL) * value + rec.Close;
          int data = btr.GetInt();
          rec.High = (int) ((hdr.LimitPoints & 0xFFFFFFFFL) * data);
          data = btr.GetInt();
          rec.Low = (int) ((hdr.LimitPoints & 0xFFFFFFFFL) * data);
          value = btr.GetSignLong();
          rec.Close = (int) ((hdr.LimitPoints & 0xFFFFFFFFL) * (int) value);
          rec.TickVolume = btr.GetULong();
          if (bSpread) {
            rec.Spread = btr.GetSignInt();
          }
          if (bVolume) {
            rec.Volume = btr.GetULong();
          }
          btr.SkipRecords(flags, 4);
          bars.add(RecordToBar(rec.clone(), hdr.Digits).clone());
          numBars++;
        }
      } else if (type == 2) {
        long value = btr.GetLong();
        rec.Time += value * 60;
      }
    }
    return bars;
    // QuoteClient.OnQuoteHistory(symbol, bars.toArray(new Bar[0]));
    // m_Hdr.m_nBitSize = btr.m_nBitPos;
    // if (!numBars)
    //    return true;
    // int i = firstPos;
    // while ((TimeToDate(arrBar[i].m_lTime) != m_Hdr.m_Date) && (i < arrBar.GetSize()))
    //    i++;
    // int removed = 0;
    // if (i != firstPos)
    // {
    //    removed = i - firstPos + 1;
    //    arrBar.ShrinkTo(firstPos, i);
    // }
    // if (arrBar.GetSize())
    // {
    //    i = arrBar.GetSize() - 1;
    //    while ((TimeToDate(arrBar[i].m_lTime) != m_Hdr.m_Date) && (i > firstPos))
    //        i--;
    //    if (i != arrBar.GetSize() - 1)
    //    {
    //        removed = arrBar.GetSize() - i;
    //        arrBar.SetSize(i);
    //    }
    // }
  }

  private Bar RecordToBar(BarRecord rec, int digits) {
    Bar bar = new Bar();
    bar.Time = ConvertTo.DateTime(rec.Time);
    bar.OpenPrice = ConvertTo.LongLongToDouble(digits, rec.OpenPrice);
    bar.HighPrice = ConvertTo.LongLongToDouble(digits, rec.OpenPrice + rec.High);
    bar.LowPrice = ConvertTo.LongLongToDouble(digits, rec.OpenPrice - rec.Low);
    bar.ClosePrice = ConvertTo.LongLongToDouble(digits, rec.OpenPrice + rec.Close);
    bar.Volume = rec.Volume;
    bar.TickVolume = rec.TickVolume;
    bar.Spread = rec.Spread;
    return bar;
  }
}
/*
 * bool vHistorySymbol::SendRequest()
{
	if (m_bAbsentSymbol || (m_pClient->GetServerStatus() != vAcceptAccount) ||
		(m_History.m_Mode == vMode_StartRequest) || m_History.m_BeginHistory.IsEmpty())
		return false;
	vDate firstDate = m_History.m_BeginDay;
	if (firstDate != vDate(0, 1, 1))
		firstDate = TimeToDate(DateToTime(firstDate) - 24 * 3600);
	vDate begin = vDate(0, 1, 1);
	if ((m_History.m_BeginHistory != vDate(0, 1, 1)) && (firstDate != vDate(0, 1, 1)))
	{
		begin = m_History.m_BeginHistory;
		if (firstDate < begin)
			return false;
	}
	vSockBufManager bufMan(0);
	bufMan.ByteToBuffer(9);
	bufMan.DataToBuffer(m_Symbol.m_SymInfo.m_sCurrency, 32 * sizeof(wchar_t));
	bufMan.DataToBuffer(&begin, 2);
	bufMan.DataToBuffer(&firstDate, 2);
#ifdef DEBUG_QH
	vString<256> sDate1, sDate2;
	s_pLogger->LogMessage(vMessage, L"vHistory", L"SendRequest: 9 %s, Date1 %s, Date2 %s", m_Symbol.m_SymInfo.m_sCurrency,
		sDate1.DateToString(DateToTime(begin)), sDate2.DateToString(DateToTime(firstDate)));
#endif
	if (m_pClient->SendPacket(0x66, &bufMan))
		return true;
	s_pLogger->LogMessage(vFatal, L"History", L"'%s' request sending failed [%u]", m_Symbol.m_SymInfo.m_sCurrency, GetLastError());
	return false;
}

 */
