package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x88, CharSet = CharSet.Unicode)]*/
public class MailRecipient extends FromBufReader {
  /*[FieldOffset(0)]*/ public long Id;
  /*[FieldOffset(8)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String Name;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 136;
    MailRecipient st = new MailRecipient();
    st.Id = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.Name = GetString(buf.Bytes(128));
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
