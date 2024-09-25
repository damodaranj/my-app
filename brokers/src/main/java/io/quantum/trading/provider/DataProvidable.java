package io.quantum.trading.provider;

import io.quantum.trading.brokers.LiveTick;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public interface DataProvidable {
  Set<String> WATCH_LIST = new HashSet<>();
  String WATCH_LIST_LOCK = "WATCH_LIST_LOCK";

  default void addToWatchList(String exchangeScript) {
    synchronized (WATCH_LIST_LOCK) {
      WATCH_LIST.add(exchangeScript);
      subscribe(exchangeScript);
    }
  }

  default void removeFromWatchList(String exchangeScript) {
    synchronized (WATCH_LIST_LOCK) {
      WATCH_LIST.remove(exchangeScript);
      unSubscribe(exchangeScript);
    }
  }

  void startFeed(Function<List<LiveTick>, Void> liveTickConsumer);

  void disconnectFeed();

  void subscribe(String script);

  void unSubscribe(String script);

  default void reconnectFeed() {
    WATCH_LIST.forEach(this::subscribe);
  }
}
