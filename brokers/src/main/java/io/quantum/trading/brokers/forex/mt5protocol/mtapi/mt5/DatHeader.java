package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/*[StructLayout(LayoutKind.Explicit, Pack = 1, Size = 0x1AC, CharSet = CharSet.Unicode)]*/
public class DatHeader extends FromBufReader {
  /*[FieldOffset(0)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public uint Id;
  public int Id;
  /*[FieldOffset(4)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 64)]*/ public String Copyright;
  /*[FieldOffset(132)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValTStr, SizeConst = 16)]*/ public String DataType;
  /*[FieldOffset(164)]*/ public long FileTime;
  /*[FieldOffset(172)]*/ public int ObjNumber;
  /*[FieldOffset(176)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 16)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] Md5Key;
  public byte[] Md5Key;
  /*[FieldOffset(192)]*/
  /*[MarshalAsAttribute(UnmanagedType.ByValArray, SizeConst = 228)]*/
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] sC0;
  public byte[] sC0;
  /*[FieldOffset(420)]*/ public int s1A4;
  /*[FieldOffset(424)]*/ public int s1A8;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 428;
    DatHeader st = new DatHeader();
    st.Id = (int) BitConverter.ToUInt32(buf.Bytes(4), 0);
    st.Copyright = GetString(buf.Bytes(128));
    st.DataType = GetString(buf.Bytes(32));
    st.FileTime = BitConverter.ToInt64(buf.Bytes(8), 0);
    st.ObjNumber = BitConverter.ToInt32(buf.Bytes(4), 0);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.Md5Key = new byte[16];
    st.Md5Key = new byte[16];
    for (int i = 0; i < 16; i++) {
      st.Md5Key[i] = buf.Byte();
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: st.sC0 = new byte[228];
    st.sC0 = new byte[228];
    for (int i = 0; i < 228; i++) {
      st.sC0[i] = buf.Byte();
    }
    st.s1A4 = BitConverter.ToInt32(buf.Bytes(4), 0);
    st.s1A8 = BitConverter.ToInt32(buf.Bytes(4), 0);
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
