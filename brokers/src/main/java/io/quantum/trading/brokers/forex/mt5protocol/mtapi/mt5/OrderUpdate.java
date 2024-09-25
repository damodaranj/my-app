package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public class OrderUpdate {
  public TransactionInfo Trans;
  public OrderInternal OrderInternal;
  public DealInternal Deal;
  public DealInternal OppositeDeal;
  public UpdateType Type;
  public Order Order;
}
