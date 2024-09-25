package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x4E, CharSet = CharSet.Unicode)]*/
public class TickRec extends FromBufReader {
  /*[FieldOffset(0)]*/ public int Id;
  /*[FieldOffset(4)]*/ public long Time;
  /*[FieldOffset(12)]*/ public long TimeMs;
  /*[FieldOffset(20)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong UpdateMask;
  public long UpdateMask;
  /*[FieldOffset(28)]*/ public long Bid;
  /*[FieldOffset(36)]*/ public long Ask;
  /*[FieldOffset(44)]*/ public long Last;
  /*[FieldOffset(52)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ulong Volume;
  public long Volume;
  /*[FieldOffset(60)]*/ public long s3C;
  /*[FieldOffset(68)]*/ public long s44;
  /*[FieldOffset(76)]*/ public short BankId;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 78;
    TickRec st = new TickRec();
    st.Id = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.Time = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.TimeMs = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.UpdateMask = BitConverter.ToUInt64(buf.Bytes(8), 0);
    st.Bid = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Ask = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Last = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Volume = BitConverter.ToUInt64(buf.Bytes(8), 0);
    st.s3C = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s44 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.BankId = BitConverter.ToInt16(buf.Bytes(2), 0);
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
