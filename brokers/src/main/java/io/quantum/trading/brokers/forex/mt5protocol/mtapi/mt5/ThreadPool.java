package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
  static Logger Log = new Logger("ThreadPool");

  // public static ExecutorService Executor = Executors.newFixedThreadPool(10);

  public static void QueueUserWorkItem(Runnable worker) {
    // Executor.execute(worker);
    CompletableFuture.runAsync(worker)
        .orTimeout(300, TimeUnit.SECONDS)
        .exceptionally(
            throwable -> {
              Log.error(throwable.getMessage());
              return null;
            });
  }
}
