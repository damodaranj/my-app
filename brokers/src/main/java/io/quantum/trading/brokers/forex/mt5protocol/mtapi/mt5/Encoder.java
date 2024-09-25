package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

class Encoder {
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: private byte Last = 0;
  private byte Last = 0;
  private int KeyInd = 0;
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: private byte[] Key;
  private byte[] Key;

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public Encoder(byte[] key)
  public Encoder(byte[] key) {
    Key = key;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public void ChangeKey(byte[] key)
  public final void ChangeKey(byte[] key) {
    Key = key;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] GetKey()
  public final byte[] GetKey() {
    return Key;
  }

  public final void Reset() {
    Last = 0;
    KeyInd = 0;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public byte[] Encode(byte[] buf)
  public final byte[] Encode(byte[] buf) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte[] res = new byte[buf.Length];
    byte[] res = new byte[buf.length];
    for (int i = 0; i < buf.length; i++) {
      KeyInd &= 0xF;
      // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
      // ORIGINAL LINE: res[i] = (byte)(buf[i] ^ (Last + Key[KeyInd]));
      res[i] = (byte) (((buf[i] & 0xFF) ^ ((Last & 0xFF) + (Key[KeyInd] & 0xFF))) & 0xFF);
      KeyInd++;
      Last = buf[i];
    }
    return res;
  }
}
