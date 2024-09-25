package io.quantum.trading.brokers;

import io.quantum.trading.brokers.forex.mt5.MT5Broker;
import io.quantum.trading.brokers.forex.mt5.Mt5BacktestBroker;
import io.quantum.trading.brokers.indian.GlobalDataFeed;
import io.quantum.trading.brokers.indian.IndianDemoBroker;
import io.quantum.trading.brokers.indian.NseBroker;
import io.quantum.trading.brokers.indian.ZerodhaBroker;
import io.quantum.trading.brokers.indian.fyers.FyersBroker;
import io.quantum.trading.entities.*;
import io.quantum.trading.entities.BrokerConfigDetails.Mt5BrokerConfigDetails;
import io.quantum.trading.redis.RedisCrudService;
import io.quantum.trading.services.ScriptMetadataRefresherService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BrokerFactory {
  private final BrokerConfigRepository brokerConfigRepository;
  private final DemoBrokerRepository demoBrokerRepository;
  private final RedisCrudService redisCrudService;
  private final ScriptMetadataRefresherService scriptMetadataRefresherService;
  private static final Map<String, Broker> BROKERS = new ConcurrentHashMap<>();

  public Broker getBroker(String brokerConfigId) {
    synchronized (brokerConfigId.intern()) {
      if (!BROKERS.containsKey(brokerConfigId)) {
        log.info("Broker, {} is not cached. Creating a new one", brokerConfigId);
        var brokerConfigOpt = brokerConfigRepository.findById(brokerConfigId);
        if (brokerConfigOpt.isEmpty())
          throw new RuntimeException("BrokerConfigId, " + brokerConfigId + " is not found");

        var brokerConfig = brokerConfigOpt.get();

        var broker =
            switch (brokerConfig.getType()) {
              case BrokerConfig.BrokerType.ZERODHA ->
                  new ZerodhaBroker(() -> brokerConfigRepository.findById(brokerConfigId).get());
              case BrokerConfig.BrokerType.MT5 ->
                  new MT5Broker((Mt5BrokerConfigDetails) brokerConfig.getBrokerConfigDetails());
              case BrokerConfig.BrokerType.MT5_BACKTEST ->
                  new Mt5BacktestBroker(
                      (BrokerConfigDetails.Mt5BacktestBrokerConfigDetails)
                          brokerConfig.getBrokerConfigDetails(),
                      redisCrudService);
              case BrokerConfig.BrokerType.GLOBAL_DATAFEEDS ->
                  new GlobalDataFeed(
                      (BrokerConfigDetails.GlobalDataFeedConfigDetails)
                          brokerConfig.getBrokerConfigDetails());
              case BrokerConfig.BrokerType.INDIAN_MKT_DEMO,
                      BrokerConfig.BrokerType.INDIAN_MKT_BACKTEST ->
                  new IndianDemoBroker(brokerConfig, redisCrudService, demoBrokerRepository);
              case BrokerConfig.BrokerType.FYERS ->
                  new FyersBroker(
                      (BrokerConfigDetails.FyersBrokerConfigDetails)
                          brokerConfig.getBrokerConfigDetails());

              default ->
                  throw new IllegalArgumentException(
                      "Unknown broker type, " + brokerConfig.getType());
            };
        BROKERS.put(brokerConfigId, broker);
        synchronized (BrokerFactory.class) {
          getScriptMetadataMap(BatchLastRunDetails.BatchType.NSE_SCRIPT_METADATA);
          getScriptMetadataMap(BatchLastRunDetails.BatchType.FOREX_SCRIPT_METADATA);
        }
      }
    }
    return BROKERS.get(brokerConfigId);
  }

  private synchronized void getScriptMetadataMap(BatchLastRunDetails.BatchType batchType) {
    var scriptMetadataRes =
        scriptMetadataRefresherService.metadataWatcher(
            batchType,
            "BrokerFactoryCb",
            scriptMetadata -> {
              updateMetadataInBrokers(scriptMetadata);
              return null;
            });
    updateMetadataInBrokers(scriptMetadataRes);
  }

  private void updateMetadataInBrokers(List<? extends ScriptMetadata> scriptMetadata) {
    if (!scriptMetadata.isEmpty()) {
      if (scriptMetadata.get(0) instanceof NseScriptMetadata) {
        log.info("Updating NSE brokers with latest metadata");
        var nseScriptMetadataMap =
            ((List<NseScriptMetadata>) scriptMetadata)
                .stream()
                    .collect(
                        Collectors.toMap(
                            NseScriptMetadata::getId, nseScriptMetadata -> nseScriptMetadata));
        // TODO The moment we introduce the 2nd NseBroker, we have to make the below code dynamic
        var scriptTokenSymbolMap =
            nseScriptMetadataMap.entrySet().stream()
                .collect(
                    Collectors.groupingBy(
                        e ->
                            Long.parseLong(
                                e.getValue()
                                    .getBrokerScriptIdMap()
                                    .get(BrokerConfig.BrokerType.ZERODHA)),
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
        BROKERS.values().stream()
            .filter(broker -> broker instanceof NseBroker)
            .forEach(
                broker -> {
                  ((NseBroker) broker)
                      .updateScriptMetadata(nseScriptMetadataMap, scriptTokenSymbolMap);
                });
      } else if (scriptMetadata.get(0) instanceof ForexScriptMetadata) {
        // Do nothing
      }
    }
  }
}
