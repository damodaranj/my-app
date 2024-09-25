package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/** Convert string to byte array. */
class vUTF {
  /** Convert UNICODE string to byte array. */
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public static byte[] toByte(string str)
  public static byte[] toByte(String str) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: byte[] res = new byte[str.Length];
    byte[] res = new byte[str.length()];
    for (int i = 0; i < str.length(); i++) {
      // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
      // ORIGINAL LINE: res[i] = (byte)(str[i] & 0xFF);
      res[i] = (byte) (str.charAt(i) & 0xFF);
    }
    return res;
  }

  /** Convert byte array to UNICODE string. */
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public static string toString(byte[] bytes, int offset)
  public static String toString(byte[] bytes, int offset) {
    String str = "";
    for (int i = offset; i < bytes.length; i++) {
      if ((bytes[i] & 0xFF) == 0) {
        break;
      }
      str += (char) (bytes[i] & 0xFF);
    }
    return str;
  }
}
