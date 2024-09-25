package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x50, CharSet = CharSet.Unicode)]*/
public class C54 extends FromBufReader {
  /*[FieldOffset(0)]*/ public long s0;
  /*[FieldOffset(8)]*/ public long s8;
  /*[FieldOffset(16)]*/ public int s10;
  /*[FieldOffset(20)]*/ public int s14;
  /*[FieldOffset(24)]*/ public int s18;
  /*[FieldOffset(28)]*/ public int s1C;
  /*[FieldOffset(32)]*/ public long s20;
  /*[FieldOffset(40)]*/ public int s28;
  /*[FieldOffset(44)]*/ public int s2C;
  /*[FieldOffset(48)]*/ public int s30;
  /*[FieldOffset(52)]*/ public int s34;
  /*[FieldOffset(56)]*/ public int s38;
  /*[FieldOffset(60)]*/ public int s3C;
  /*[FieldOffset(64)]*/ public int s40;
  /*[FieldOffset(68)]*/ public int s44;
  /*[FieldOffset(72)]*/ public int s48;
  /*[FieldOffset(76)]*/ public int s4C;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 80;
    C54 st = new C54();
    st.s0 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s8 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s10 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s14 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s18 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s1C = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s20 = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.s28 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s2C = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s30 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s34 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s38 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s3C = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s40 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s44 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s48 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s4C = BitConverter.ToInt32(buf.Bytes(4), 0);
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
