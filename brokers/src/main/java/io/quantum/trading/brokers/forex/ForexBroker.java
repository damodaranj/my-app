package io.quantum.trading.brokers.forex;

import io.quantum.trading.brokers.Broker;
import io.quantum.trading.brokers.BrokerException;

public abstract class ForexBroker extends Broker {
  protected abstract long getServerTimeZoneInMillis();

  protected abstract String getAccountType();

  public abstract void partialCloseOrder(String orderId, double qty) throws BrokerException;
}
