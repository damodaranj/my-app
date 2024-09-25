package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public abstract class FromBufReader {
  public abstract Object ReadFromBuf(InBuf buf);

  public FromBufReader() {}

  public final String GetString(byte[] buf) {
    return UDT.readString(buf, 0, buf.length);
  }
}
