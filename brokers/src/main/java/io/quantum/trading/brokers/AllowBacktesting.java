package io.quantum.trading.brokers;

public interface AllowBacktesting {
  void synchronizeOrders();

  boolean isInBacktestMode();

  boolean isUtMode();
}
