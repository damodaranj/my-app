package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

class BitReaderQuotes {
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
  // ORIGINAL LINE: public BitReaderQuotes(byte[] data, HistHeader hdr)
  public BitReaderQuotes(byte[] data, HistHeader hdr) {
    Data = data;
    Align = hdr.AlignBit;
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: Blank = (byte)((1 << hdr.AlignBit) - 1);
    Blank = (byte) (((1 << (hdr.AlignBit & 0xFF)) - 1) & 0xFF);
    BitSize = hdr.BitSize;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public BitReaderQuotes(byte[] data, int bitSize)
  public BitReaderQuotes(byte[] data, int bitSize) {
    Data = data;
    BitSize = bitSize;
  }

  public final boolean GetLong(RefObject<Long> res) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte[] buf;
    byte[] buf = null;
    RefObject<byte[]> tempRef_buf = new RefObject<byte[]>(buf);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: if (!GetRecord(8, out buf))
    if (!GetRecord(8, tempRef_buf)) {
      buf = tempRef_buf.argValue;
      res.argValue = 0L;
      return false;
    } else {
      buf = tempRef_buf.argValue;
      res.argValue = BitConverter.ToInt64(buf, 0);
      return true;
    }
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal bool GetULong(out ulong res)
  public final boolean GetULong(RefObject<Long> res) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte[] buf;
    byte[] buf = null;
    RefObject<byte[]> tempRef_buf = new RefObject<byte[]>(buf);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: if (!GetRecord(8, out buf))
    if (!GetRecord(8, tempRef_buf)) {
      buf = tempRef_buf.argValue;
      res.argValue = 0L;
      return false;
    } else {
      buf = tempRef_buf.argValue;
      res.argValue = BitConverter.ToInt64(buf, 0);
      return true;
    }
  }

  public final boolean GetInt(RefObject<Integer> res) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte[] buf;
    byte[] buf = null;
    RefObject<byte[]> tempRef_buf = new RefObject<byte[]>(buf);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: if (!GetRecord(4, out buf))
    if (!GetRecord(4, tempRef_buf)) {
      buf = tempRef_buf.argValue;
      res.argValue = 0;
      return false;
    } else {
      buf = tempRef_buf.argValue;
      res.argValue = BitConverter.ToInt32(buf, 0);
      return true;
    }
  }

  public final boolean GetShort(RefObject<Short> res) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte[] buf;
    byte[] buf = null;
    RefObject<byte[]> tempRef_buf = new RefObject<byte[]>(buf);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: if (!GetRecord(2, out buf))
    if (!GetRecord(2, tempRef_buf)) {
      buf = tempRef_buf.argValue;
      res.argValue = 0;
      return false;
    } else {
      buf = tempRef_buf.argValue;
      res.argValue = BitConverter.ToInt16(buf, 0);
      return true;
    }
  }

  public final boolean GetSignInt(RefObject<Integer> res) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte[] buf;
    byte[] buf = null;
    RefObject<byte[]> tempRef_buf = new RefObject<byte[]>(buf);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: if (!GetSignRecord(4, out buf))
    if (!GetSignRecord(4, tempRef_buf)) {
      buf = tempRef_buf.argValue;
      res.argValue = 0;
      return false;
    } else {
      buf = tempRef_buf.argValue;
      res.argValue = BitConverter.ToInt32(buf, 0);
      return true;
    }
  }

  public final boolean GetSignLong(RefObject<Long> res) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte[] buf;
    byte[] buf = null;
    RefObject<byte[]> tempRef_buf = new RefObject<byte[]>(buf);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: if (!GetSignRecord(8, out buf))
    if (!GetSignRecord(8, tempRef_buf)) {
      buf = tempRef_buf.argValue;
      res.argValue = 0L;
      return false;
    } else {
      buf = tempRef_buf.argValue;
      res.argValue = BitConverter.ToInt64(buf, 0);
      return true;
    }
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal bool GetRecord(int size, out byte[] res)
  public final boolean GetRecord(int size, RefObject<byte[]> res) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte buf = 0;
    byte buf = 0;
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte bitSize = 0;
    byte bitSize = 0;
    do {
      // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
      // ORIGINAL LINE: byte[] value;
      byte[] value = null;
      RefObject<byte[]> tempRef_value = new RefObject<byte[]>(value);
      // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
      // ORIGINAL LINE: if (!ReadValue(Data, Align, out value))
      if (!ReadValue(Data, (Align & 0xFF), tempRef_value)) {
        value = tempRef_value.argValue;
        res.argValue = null;
        return false;
      } else {
        value = tempRef_value.argValue;
      }
      buf = value[0];
      bitSize += (buf & 0xFF);
    } while ((buf & 0xFF) == (Blank & 0xFF));
    bitSize *= 2;
    if (size * 8 < (bitSize & 0xFF)) {
      throw new RuntimeException("size * 8 < bitSize");
    }
    if (!ReadValue(Data, (bitSize & 0xFF), res)) {
      res.argValue = null;
      return false;
    }
    res.argValue = Ret(res.argValue, size);
    return true;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal bool ReadValue(byte[] data, int size, out byte[] res)
  public final boolean ReadValue(byte[] data, int size, RefObject<byte[]> res) {
    if (size + BitPos > BitSize) {
      res.argValue = null;
      return false; // throw new Exception("End of stream");
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
    res.argValue = value;
    return true;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal void Initialize(byte align, byte blank)
  public final void Initialize(byte align, byte blank) {
    Align = align;
    Blank = blank;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal bool GetSignRecord(int size, out byte[] res)
  public final boolean GetSignRecord(int size, RefObject<byte[]> res) {
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
      // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
      // ORIGINAL LINE: byte[] value;
      byte[] value = null;
      RefObject<byte[]> tempRef_value = new RefObject<byte[]>(value);
      // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
      // ORIGINAL LINE: if (!ReadValue(Data, Align, out value))
      if (!ReadValue(Data, (Align & 0xFF), tempRef_value)) {
        value = tempRef_value.argValue;
        res.argValue = null;
        return false;
      } else {
        value = tempRef_value.argValue;
      }
      buf = value[0];
      bitSize += (buf & 0xFF);
    } while ((buf & 0xFF) == (Blank & 0xFF));
    bitSize *= 2;
    if (size * 8 < (bitSize & 0xFF)) {
      throw new RuntimeException("size * 8 < bitSize");
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte[] val;
    byte[] val = null;
    RefObject<byte[]> tempRef_val = new RefObject<byte[]>(val);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: if (!ReadValue(Data, 1, out val))
    if (!ReadValue(Data, 1, tempRef_val)) {
      val = tempRef_val.argValue;
      res.argValue = null;
      return false;
    } else {
      val = tempRef_val.argValue;
    }
    sign = val[0];
    RefObject<byte[]> tempRef_val2 = new RefObject<byte[]>(val);
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: if (!ReadValue(Data, bitSize, out val))
    if (!ReadValue(Data, (bitSize & 0xFF), tempRef_val2)) {
      val = tempRef_val2.argValue;
      res.argValue = null;
      return false;
    } else {
      val = tempRef_val2.argValue;
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte[] data = Ret(val, size);
    byte[] data = Ret(val, size);
    if ((sign & 0xFF) == 0) {
      res.argValue = data;
      return true;
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
    res.argValue = data;
    return true;
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
        // ORIGINAL LINE: byte[] buf;
        byte[] buf = null;
        RefObject<byte[]> tempRef_buf = new RefObject<byte[]>(buf);
        // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
        // ORIGINAL LINE: GetRecord(8, out buf);
        GetRecord(8, tempRef_buf);
        buf = tempRef_buf.argValue;
        // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
        // ORIGINAL LINE: ulong value = BitConverter.ToUInt64(buf, 0);
        long value = BitConverter.ToUInt64(buf, 0);
      }
      startFlag <<= 1;
    }
  }
}
