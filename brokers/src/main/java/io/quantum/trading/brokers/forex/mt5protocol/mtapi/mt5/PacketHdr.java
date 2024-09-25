package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Size = 0x09, CharSet = CharSet.Unicode)]*/
public class PacketHdr extends FromBufReader // sizeof 0x09 c
{
  /*[FieldOffset(0)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte Type;
  public byte Type; // 0
  /*[FieldOffset(1)]*/ public int PacketSize; // 1
  /*[FieldOffset(5)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ushort Id;
  public short Id; // 5
  /*[FieldOffset(7)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ushort Flags;
  public short Flags; // 7

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 9;
    PacketHdr st = new PacketHdr(); // sizeof 0x09 c();
    st.Type = buf.Byte();
    st.PacketSize = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.Id = BitConverter.ToInt16(buf.Bytes(2), 0);
    st.Flags = BitConverter.ToInt16(buf.Bytes(2), 0);
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
