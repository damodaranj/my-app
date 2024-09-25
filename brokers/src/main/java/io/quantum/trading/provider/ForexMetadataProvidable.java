package io.quantum.trading.provider;

import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.SymbolInfo;
import java.util.List;

public interface ForexMetadataProvidable {
  List<SymbolInfo> getSymbols();

  String getGroupName(String symbol);
}
