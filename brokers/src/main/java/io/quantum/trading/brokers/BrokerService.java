package io.quantum.trading.brokers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quantum.trading.enums.TimeFrame;
import io.quantum.trading.exception.ApiError;
import io.quantum.trading.patterns.Candle;
import io.quantum.trading.redis.RedisCrudService;
import io.quantum.trading.util.CandleType;
import io.quantum.trading.util.candles.CandleConfig;
import io.quantum.trading.util.candles.CandleConsumer;
import io.quantum.trading.util.candles.RenkoCandleConsumer;
import java.util.*;
import java.util.concurrent.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnProperty(prefix = "redis", name = "host")
public class BrokerService {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private final RedisCrudService redisCrudService;
  private Map<String, List<LiveTick>> inMemCache = new ConcurrentHashMap<>();
  private Map<CandleConfig, List<Candle>> inMemCandles = new ConcurrentHashMap<>();
  private static final ExecutorService IN_MEM_CACHE_MANAGER =
      Executors.newSingleThreadExecutor(
          r -> {
            var t = new Thread(r, "in-mem-cache-manager");
            t.setDaemon(true);
            return t;
          });
  private static final String IN_MEM_CACHE_LCK = "IN_MEM_CACHE_LCK";
  private static final long IN_MEM_CACHE_MANAGER_SYNC_WAIT_TIME_MILLIS = 100;
  private Future<Object> inMemCacheManagerTask;

  private void cacheListsSync(Map<String, List<CandleConsumer>> cacheKeys) {
    log.info("Cache Manager syncing cacheKeys - {}", cacheKeys);
    synchronized (IN_MEM_CACHE_LCK) {
      if (this.inMemCacheManagerTask != null) {
        log.warn("IN_MEM_CACHE_MANAGER task is running. Killing it first");
        this.inMemCacheManagerTask.cancel(true);
      }
      inMemCache.clear();
      cacheKeys
          .keySet()
          .forEach(
              cacheKey ->
                  inMemCache.put(cacheKey, Collections.synchronizedList(new LinkedList<>())));
    }

    this.inMemCacheManagerTask =
        IN_MEM_CACHE_MANAGER.submit(
            () -> {
              synchronized (IN_MEM_CACHE_LCK) {
                while (true) {
                  for (String cacheKey : cacheKeys.keySet()) {
                    var candleConsumers = cacheKeys.get(cacheKey);
                    var redisListSize = redisCrudService.llen(cacheKey);
                    var inMemListSize = inMemCache.get(cacheKey).size();
                    var redisFirstEle =
                        redisListSize > 0
                            ? MAPPER.readValue(
                                redisCrudService.getFromList(cacheKey, 0, 0).get(0).toString(),
                                LiveTick.class)
                            : null;
                    var inMemFirstEle = inMemListSize > 0 ? inMemCache.get(cacheKey).get(0) : null;

                    if (redisListSize == 0 && inMemListSize == 0) {
                      log.debug("Redis and inMemCache is empty for key, {}", cacheKey);
                    } else if (redisListSize < inMemListSize
                        || inMemFirstEle == null
                        || !redisFirstEle.equals(inMemFirstEle)) {
                      // 1. The inMemListSize is higher than the redis one.
                      // This cannot happen
                      // 2. inMemListSize is 0 but redis has data
                      // 3. The 1st elements in redis and inMemCache doesn't
                      // match
                      log.debug("Hard refreshing for key, {}", cacheKey);
                      var inMemList = inMemCache.get(cacheKey);
                      inMemList.clear(); // Just to be safe
                      // In case the response from Redis is empty, the
                      // imMemCandles are not cleared. So, clearing
                      candleConsumers.forEach(
                          candleConsumer ->
                              inMemCandles.put(
                                  candleConsumer.getCandleConfig(), Collections.emptyList()));
                      redisCrudService
                          .getFromList(cacheKey, 0)
                          .forEach(
                              item -> {
                                try {
                                  var liveTick = MAPPER.readValue(item.toString(), LiveTick.class);
                                  inMemList.add(liveTick);
                                  candleConsumers.forEach(
                                      candleConsumer -> {
                                        candleConsumer.consume(liveTick);
                                        inMemCandles.put(
                                            candleConsumer.getCandleConfig(),
                                            candleConsumer.getCandles());
                                      });
                                } catch (JsonProcessingException e) {
                                  log.error("Error while parsing LiveTick", e);
                                }
                              });
                    } else {
                      // Soft refresh
                      log.debug("Soft refreshing for key, {}", cacheKey);
                      var inMemList = inMemCache.get(cacheKey);
                      // Here size()-1 is not required. We are asking for the
                      // next position in the list
                      redisCrudService
                          .getFromList(cacheKey, inMemList.size())
                          .forEach(
                              item -> {
                                try {
                                  var liveTick = MAPPER.readValue(item.toString(), LiveTick.class);
                                  inMemList.add(liveTick);
                                  candleConsumers.forEach(
                                      candleConsumer -> {
                                        candleConsumer.consume(liveTick);
                                        inMemCandles.put(
                                            candleConsumer.getCandleConfig(),
                                            candleConsumer.getCandles());
                                      });
                                } catch (JsonProcessingException e) {
                                  log.error("Error while parsing LiveTick", e);
                                }
                              });
                    }
                  }
                  Thread.sleep(IN_MEM_CACHE_MANAGER_SYNC_WAIT_TIME_MILLIS);
                }
              }
            });
  }

  public void cacheCandles(List<CandleConfig> candleConfigs) {
    Map<String, List<CandleConsumer>> cacheKeys = new HashMap<>();
    for (CandleConfig candleConfig : candleConfigs) {
      var symbol = candleConfig.getSymbol() + "_today";
      if (!cacheKeys.containsKey(symbol)) {
        cacheKeys.put(symbol, new ArrayList<>());
      }
      switch (candleConfig.getType()) {
        case CandleType.RENKO:
          cacheKeys.get(symbol).add(new RenkoCandleConsumer(candleConfig));
          break;
        case CandleType.HA:
        default:
          log.warn("Candle type, {} is not supported", candleConfig.getType());
      }
      cacheListsSync(cacheKeys);
    }
  }

  public List<Candle> getCandles(CandleConfig candleConfig) {
    if (inMemCandles.containsKey(candleConfig)) return inMemCandles.get(candleConfig);
    else return Collections.emptyList();
  }

  public Optional<List<Candle>> getLatestCandles() {
    var dt = new Date();
    List<Candle> candles = new ArrayList<>();
    while (System.currentTimeMillis() - dt.getTime() < 2000) {
      var expectedLastCandleTs =
          new DateTime().withMillisOfSecond(0).withSecondOfMinute(0).minusMinutes(1).toDate();
      var expectedNextCandleTs =
          new DateTime().withMillisOfSecond(0).withSecondOfMinute(0).toDate();
      Candle lastCandle = null;
      Candle nextCandle = null;
      try {
        lastCandle =
            MAPPER.readValue(redisCrudService.getKeyValue("BankNifty").toString(), Candle.class);
        nextCandle =
            MAPPER.readValue(
                redisCrudService.getKeyValue("BankNifty_Next").toString(), Candle.class);
      } catch (Throwable e) {
        // ignore
        log.warn("Unable to get latest candles", e);
        return Optional.empty();
      }
      if (expectedLastCandleTs.equals(lastCandle.getBeginTs())
          && expectedNextCandleTs.equals(nextCandle.getBeginTs())) {
        candles.add(lastCandle);
        candles.add(nextCandle);
        return Optional.of(candles);
      }
    }
    return Optional.empty();
  }

  public Optional<List<Candle>> getLatestCandles(String prefix) {
    var dt = new Date();
    List<Candle> candles = new ArrayList<>();
    while (System.currentTimeMillis() - dt.getTime() < 6000) {
      var expectedLastCandleTs =
          new DateTime().withMillisOfSecond(0).withSecondOfMinute(0).minusMinutes(1).toDate();
      var expectedNextCandleTs =
          new DateTime().withMillisOfSecond(0).withSecondOfMinute(0).toDate();
      Candle lastCandle = null;
      Candle nextCandle = null;
      try {
        lastCandle =
            MAPPER.readValue(redisCrudService.getKeyValue(prefix).toString(), Candle.class);
        nextCandle =
            MAPPER.readValue(
                redisCrudService.getKeyValue(prefix + "_Next").toString(), Candle.class);
      } catch (Throwable e) {
        // ignore
        log.warn("Unable to get latest candles", e);
        return Optional.empty();
      }
      if (expectedLastCandleTs.equals(lastCandle.getBeginTs())
          && expectedNextCandleTs.equals(nextCandle.getBeginTs())) {
        candles.add(lastCandle);
        candles.add(nextCandle);
        return Optional.of(candles);
      }
    }
    return Optional.empty();
  }

  public Candle getLiveTick(String instrumentSymbol, boolean isBacktestMode) {
    try {
      var redisVal = redisCrudService.getKeyValue(instrumentSymbol);
      if (redisVal != null) {
        if (isBacktestMode) {
          return MAPPER.readValue(redisVal.toString(), Candle.class);
        } else {
          return MAPPER.readValue(redisVal.toString(), LiveTick.class).toCandle();
        }
      } else throw new BrokerException(ApiError.NOT_FOUND, instrumentSymbol);
    } catch (JsonProcessingException e) {
      log.error("Error while getting LTP from cache store", e);
      return getLiveTick(instrumentSymbol, isBacktestMode);
    }
  }

  public List<LiveTick> getTodayLtps(String instrumentSymbol) {
    var todayLiveTicksKey = instrumentSymbol + "_today";
    if (inMemCache.containsKey(todayLiveTicksKey)) {
      log.debug("Coming from cache");
      return inMemCache.get(todayLiveTicksKey);
    } else {
      log.debug("Not Coming from cache");
      var liveTicks = new ArrayList<LiveTick>();
      redisCrudService
          .getFromList(instrumentSymbol + "_today", 0)
          .forEach(
              item -> {
                try {
                  var liveTick = MAPPER.readValue(item.toString(), LiveTick.class);
                  liveTicks.add(liveTick);
                } catch (JsonProcessingException e) {
                  log.error("Error while parsing LiveTick", e);
                }
              });
      return liveTicks;
    }
  }

  public List<Candle> getPreviousNCandles(String instrumentToken, int n) {
    /*var from = DateTime.now()
            .minusMinutes(n)
            .withSecondOfMinute(0)
            .withMillisOfSecond(0);
    var to = from.plusMinutes(n)
            .minusMillis(1);
    log.info("Starting triggerGetting historical api");
    var candles = getHistoricalData(valueOf(instrumentToken), from, to, MINUTE_1);
    log.info("Obtained historical candles for {} - {} as {}", from ,to, candles);
    return candles;*/
    return null;
  }

  public List<Candle> getPreviousTodayCandles(String instrumentToken, TimeFrame timeFrame) {
    /*var from = DateTime.now()
            .withTimeAtStartOfDay()
            .withHourOfDay(9)
            .withMinuteOfHour(15);
    var to = DateTime.now()
            .withSecondOfMinute(0)
            .withMillisOfSecond(0)
            .minusMillis(1);
    log.info("Starting triggerGetting historical api");
    var candles = getHistoricalData(valueOf(instrumentToken), from, to, timeFrame);
    log.info("Obtained historical candles for {} - {} as {}", from ,to, candles);
    return candles;*/
    return null;
  }
}
