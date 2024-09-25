package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x188, CharSet = CharSet.Unicode)]*/
public class SymbolSet extends FromBufReader {
  /*[FieldOffset(0)]*/ public long UpdateTime;
  /*[FieldOffset(8)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 128)]*/ public String GroupNames;
  /*[FieldOffset(264)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String s108;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 392;
    SymbolSet st = new SymbolSet();
    st.UpdateTime = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.GroupNames = GetString(buf.Bytes(256));
    st.s108 = GetString(buf.Bytes(128));
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
