package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

class MarketOpenWaiter implements OnOrderProgress, OnOrderUpdate {
  private MT5API Client;
  private int Id;
  private int Timeout;
  private ConcurrentLinkedQueue<OrderProgress> Progr = new ConcurrentLinkedQueue<OrderProgress>();
  private ConcurrentLinkedQueue<OrderUpdate> Updates = new ConcurrentLinkedQueue<OrderUpdate>();
  private Order Order;
  private long Ticket;

  public MarketOpenWaiter(MT5API client, int id, int timeout) {
    Client = client;
    Id = id;
    Timeout = timeout;
    Client.ProgressWaiters.add(this);
    Client.UpdateWaiters.add(this);
  }

  public void invoke(MT5API sender, OrderUpdate update) {
    if (update.Deal != null)
      if (Ticket == 0) Updates.add(update);
      else if (update.Deal.OrderTicket == Ticket)
        Order = new Order(new DealInternal[] {update.Deal}, Id);
  }

  public void invoke(MT5API sender, OrderProgress progress) {
    if (progress.TradeRequest.RequestId == Id) Progr.add(progress);
  }

  public Order Wait() {
    try {
      return WaitInternal();
    } finally {
      Client.ProgressWaiters.remove(this);
      Client.UpdateWaiters.remove(this);
      Progr.clear();
      Updates.clear();
    }
  }

  Order WaitInternal() {
    LocalDateTime start = LocalDateTime.now();
    while (true) {
      if (Duration.between(start, LocalDateTime.now()).toMillis() > Timeout) {
        LocalDateTime now = LocalDateTime.now();
        throw new RuntimeException("Trade timeout");
      }
      for (OrderProgress progr : Progr) {
        Msg status = progr.TradeResult.Status;
        if (status != Msg.REQUEST_ACCEPTED
            && status != Msg.REQUEST_ON_WAY
            && status != Msg.REQUEST_EXECUTED
            && status != Msg.DONE
            && status != Msg.ORDER_PLACED) throw new ServerException(status);
      }
      for (OrderProgress progr : Progr)
        if (progr.TradeResult.TicketNumber != 0) {
          Ticket = progr.TradeResult.TicketNumber;
          for (OrderUpdate update : Updates)
            if (update.Deal.PositionTicket == Ticket || update.Deal.OrderTicket == Ticket)
              Order = new Order(new DealInternal[] {update.Deal}, Id);
        }
      if (Order != null) {
        Order.RequestId = Id;
        if (Client.AccountMethod() == AccMethod.Hedging)
          Client.Orders.Opened.put(Order.Ticket, Order);
        return Order;
      }
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
      }
    }
  }
}
