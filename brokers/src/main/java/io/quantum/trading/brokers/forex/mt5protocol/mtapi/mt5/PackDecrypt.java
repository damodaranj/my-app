package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public class PackDecrypt {
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: byte DecryptByte = 0;
  private byte DecryptByte = 0;
  private int DecryptIndex = 0;
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: byte[] CryptKey;
  private byte[] CryptKey;

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal PackDecrypt(byte[] cryptKey)
  public PackDecrypt(byte[] cryptKey) {
    CryptKey = cryptKey;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal byte[] Decrypt(byte[] bytes)
  public final byte[] Decrypt(byte[] bytes) {
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] ^=
          (byte)
              (((DecryptByte & 0xFF) + (CryptKey[DecryptIndex % CryptKey.length] & 0xFF)) & 0xFF);
      DecryptIndex++;
      DecryptByte = bytes[i];
    }
    return bytes;
  }
}
