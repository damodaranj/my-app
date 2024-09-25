package io.quantum.trading.brokers.indian;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quantum.trading.brokers.Broker;
import io.quantum.trading.brokers.BrokerException;
import io.quantum.trading.brokers.Currency;
import io.quantum.trading.brokers.Exchange;
import io.quantum.trading.entities.NseScriptMetadata;
import io.quantum.trading.util.OmsUtil;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class NseBroker extends Broker {

  public static final String MIS = "MIS";
  public static final String CNC = "CNC";
  public static final String NRML = "NRML";

  private Map<String, Integer> freezeLimits = new HashMap<>();

  @Getter protected Map<NseScriptMetadata.NseMetadataId, NseScriptMetadata> scriptMetadataMap;
  protected Map<Long, List<NseScriptMetadata.NseMetadataId>> scriptsTokenSymbolMap;
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  public Currency getCurrency() throws BrokerException {
    return Currency.INR;
  }

  public final int getLotSize(String exchangeScript) {
    var script = OmsUtil.getScript(exchangeScript);
    var exchange = OmsUtil.getExchange(exchangeScript);
    return scriptMetadataMap
        .get(new NseScriptMetadata.NseMetadataId(script, Exchange.valueOf(exchange)))
        .getLotSize();
  }

  public final int getFreezeLimitForScript(String exchangeScript) {
    var script = OmsUtil.getScript(exchangeScript);
    var exchange = OmsUtil.getExchange(exchangeScript);
    return scriptMetadataMap
        .get(new NseScriptMetadata.NseMetadataId(script, Exchange.valueOf(exchange)))
        .getFreezeLimit();
  }

  public abstract String getBrokerToken(NseScriptMetadata nseScriptMetadata);

  public abstract String getBrokerScriptName(NseScriptMetadata nseScriptMetadata);

  public final void updateScriptMetadata(
      Map<NseScriptMetadata.NseMetadataId, NseScriptMetadata> scriptMetadataMap,
      Map<Long, List<NseScriptMetadata.NseMetadataId>> scriptTokenSymbolMap) {
    this.scriptMetadataMap = scriptMetadataMap;
    this.scriptsTokenSymbolMap = scriptTokenSymbolMap;
  }

  //  public Instrument getOptionsChildInstrumentWithAtm(
  //      double atm, double roundOff, int factor, TradeType optionsType, String instrumentName) {
  //    var type = optionsType == TradeType.OPTIONS_CE ? "CE" : "PE";
  //    String regex;
  //    int strikePrice;
  //    if (atm % roundOff <= roundOff / 2) {
  //      strikePrice = (((int) (Math.floor(atm / roundOff)) *
  // Double.valueOf(roundOff).intValue()));
  //    } else {
  //      strikePrice = (((int) (Math.ceil(atm / roundOff)) * Double.valueOf(roundOff).intValue()));
  //    }
  //
  //    strikePrice = strikePrice + factor;
  //    regex = strikePrice + type;
  //
  //    //        var instruments = getInstruments();
  //    //        var filtered = instruments.stream()
  //    //                .filter(instrument -> instrument.name != null &&
  //    // instrument.name.equals(instrumentName))
  //    //                .filter(instrument -> instrument.tradingsymbol.endsWith(regex))
  //    //                .sorted(Comparator.comparing(o -> o.expiry))
  //    //                .toList();
  //    //        return filtered.get(0);
  //
  //    return null;
  //  }

  protected final int getFreezeLimit(String name) {
    if (freezeLimits.isEmpty()) {
      var fyersUrls =
          List.of(
              "https://public.fyers.in/sym_details/NSE_CM_sym_master.json",
              "https://public.fyers.in/sym_details/BSE_FO_sym_master.json",
              "https://public.fyers.in/sym_details/NSE_FO_sym_master.json");
      fyersUrls.forEach(
          (fyersUrl) -> {
            try (var client = HttpClient.newHttpClient()) {
              var req =
                  HttpRequest.newBuilder()
                      .uri(new URI(fyersUrl))
                      .timeout(Duration.of(10, SECONDS.toChronoUnit()))
                      .GET()
                      .build();
              var res = client.send(req, HttpResponse.BodyHandlers.ofString());
              Map<String, Map<String, Object>> scriptMap =
                  OBJECT_MAPPER.readValue(res.body(), Map.class);
              scriptMap.forEach(
                  (symbol, details) -> {
                    var freezeLimit = String.valueOf(details.get("qtyFreeze")).trim();
                    if (!freezeLimit.isEmpty()) {
                      freezeLimits.put(
                          details.get("underSym").toString(), Integer.parseInt(freezeLimit));
                    }
                  });

            } catch (URISyntaxException | IOException | InterruptedException e) {
              throw new RuntimeException(e);
            }
          });
    }
    return freezeLimits.getOrDefault(name, 0);
  }
}
