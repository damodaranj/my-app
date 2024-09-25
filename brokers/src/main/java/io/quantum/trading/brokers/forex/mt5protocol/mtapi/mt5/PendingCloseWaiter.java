package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.time.Duration;
import java.time.LocalDateTime;

class PendingCloseWaiter implements OnOrderProgress, OnOrderUpdate {
  private MT5API Client;
  private int Id;
  private int Timeout;
  private OrderProgress Progr;
  private Order Order;
  private long Ticket;

  public PendingCloseWaiter(MT5API client, int id, int timeout, long ticket) {
    Client = client;
    Id = id;
    Timeout = timeout;
    Ticket = ticket;
    Client.ProgressWaiters.add(this);
    Client.UpdateWaiters.add(this);
  }

  public void invoke(MT5API sender, OrderUpdate update) {
    if (update.OrderInternal != null)
      if (update.OrderInternal.TicketNumber == Ticket)
        if (update.OrderInternal.State == OrderState.Cancelled)
          Order = new Order(update.OrderInternal);
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

  public Order WaitInternal() {
    LocalDateTime start = LocalDateTime.now();
    while (true) {
      if (Duration.between(start, LocalDateTime.now()).toMillis() > Timeout)
        throw new RuntimeException("Trade timeout");
      if (Progr != null) {
        Msg status = Progr.TradeResult.Status;
        if (status != Msg.REQUEST_ACCEPTED
            && status != Msg.REQUEST_ON_WAY
            && status != Msg.REQUEST_EXECUTED
            && status != Msg.DONE
            && status != Msg.ORDER_PLACED) throw new RuntimeException(status.toString());
      }
      if (Order != null) return Order;
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
      }
    }
  }
}
