package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.util.concurrent.atomic.AtomicLong;

class MyThread {
  public interface ThrowableRunnable {
    void run() throws Exception;
  }

  private Thread Thread;
  public Exception Exception = null;
  public boolean Timeout = false;
  private static final AtomicLong THREAD_COUNTER = new AtomicLong(0);

  public MyThread(ThrowableRunnable throwableRunnable, long user) {
    Runnable runnable =
        () -> {
          try {
            throwableRunnable.run();
          } catch (Exception ex) {
            Exception = ex;
          }
        };
    Thread = new Thread(runnable);
    Thread.setName(
        String.format("MT5ConnectorThread-%s-%s", user, THREAD_COUNTER.incrementAndGet()));
  }

  // Thread methods / properties
  public void Start() {
    Thread.start();
  }

  public void Join() throws InterruptedException {
    Thread.join();
  }

  public boolean Join(int timeout) {
    try {
      Thread.join(timeout);
    } catch (InterruptedException e) {
      return false;
    }
    if (!Thread.isAlive()) return true;
    Timeout = true;
    return false;
  }

  public boolean IsAlive() {
    return Thread.isAlive();
  }
}
