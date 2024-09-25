package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

class PendingOpenWaiter implements OnOrderProgress, OnOrderUpdate {
  private MT5API Client;
  private int Id;
  private int Timeout;
  private OrderProgress Progr;
  private Order Order;
  private long Ticket;
  private ConcurrentLinkedQueue<Order> Orders = new ConcurrentLinkedQueue<Order>();

  public PendingOpenWaiter(MT5API client, int id, int timeout) {
    Client = client;
    Id = id;
    Timeout = timeout;
    Client.ProgressWaiters.add(this);
    Client.UpdateWaiters.add(this);
  }

  public void invoke(MT5API sender, OrderUpdate update) {
    if (update.OrderInternal != null)
      if (Ticket == 0) Orders.add(new Order(update.OrderInternal));
      else if (update.OrderInternal.TicketNumber == Ticket) Order = new Order(update.OrderInternal);
  }

  public void invoke(MT5API sender, OrderProgress progress) {
    if (progress.TradeRequest.RequestId == Id) Progr = progress;
  }

  public Order Wait() {
    try {
      return WaitInternal();
    } finally {
      Client.ProgressWaiters.remove(this);
      Client.UpdateWaiters.remove(this);
    }
  }

  Order WaitInternal() {
    java.time.LocalDateTime start = java.time.LocalDateTime.now();
    while (true) {
      if (Duration.between(start, LocalDateTime.now()).toMillis() > Timeout) {
        throw new RuntimeException("Trade timeout");
      }
      if (Progr != null) {
        Msg status = Progr.TradeResult.Status;
        if (status != Msg.REQUEST_ACCEPTED
            && status != Msg.REQUEST_ON_WAY
            && status != Msg.REQUEST_EXECUTED
            && status != Msg.DONE
            && status != Msg.ORDER_PLACED) throw new RuntimeException(status.toString());
        if (Progr != null) {
          if (Progr.TradeResult.TicketNumber != 0) {
            Ticket = Progr.TradeResult.TicketNumber;
            for (Order order : Orders) if (order.Ticket == Ticket) Order = order;
          }
        }
      }
      if (Order != null) return Order;
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
      }
    }
  }
}
