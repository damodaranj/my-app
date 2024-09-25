package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x94, CharSet = CharSet.Unicode)]*/
public class AddressRec extends FromBufReader {
  public static final int Size = 148;
  /*[FieldOffset(0)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/
  public String Address;
  /*[FieldOffset(128)]*/ public int s80;
  /*[FieldOffset(132)]*/ public int s84;
  /*[FieldOffset(136)]*/ public int s88;
  /*[FieldOffset(140)]*/ public int s8C;
  /*[FieldOffset(144)]*/ public int s90;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 148;
    AddressRec st = new AddressRec();
    st.Address = GetString(buf.Bytes(128));
    st.s80 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s84 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s88 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s8C = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s90 = BitConverter.ToInt32(buf.Bytes(4), 0);
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
