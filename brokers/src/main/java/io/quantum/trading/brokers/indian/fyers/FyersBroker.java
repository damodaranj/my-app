package io.quantum.trading.brokers.indian.fyers;

import static io.quantum.trading.brokers.Exchange.BFO;
import static io.quantum.trading.brokers.Exchange.NFO;
import static io.quantum.trading.entities.BrokerConfig.BrokerType.FYERS;
import static io.quantum.trading.entities.NseScriptMetadata.Segment.INDICES;
import static java.util.concurrent.TimeUnit.SECONDS;

import io.quantum.trading.brokers.*;
import io.quantum.trading.brokers.indian.NseBroker;
import io.quantum.trading.entities.BrokerConfig;
import io.quantum.trading.entities.BrokerConfigDetails;
import io.quantum.trading.entities.NseScriptMetadata;
import io.quantum.trading.enums.TimeFrame;
import io.quantum.trading.patterns.Candle;
import io.quantum.trading.provider.HistoricalDataProvidable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class FyersBroker extends NseBroker implements HistoricalDataProvidable {

  private final BrokerConfigDetails.FyersBrokerConfigDetails fyersBrokerConfigDetails;

  private final RestTemplate restTemplate = new RestTemplate();

  private Map<String, Map<String, String>> exchangeTokenBrokerMetadataCache;

  private final String[] FYERS_EXCHANGE_TOKEN_URLS = {
    "https://public.fyers.in/sym_details/NSE_CD.csv",
    "https://public.fyers.in/sym_details/NSE_FO.csv",
    "https://public.fyers.in/sym_details/NSE_CM.csv",
    "https://public.fyers.in/sym_details/BSE_CM.csv",
    "https://public.fyers.in/sym_details/BSE_FO.csv",
    "https://public.fyers.in/sym_details/MCX_COM.csv"
  };

  public FyersBroker(BrokerConfigDetails.FyersBrokerConfigDetails fyersBrokerConfigDetails) {
    this.fyersBrokerConfigDetails = fyersBrokerConfigDetails;
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
    loadBrokerMetadata();
    var segment = nseScriptMetadata.getSegment();
    var exchange = nseScriptMetadata.getId().getExchange();
    var scriptName = nseScriptMetadata.getId().getScript().trim().replaceAll(" ", "");

    var metadataMapperKey = nseScriptMetadata.getExchangeToken();
    if (segment.equals(INDICES)) metadataMapperKey = metadataMapperKey + "_" + segment;
    else metadataMapperKey = metadataMapperKey + "_" + exchange;
    if (!exchangeTokenBrokerMetadataCache.containsKey(metadataMapperKey)) {
      List<String> matchingKeys =
          exchangeTokenBrokerMetadataCache.entrySet().stream()
              .filter(entry -> entry.getKey().contains("_INDICES"))
              .filter(
                  entry ->
                      entry
                          .getValue()
                          .get("script_name")
                          .trim()
                          .replaceAll(" ", "")
                          .contains(scriptName + "-"))
              .map(Map.Entry::getKey)
              .collect(Collectors.toList());
      if (matchingKeys.isEmpty()) {
        log.info("Token not found for script {}, so returning empty string ", metadataMapperKey);
        return "";
      } else if (matchingKeys.size() > 1) {
        log.info(
            "More than one match found from broker metadata with ScriptName mapping {} ,{} ",
            scriptName,
            matchingKeys);
        return "";
      }
      return exchangeTokenBrokerMetadataCache.get(matchingKeys.get(0)).get("token");
    }
    return exchangeTokenBrokerMetadataCache.get(metadataMapperKey).get("token");
  }

  private void loadBrokerMetadata() {
    if (exchangeTokenBrokerMetadataCache == null || exchangeTokenBrokerMetadataCache.isEmpty()) {
      exchangeTokenBrokerMetadataCache = new HashMap<>();
      Arrays.stream(FYERS_EXCHANGE_TOKEN_URLS)
          .toList()
          .forEach(
              fyersExchangeTokenUrl -> {
                log.info("Fetching broker tokens from , {}", fyersExchangeTokenUrl);
                try (var client = HttpClient.newHttpClient()) {
                  var req =
                      HttpRequest.newBuilder()
                          .uri(new URI(fyersExchangeTokenUrl))
                          .timeout(Duration.of(10, SECONDS.toChronoUnit()))
                          .GET()
                          .build();
                  var res = client.send(req, HttpResponse.BodyHandlers.ofString());
                  Stream.of(res.body().split("\n"))
                      .forEach(
                          row -> {
                            var fields = row.split(",");
                            var scriptName = fields[9].trim();
                            var exchangeToken = fields[12].trim();
                            Map<String, String> brokerMetadata = new HashMap<>();
                            brokerMetadata.put("token", fields[0].trim());
                            brokerMetadata.put("script_name", scriptName);
                            if (scriptName.endsWith("-INDEX"))
                              exchangeTokenBrokerMetadataCache.put(
                                  exchangeToken + "_" + INDICES, brokerMetadata);
                            else if (scriptName.startsWith("NSE")
                                && (scriptName.endsWith("CE")
                                    || scriptName.endsWith("PE")
                                    || scriptName.endsWith("FUT")))
                              exchangeTokenBrokerMetadataCache.put(
                                  exchangeToken + "_" + NFO, brokerMetadata);
                            else if (scriptName.startsWith("BSE")
                                && (scriptName.endsWith("CE")
                                    || scriptName.endsWith("PE")
                                    || scriptName.endsWith("FUT")))
                              exchangeTokenBrokerMetadataCache.put(
                                  exchangeToken + "_" + BFO, brokerMetadata);
                            else
                              exchangeTokenBrokerMetadataCache.put(
                                  exchangeToken + "_" + scriptName.split(":")[0], brokerMetadata);
                          });
                } catch (URISyntaxException | IOException | InterruptedException e) {
                  throw new RuntimeException(e);
                }
              });
    }
  }

  @Override
  public String getBrokerScriptName(NseScriptMetadata nseScriptMetadata) {
    loadBrokerMetadata();
    var segment = nseScriptMetadata.getSegment();
    var scriptName = nseScriptMetadata.getId().getScript().trim().replaceAll(" ", "");
    var exchange = nseScriptMetadata.getId().getExchange();

    var metadataMapperKey = nseScriptMetadata.getExchangeToken();
    if (segment.equals(INDICES)) metadataMapperKey = metadataMapperKey + "_" + segment;
    else metadataMapperKey = metadataMapperKey + "_" + exchange;
    if (!exchangeTokenBrokerMetadataCache.containsKey(metadataMapperKey)) {
      List<String> matchingKeys =
          exchangeTokenBrokerMetadataCache.entrySet().stream()
              .filter(entry -> entry.getKey().contains("_INDICES"))
              .filter(
                  entry ->
                      entry
                          .getValue()
                          .get("script_name")
                          .trim()
                          .replaceAll(" ", "")
                          .contains(scriptName + "-"))
              .map(Map.Entry::getKey)
              .collect(Collectors.toList());
      if (matchingKeys.isEmpty()) {
        log.info(
            "ScriptName not found for script {}, so returning empty string ", metadataMapperKey);
        return "";
      } else if (matchingKeys.size() > 1) {
        log.info(
            "More than one match found from broker metadata with ScriptName mapping {} ,{} ",
            scriptName,
            matchingKeys);
        return "";
      }
      return exchangeTokenBrokerMetadataCache.get(matchingKeys.get(0)).get("script_name");
    }
    return exchangeTokenBrokerMetadataCache.get(metadataMapperKey).get("script_name");
  }

  @Override
  public List<Candle> getHistoricalDataFromProvider(
      NseScriptMetadata nseScriptMetadata, DateTime from, DateTime to, TimeFrame timeFrame) {
    String token = nseScriptMetadata.getBrokerScriptIdMap().get(FYERS);
    if (token == null || token.isEmpty()) {
      return new ArrayList<>();
    } else {
      var candles = new ArrayList<Candle>();
      var start = from;
      while (start.isBefore(to)) {
        if (start.plusDays(99).isBefore(to)) {
          candles.addAll(
              getHistoricalData(nseScriptMetadata, start, start.plusDays(99), timeFrame));
          start = start.plusDays(100);
        } else {
          candles.addAll(getHistoricalData(nseScriptMetadata, start, to, timeFrame));
          start = to;
        }
      }
      return candles;
    }
  }

  @NotNull
  private List<Candle> getHistoricalData(
      NseScriptMetadata nseScriptMetadata, DateTime from, DateTime to, TimeFrame timeFrame) {
    var resolution =
        switch (timeFrame) {
          case MINUTE_1 -> "1";
          case MINUTE_3 -> "3";
          case MINUTE_5 -> "5";
          case MINUTE_10 -> "10";
          case MINUTE_15 -> "15";
          case MINUTE_30 -> "30";
          case HOUR_1 -> "60";
          default -> throw new RuntimeException("Timeframe, " + timeFrame + " is not supported");
        };
    String formattedFrom = from.toLocalDate().toString();
    String formattedTo = to.toLocalDate().toString();
    String symbol = nseScriptMetadata.getBrokerScriptNameMap().get(BrokerConfig.BrokerType.FYERS);
    String continuousFlag =
        (nseScriptMetadata.getId().getScript().endsWith("1!")
                || nseScriptMetadata.getId().getScript().endsWith("2!")
                || nseScriptMetadata.getId().getScript().endsWith("3!"))
            ? "1"
            : "";

    final String url =
        "https://api.fyers.in/data-rest/v2/history/?resolution={resolution}&date_format=1&range_from={formattedFrom}&range_to={formattedTo}&cont_flag={continuousFlag}&symbol={symbol}";
    Map<String, String> params = new HashMap<>();
    params.put("resolution", resolution);
    params.put("formattedFrom", formattedFrom);
    params.put("formattedTo", formattedTo);
    params.put("symbol", symbol);
    params.put("continuousFlag", continuousFlag);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.put(
        "Authorization",
        Collections.singletonList(
            fyersBrokerConfigDetails.getAppId() + ":" + fyersBrokerConfigDetails.getAccessToken()));
    HttpEntity request = new HttpEntity(headers);
    try {
      ResponseEntity<FyersHistoricalResponse> fyersHistoricalResponse =
          restTemplate.exchange(
              url, HttpMethod.GET, request, FyersHistoricalResponse.class, params);
      return fyersHistoricalResponse.getBody().getCandles().stream()
          .map(singleCandle -> createCandleFromHistoricalData(singleCandle, timeFrame))
          .collect(Collectors.toList());
    } catch (Exception e) {
      log.error("Cannot get data for {}", symbol, e);
      return new ArrayList<>();
    }
  }

  private static Candle createCandleFromHistoricalData(
      List<String> singleCandle, TimeFrame timeFrame) {
    return new Candle(
        new Date(Instant.ofEpochSecond(Long.parseLong(singleCandle.get(0))).getMillis()),
        Double.parseDouble(singleCandle.get(1)),
        Double.parseDouble(singleCandle.get(2)),
        Double.parseDouble(singleCandle.get(3)),
        Double.parseDouble(singleCandle.get(4)),
        Double.parseDouble(singleCandle.get(5)),
        0,
        timeFrame);
  }
}
