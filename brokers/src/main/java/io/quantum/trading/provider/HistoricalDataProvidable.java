package io.quantum.trading.provider;

import io.quantum.trading.entities.NseScriptMetadata;
import io.quantum.trading.enums.TimeFrame;
import io.quantum.trading.patterns.Candle;
import java.util.List;
import org.joda.time.DateTime;

public interface HistoricalDataProvidable {
  List<Candle> getHistoricalDataFromProvider(
      NseScriptMetadata nseScriptMetadata, DateTime from, DateTime to, TimeFrame timeFrame);
}
