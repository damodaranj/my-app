package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class ModifyWaiter implements OnOrderProgress {
  private MT5API Client;
  private int Id;
  private int Timeout;
  private ArrayList<OrderProgress> Progr = new ArrayList<OrderProgress>();
  private Order Order;
  private long Ticket;

  public ModifyWaiter(MT5API client, int id, int timeout, long ticket) {
    Client = client;
    Id = id;
    Timeout = timeout;
    Ticket = ticket;
    synchronized (Client.ProgressWaiters) {
      Client.ProgressWaiters.add(this);
    }
  }

  public void invoke(MT5API sender, OrderProgress progress) {
    if (progress.TradeRequest.RequestId == Id) {
      synchronized (Progr) {
        Progr.add(progress);
      }
    }
  }

  public final Order Wait() {
    java.time.LocalDateTime start = java.time.LocalDateTime.now();
    while (true) {
      if (Duration.between(start, LocalDateTime.now()).toMillis() > Timeout) {
        throw new RuntimeException("Trade timeout");
      }
      if (Progr == null) {
        continue;
      }
      synchronized (Progr) {
        for (OrderProgress progr : Progr) {
          Msg status = progr.TradeResult.Status;
          if (status != Msg.REQUEST_ACCEPTED
              && status != Msg.REQUEST_ON_WAY
              && status != Msg.REQUEST_EXECUTED
              && status != Msg.DONE
              && status != Msg.ORDER_PLACED) {
            throw new RuntimeException(status.toString());
          }
          if (status == Msg.REQUEST_EXECUTED) {
            synchronized (Client.ProgressWaiters) {
              Client.ProgressWaiters.remove(this);
            }
            return Order;
          }
        }
      }
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
