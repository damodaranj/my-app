package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.util.*;

class OutBuf {

  public ArrayList<Byte> List;

  public OutBuf() {
    List = new ArrayList<Byte>();
  }

  public OutBuf(byte[] bytes) {
    List = convertBytesToList(bytes);
  }

  private static ArrayList<Byte> convertBytesToList(byte[] bytes) {
    final ArrayList<Byte> list = new ArrayList<>();
    for (byte b : bytes) {
      list.add(b);
    }
    return list;
  }

  public final void CreateHeader(byte type, int id, boolean compressed) {
    byte[] hdr = new byte[9];
    hdr[0] = type; // type
    System.arraycopy(
        BitConverter.GetBytes(List.size()),
        0,
        hdr,
        1,
        BitConverter.GetBytes(List.size()).length); // size
    System.arraycopy(
        BitConverter.GetBytes((short) id),
        0,
        hdr,
        5,
        BitConverter.GetBytes((short) id).length); // ID
    if (compressed) {
      System.arraycopy(
          BitConverter.GetBytes((short) 3), 0, hdr, 7, BitConverter.GetBytes((short) 3).length);
    } else {
      System.arraycopy(
          BitConverter.GetBytes((short) 2),
          0,
          hdr,
          7,
          BitConverter.GetBytes((short) 2).length); // Flags PHF_COMPLETE
    }
    ByteLists.addPrimitiveArrayToList(0, hdr, List);
  }

  public final byte[] ToArray() {
    return ByteLists.toArray(List);
  }

  public final void Add(byte b) {
    List.add(b);
  }

  public final void Add(long l) {
    Add(BitConverter.GetBytes(l));
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public void Add(ushort l)
  public final void Add(short l) {
    Add(BitConverter.GetBytes(l));
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public void Add(uint l)
  public final void Add(int l) {
    Add(BitConverter.GetBytes(l));
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public void Add(byte[] bytes)
  public final void Add(byte[] bytes) {
    ByteLists.addPrimitiveArrayToList(bytes, List);
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal void ByteToBuffer(byte v)
  public final void ByteToBuffer(byte v) {
    Add(v);
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal void LongToBuffer(uint v)
  public final void LongToBuffer(int v) {
    Add(v);
  }

  public final void IntToBuffer(int v) {
    Add(v);
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal void WordToBuffer(ushort v)
  public final void WordToBuffer(short v) {
    Add(v);
  }

  public final void LongLongToBuffer(long v) {
    Add(v);
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal void DataToBuffer(byte[] v)
  public final void DataToBuffer(byte[] v) {
    Add(v);
  }

  public final void Add(String str, int len) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: var res = new byte[len*4];
    byte[] res = new byte[len * 4];
    if (str != null) {
      System.arraycopy(
          str.getBytes(java.nio.charset.StandardCharsets.UTF_16LE),
          0,
          res,
          0,
          str.getBytes(java.nio.charset.StandardCharsets.UTF_16LE).length);
    }
    Add(res);
  }

  public final void Add(double price) {
    Add(BitConverter.GetBytes(price));
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal void Add(byte[] ar, int len)
  public final void Add(byte[] ar, int len) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: var res = new byte[len];
    byte[] res = new byte[len];
    if (ar != null) {
      System.arraycopy(ar, 0, res, 0, ar.length);
    }
    Add(res);
  }
}
