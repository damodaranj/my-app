package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

class String8 {
  byte[] Data;
  int Index;

  public String8(int sLen) {
    Data = new byte[sLen];
    Index = 0;
  }

  public String toString() {
    if (Index < Data.length)
      return "Byte = "
          + String.format("%02x", Data[Index] & 0xff)
          + " Index = "
          + Index
          + " DataLen = "
          + Data.length;
    else return "Byte = out Index = " + Index + " DataLen = " + Data.length;
  }

  public String8(byte[] src) {
    Data = src;
    Index = 0;
  }

  public String8() {}

  static String8 nnc(String8 src) {
    String8 dst = new String8();
    dst.Data = src.Data;
    dst.Index = src.Index;
    return dst;
  }

  static int dataDistance(String8 a, String8 b) {
    return b.Index - a.Index;
  }

  static String8 fromData(byte[] src) {
    String8 s = new String8();
    s.Data = src;
    s.Index = 0;
    return s;
  }

  public static String8 from(Container<String8> mPBuffer_u) {
    throw new RuntimeException();
  }

  public static String8 from(String8 src) {
    String8 s = new String8();
    s.Data = src.Data;
    s.Index = src.Index;
    return s;
  }

  public static String8 from(String8 src, int index) {
    String8 s = new String8();
    s.Data = src.Data;
    s.Index = index;
    return s;
  }

  String8 shift(int i) {
    String8 s = new String8();
    s.Data = Data;
    s.Index = Index + i;
    return s;
  }

  byte get(int i) {
    return Data[Index + i];
  }

  byte set(int i, byte value) {
    Data[Index + i] = value;
    return value;
  }

  byte get() {
    return Data[Index];
  }

  public void fill(int i, int sz_u, byte b) {}
}
