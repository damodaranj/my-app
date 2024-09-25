package io.quantum.trading.brokers.forex.mt5protocol;

import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.*;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Examples {

  static void startThreads() throws ConnectException {
    new Thread(
            () -> {
              for (int i = 0; i < 5; i++) {
                System.out.println("Starting thread " + i);
                var exnessApi = new MT5API(175763435, "Dinesh@123", "15.184.133.53", 443);
                var octaFxapi = new MT5API(213029201, "YYa7G$^j", "d51a1.octanetwork.net", 443);
                try {
                  exnessApi.Connect();
                  octaFxapi.Connect();
                } catch (ConnectException e) {
                  throw new RuntimeException(e);
                }
              }
            })
        .start();
  }

  static void printThreadCount() {
    while (true) {
      ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
      ThreadGroup parentGroup;

      // Traverse through all thread groups to get all active threads
      while ((parentGroup = rootGroup.getParent()) != null) {
        rootGroup = parentGroup;
      }

      // Get the number of active threads
      int activeCount = rootGroup.activeCount();

      // Create an array to hold the active threads
      Thread[] threads = new Thread[activeCount];

      // Enumerate the active threads into the array
      rootGroup.enumerate(threads);
      var threadCountMap =
          new HashMap<>(
              Map.of(
                  "MT5CmdHandler-175763435", 0L,
                  "MT5CmdHandler-213029201", 0L,
                  "MT5ConnectorThread-175763435", 0L,
                  "MT5ConnectorThread-213029201", 0L));
      threadCountMap
          .keySet()
          .forEach(
              k -> {
                threadCountMap.put(
                    k,
                    Arrays.stream(threads)
                        .filter(Objects::nonNull)
                        .filter(t -> t.getName().startsWith(k))
                        .count());
              });

      System.out.println(threadCountMap);
      try {
        Thread.sleep(1_000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    startThreads();
    printThreadCount();
    // Copier5ApiTrader.main(args);
    //       new Examples().run();
  }

  public void run() throws Exception {
    Logger.OnMsg.addListener(new OnMsg());
    // String s = Broker.Search("fxdd");
    /// modifyAsync();
    // quotes();
    // orderHistory2();
    // orderHistory3();
    // orderHistory5();
    // marketOrder();
    // marketOrderAsync();
    // modifyOrder();
    // pendingOrder();
    // serversDat();
    // accountBalanceWithServersDat();
    // brokerSearch();
    // certificate();
    // tradeMode();
    // isInvestor();
    // orderUpdate();
    // partialClose();
    // test();
    // modifyCheck();
    // multipleAccounts();
    // orderHistory1();
    // quoteHitory();
    // tradeTest();
    // onDisconnect();
    // multiThreadTrading();
    // orderUpdate2();
    // pingPong();
    // nettingOrHedging();
    profit();
  }

  private void profit() throws Exception {
    // This is a demo account
    MT5API api = new MT5API(213029201, "YYa7G$^j", "d51a1.octanetwork.net", 443);
    api.Connect();
    System.out.println("Connected. Balance = " + api.AccountBalance());
    // Closing all the other orders so as to not cause any confusions
    for (Order order : api.GetOpenedOrders()) {
      api.OrderClose(order.Ticket, order.Symbol, 0, order.Lots, order.OrderType, 0);
    }
    System.out.println("Connected. Balance after closing open orders = " + api.AccountBalance());
    Thread.sleep(5_000);
    // Place an order for XAUUSD.
    api.OrderSend("XAUUSD", 0.01, 0, OrderType.Buy, 0);

    // Even though the tick value for XAUUSD changes, the Profit field is always zero
    while (true) {
      for (Order order : api.GetOpenedOrders()) {
        System.out.println(order.Ticket + " -- " + order.Symbol + " -- " + order.Profit);
      }
      Thread.sleep(2_000);
    }
  }

  private void nettingOrHedging() throws ConnectException {
    MT5API api = new MT5API(62333850, "tecimil4", "78.140.180.198", 443);
    api.Connect();
    System.out.println("Connected " + api.AccountMethod());
  }

  void loginid() throws IOException, ConnectException, TimeoutException {
    MT5API api = new MT5API(62333850, "tecimil4", "78.140.180.198", 443);
    api.LoginIdExServerUrls = new ArrayList<String>(Arrays.asList("http://localhost:5101"));
    api.Connect();
    System.out.println("Connected");
  }

  private void modifyAsync() throws ConnectException, IOException {
    // MT5API api = new MT5API(101240050, "Ava112358", "3.10.134.148", 443);
    MT5API api = new MT5API(62333850, "tecimil4", "78.140.180.198", 443);
    api.Connect();
    api.OrderModifyAsync(
        api.GetRequestId(), 1792712504, "EURUSD", 0.1, 1, OrderType.BuyLimit, 0, 0, 0, null, 0);
  }

  private static void orderUpdate2()
      throws TimeoutException, IOException, ConnectException, InterruptedException {

    // MT5API api = new MT5API(2003, "KDejcx$385", "91.223.0.56", 443);
    // MT5API api = new MT5API(210527124, "Dy3YPa9E", "34.105.200.212", 443);
    MT5API api = new MT5API(21455, "1nJeS+Ae", "95.217.147.61", 443);
    api.OnOrderUpdate.addListener(
        new OnOrderUpdate() {

          @Override
          public void invoke(MT5API sender, OrderUpdate update) {
            System.out.println(
                update.Type + " " + update.Order.OpenTime + " " + api.GetOpenedOrders().length);
          }
        });

    api.Connect();
    api.Subscribe("EURUSD");

    System.out.println(api.Account.Type);
    Thread.sleep(100000);
  }

  private void multiThreadTrading() throws Exception {
    // MT5API qc2 = new MT5API(62333850, "tecimil4", "78.140.180.198", 443);
    // MT5API api = new MT5API(5012314181L, "4uxvtuwu", "78.140.180.201", 443);
    MT5API api = new MT5API(101240050, "Ava112358", "3.10.134.148", 443);
    api.Connect();
    String group = api.Account.Type;
    String symbol = "EURUSD";
    for (int i = 0; i < 30; i++) {
      tradeAsync(api, symbol);
    }
    System.out.println("Orders sent");
  }

  private void tradeTest() throws ConnectException, IOException, InterruptedException {
    String symbol = "EURUSD";
    MT5API api = new MT5API(101240050, "Ava112358", "3.10.134.148", 443);
    //        api.LastMessageFromServerTimeoutMs = 20000;
    api.Connect();
    System.out.println("Connect to server successfully at " + new Date());
    System.out.println("Connected. Balance = " + api.AccountBalance());
    api.OnQuote.addListener((sender, quote) -> System.out.print("."));
    api.Subscribe(symbol);
    while (true) {
      if (!api.Connected()) {
        System.out.println("\nThe connected of socket is false at " + new Date());
        try {
          Order order = api.OrderSend("EURUSD", 0.01, 0, OrderType.Buy);
          if (order != null) {
            System.out.println("Order open successfully.");
            break;
          } else {
            System.out.println("Order open fails.");
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        System.out.println("\nThe connected of socket is true at " + new Date());
      }
      Thread.sleep(20000);
    }
  }

  void quoteHitory() throws IOException, TimeoutException, ConnectException {
    MT5API qc = new MT5API(101240050, "Ava112358", "3.10.134.148", 443);
    qc.Connect();
    SymbolInfo info = qc.Symbols.GetInfo("EURUSD");
    SymGroup group = qc.Symbols.GetGroup("EURUSD");
    double minlots = group.MinLots();
    double digits = info.Digits;
    double tv = qc.GetTickValue("EURUSD");
    double ts = info.tick_size;

    System.out.println("Connected");
    qc.OnQuoteHistory.addListener(
        (mt5API, args) -> {
          System.out.println(args.Bars.size());
          List<Bar> h4bars = MT5API.ConvertToTimeframe(args.Bars, 240);
          System.out.println(h4bars.size());
        });
    qc.RequestQuoteHistoryMonth("EURUSD", 2023, 3, 1);
  }

  private void multipleAccounts() throws IOException, ConnectException, TimeoutException {
    MT5API qc1 = new MT5API(101240050, "Ava112358", "3.10.134.148", 443);
    qc1.Connect();
    System.out.println(qc1.AccountCompanyName() + " connected. Balance = " + qc1.AccountBalance());
    MT5API qc2 = new MT5API(62333850, "tecimil4", "78.140.180.198", 443);
    qc2.Connect();
    System.out.println(qc2.AccountCompanyName() + " connected. Balance = " + qc2.AccountBalance());
    String symbol = "EURUSD";
    tradeAsync(qc1, symbol);
    tradeAsync(qc2, symbol);
  }

  void tradeAsync(MT5API qc, String symbol) {
    (new Thread(
            new Runnable() {
              public void run() {

                try {
                  while (qc.GetQuote(symbol) == null) Thread.sleep(1);
                  double ask = qc.GetQuote(symbol).Ask;
                  Order order = qc.OrderSend(symbol, 0.01, ask, OrderType.Buy);
                  System.out.println(qc.AccountCompanyName() + " opened " + order.Ticket);
                  double bid = qc.GetQuote(symbol).Bid;
                  order = qc.OrderClose(order.Ticket, order.Symbol, bid, order.Lots, OrderType.Buy);
                  System.out.println(qc.AccountCompanyName() + " closed");
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }))
        .start();
  }

  public void modifyCheck() {
    String serverName = "Ava-Demo 1-MT5";
    try {
      MT5API mt5Api = new MT5API(101240050, "Ava112358", "3.10.134.148", 443);
      mt5Api.OnOrderUpdate.addListener(
          (mt5API, orderUpdate) -> {
            if (orderUpdate.Type != null)
              System.out.println(orderUpdate.Type + " " + orderUpdate.Order.Ticket);
          });
      mt5Api.Connect();
      Arrays.stream(mt5Api.GetOpenedOrders())
          .forEach(o -> System.out.println(o.Symbol + " " + o.Ticket + " " + o.Lots));
      String eurusd = "AUDCAD";
      Order openOrder =
          mt5Api.OrderSend(
              eurusd,
              0.01,
              0,
              OrderType.Buy,
              0.0,
              0.0,
              10000,
              "",
              2000060,
              mt5Api.GetFillPolicy(eurusd, OrderType.Buy));
      System.out.println("Order " + openOrder.Ticket + " is opened.");
      Arrays.stream(mt5Api.GetOpenedOrders())
          .forEach(o -> System.out.println(o.Symbol + " " + o.Ticket + " " + o.Lots));
      mt5Api.OrderModify(openOrder.Ticket, null, 0, 0, openOrder.OrderType, 0.5, 1.5);
      Arrays.stream(mt5Api.GetOpenedOrders())
          .forEach(o -> System.out.println(o.Symbol + " " + o.Ticket + " " + o.Lots));
      Order order =
          Arrays.stream(mt5Api.GetOpenedOrders())
              .filter(o -> o.Ticket == openOrder.Ticket)
              .findFirst()
              .orElse(null);
      if (order == null) {
        System.out.println(
            " After modification order " + openOrder.Ticket + " is not in opened orders.");
      } else {
        System.out.println(
            "Order " + order.Ticket + " is still in opened orders and lots is " + order.Lots);
      }
      while (true) {
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    } catch (Exception ignored) {
      ignored.printStackTrace();
    }
  }

  void test() {
    String serverName = "Ava-Demo 1-MT5";
    try {
      MT5API mt5Api = new MT5API(101240050, "Ava112358", "3.10.134.148", 443);
      mt5Api.Connect();
      Order openOrder =
          mt5Api.OrderSend(
              "EURUSD",
              0.01,
              0,
              OrderType.Buy,
              0,
              0,
              1000,
              "",
              0,
              mt5Api.GetFillPolicy("EURUSD", OrderType.Buy));
      System.out.println("Order " + openOrder.Ticket + " is opened.");
      mt5Api.OrderClose(openOrder.Ticket, "EURUSD", 0, openOrder.Lots, OrderType.Buy);
      System.out.println("Order " + openOrder.Ticket + " is closed.");
      Arrays.stream(mt5Api.GetOpenedOrders())
          .forEach(o -> System.out.println(o.Symbol + " " + o.Ticket + " " + o.Lots));
      Order order =
          Arrays.stream(mt5Api.GetOpenedOrders())
              .filter(o -> o.Ticket == openOrder.Ticket)
              .findFirst()
              .orElse(null);
      if (order == null) {
        System.out.println("Order is not in opened orders.");
      } else {
        System.out.println(
            "Order " + order.Ticket + " is still in opened orders and lots is " + order.Lots);
      }
    } catch (Exception ignored) {
    }
  }

  private void partialClose() throws IOException {
    String serverName = "Ava-Demo 1-MT5";
    try {
      MT5API mt5Api = new MT5API(101240050, "Ava112358", "3.10.134.148", 443);
      mt5Api.Connect();
      Order openOrder =
          mt5Api.OrderSend(
              "EURUSD",
              0.02,
              0,
              OrderType.Buy,
              0,
              0,
              1000,
              "",
              0,
              mt5Api.GetFillPolicy("EURUSD", OrderType.Buy));
      System.out.println("opened " + openOrder.Ticket);
      Order closeOrder =
          mt5Api.OrderClose(
              openOrder.Ticket,
              "EURUSD",
              0,
              0.01,
              OrderType.Buy,
              0,
              mt5Api.GetFillPolicy("EURUSD", OrderType.Buy));
      Order[] orders = mt5Api.GetOpenedOrders();
      Order order =
          Arrays.stream(mt5Api.GetOpenedOrders())
              .filter(o -> o.Ticket == openOrder.Ticket)
              .findFirst()
              .orElse(null);
      if (order == null) {
        System.out.println("we didnt find the partial close order.");
      } else {
        System.out.println("we find the partial close order.");
      }
      mt5Api.Disconnect();
      mt5Api.Connect();
      order =
          Arrays.stream(mt5Api.GetOpenedOrders())
              .filter(o -> o.Ticket == openOrder.Ticket)
              .findFirst()
              .orElse(null);
      if (order == null) {
        System.out.println("we didnt find the partial close order.");
      } else {
        System.out.println("we find the partial close order.");
      }
    } catch (Exception ignored) {
    }
  }

  private void marketOrderSafe() throws Exception {
    MT5API api = new MT5API(62333850, "tecimil4", "78.140.180.198", 443);
    api.Connect();
    System.out.println("Connected. Balance = " + api.AccountBalance());
    String symbol = "EURUSD";
    api.Subscribe(symbol);
    while (api.GetQuote(symbol) == null) Thread.sleep(1);
    System.out.println("Got first quote " + api.GetQuote(symbol));
    Order o;
    try {
      o = api.OrderSend(symbol, 0.1, api.GetQuote(symbol).Ask, OrderType.Buy, 0, 0, 1000, null, 0);
    } catch (java.net.ConnectException ex) {
      api.Disconnect();
      api.Connect();
      o = api.OrderSend(symbol, 0.1, api.GetQuote(symbol).Ask, OrderType.Buy, 0, 0, 1000, null, 0);
    } catch (SocketException ex) {
      api.Disconnect();
      api.Connect();
      o = api.OrderSend(symbol, 0.1, api.GetQuote(symbol).Ask, OrderType.Buy, 0, 0, 1000, null, 0);
    }

    System.out.println("Order " + o.Ticket + " opened at " + o.OpenPrice);
    System.out.println("Opened orders:");
    for (Order order : api.GetOpenedOrders()) {
      System.out.println(order.Ticket);
    }
    Thread.sleep(1000);
    o = api.OrderClose(o.Ticket, o.Symbol, api.GetQuote(symbol).Bid, o.Lots, o.OrderType, 1000);
    System.out.println("Closed at " + o.ClosePrice);
  }

  private void orderUpdate() throws TimeoutException, IOException, ConnectException {
    // MT5API api = new MT5API(66903271, "e3gmvlml", "51.116.173.111", 443);
    MT5API api = new MT5API(1009023, "otso123", "20.48.6.157", 443);
    api.OnOrderUpdate.addListener(
        new OnOrderUpdate() {
          @Override
          public void invoke(MT5API sender, OrderUpdate update) {

            System.out.println(
                update.Type + " " + update.Order.OpenTime + " " + api.GetOpenedOrders().length);
          }
        });
    api.Connect();

    System.out.println(api.Account.Type);
  }

  private void isInvestor() throws TimeoutException, IOException, ConnectException {
    MT5API api = new MT5API(66903271, "e3gmvlml", "51.116.173.111", 443);
    api.Connect();
    System.out.println(api.IsInvestor());
    System.out.println(api.Account.Type);
  }

  void tradeMode() throws TimeoutException, IOException, ConnectException {

    // var api = new MT5API(62401092, "Chien123456", "35.157.110.232", 443);
    // var api = new MT5API(8631960, "dd4444vv", "mt5demo.acetopfx.com", 443);
    MT5API api = new MT5API(66903271, "e3gmvlml", "51.116.173.111", 443);
    api.Connect();
    SymGroup g = api.Symbols.GetGroup("EURUSD");
    System.out.println(g.TradeMode);
    System.out.println(isTradeSession(api, "EURUSD"));
  }

  public boolean isTradeSession(MT5API api, String symbol) {
    ArrayList<ArrayList<Session>> tradeSessionsForWeek = api.Symbols.Sessions.get(symbol).Trades;
    ArrayList<Session> todaySessions =
        tradeSessionsForWeek.get(api.ServerTime().getDayOfWeek().getValue());
    for (Session item : todaySessions)
      if (api.ServerTime().getMinute() > item.StartTime
          && api.ServerTime().getMinute() < item.EndTime) return true;
    return false;
  }

  private void certificate() throws IOException, TimeoutException, ConnectException {
    String certPath = "C:\\Yandex.Disk\\Tim\\docs\\tmp\\2041174_HungDingFinancial_22.pfx";
    String certPass = "Dev@1234";
    MT5API api =
        new MT5API(
            2041174,
            "Dev@1234",
            "20.48.6.157",
            443,
            Files.readAllBytes(Paths.get(certPath)),
            certPass);
    api.Connect();
    String symbol = "USDCHF";
    System.out.println("Connected");
    Order o = api.OrderSend(symbol, 0.01, 0, OrderType.Buy);
    System.out.println("Press any key...");
  }

  private void brokerSearch() throws Exception {
    String json = Broker.Search("ava-");
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine scriptEngine = manager.getEngineByName("JavaScript");
    scriptEngine.put("jsonString", json);
    scriptEngine.eval("result = JSON.stringify(JSON.parse(jsonString), null, 2)");
    String prettyPrintedJson = (String) scriptEngine.get("result");
    System.out.println(prettyPrintedJson);
  }

  private void onDisconnect() throws Exception {
    // MT5API api = new MT5API(8631960, "dd4444vv", "mt5demo.acetopfx.com", 443);
    MT5API api = new MT5API(101240050, "Ava112358", "3.10.134.148", 443);
    api.OnConnectProgress.addListener(new OnConnectProgr());
    System.out.println("Connecting...");
    api.Connect();
    System.out.println("Connected");
    api.OnQuote.addListener((sender, quote) -> System.out.print("."));
    api.Subscribe("EURUSD");
    while (true) {
      if (Duration.between(api.TimeOfLastMessageFromServer, LocalDateTime.now()).toMillis()
          > 10000) {
        System.out.println("Warning: no messsages from server for a long time");
      }
      Thread.sleep(5000);
    }
  }

  class OnConnectProgr implements OnConnectProgress {
    @java.lang.Override
    public void invoke(MT5API sender, ConnectEventArgs args) {
      if (args.Progress == ConnectProgress.Disconnect) {
        System.out.println("Try reconnect " + sender.User);
        for (int i = 0; i < 100; i++) {
          try {
            sender.Connect();
            System.out.println(sender.User + " reconnected");
            break;
          } catch (Exception e) {
            System.out.println("Waring: reconnect attempt #" + i + " fail");
          }
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }

  private void symbols() throws Exception {
    MT5API api = new MT5API(8631960, "dd4444vv", "mt5demo.acetopfx.com", 443);
    System.out.println("Connecting...");
    api.Connect();
    System.out.println("Connected");
    for (SymbolInfo symbol : api.Symbols.Infos) System.out.println(symbol.Name);
    System.out.println(api.AccountMargin());
  }

  private void accountBalanceWithServersDat() throws IOException {
    Map<String, String[]> servers = serversDat();
    String serverName = "Acetop-Demo";
    String[] addresses = servers.get(serverName);
    for (String address : addresses) {
      try {
        Map.Entry<String, Integer> hostAndPort = parseAdr(address);
        MT5API api = new MT5API(8631960, "dd4444vv", hostAndPort.getKey(), hostAndPort.getValue());
        api.Connect();
        System.out.println(api.AccountBalance());
        break;
      } catch (Exception e) {
        continue;
      }
    }
  }

  Map<String, String[]> serversDat() throws IOException {
    Server[] servers = MT5API.LoadServersDat("C:\\Users\\sam\\Downloads\\servers (1).dat");
    Map<String, String[]> res = new HashMap<>();
    for (Server server : servers) {
      String name = null;
      if (server.ServerInfo != null) {
        System.out.println(server.ServerInfo.ServerName);
        name = server.ServerInfo.ServerName;
      }
      if (server.ServerInfoEx != null) {
        System.out.println(server.ServerInfoEx.ServerName);
        name = server.ServerInfoEx.ServerName;
      }
      if (name == null) throw new RuntimeException("Server name is null");
      List<String> adr = new LinkedList<>();
      for (Access access : server.Accesses)
        for (AddressRec addr : access.Addresses) {
          System.out.println(addr.Address);
          adr.add(addr.Address);
        }
      res.put(name, adr.toArray(new String[0]));
      System.out.println();
    }
    return res;
  }

  Map.Entry<String, Integer> parseAdr(String ip) {
    int port;
    int i = 0;
    String host = "";
    while (i < ip.length() && ip.getBytes()[i] != (byte) ':') host += (char) ip.getBytes()[i++];
    if (i == ip.length()) port = 443;
    else {
      i++;
      String strPort = "";
      while (i < ip.length()) strPort += (char) ip.getBytes()[i++];
      port = Integer.parseInt(strPort);
    }
    return new AbstractMap.SimpleEntry<String, Integer>(host.trim(), port);
  }

  private void orderHistory1() throws Exception {
    // MT5API api = new MT5API(99083840, "ychqluy5", "112.126.96.179", 443);
    // MT5API api = new MT5API(101240050, "Ava112358", "3.10.134.148", 443);
    MT5API api = new MT5API(75011662, "HArN2w&BC&7&tfK", "dc-r70-03.mt4servers.com", 443);
    api.Connect();
    System.out.println("Connected. Balance = " + api.AccountBalance());
    api.OnTradeHistory.addListener(
        new OnTradeHistory() {
          @Override
          public void invoke(MT5API sender, OrderHistoryEventArgs args) {
            for (Order order : args.Orders) {
              System.out.println(order.Ticket + " " + order.CloseTime);
            }
          }
        });
    api.RequestDealHistory(LocalDateTime.now().minusDays(1000), LocalDateTime.now().plusDays(1));
  }

  void pendingOrder() throws Exception {
    MT5API api = new MT5API(8631960, "dd4444vv", "mt5demo.acetopfx.com", 443);
    System.out.println("Connecting...");
    api.Connect();
    System.out.println(api.AccountMargin());
    System.out.println("Connected");
    String symbol = "EURUSD.DEMO";
    api.Subscribe(symbol);
    while (api.GetQuote(symbol) == null) Thread.sleep(1);
    Order o =
        api.OrderSend(
            symbol,
            0.02,
            api.GetQuote(symbol).Ask * 1.1,
            OrderType.BuyStop,
            0,
            0,
            100,
            null,
            0,
            FillPolicy.FlashFill,
            0,
            LocalDateTime.now().plusDays(3));
    System.out.println("Order " + o.Ticket + " opened at " + o.OpenPrice);
    System.out.println("Opened orders:");
    for (Order order : api.GetOpenedOrders()) {
      System.out.println(order.Ticket);
    }
    //        o = api.OrderClose(o.Ticket, o.Symbol, o.OpenPrice, o.Lots, o.OrderType, 0);
    //        System.out.println("Closed");
    //        System.out.println("Opened orders:");
    //        for (Order order: api.GetOpenedOrders()) {
    //            System.out.println(order.Ticket);
    //        }
  }

  void modifyOrder() throws Exception {
    MT5API api = new MT5API(8631960, "dd4444vv", "mt5demo.acetopfx.com", 443);
    System.out.println("Connecting...");
    api.Connect();
    System.out.println("Connected");
    String symbol = "EURUSD.DEMO";
    api.Subscribe(symbol);
    while (api.GetQuote(symbol) == null) Thread.sleep(1);
    Order o =
        api.OrderSend(symbol, 0.02, api.GetQuote(symbol).Ask, OrderType.Buy, 0, 0, 100, null, 0);
    System.out.println("Order " + o.Ticket + " opened at " + o.OpenPrice);
    api.OrderModify(o.Ticket, o.Symbol, o.Lots, o.OpenPrice, OrderType.Buy, 0.9, 1.2, 0, null, 0);
    System.out.println("Modified");
    for (Order order : api.GetOpenedOrders()) {
      System.out.println(order.Ticket);
    }
    o = api.OrderClose(o.Ticket, o.Symbol, api.GetQuote(symbol).Bid, o.Lots, o.OrderType, 100);
    System.out.println("Closed at " + o.ClosePrice);
  }

  private void marketOrderAsync() throws Exception {
    MT5API api = new MT5API(8631960, "dd4444vv", "mt5demo.acetopfx.com", 443);
    api.Connect();
    System.out.println("Connected. Balance = " + api.AccountBalance());
    String symbol = "EURUSD.DEMO";
    api.Subscribe(symbol);
    while (api.GetQuote(symbol) == null) Thread.sleep(1);
    System.out.println("Got quote");
    api.OnOrderProgress.addListener(
        new OnOrderProgress() {
          @Override
          public void invoke(MT5API sender, OrderProgress progress) {
            System.out.println(progress.TradeRequest.RequestId + " " + progress.TradeResult.Status);
          }
        });
    int id = api.GetRequestId();
    api.OrderSendAsync(
        id, symbol, 0.01, api.GetQuote(symbol).Ask, OrderType.Buy, 0, 0, 1000, null, 0);
  }

  private void marketOrder() throws Exception {
    // MT5API api = new MT5API(2232, "ASDF12345", "2.58.47.226", 443);
    // MT5API api = new MT5API(60550297, "4cobucmw", "78.140.180.198", 443);
    MT5API api = new MT5API(62333850, "tecimil4", "78.140.180.198", 443);
    api.Connect();
    api.PlacedType = PlacedType.Manually;
    System.out.println("Connected. Balance = " + api.AccountBalance());
    String symbol = "EURUSD";
    while (api.GetQuote(symbol) == null) Thread.sleep(1);
    System.out.println("Got first quote " + api.GetQuote(symbol));
    Order o =
        api.OrderSend(symbol, 0.1, api.GetQuote(symbol).Ask, OrderType.Buy, 0, 0, 1000, null, 0);
    System.out.println("Order " + o.Ticket + " opened at " + o.OpenPrice);
    System.out.println("Opened orders:");
    for (Order order : api.GetOpenedOrders()) {
      System.out.println(order.Ticket);
    }
    Thread.sleep(1000);
    o = api.OrderClose(o.Ticket, o.Symbol, api.GetQuote(symbol).Bid, o.Lots, o.OrderType, 1000);
    System.out.println("Closed at " + o.ClosePrice);
  }

  private void orderHistory() throws Exception {
    // MT5API api = new MT5API(99083840, "ychqluy5", "112.126.96.179", 443);
    // MT5API api = new MT5API(4000000825L, "InvPi8Ya3i3Ai", "185.97.161.224", 443);
    // MT5API api = new MT5API(26891038, "v85Tu10mXfN4FpR", "35.230.145.63", 443);
    MT5API api = new MT5API(26817585, "Readonly12", "35.230.145.63", 443);
    api.Connect();

    System.out.println("Connected. Balance = " + api.AccountBalance());
    final int[] dealsTotal = {0};
    final int[] ordersTotal = {0};

    api.OnTradeHistory.addListener(
        new OnTradeHistory() {
          @Override
          public void invoke(MT5API sender, OrderHistoryEventArgs args)
              throws IOException, TimeoutException, ConnectException {
            if (args.InternalDeals != null)
              if (args.InternalDeals.size() > 0) {
                DealInternal first = args.InternalDeals.get(0);
                DealInternal last = args.InternalDeals.get(args.InternalDeals.size() - 1);
                dealsTotal[0] += args.InternalDeals.size();
                System.out.println(
                    "Got deals "
                        + args.InternalDeals.size()
                        + " "
                        + first.OpenTimeAsDateTime()
                        + " deals total "
                        + dealsTotal[0]);
                if (args.Action == 14) {
                  System.out.println("Requesting more deals...");
                  sender.RequestDealHistory(
                      last.OpenTimeAsDateTime().getYear(),
                      last.OpenTimeAsDateTime().getMonth().getValue(),
                      args.InternalDeals);
                }
              }
            if (args.InternalOrders != null)
              if (args.InternalOrders.size() > 0) {
                OrderInternal first = args.InternalOrders.get(0);
                OrderInternal last = args.InternalOrders.get(args.InternalOrders.size() - 1);
                ordersTotal[0] += args.InternalOrders.size();
                System.out.println(
                    "Got orders "
                        + args.InternalOrders.size()
                        + " "
                        + first.OpenTimeAsDateTime()
                        + " "
                        + last.OpenTimeAsDateTime()
                        + " orders total "
                        + ordersTotal[0]);
                if (args.Action == 14) {
                  System.out.println("Requesting more orders...");
                  sender.RequestOrderHistory(
                      last.OpenTimeAsDateTime().getYear(),
                      last.OpenTimeAsDateTime().getMonth().getValue(),
                      args.InternalOrders);
                }
              }
          }
        });

    for (int i = 6; i < 12; i++) {
      api.RequestDealHistory(2021, i);
      Thread.sleep(1000);
    }
    for (int i = 1; i < 7; i++) {
      api.RequestDealHistory(2022, i);
      Thread.sleep(1000);
    }

    for (int i = 6; i < 12; i++) {
      api.RequestOrderHistory(2021, i);
      Thread.sleep(1000);
    }
    for (int i = 1; i < 7; i++) {
      api.RequestOrderHistory(2022, i);
      Thread.sleep(1000);
    }
  }

  private void orderHistory2() throws Exception {
    // MT5API api = new MT5API(26891038, "v85Tu10mXfN4FpR", "35.230.145.63", 443);
    // MT5API api = new MT5API(1009023, "otso123", "20.48.6.157", 443);
    MT5API api = new MT5API(101240050, "Ava112358", "3.10.134.148", 443);

    api.Connect();
    System.out.println("Connected to " + api.Account.Login + ". Balance = " + api.AccountBalance());
    SymGroup gr = api.Symbols.GetGroup("GOLD");
    System.out.println("MinVolume " + (double) gr.MinVolume / 100000000);
    System.out.println("MaxVolume " + (double) gr.MaxVolume / 100000000);
    System.out.println("VolumeStep " + (double) gr.VolumeStep / 100000000);

    LocalDateTime now = LocalDateTime.now();

    api.OnTradeHistory.addListener(
        (sender, args) -> {
          System.out.println("Action = " + args.Action);
          System.out.println("orders = " + args.Orders.size());
          System.out.println(
              "internal_deals = "
                  + (args.InternalDeals == null ? "null" : args.InternalDeals.size()));
          System.out.println(
              "internal_orders = "
                  + (args.InternalOrders == null ? "null" : args.InternalOrders.size()));
          System.out.println("end " + LocalDateTime.now());

          if (args.Action == 14) {
            if (args.InternalDeals != null) {
              DealInternal last = args.InternalDeals.get(args.InternalDeals.size() - 1);
              LocalDateTime start =
                  LocalDateTime.of(
                      last.OpenTimeAsDateTime().getYear(),
                      last.OpenTimeAsDateTime().getMonth().getValue(),
                      1,
                      0,
                      0);
              System.out.println("Requesting more deals for " + start.getMonth());
              sender.RequestDealHistory(
                  start.getYear(), start.getMonth().getValue(), args.InternalDeals);
              System.out.println("Requesting more deals " + start.plusMonths(1) + " to " + now);
              sender.RequestDealHistory(start.plusMonths(1), LocalDateTime.now());
            } else if (args.InternalOrders != null) {
              OrderInternal last = args.InternalOrders.get(args.InternalOrders.size() - 1);
              LocalDateTime start =
                  LocalDateTime.of(
                      last.OpenTimeAsDateTime().getYear(),
                      last.OpenTimeAsDateTime().getMonth().getValue(),
                      1,
                      0,
                      0);
              System.out.println(
                  "Requesting more orders for " + last.OpenTimeAsDateTime().getMonth());
              sender.RequestOrderHistory(
                  start.getYear(), start.getMonth().getValue(), args.InternalOrders);
              System.out.println("Requesting more orders " + start.plusMonths(1) + " to " + now);
              sender.RequestOrderHistory(start.plusMonths(1), LocalDateTime.now());
            }
          }
        });
    System.out.println("start " + LocalDateTime.now());
    // api.RequestOrderHistory(LocalDateTime.of(2005, 1, 1, 0, 0, 0), now);
    api.RequestDealHistory(LocalDateTime.of(2005, 1, 1, 0, 0, 0), now);
    // api.RequestDealHistory(now, now);
  }

  private void orderHistory3() throws Exception {
    MT5API api = new MT5API(26891038, "v85Tu10mXfN4FpR", "35.230.145.63", 443);
    api.Connect();

    System.out.println("Connected to " + api.Account.Login + ". Balance = " + api.AccountBalance());
    LocalDateTime now = LocalDateTime.now();
    final boolean[] DealHistoryRequestedByMonth = {false};
    final boolean[] OrderHistoryRequestedByMonth = {false};

    api.OnTradeHistory.addListener(
        (sender, args) -> {
          System.out.println("Action = " + args.Action);
          System.out.println("orders = " + args.Orders.size());
          System.out.println(
              "internal_deals = "
                  + (args.InternalDeals == null ? "null" : args.InternalDeals.size()));
          System.out.println(
              "internal_orders = "
                  + (args.InternalOrders == null ? "null" : args.InternalOrders.size()));
          System.out.println("end " + LocalDateTime.now());

          if (args.Action == 14) {
            if (args.InternalDeals != null) {
              DealInternal last = args.InternalDeals.get(args.InternalDeals.size() - 1);
              LocalDateTime start =
                  LocalDateTime.of(
                      last.OpenTimeAsDateTime().getYear(),
                      last.OpenTimeAsDateTime().getMonth().getValue(),
                      1,
                      0,
                      0);
              System.out.println("Requesting more deals for " + start.getMonth());
              sender.RequestDealHistory(
                  start.getYear(), start.getMonth().getValue(), args.InternalDeals);
              if (!DealHistoryRequestedByMonth[0]) {
                DealHistoryRequestedByMonth[0] = true;
                System.out.println(
                    "Requesting more deals monthly " + last.OpenTimeAsDateTime() + " to " + now);
                while (start.isBefore(LocalDateTime.now())) {
                  start = start.plusMonths(1);
                  sender.RequestDealHistory(start.getYear(), start.getMonth().getValue());
                  try {
                    Thread.sleep(1000);
                  } catch (InterruptedException e) {
                  }
                }
              }

            } else if (args.InternalOrders != null) {
              OrderInternal last = args.InternalOrders.get(args.InternalOrders.size() - 1);
              LocalDateTime start =
                  LocalDateTime.of(
                      last.OpenTimeAsDateTime().getYear(),
                      last.OpenTimeAsDateTime().getMonth().getValue(),
                      1,
                      0,
                      0);
              System.out.println(
                  "Requesting more orders for " + last.OpenTimeAsDateTime().getMonth());
              sender.RequestOrderHistory(
                  start.getYear(), start.getMonth().getValue(), args.InternalOrders);
              if (!OrderHistoryRequestedByMonth[0]) {
                OrderHistoryRequestedByMonth[0] = true;
                System.out.println(
                    "Requesting more orders monthly " + last.OpenTimeAsDateTime() + " to " + now);
                while (start.isBefore(LocalDateTime.now())) {
                  start = start.plusMonths(1);
                  sender.RequestOrderHistory(start.getYear(), start.getMonth().getValue());
                  try {
                    Thread.sleep(1000);
                  } catch (InterruptedException e) {
                  }
                }
              }
            }
          }
        });
    System.out.println("start " + LocalDateTime.now());
    api.RequestOrderHistory(LocalDateTime.of(2005, 1, 1, 0, 0, 0), now);
    // api.RequestDealHistory(LocalDateTime.of(2005, 1, 1, 0, 0, 0), now);
  }

  int j = 1;

  private void orderHistory4() throws Exception {
    MT5API api = new MT5API(101240050, "Ava112358", "3.10.134.148", 443);
    api.Connect();
    System.out.println("Connected to " + api.Account.Login + ". Balance = " + api.AccountBalance());
    api.OnTradeHistory.addListener(
        (sender, args) -> {
          System.out.println("end " + LocalDateTime.now() + " " + j++ + " " + args.Orders.size());
        });
    System.out.println("start " + LocalDateTime.now());
    for (int i = 0; i < 50; i++) {
      api.RequestOrderHistory(LocalDateTime.of(2023, 1, 1, 0, 0, 0), LocalDateTime.now());
      api.RequestDealHistory(LocalDateTime.of(2023, 1, 1, 0, 0, 0), LocalDateTime.now());
      Thread.sleep(100);
    }
  }

  private void orderHistory5() throws Exception {
    MT5API api = new MT5API(101240050, "Ava112358", "3.10.134.148", 443);
    api.Connect();
    System.out.println("Connected to " + api.Account.Login + ". Balance = " + api.AccountBalance());
    api.OnTradeHistory.addListener(
        (sender, args) -> {
          System.out.println("end " + LocalDateTime.now() + " " + args.Orders.size());
        });
    System.out.println("start " + LocalDateTime.now());
    // api.RequestOrderHistory(LocalDateTime.of(2023, 1, 1, 0, 0, 0), LocalDateTime.now());
    api.RequestDealHistory(LocalDateTime.of(2023, 1, 1, 0, 0, 0), LocalDateTime.now());
  }

  public void quotes() throws Exception {
    // MT5API api = new MT5API(99083840, "ychqluy5", "112.126.96.179", 443);
    // MT5API api = new MT5API(9956, "TYTY1212", "2.58.47.214", 443);
    // MT5API api = new MT5API(45045, "msms1616", "95.216.98.166", 443);
    // MT5API api = new MT5API(45035, "trade45035", "95.216.98.166", 443);
    // MT5API api = new MT5API(56655349, "zcqj1aqp", "78.140.180.198", 443);
    // MT5API api = new MT5API(50765, "A123456789", "211.72.241.36", 443);
    // MT5API api = new MT5API(667028, "jf49fikz", "13.112.1.146", 1953);
    // MT5API api = new MT5API(2000025, "bruce123", "89.187.118.51", 1951);
    // MT5API api = new MT5API(7556796, "Aa2408550", "82.163.250.77", 443);
    // MT5API api = new MT5API(107015354, "Colen@123", "18.143.116.66", 443);
    // MT5API api = new MT5API(75071507, "-2QxOkPt", "27.111.161.145", 1950);
    MT5API api = new MT5API(62333850, "tecimil4", "78.140.180.198", 443);
    // MT5API api = new MT5API(21455, "1nJeS+Ae", "95.217.147.61", 443);
    // MT5API api = new MT5API(2100051275 , "6qZpNgH@", "mt5demo.fxdd.com", 443);
    api.Connect();
    String n = api.Account.UserName;

    System.out.println("Connected. Balance = " + api.AccountBalance());
    api.OnQuote.addListener(
        new OnQuote() {
          @Override
          public void invoke(MT5API sender, Quote quote) {
            System.out.println(
                quote
                    + " "
                    + sender.AccountProfit()
                    + " "
                    + sender.GetOpenedOrders().length
                    + " "
                    + quote.Spread);
          }
        });
    api.Subscribe("EURUSD");
  }

  public void pingPong() throws Exception {
    String symbol = "EURUSD";
    MT5API api = new MT5API(2100051275, "6qZpNgH@", "mt5demo.fxdd.com", 443);
    api.Connect();
    if (api.PingPong()) System.out.println("PingPing success");
  }

  class OnMsg implements OnMsgHandler {
    @Override
    public void invoke(Object sender, String msg, MsgType type) {
      if (type == MsgType.Trace) return;
      String m =
          new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())
              + " "
              + type.toString().substring(0, 3)
              + ": "
              + sender.getClass().getSimpleName();
      m += " " + msg;
      System.out.println(m);
    }
  }

  public static List<Bar> ConvertToTimeframe(List<Bar> bars, int minutes) {
    if (minutes > 60)
      if (minutes % 60 != 0)
        throw new RuntimeException("If timeframe > 60 it should be in whole hours");
    if (bars.size() == 0) return new LinkedList<Bar>();
    int i = 0;
    if (minutes <= 60) while (bars.get(i).Time.getMinute() % minutes > 0) i++;
    List<Bar> res = new LinkedList<Bar>();
    Bar bar = new Bar();
    bar.OpenPrice = bars.get(0).OpenPrice;
    bar.Time = bars.get(0).Time;
    bar.LowPrice = bars.get(0).LowPrice;
    LocalDateTime time = bars.get(0).Time.plusMinutes(minutes);
    if (minutes == 1440)
      time = LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 0, 0, 0);
    for (; i < bars.size(); i++) {
      if (bars.get(i).Time.isAfter(time)
          || bars.get(i).Time.isEqual(time)
          || i == bars.size() - 1) {
        bar.ClosePrice = bars.get(i - 1).ClosePrice;
        res.add(bar);
        bar = new Bar();
        bar.OpenPrice = bars.get(i).OpenPrice;
        bar.Time = bars.get(i).Time;
        bar.LowPrice = bars.get(i).LowPrice;
        while (time.isBefore(bar.Time) || time.isEqual(bar.Time)) time = time.plusMinutes(minutes);
        if (minutes == 1440) {
          LocalDateTime nextday = bars.get(i).Time.plusHours(24);
          time =
              LocalDateTime.of(
                  nextday.getYear(), nextday.getMonth(), nextday.getDayOfMonth(), 0, 0, 0);
        }
      }
      if (bars.get(i).HighPrice > bar.HighPrice) bar.HighPrice = bars.get(i).HighPrice;
      if (bars.get(i).LowPrice < bar.LowPrice) bar.LowPrice = bars.get(i).LowPrice;
    }
    return res;
  }
}
