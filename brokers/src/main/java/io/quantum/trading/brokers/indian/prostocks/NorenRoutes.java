package io.quantum.trading.brokers.indian.prostocks;

import java.util.HashMap;
import java.util.Map;

public class NorenRoutes {
  public Map<String, String> routes =
      new HashMap<String, String>() {
        {
          this.put("authorize", "/QuickAuth");
          this.put("logout", "/Logout");
          this.put("searchscrip", "/SearchScrip");
          this.put("orderbook", "/OrderBook");
          this.put("tradebook", "/TradeBook");
          this.put("placeorder", "/PlaceOrder");
          this.put("modifyorder", "/ModifyOrder");
          this.put("cancelorder", "/CancelOrder");
          this.put("getquotes", "/GetQuotes");
          this.put("singleorderhistory", "/SingleOrdHist");
        }
      };
  public static String _host = "http://kurma.kambala.co.in:9959/NorenWClient/";

  public NorenRoutes() {}

  public String get(String key) {
    String var10000 = _host;
    return var10000 + (String) this.routes.get(key);
  }
}
