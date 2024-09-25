package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

class MarketCloseWaiter implements OnOrderProgress, OnOrderUpdate {
  private MT5API Client;
  private int Id;
  private int Timeout;
  private ConcurrentLinkedQueue<OrderProgress> Progr = new ConcurrentLinkedQueue<OrderProgress>();
  private Order Order;
  private long Ticket;

  public MarketCloseWaiter(MT5API client, int id, int timeout, long ticket) {
    Client = client;
    Id = id;
    Timeout = timeout;
    Client.ProgressWaiters.add(this);
    Client.UpdateWaiters.add(this);
    Ticket = ticket;
  }

  public void invoke(MT5API sender, OrderUpdate update) {
    if (update.Deal != null) {
      if (update.Deal.PositionTicket == Ticket) {
        Order = new Order(new DealInternal[] {update.Deal, update.OppositeDeal}, Id);
      }
    }
  }

  public void invoke(MT5API sender, OrderProgress progress) {
    if (progress.TradeRequest.RequestId == Id) {
      Progr.add(progress);
    }
  }

  public Order Wait() {
    try {
      return WaitInternal();
    } finally {
      Client.ProgressWaiters.remove(this);
      Client.UpdateWaiters.remove(this);
      Progr.clear();
    }
  }

  public Order WaitInternal() {
    java.time.LocalDateTime start = java.time.LocalDateTime.now();
    while (true) {
      if (Duration.between(start, LocalDateTime.now()).toMillis() > Timeout)
        throw new RuntimeException("Trade timeout");
      for (OrderProgress progr : Progr) {
        Msg status = progr.TradeResult.Status;
        if (status != Msg.REQUEST_ACCEPTED
            && status != Msg.REQUEST_ON_WAY
            && status != Msg.REQUEST_EXECUTED
            && status != Msg.DONE
            && status != Msg.ORDER_PLACED) {
          throw new ServerException(status);
        }
      }
      if (Order != null) {
        Order.RequestId = Id;
        return Order;
      }
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
      }
    }
  }
}
