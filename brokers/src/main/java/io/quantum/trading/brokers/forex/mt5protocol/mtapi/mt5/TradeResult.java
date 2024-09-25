package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x104, CharSet = CharSet.Unicode)]*/
public class TradeResult extends FromBufReader {
  /*[FieldOffset(0)]*/ public Msg Status = Msg.values()[0];
  /*[FieldOffset(4)]*/ public long PositionId;
  /*[FieldOffset(12)]*/ public long TicketNumber;
  /*[FieldOffset(20)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong Volume;
  public long Volume;
  /*[FieldOffset(28)]*/ public double OpenPrice;
  /*[FieldOffset(36)]*/ public int s0;
  /*[FieldOffset(40)]*/ public int s4;
  /*[FieldOffset(44)]*/ public double Bid;
  /*[FieldOffset(52)]*/ public double Ask;
  /*[FieldOffset(60)]*/ public double Last;
  /*[FieldOffset(68)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 32)]*/ public String Comment;
  /*[FieldOffset(132)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 128)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] s84;
  public byte[] s84;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 260;
    TradeResult st = new TradeResult();
    st.Status = Msg.forValue(BitConverter.ToInt32(buf.Bytes(4), 0));
    st.PositionId = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.TicketNumber = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Volume = BitConverter.ToUInt64(buf.Bytes(8), 0);
    st.OpenPrice = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.s0 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s4 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.Bid = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Ask = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Last = BitConverter.ToDouble(buf.Bytes(8), 0);
    st.Comment = GetString(buf.Bytes(64));
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.s84 = new byte[128];
    st.s84 = new byte[128];
    for (int i = 0; i < 128; i++) {
      st.s84[i] = buf.Byte();
    }
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
