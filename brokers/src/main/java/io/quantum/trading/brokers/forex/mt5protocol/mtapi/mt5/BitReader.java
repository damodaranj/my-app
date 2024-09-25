package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

class BitReader {
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: byte Align;
  private byte Align;
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: byte Blank;
  private byte Blank;
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: byte[] Data;
  private byte[] Data;
  public int BitSize;

  public int BitPos = 0;

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public BitReader(byte[] data, HistHeader hdr)
  public BitReader(byte[] data, HistHeader hdr) {
    Data = data;
    Align = hdr.AlignBit;
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: Blank = (byte)((1 << hdr.AlignBit) - 1);
    Blank = (byte) (((1 << (hdr.AlignBit & 0xFF)) - 1) & 0xFF);
    BitSize = hdr.BitSize;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public BitReader(byte[] data, int bitSize)
  public BitReader(byte[] data, int bitSize) {
    Data = data;
    BitSize = bitSize;
  }

  public final long GetLong() {
    return BitConverter.ToInt64(GetRecord(8), 0);
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal ulong GetULong()
  public final long GetULong() {
    return BitConverter.ToInt64(GetRecord(8), 0);
  }

  public final int GetInt() {
    return BitConverter.ToInt32(GetRecord(4), 0);
  }

  public final short GetShort() {
    return BitConverter.ToInt16(GetRecord(2), 0);
  }

  public final int GetSignInt() {
    return BitConverter.ToInt32(GetSignRecord(4), 0);
  }

  public final long GetSignLong() {
    return BitConverter.ToInt64(GetSignRecord(8), 0);
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal byte[] GetRecord(int size)
  public final byte[] GetRecord(int size) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte buf = 0;
    byte buf = 0;
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte bitSize = 0;
    byte bitSize = 0;
    do {
      buf = ReadValue(Data, (Align & 0xFF))[0];
      bitSize += (buf & 0xFF);
    } while ((buf & 0xFF) == (Blank & 0xFF));
    bitSize *= 2;
    if (size * 8 < (bitSize & 0xFF)) {
      throw new RuntimeException("size * 8 < bitSize");
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: var res = ReadValue(Data, bitSize);
    byte[] res = ReadValue(Data, (bitSize & 0xFF));
    return Ret(res, size);
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal byte[] ReadValue(byte[] data, int size)
  public final byte[] ReadValue(byte[] data, int size) {
    if (size + BitPos > BitSize) {
      throw new RuntimeException("End of stream");
    }
    int startBit = BitPos % 8;
    int bits = 0;
    int valueInd = 0;
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte[] value = new byte[size];
    byte[] value = new byte[size];
    int dataInd = BitPos / 8;
    while (size > 0) {
      for (int i = startBit; i < 8; i++) {
        if (size == 0) {
          break;
        }
        if (bits >= 8) {
          valueInd++;
          bits = 0;
        }
        // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
        // ORIGINAL LINE: var c = data[dataInd];
        byte c = data[dataInd];
        // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
        // ORIGINAL LINE: byte ch = data[dataInd];
        byte ch = data[dataInd];
        if ((ch & (1 << i)) > 0) {
          // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
          // ORIGINAL LINE: value[valueInd] |= (byte)(1 << bits);
          value[valueInd] |= (byte) ((1 << bits) & 0xFF);
        } else {
          // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
          // ORIGINAL LINE: value[valueInd] &= (byte)~(1 << bits);
          value[valueInd] &= (byte) (~(1 << bits) & 0xFF);
        }
        BitPos++;
        size--;
        bits++;
      }
      if (size == 0) {
        break;
      }
      dataInd++;
      startBit = 0;
    }
    return value;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal void Initialize(byte align, byte blank)
  public final void Initialize(byte align, byte blank) {
    Align = align;
    Blank = blank;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal byte[] GetSignRecord(int size)
  public final byte[] GetSignRecord(int size) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte buf = 0;
    byte buf = 0;
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte sign = 0;
    byte sign = 0;
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte bitSize = 0;
    byte bitSize = 0;
    do {
      buf = ReadValue(Data, (Align & 0xFF))[0];
      bitSize += (buf & 0xFF);
    } while ((buf & 0xFF) == (Blank & 0xFF));
    bitSize *= 2;
    if (size * 8 < (bitSize & 0xFF)) {
      throw new RuntimeException("size * 8 < bitSize");
    }
    sign = ReadValue(Data, 1)[0];
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte[] data = Ret(ReadValue(Data, bitSize), size);
    byte[] data = Ret(ReadValue(Data, (bitSize & 0xFF)), size);
    if ((sign & 0xFF) == 0) {
      return data;
    }
    switch (size) {
      case 1:
        // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
        // ORIGINAL LINE: data[0] = (byte)-data[0];
        data[0] = (byte) (-data[0] & 0xFF);
        break;
      case 2:
        short vs = BitConverter.ToInt16(data, 0);
        System.arraycopy(BitConverter.GetBytes(-vs), 0, data, 0, BitConverter.GetBytes(-vs).length);
        break;
      case 4:
        int vi = BitConverter.ToInt32(data, 0);
        System.arraycopy(BitConverter.GetBytes(-vi), 0, data, 0, BitConverter.GetBytes(-vi).length);
        break;
      case 8:
        long vl = BitConverter.ToInt64(data, 0);
        System.arraycopy(BitConverter.GetBytes(-vl), 0, data, 0, BitConverter.GetBytes(-vl).length);
        break;
    }
    return data;
  }

  public final void AlignBitPosition(int pos) {
    BitPos = (BitPos / 8 + pos) * 8;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: private byte[] Ret(byte[] data, int size)
  private byte[] Ret(byte[] data, int size) {
    if (data.length == 0) {
      // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
      // ORIGINAL LINE: return new byte[size];
      return new byte[size];
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: var result = new byte[size];
    byte[] result = new byte[size];
    if (data.length < result.length) {
      System.arraycopy(data, 0, result, 0, data.length);
    } else {
      System.arraycopy(data, 0, result, 0, result.length);
    }
    return result;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal void SkipRecords(ulong mask, ulong startFlag)
  public final void SkipRecords(long mask, long startFlag) {
    startFlag <<= 1;
    if ((mask < startFlag)) {
      return;
    }
    while (startFlag != 0) {
      if (startFlag > 0x8000000000000000L) {
        break;
      }
      if ((startFlag & mask) != 0) {
        // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
        // ORIGINAL LINE: ulong value = BitConverter.ToUInt64(GetRecord(8), 0);
        long value = BitConverter.ToInt64(GetRecord(8), 0);
      }
      startFlag <<= 1;
    }
  }
}
