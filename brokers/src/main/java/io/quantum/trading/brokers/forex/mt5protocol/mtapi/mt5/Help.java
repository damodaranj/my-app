package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class Help {
  public static void check(byte[] src) {
    try {
      List<String> lines =
          Files.readAllLines(Paths.get("C:\\YandexDisk\\Tim\\java\\mt5api\\ar.txt"));
      int ind = 0;
      for (String line : lines) {
        if (!line.trim().startsWith("[")) continue;
        ind = Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]")));
        byte val = hexToByte(line.substring(line.indexOf("0x") + 2, line.indexOf("byte")));
        if (ind > src.length - 1) throw new RuntimeException(ind + " big " + (src.length - 1));
        if (src[ind] != val) throw new RuntimeException(ind + " " + src[ind] + " not " + val);
      }
      if (ind != src.length - 1) throw new RuntimeException(ind + "!=" + (src.length - 1));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static byte hexToByte(String hexString) {
    int firstDigit = toDigit(hexString.charAt(0));
    int secondDigit = toDigit(hexString.charAt(1));
    return (byte) ((firstDigit << 4) + secondDigit);
  }

  public static int toDigit(char hexChar) {
    int digit = Character.digit(hexChar, 16);
    if (digit == -1) {
      throw new IllegalArgumentException("Invalid Hexadecimal Character: " + hexChar);
    }
    return digit;
  }

  public static void equal(byte[] ar1, byte[] ar2) {
    if (ar1.length != ar2.length) {
      throw new RuntimeException("len not equal");
    }
    for (int i = 0; i < ar1.length; i++) {
      if ((ar1[i] & 0xFF) != (ar2[i] & 0xFF)) {
        throw new RuntimeException("not equal " + i);
      }
    }
  }
}
