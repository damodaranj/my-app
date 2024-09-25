package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public class PackEncrypt {
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: byte EncryptByte = 0;
  private byte EncryptByte = 0;
  private int EncryptIndex = 0;
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal byte[] CryptKey;
  public byte[] CryptKey;

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: internal PackEncrypt(byte[] cryptKey)
  public PackEncrypt(byte[] cryptKey) {
    CryptKey = cryptKey;
  }

  public final void EncryptPacket(OutBuf buf) {
    for (int i = 9; i < buf.List.size(); i++) {
      byte b = buf.List.get(i);
      buf.List.set(
          i,
          (byte)
              ((byte) buf.List.get(i)
                  ^ (byte)
                      (((EncryptByte & 0xFF) + (CryptKey[EncryptIndex % CryptKey.length] & 0xFF))
                          & 0xFF)));
      EncryptIndex++;
      EncryptByte = b;
    }
  }
}
