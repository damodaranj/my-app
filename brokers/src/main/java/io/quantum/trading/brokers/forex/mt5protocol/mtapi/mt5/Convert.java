package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public class Convert {
  public static long ToUInt64(boolean b) {
    if (b) return 1;
    else return 0;
  }

  public static boolean ToBoolean(long l) {
    if (l == 0) return false;
    else return true;
  }
}
