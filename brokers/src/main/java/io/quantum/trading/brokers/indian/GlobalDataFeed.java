package io.quantum.trading.brokers.indian;

import io.quantum.trading.brokers.*;
import io.quantum.trading.entities.BrokerConfigDetails.*;
import io.quantum.trading.entities.NseScriptMetadata;

public class GlobalDataFeed extends NseBroker {

  private final GlobalDataFeedConfigDetails globalDataFeedConfigDetails;

  public GlobalDataFeed(GlobalDataFeedConfigDetails globalDataFeedConfigDetails) {
    this.globalDataFeedConfigDetails = globalDataFeedConfigDetails;
    // TODO establish connect
  }

  @Override
  public double getBalance() throws BrokerException {
    return 0;
  }

  @Override
  public boolean testConnection() throws BrokerException {
    return false;
  }

  @Override
  public void disConnect() throws BrokerException {}

  @Override
  public BrokerOrderDetail placeOrder(BrokerPlaceOrderRequest brokerPlaceOrderRequest)
      throws BrokerException {
    return null;
  }

  @Override
  public BrokerOrderDetail modifyOrder(BrokerModifyOrderRequest brokerModifyOrderRequest)
      throws BrokerException {
    return null;
  }

  @Override
  public void cancelOrder(String orderId) throws BrokerException {}

  @Override
  public void closeOrder(String orderId) throws BrokerException {}

  @Override
  public BrokerOrderDetail getOrderDetails(String orderId) throws BrokerException {
    return null;
  }

  @Override
  public String getBrokerToken(NseScriptMetadata nseScriptMetadata) {
    return null;
  }

  @Override
  public String getBrokerScriptName(NseScriptMetadata nseScriptMetadata) {
    return null;
  }
}
