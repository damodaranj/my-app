package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

class Connector {
  private MT5API QC;
  private MyThread ConnectThread;
  private Logger Log;
  private AtomicBoolean stop = new AtomicBoolean(false);

  public Connector(MT5API qc) {
    QC = qc;
    Log = qc.Log;
  }

  Object ConnectLock = new Object();

  public final void go(int msTimeout, String host, int port) throws Exception {
    stop.set(false);
    while (true) {
      if (QC.Connected()) return;
      if (ConnectThread != null) {
        if (ConnectThread.IsAlive())
          if (ConnectThread.Timeout == false)
            if (ConnectThread.Join(msTimeout))
              if (ConnectThread.Exception != null) throw ConnectThread.Exception;
              else return;
      }
      synchronized (ConnectLock) {
        if (QC.Connected()) return;
        if (ConnectThread != null)
          if (ConnectThread.IsAlive()) if (ConnectThread.Timeout == false) continue;
        QC.Host = host;
        QC.Port = port;
        MyThread.ThrowableRunnable runnable =
            () -> {
              ConnectToAccount();
            };
        ConnectThread = new MyThread(runnable, QC.User);
        if (QC.CmdHandler != null) QC.CmdHandler.stop();
        if (QC.Connection != null) QC.Connection.Close();
        QC.GotAccountInfo = false;
        QC.TimeoutDuringConnect = false;
        ConnectThread.Start();
        break;
      }
    }
    if (ConnectThread.Join(msTimeout)) {
      if (ConnectThread.Exception == null) {
        LinkedList<String> subscriptions =
            new LinkedList<String>(Arrays.asList(QC.Subscriptions()));
        for (Order order : QC.GetOpenedOrders())
          if (order.OrderType == OrderType.Buy || order.OrderType == OrderType.Sell)
            if (!subscriptions.contains(order.Symbol)) subscriptions.add(order.Symbol);
        if (subscriptions.size() > 0)
          QC.Subscriber.Subscribe(subscriptions.toArray(new String[0])); // in case of reconnection
        QC.OnConnectCall(null, ConnectProgress.Connected);
      } else {
        QC.OnConnectCall(ConnectThread.Exception, ConnectProgress.Exception);
        throw ConnectThread.Exception;
      }
    } else {
      QC.TimeoutDuringConnect = true;
      TimeoutException ex = new TimeoutException("Not connected in " + msTimeout + " ms", QC.Log);
      QC.OnConnectCall(ex, ConnectProgress.Exception);
      throw ex;
    }
  }

  public final void runAsync(int msTimeout) {
    new Thread(
            () -> {
              try {
                go(msTimeout, QC.Host, QC.Port);
              } catch (Exception e) {
              }
            })
        .start();
  }

  public final void stop() {
    stop.set(true);
  }

  private void ConnectToAccount() throws Exception {
    Connection con = new Connection(QC);
    con.Login(false, QC.Log); // login
    CmdHandler cmd = new CmdHandler(QC);
    cmd.StartCmdHandler(con, QC.User);
    QC.Connection = con;
    QC.CmdHandler = cmd;
    while (QC.GotAccountInfo == false) {
      if (stop.get()) {
        break;
      }
      if (cmd.AccountLoaderException != null) throw cmd.AccountLoaderException;
      Thread.sleep(1);
    }
  }
}
