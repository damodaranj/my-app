package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x28, CharSet = CharSet.Unicode)]*/
public class Session extends FromBufReader {
  /** Start time (in minites) */
  /*[FieldOffset(0)]*/ public int StartTime;

  /** End time (in minutes) */
  /*[FieldOffset(4)]*/ public int EndTime;

  /*[FieldOffset(8)]*/ public int s8;
  /*[FieldOffset(12)]*/ public int sC;
  /*[FieldOffset(16)]*/ public int s10;
  /*[FieldOffset(20)]*/ public int s14;
  /*[FieldOffset(24)]*/ public int s18;
  /*[FieldOffset(28)]*/ public int s1C;
  /*[FieldOffset(32)]*/ public int s20;
  /*[FieldOffset(36)]*/ public int s24;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 40;
    Session st = new Session();
    st.StartTime = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.EndTime = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s8 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.sC = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s10 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s14 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s18 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s1C = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s20 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s24 = BitConverter.ToInt32(buf.Bytes(4), 0);
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
