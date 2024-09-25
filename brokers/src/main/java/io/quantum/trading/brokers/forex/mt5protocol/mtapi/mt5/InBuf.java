package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.lang.reflect.Array;
import java.util.*;

class InBuf {
  short SymBuild;
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: byte[] Buf;
  byte[] Buf;
  int Ind;
  public PacketHdr Hdr;

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public InBuf(byte[] buf, int start)
  public InBuf(byte[] buf, int start) {
    Buf = buf;
    Ind = start;
  }

  // C# TO JAVA CONVERTER TODO TASK: The C# 'new()' constraint has no equivalent in Java:
  // ORIGINAL LINE: public T Struct<T>() where T : FromBufReader, new()
  public final <T extends FromBufReader> T Struct(T t) {
    // var size = Marshal.SizeOf(typeof(T));
    T res = UDT.<T>ReadStruct(this, t);
    // Ind += size;
    return res;
  }

  // C# TO JAVA CONVERTER TODO TASK: The C# 'new()' constraint has no equivalent in Java:
  // ORIGINAL LINE: public T CryptStruct<T>(int size) where T : FromBufReader, new()
  public final <T extends FromBufReader> T CryptStruct(int size, T t) {
    // var size = Marshal.SizeOf(typeof(T));
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: var bytes = Bytes(size);
    byte[] bytes = Bytes(size);
    Crypt.EasyDecrypt(bytes);
    return UDT.<T>ReadStruct(bytes, 0, size, t);
  }

  // C# TO JAVA CONVERTER TODO TASK: The C# 'new()' constraint has no equivalent in Java:
  // ORIGINAL LINE: public T[] CryptArray<T>(int size) where T : FromBufReader, new()
  public final <T extends FromBufReader> T[] CryptArray(int size, T t) {
    int num = Int();
    T[] ar = (T[]) Array.newInstance(t.getClass(), num);
    for (int i = 0; i < num; i++) {
      ar[i] = this.<T>CryptStruct(size, t);
    }
    return ar;
  }

  // C# TO JAVA CONVERTER TODO TASK: The C# 'new()' constraint has no equivalent in Java:
  // ORIGINAL LINE: public T[] Array<T>() where T : FromBufReader, new()
  public final <T extends FromBufReader> T[] Array(T t) {
    int num = Int();
    T[] ar = (T[]) Array.newInstance(t.getClass(), num);
    for (int i = 0; i < num; i++) {
      ar[i] = this.<T>Struct(t);
    }
    return ar;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public void SetBuf(byte[] buf)
  public final void SetBuf(byte[] buf) {
    Buf = buf;
    Hdr.PacketSize = buf.length;
    Ind = 0;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public InBuf(byte[] buf, PacketHdr hdr)
  public InBuf(byte[] buf, PacketHdr hdr) {
    Buf = buf;
    Hdr = hdr;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] ToBytes()
  public final byte[] ToBytes() {
    return Buf;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte Byte()
  public final byte Byte() {
    return Buf[Ind++];
  }

  public final int getLeft() {
    return Buf.length - Ind;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] Bytes(int count)
  public final byte[] Bytes(int count) {
    if (Ind + count > Buf.length) {
      throw new RuntimeException("Not enough data");
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: var res = new byte[count];
    byte[] res = new byte[count];
    for (int i = 0; i < count; i++) {
      // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
      // ORIGINAL LINE: res[i] = Byte();
      res[i] = Byte();
    }
    return res;
  }

  public final int Int() {

    int res = BitConverter.ToInt32(Buf, Ind);
    Ind += 4;
    return res;
  }

  public final long Long() {
    long res = BitConverter.ToInt64(Buf, Ind);
    Ind += 8;
    return res;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public ushort UShort()
  public final short Short() {

    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: ushort res = BitConverter.ToUInt16(Buf, Ind);
    short res = BitConverter.ToInt16(Buf, Ind);
    Ind += 2;
    return res;
  }

  public final String Str() {

    int sz = Int();
    String res = UDT.readString(Buf, Ind, sz);
    Ind += sz;
    return res;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] ByteAr()
  public final byte[] ByteAr() {

    int sz = Int();
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: var res = new byte[sz];
    byte[] res = new byte[sz];
    System.arraycopy(Buf, Ind, res, 0, sz);
    Ind += sz;
    return res;
  }

  public final boolean gethasData() {
    return Ind < Buf.length;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal void AddRange(List<byte> list)
  public final void AddRange(ArrayList<Byte> list) {
    if (Ind > 0) {
      throw new RuntimeException("Cannot insert");
    }
    ByteLists.addPrimitiveArrayToList(Buf, list);
    Buf = ByteLists.toArray(list);
  }

  public short UShort() {
    return Short();
  }

  public HashMap<Long, List<DealInternal>> ArrayDeal() {
    int num = Int();
    HashMap<Long, List<DealInternal>> res = new HashMap<Long, List<DealInternal>>();
    for (int i = 0; i < num; i++) {
      DealInternal deal = Struct(new DealInternal());
      if (!res.containsKey(deal.PositionTicket))
        res.put(deal.PositionTicket, new LinkedList<DealInternal>());
      res.get(deal.PositionTicket).add(deal);
    }
    return res;
  }

  // public void Skip()
  // {
  //    Ind += Int();
  // }
}
