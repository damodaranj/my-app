package io.quantum.trading.brokers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Broker {
  protected static final String TAG_PREFIX = "QUANTUM_BOT";
  protected static final String DUMMY_TRADE = "NO_TRADE";

  public abstract double getBalance() throws BrokerException;

  public abstract Currency getCurrency() throws BrokerException;

  public abstract boolean testConnection() throws BrokerException;

  public abstract void disConnect() throws BrokerException;

  public abstract BrokerOrderDetail placeOrder(BrokerPlaceOrderRequest brokerPlaceOrderRequest)
      throws BrokerException;

  public abstract BrokerOrderDetail modifyOrder(BrokerModifyOrderRequest brokerModifyOrderRequest)
      throws BrokerException;

  public abstract void cancelOrder(String orderId) throws BrokerException;

  public abstract void closeOrder(String orderId) throws BrokerException;

  public abstract BrokerOrderDetail getOrderDetails(String orderId) throws BrokerException;

  public boolean isDemoAcc() {
    return false;
  }
}
