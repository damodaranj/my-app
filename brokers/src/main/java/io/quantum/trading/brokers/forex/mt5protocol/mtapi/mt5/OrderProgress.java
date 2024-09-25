package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public class OrderProgress extends FromBufReader {
  public TransactionInfo OrderUpdate;
  public TradeRequest TradeRequest;
  public TradeResult TradeResult;
  public DealsResult DealsResult;

  @Override
  public Object ReadFromBuf(InBuf buf) {
    int endInd = buf.Ind + 0;
    OrderProgress st = new OrderProgress();
    if (buf.Ind != endInd) {
      throw new RuntimeException(
          "Wrong reading from buffer(buf.Ind != endInd): " + buf.Ind + " != " + endInd);
    }
    return st;
  }
}
