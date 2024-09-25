package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x81, CharSet = CharSet.Unicode)]*/
public class HistHeader extends FromBufReader {
  /*[FieldOffset(0)]*/ public short HdrSize;
  /*[FieldOffset(2)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Currency;
  /*[FieldOffset(66)]*/ public short s42;
  /*[FieldOffset(68)]*/ public short Date;
  /*[FieldOffset(70)]*/ public short s46;
  /*[FieldOffset(72)]*/ public int DataSize;
  /*[FieldOffset(76)]*/ public int InflateSize;
  /*[FieldOffset(80)]*/ public int BitSize;
  /*[FieldOffset(84)]*/ public int NumberBars;
  /*[FieldOffset(88)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte AlignBit;
  public byte AlignBit;
  /*[FieldOffset(89)]*/ public int Time;
  /*[FieldOffset(93)]*/ public short Digits;
  /*[FieldOffset(95)]*/ public short Flags;
  /*[FieldOffset(97)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public uint LimitPoints;
  public int LimitPoints;
  /*[FieldOffset(101)]*/ public int Spread;
  /*[FieldOffset(105)]*/ public float fSwapLong;
  /*[FieldOffset(109)]*/ public float fSwapShort;
  /*[FieldOffset(113)]*/ public long s71;
  /*[FieldOffset(121)]*/ public long s79;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 129;
    HistHeader st = new HistHeader();
    st.HdrSize = BitConverter.ToInt16(buf.Bytes(2), 0);
    st.Currency = GetString(buf.Bytes(64));
    st.s42 = BitConverter.ToInt16(buf.Bytes(2), 0);
    st.Date = BitConverter.ToInt16(buf.Bytes(2), 0);
    st.s46 = BitConverter.ToInt16(buf.Bytes(2), 0);
    st.DataSize = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.InflateSize = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.BitSize = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.NumberBars = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.AlignBit = buf.Byte();
    st.Time = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.Digits = BitConverter.ToInt16(buf.Bytes(2), 0);
    st.Flags = BitConverter.ToInt16(buf.Bytes(2), 0);
    st.LimitPoints = (int) BitConverter.ToUInt32(buf.Bytes(4), 0);
    st.Spread = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.fSwapLong = (float) BitConverter.ToInt32(buf.Bytes(4), 0);
    st.fSwapShort = (float) BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s71 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s79 = BitConverter.ToInt64(buf.Bytes(8), 0);
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
