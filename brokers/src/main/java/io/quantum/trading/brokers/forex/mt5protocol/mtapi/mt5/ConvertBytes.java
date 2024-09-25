package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/** Convert byte array to strings. */
class ConvertBytes {
  /** Convert to HEX string. */
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public static string ToHex(byte[] bytes)
  public static String ToHex(byte[] bytes) {
    String res = "";
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: foreach (byte b in bytes)
    for (byte b : bytes) {
      res += DotNetToJavaStringHelper.padLeft(String.format("%X", b), 2, '0') + " ";
    }
    return res;
  }

  /** Convert to ASCII string. */
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public static string ToAscii(byte[] bytes)
  public static String ToAscii(byte[] bytes) {
    return readStringASCII(bytes, 0, bytes.length);
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public static string ToUnicode(byte[] bytes)
  public static String ToUnicode(byte[] bytes) {
    return readString(bytes, 0, bytes.length);
  }

  static String readString(byte[] buf, int of, int len) {
    ArrayList<Byte> res = new ArrayList<Byte>();
    for (int i = 0; i < len; i += 2) {
      if (buf[of + i] == 0 && buf[of + i + 1] == 0) break;
      res.add(buf[of + i]);
      res.add(buf[of + i + 1]);
    }
    byte[] array = new byte[res.size()];
    for (int i = 0; i < res.size(); i++) array[i] = res.get(i);

    try {
      return new String(array, Charset.defaultCharset().toString());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  static String readStringASCII(byte[] buf, int of, int len) {
    ArrayList<Byte> res = new ArrayList<Byte>();
    for (int i = 0; i < len; i += 1) {
      if (buf[of + i] == 0) // && buf[of + i + 1] == 0
      if (i > 0) break;
      res.add(buf[of + i]);
      // res.Add(buf[of + i + 1]);
    }
    if (res.size() > 0) if (res.get(0) == 0) res.remove(0);
    byte[] array = new byte[res.size()];
    for (int i = 0; i < res.size(); i++) array[i] = res.get(i);
    String r = null;
    try {
      r = new String(array, Charset.forName("ASCII").toString());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return r;
  }
}
