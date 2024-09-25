package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MT5API {

  public ArrayList<String> LoginIdExServerUrls =
      new ArrayList<String>(Arrays.asList("http://loginid-mt5.mtapi.io"));

  /**
   * Cluster general information.
   *
   * @return
   */
  public ServerRec ClusterSummary() {
    return ServerDetails.getKey();
  }

  /** Local time of last message recieved from server */
  public LocalDateTime TimeOfLastMessageFromServer;

  /** Cluster members */
  public ArrayList<Map.Entry<AccessInfo, AddressRec[]>> ClusterMembers() {
    return ServerDetails.getValue();
  }

  boolean TimeoutDuringConnect;
  String Guid = "1288942f-aadb-4d98-8cc1-c06f33730d76";
  public int LoginIdWebServerTimeout = 10000;

  /** Manually or EA */
  public PlacedType PlacedType =
      io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.PlacedType.Manually;

  private static HashMap<String, MT5API> Clients = new HashMap<String, MT5API>();

  public ProcessEvents ProcessEventsIn = ProcessEvents.ThreadPool;

  Queue<OnOrderProgress> ProgressWaiters = new ConcurrentLinkedQueue<OnOrderProgress>();

  Queue<OnOrderUpdate> UpdateWaiters = new ConcurrentLinkedQueue<OnOrderUpdate>();

  /** Account currency */
  public final String AccountCurrency() {
    return Symbols.Base.Currency;
  }

  /** Company name */
  public final String AccountCompanyName() {
    return Symbols.Base.CompanyName;
  }

  /** Netting or hedging */
  public final AccMethod AccountMethod() {
    return Symbols.Base.AccMethod;
  }

  /** Force update order profits */
  public final void UpdateProfits() throws IOException {
    for (Order item : GetOpenedOrders()) {
      if (item.OrderType == OrderType.Buy || item.OrderType == OrderType.Sell) {
        while (GetQuote(item.Symbol) == null) {
          try {
            Thread.sleep(1);
          } catch (InterruptedException e) {
          }
        }
        Quote quote = GetQuote(item.Symbol);
        OrderProfit.Update(item, quote.Bid, quote.Ask);
      }
    }
  }

  /** Account used margin */
  public final double AccountMargin() throws IOException {
    HashMap<String, SymbolMargin> syms = new HashMap<String, SymbolMargin>();
    for (Order order : GetOpenedOrders()) {
      if (!syms.containsKey(order.Symbol)) {
        syms.put(order.Symbol, new SymbolMargin(this, order.Symbol));
      }
      if (order.DealInternalIn != null) {
        syms.get(order.Symbol).AcceptDeal(order.DealInternalIn);
      } else if (order.OrderInternal != null) {
        syms.get(order.Symbol).AcceptOrder(order.OrderInternal, false);
      }
    }
    double sum = 0;
    for (SymbolMargin item : syms.values()) {
      sum += item.GetTradeMargin();
    }
    return sum;
  }

  /** Account free margin */
  public final double AccountFreeMargin() throws IOException {
    return AccountEquity() - AccountMargin();
  }

  /** Maximum ms to wait for execution */
  public int ExecutionTimeout = 30000;

  private static int RequestId = 1;
  private static final Object RequestIdLock = new Object();

  public AccountRec Account;

  /**
   * Get uniq request ID for async trading
   *
   * @return
   */
  public final int GetRequestId() {
    synchronized (RequestIdLock) {
      return RequestId++;
    }
  }

  /**
   * Send order and don't wait execution. Use OnOrderProgress event to get result.
   *
   * @param requestId Uniq temporary ID that can be used before ticket would be assigned. You can
   *     use GetRequestID()
   * @param symbol Symbol
   * @param lots Lots
   * @param price Price
   * @param type Order type
   * @param sl Stop Loss
   * @param tp Take Profit
   * @param deviation Max deviation from specified price also known as Slppage
   * @param comment String comment
   * @param expertID Also known as magic number
   * @param fillPolicy Fill policy depends on symbol settings on broker
   */
  public final void OrderSendAsync(
      int requestId,
      String symbol,
      double lots,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID,
      FillPolicy fillPolicy)
      throws IOException {
    OrderSendAsync(
        requestId, symbol, lots, price, type, sl, tp, deviation, comment, expertID, fillPolicy, 0);
  }

  public final void OrderSendAsync(
      int requestId,
      String symbol,
      double lots,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID)
      throws IOException {
    OrderSendAsync(
        requestId,
        symbol,
        lots,
        price,
        type,
        sl,
        tp,
        deviation,
        comment,
        expertID,
        FillPolicy.FillOrKill,
        0);
  }

  public final void OrderSendAsync(
      int requestId,
      String symbol,
      double lots,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment)
      throws IOException {
    OrderSendAsync(
        requestId,
        symbol,
        lots,
        price,
        type,
        sl,
        tp,
        deviation,
        comment,
        0,
        FillPolicy.FillOrKill,
        0);
  }

  public final void OrderSendAsync(
      int requestId,
      String symbol,
      double lots,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation)
      throws IOException {
    OrderSendAsync(
        requestId, symbol, lots, price, type, sl, tp, deviation, null, 0, FillPolicy.FillOrKill, 0);
  }

  public final void OrderSendAsync(
      int requestId, String symbol, double lots, double price, OrderType type, double sl, double tp)
      throws IOException {
    OrderSendAsync(
        requestId, symbol, lots, price, type, sl, tp, 0, null, 0, FillPolicy.FillOrKill, 0);
  }

  public final void OrderSendAsync(
      int requestId, String symbol, double lots, double price, OrderType type, double sl)
      throws IOException {
    OrderSendAsync(
        requestId, symbol, lots, price, type, sl, 0, 0, null, 0, FillPolicy.FillOrKill, 0);
  }

  public final void OrderSendAsync(
      int requestId, String symbol, double lots, double price, OrderType type) throws IOException {
    OrderSendAsync(
        requestId, symbol, lots, price, type, 0, 0, 0, null, 0, FillPolicy.FillOrKill, 0);
  }

  public final void OrderSendAsync(
      int requestId,
      String symbol,
      double lots,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID,
      FillPolicy fillPolicy,
      double stoplimit)
      throws IOException {
    OrderSendAsync(
        requestId,
        symbol,
        lots,
        price,
        type,
        sl,
        tp,
        deviation,
        comment,
        expertID,
        fillPolicy,
        stoplimit,
        null);
  }

  public final void OrderSendAsync(
      int requestId,
      String symbol,
      double lots,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID,
      FillPolicy fillPolicy,
      double stoplimit,
      LocalDateTime expiration)
      throws IOException {
    TradeRequest req = new TradeRequest();
    req.Flags &= ~0x100;
    req.Flags &= ~0x200;
    req.PlacedType = PlacedType;
    req.Login = User;
    req.Digits = Symbols.GetInfo(symbol).Digits;

    req.OrderPrice = stoplimit;
    req.Price = price;
    req.Lots = BigDecimal.valueOf(lots).multiply(new BigDecimal(100000000)).longValue();
    req.Currency = symbol;
    req.RequestId = requestId;
    if (type == OrderType.Buy || type == OrderType.Sell)
      req.TradeType = TradeType.forValue(Symbols.GetGroup(symbol).TradeType.getValue() + 1);
    else req.TradeType = TradeType.SetOrder;
    req.OrderType = type;
    req.StopLoss = sl;
    req.TakeProfit = tp;
    req.Deviation = deviation;
    req.Comment = comment;
    req.ExpertId = expertID;
    req.FillPolicy = fillPolicy;
    if (expiration != null) {
      req.ExpirationType = ExpirationType.Specified;
      req.ExpirationTime = ConvertTo.Long(expiration);
    }
    (new OrderSender(Connection)).Send(req);
  }

  /**
   * Send order and don't wait execution. Use OnOrderProgress event to get result.
   *
   * @param symbol Symbol
   * @param lots Lots
   * @param price Price
   * @param type Order type
   * @param sl Stop Loss
   * @param tp Take Profit
   * @param deviation Max deviation from specified price also known as Slppage
   * @param comment String comment
   * @param expertID Also known as magic number
   * @param fillPolicy Fill policy depends on symbol settings on broker
   */
  public final Order OrderSend(
      String symbol,
      double lots,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID,
      FillPolicy fillPolicy)
      throws IOException {
    return OrderSend(
        symbol, lots, price, type, sl, tp, deviation, comment, expertID, fillPolicy, 0);
  }

  public final Order OrderSend(
      String symbol,
      double lots,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID,
      FillPolicy fillPolicy,
      double stoplimit)
      throws IOException {
    return OrderSend(
        symbol,
        lots,
        price,
        type,
        sl,
        tp,
        deviation,
        comment,
        expertID,
        fillPolicy,
        stoplimit,
        null);
  }

  public final Order OrderSend(
      String symbol,
      double lots,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID)
      throws IOException {
    return OrderSend(
        symbol, lots, price, type, sl, tp, deviation, comment, expertID, FillPolicy.FillOrKill, 0);
  }

  public final Order OrderSend(
      String symbol,
      double lots,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment)
      throws IOException {
    return OrderSend(
        symbol, lots, price, type, sl, tp, deviation, comment, 0, FillPolicy.FillOrKill, 0);
  }

  public final Order OrderSend(
      String symbol,
      double lots,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation)
      throws IOException {
    return OrderSend(
        symbol, lots, price, type, sl, tp, deviation, null, 0, FillPolicy.FillOrKill, 0);
  }

  public final Order OrderSend(
      String symbol, double lots, double price, OrderType type, double sl, double tp)
      throws IOException {
    return OrderSend(symbol, lots, price, type, sl, tp, 0, null, 0, FillPolicy.FillOrKill, 0);
  }

  public final Order OrderSend(String symbol, double lots, double price, OrderType type, double sl)
      throws IOException {
    return OrderSend(symbol, lots, price, type, sl, 0, 0, null, 0, FillPolicy.FillOrKill, 0);
  }

  public final Order OrderSend(String symbol, double lots, double price, OrderType type)
      throws IOException {
    return OrderSend(symbol, lots, price, type, 0, 0, 0, null, 0, FillPolicy.FillOrKill, 0);
  }

  public final Order OrderSend(
      String symbol,
      double lots,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID,
      FillPolicy fillPolicy,
      double stoplimit,
      LocalDateTime expiration)
      throws IOException {
    int id = GetRequestId();
    Order order;
    if (type == OrderType.Buy || type == OrderType.Sell) {
      MarketOpenWaiter waiter = new MarketOpenWaiter(this, id, ExecutionTimeout);
      OrderSendAsync(
          id,
          symbol,
          lots,
          price,
          type,
          sl,
          tp,
          deviation,
          comment,
          expertID,
          fillPolicy,
          stoplimit,
          expiration);
      order = waiter.Wait();
    } else {
      PendingOpenWaiter waiter = new PendingOpenWaiter(this, id, ExecutionTimeout);
      OrderSendAsync(
          id,
          symbol,
          lots,
          price,
          type,
          sl,
          tp,
          deviation,
          comment,
          expertID,
          fillPolicy,
          stoplimit,
          expiration);
      order = waiter.Wait();
    }
    if (AccountMethod() == AccMethod.Hedging) {
      Order value = Orders.Opened.get(order.Ticket);
      if (value != null) value.Update(order);
      else Orders.Opened.put(order.Ticket, order);
    }
    return order;
  }

  /**
   * Send order close request and don't wait execution. Use OnOrderProgress event to get result.
   *
   * @param requestId Uniq temporary ID that can be used before ticket would be assigned. You can
   *     use GetID()
   * @param ticket Order ticket
   * @param lots Symbol
   * @param lots Volume
   * @param price Price
   * @param type Order type
   * @param deviation Max deviation from specified price also known as Slppage
   */
  public final void OrderCloseAsync(
      int requestId,
      long ticket,
      String symbol,
      double price,
      double lots,
      OrderType type,
      long deviation)
      throws IOException {
    OrderCloseAsync(requestId, ticket, symbol, price, lots, type, deviation, FillPolicy.FillOrKill);
  }

  public final void OrderCloseAsync(
      int requestId, long ticket, String symbol, double price, double lots, OrderType type)
      throws IOException {
    OrderCloseAsync(requestId, ticket, symbol, price, lots, type, 0, FillPolicy.FillOrKill);
  }

  // C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are
  // created above:
  // ORIGINAL LINE: public void OrderCloseAsync(int requestId, long ticket, string symbol, double
  // price, double lots, OrderType type, ulong deviation = 0, FillPolicy fillPolicy =
  // FillPolicy.FillOrKill)
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  public final void OrderCloseAsync(
      int requestId,
      long ticket,
      String symbol,
      double price,
      double lots,
      OrderType type,
      long deviation,
      FillPolicy fillPolicy)
      throws IOException {

    TradeRequest req = new TradeRequest();
    req.Flags &= ~0x100;
    req.Flags &= ~0x200;
    req.PlacedType = PlacedType;
    req.Login = User;
    req.Digits = Symbols.GetInfo(symbol).Digits;
    req.RequestId = requestId;
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: req.Lots = (ulong)(lots * 100000000);
    req.Lots = BigDecimal.valueOf(lots).multiply(new BigDecimal(100000000)).longValue();
    if (req.Lots == 0) {
      throw new RuntimeException("Request Lots = 0 ");
    }
    req.Currency = symbol;
    if (type == OrderType.Buy || type == OrderType.Sell) {
      req.Price = price;
      req.DealTicket = ticket;
      req.FillPolicy = fillPolicy;
      req.TradeType = TradeType.forValue(Symbols.GetGroup(symbol).TradeType.getValue() + 1);
      if (type == OrderType.Buy) {
        req.OrderType = OrderType.Sell;
      } else if (type == OrderType.Sell) {
        req.OrderType = OrderType.Buy;
      }
      req.Deviation = deviation;
      // req.Flags = 2;
      // MarketCloseRequests.Add(ticket, requestId);
    } else {
      req.OrderTicket = ticket;
      req.TradeType = TradeType.CancelOrder;
      req.OrderType = type;
      // PendingCloseRequests.Add(ticket, requestId);
    }
    if (req.FillPolicy == null) req.FillPolicy = FillPolicy.FillOrKill;
    (new OrderSender(Connection)).Send(req);
  }

  /**
   * Send order close request and wait for execution.
   *
   * @param ticket Order ticket
   * @param symbol Symbol
   * @param lots Volume
   * @param price Price
   * @param type Order type
   * @param deviation Max deviation from specified price also known as Slppage
   * @return Closed order
   */
  public final Order OrderClose(
      long ticket, String symbol, double price, double lots, OrderType type, long deviation)
      throws IOException {
    return OrderClose(ticket, symbol, price, lots, type, deviation, FillPolicy.FillOrKill);
  }

  public final Order OrderClose(
      long ticket, String symbol, double price, double lots, OrderType type) throws IOException {
    return OrderClose(ticket, symbol, price, lots, type, 0, FillPolicy.FillOrKill);
  }

  // C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are
  // created above:
  // ORIGINAL LINE: public Order OrderClose(long ticket, string symbol, double price, double lots,
  // OrderType type, ulong deviation = 0, FillPolicy fillPolicy = FillPolicy.FillOrKill)
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  public final Order OrderClose(
      long ticket,
      String symbol,
      double price,
      double lots,
      OrderType type,
      long deviation,
      FillPolicy fillPolicy)
      throws IOException {
    Order order;
    if (type == OrderType.Buy || type == OrderType.Sell) {
      int id = GetRequestId();
      MarketCloseWaiter waiter = new MarketCloseWaiter(this, id, ExecutionTimeout, ticket);
      OrderCloseAsync(id, ticket, symbol, price, lots, type, deviation, fillPolicy);
      order = waiter.Wait();
    } else {
      int id = GetRequestId();
      PendingCloseWaiter waiter = new PendingCloseWaiter(this, id, ExecutionTimeout, ticket);
      OrderCloseAsync(id, ticket, symbol, price, lots, type, deviation, fillPolicy);
      order = waiter.Wait();
    }
    return order;
  }

  public final void OrderModify(
      long ticket,
      String symbol,
      double volume,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID,
      FillPolicy fillPolicy)
      throws IOException {
    OrderModify(
        ticket, symbol, volume, price, type, sl, tp, deviation, comment, expertID, fillPolicy, 0);
  }

  public final void OrderModify(
      long ticket,
      String symbol,
      double volume,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID)
      throws IOException {
    OrderModify(
        ticket,
        symbol,
        volume,
        price,
        type,
        sl,
        tp,
        deviation,
        comment,
        expertID,
        FillPolicy.FillOrKill,
        0);
  }

  public final void OrderModify(
      long ticket,
      String symbol,
      double volume,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment)
      throws IOException {
    OrderModify(
        ticket,
        symbol,
        volume,
        price,
        type,
        sl,
        tp,
        deviation,
        comment,
        0,
        FillPolicy.FillOrKill,
        0);
  }

  public final void OrderModify(
      long ticket,
      String symbol,
      double volume,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation)
      throws IOException {
    OrderModify(
        ticket, symbol, volume, price, type, sl, tp, deviation, "", 0, FillPolicy.FillOrKill, 0);
  }

  public final void OrderModify(
      long ticket, String symbol, double volume, double price, OrderType type, double sl, double tp)
      throws IOException {
    OrderModify(ticket, symbol, volume, price, type, sl, tp, 0, "", 0, FillPolicy.FillOrKill, 0);
  }

  // C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are
  // created above:
  // ORIGINAL LINE: public void OrderModify(long ticket, string symbol, double volume, double price,
  // OrderType type, double sl, double tp, ulong deviation = 0, string comment = "", long expertID =
  // 0, FillPolicy fillPolicy = FillPolicy.FillOrKill, double stoplimit = 0)
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  public final void OrderModify(
      long ticket,
      String symbol,
      double volume,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID,
      FillPolicy fillPolicy,
      double stoplimit)
      throws IOException {
    int id = GetRequestId();
    ModifyWaiter waiter = new ModifyWaiter(this, id, ExecutionTimeout, ticket);
    OrderModifyAsync(
        id,
        ticket,
        symbol,
        volume,
        price,
        type,
        sl,
        tp,
        deviation,
        comment,
        expertID,
        fillPolicy,
        stoplimit);
    waiter.Wait();
  }

  public final void OrderModifyAsync(
      int requestId,
      long ticket,
      String symbol,
      double volume,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID,
      FillPolicy fillPolicy)
      throws IOException {
    OrderModifyAsync(
        requestId,
        ticket,
        symbol,
        volume,
        price,
        type,
        sl,
        tp,
        deviation,
        comment,
        expertID,
        fillPolicy,
        0);
  }

  public final void OrderModifyAsync(
      int requestId,
      long ticket,
      String symbol,
      double volume,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID)
      throws IOException {
    OrderModifyAsync(
        requestId,
        ticket,
        symbol,
        volume,
        price,
        type,
        sl,
        tp,
        deviation,
        comment,
        expertID,
        FillPolicy.FillOrKill,
        0);
  }

  public final void OrderModifyAsync(
      int requestId,
      long ticket,
      String symbol,
      double volume,
      double price,
      OrderType type,
      double sl,
      double tp,
      long deviation,
      String comment,
      long expertID,
      FillPolicy fillPolicy,
      double stoplimit)
      throws IOException {
    TradeRequest req = new TradeRequest();
    req.PlacedType = PlacedType;
    req.Lots = BigDecimal.valueOf(volume).multiply(new BigDecimal(100000000)).longValue();
    req.Currency = symbol;
    req.RequestId = requestId;
    req.OrderPrice = stoplimit;
    req.Price = price;
    if (type == OrderType.Buy || type == OrderType.Sell) {
      req.DealTicket = ticket;
      req.TradeType = TradeType.ModifyDeal;
    } else {
      req.OrderTicket = ticket;
      req.TradeType = TradeType.ModifyOrder;
    }
    req.OrderType = type;
    req.StopLoss = sl;
    req.TakeProfit = tp;
    req.Deviation = deviation;
    req.Comment = comment;
    req.ExpertId = expertID;
    req.FillPolicy = fillPolicy;
    (new OrderSender(Connection)).Send(req);
  }

  /** Symbols information */
  public final Symbols Symbols = new Symbols();

  //// <summary>
  /** New quote */
  public Event<OnQuote> OnQuote = new Event<OnQuote>();

  //// <summary>
  /** Order history */
  public Event<OnTradeHistory> OnTradeHistory = new Event<OnTradeHistory>();

  //// <summary>
  /** Order update notification from server */
  public Event<OnOrderUpdate> OnOrderUpdate = new Event<OnOrderUpdate>();

  //// <summary>
  /** Connect progress notification */
  public Event<OnConnectProgress> OnConnectProgress = new Event<OnConnectProgress>();

  /** Open/close progress of the order before ticket number assign. */
  public Event<OnOrderProgress> OnOrderProgress = new Event<OnOrderProgress>();

  /// <summary>
  /// Quote history event. Use RequestQuoteHistory to request history.
  /// </summary>
  public Event<OnQuoteHistory> OnQuoteHistory = new Event<>();

  /** Server time, refreshing goes with using incoming quotes. */
  public java.time.LocalDateTime ServerTime() {
    return LocalDateTime.now(ZoneOffset.UTC)
        .plusMinutes(ServerDetails.getKey().TimeZone)
        .plusHours(ServerDetails.getKey().DST);
  }

  /**
   * Server time offset from UTC in minutes
   *
   * @return
   */
  public int ServrTimeZoneInMinutes() {
    return ServerDetails.getKey().TimeZone + 60 * ServerDetails.getKey().DST;
  }

  /**
   * Is imvestor mode
   *
   * @return
   */
  public boolean IsInvestor() {
    int InvestorFlag = 8;
    if ((Account.TradeFlags & InvestorFlag) != 0) return true;
    return false;
  }

  // public Workaround Workaround;
  public Map.Entry<ServerRec, ArrayList<Map.Entry<AccessInfo, AddressRec[]>>> ServerDetails;

  /** Account profit */
  public final double AccountProfit() {
    double sum = 0;
    for (Order item : GetOpenedOrders()) {
      sum += item.Profit + item.Commission + item.Swap;
    }
    return Math.round((sum) * Math.pow(10, 2)) / Math.pow(10, 2);
  }

  /** Account equity. */
  public final double AccountEquity() {
    return Math.round((Account.Balance + AccountProfit()) * Math.pow(10, 2)) / Math.pow(10, 2);
  }

  /** Account balance. */
  public final double AccountBalance() {
    if (Account == null) throw new RuntimeException("Not connected");
    return Account.Balance;
  }

  /** Check connection state. */
  public final boolean Connected() {
    if (CmdHandler == null) return false;
    if (GotAccountInfo == false) return false;
    if (CmdHandler.Stop) return false;
    if (Connection == null) return false;
    if (Connection.Sock == null) return false;
    if (!Connection.Sock.isConnected()) return false;
    // if (Duration.between(CmdHandler.LastMessageFromServer, LocalDateTime.now()).toMillis() >
    // LastMessageFromServerTimeoutMs)
    //	return false;
    return CmdHandler.getRunning();
  }

  public boolean GotAccountInfo = false;

  // C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
  // #if LoginDLL
  // #else

  // #endif

  private Object ConnectLock = new Object();
  public Logger Log;
  public java.time.LocalDateTime Time = java.time.LocalDateTime.MIN;

  CmdHandler CmdHandler;
  Connection Connection;
  Subscriber Subscriber;
  OpnedClosedOrders Orders;
  OrderHistory OrderHistory;
  OrderProfit OrderProfit;
  Connector Connector;

  /** User */
  public long User;

  /** Password */
  public String Password;

  /** Host */
  public String Host;

  /** Account currency */
  public int Port;

  /** Certificate *.pfx file */
  public byte[] PfxFile;

  /** Pfx file password */
  public String PfxFilePassword;

  public int _SleepTime;

  public static Server[] LoadServersDat(String path) throws IOException {
    return (new ServersDatLoader()).Load(path);
  }

  String guid = "1288942f-aadb-4d98-8cc1-c06f33730d76";

  public MT5API(long user, String password, String host, int port) {
    Trial.check(guid);
    Log = new Logger(this);
    User = user;
    Password = password;
    Host = host;
    Port = port;
    Connection = new Connection(this);
    Subscriber = new Subscriber(this, Log);
    Orders = new OpnedClosedOrders(this, Log);
    OrderHistory = new OrderHistory(this, Log);
    OrderProfit = new OrderProfit(this);
    Connector = new Connector(this);
  }

  public MT5API(
      long user, String password, String host, int port, byte[] pfxFile, String pfxFilePassword) {
    Trial.check(guid);
    Log = new Logger(this);
    User = user;
    Password = password;
    Host = host;
    Port = port;
    PfxFile = pfxFile;
    PfxFilePassword = pfxFilePassword;
    Connection = new Connection(this);
    Subscriber = new Subscriber(this, Log);
    Orders = new OpnedClosedOrders(this, Log);
    OrderHistory = new OrderHistory(this, Log);
    OrderProfit = new OrderProfit(this);
    Connector = new Connector(this);
  }

  /**
   * Contruct size
   *
   * @param symbol Symbol
   * @return
   */
  public final double GetContaractSize(String symbol) {
    return Symbols.GetInfo(symbol).ContractSize;
  }

  /**
   * Request closed orders.
   *
   * @param from Start time of history.
   * @param to End time of history.
   * @return Array of orders.
   */
  public final void RequestDealHistory(java.time.LocalDateTime from, java.time.LocalDateTime to)
      throws IOException {
    Log.trace("RequestOrderHistory");
    if (!Connected()) {
      throw new RuntimeException("Not connected");
    }
    OrderHistory.Request(from, to, 0, 0, true);
  }

  /**
   * Reuest closed orders.
   *
   * @param year Required year.
   * @param month Required month.
   * @param exist Existing deals.
   * @return Array of orders.
   */
  public void RequestDealHistory(int year, int month, List<DealInternal> exist)
      throws ConnectException, IOException {
    Log.trace("RequestOrderHistoryMonth");
    if (!Connected()) Connect();
    LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
    LocalDateTime end = start.plusMonths(1).plusSeconds(-1);
    if (exist == null) OrderHistory.Request(start, end, 0, 0, true);
    else {
      long max = 0;
      for (DealInternal item : exist) if (item.HistoryTime > max) max = item.HistoryTime;
      OrderHistory.Request(start, end, exist.size(), max, true);
    }
  }

  /**
   * Reuest closed orders.
   *
   * @param year Required year.
   * @param month Required month.
   * @return Array of orders.
   */
  public void RequestDealHistory(int year, int month) throws IOException, ConnectException {
    RequestDealHistory(year, month, null);
  }

  /**
   * Request closed orders.
   *
   * @param from Start time of history.
   * @param to End time of history.
   * @return Array of orders.
   */
  public final void RequestOrderHistory(java.time.LocalDateTime from, java.time.LocalDateTime to)
      throws IOException {
    Log.trace("RequestOrderHistory");
    if (!Connected()) {
      throw new RuntimeException("Not connected");
    }
    OrderHistory.Request(from, to, 0, 0, false);
  }

  /**
   * Reuest closed orders.
   *
   * @param year Required year.
   * @param month Required month.
   * @param exist Existing deals.
   * @return Array of orders.
   */
  public void RequestOrderHistory(int year, int month, List<OrderInternal> exist)
      throws IOException, ConnectException {
    Log.trace("RequestOrderHistoryMonth");
    if (!Connected()) Connect();
    LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
    LocalDateTime end = start.plusMonths(1).plusSeconds(-1);
    if (exist == null) OrderHistory.Request(start, end, 0, 0, false);
    else {
      long max = 0;
      for (OrderInternal item : exist) if (item.HistoryTime > max) max = item.HistoryTime;
      OrderHistory.Request(start, end, exist.size(), max, false);
    }
  }

  /**
   * Reuest closed orders.
   *
   * @param year Required year.
   * @param month Required month.
   * @return Array of orders.
   */
  public void RequestOrderHistory(int year, int month) throws IOException, ConnectException {
    RequestOrderHistory(year, month, null);
  }

  /** Milliseconds. Throw ConnectException if not connected at this period. */
  public int ConnectTimeout = 30000000;

  /**
   * Connect to server.
   *
   * @throws ServerException Unable to login for some reason.
   * @throws TimeoutException No reply from server.
   */
  public final void Connect() throws ConnectException {
    Exception ex = null;
    try {
      Time = LocalDateTime.MIN;
      Connector.go(ConnectTimeout, Host, Port);
      return;
    } catch (Exception e) {
      ex = e;
    }
    if (ServerDetails != null) {
      for (Map.Entry<AccessInfo, AddressRec[]> item : ServerDetails.getValue()) {
        try {
          Map.Entry<String, Integer> hostport = HostAndPort.Parse(item.getValue()[0].Address);
          Connector.go(ConnectTimeout, hostport.getKey(), hostport.getValue());
          return;
        } catch (Exception e) {
          ex = e;
        }
      }
    }
    if (!Connected())
      if (ex != null) {
        Connector.stop();
        throw new ConnectException(ex);
      }
  }

  public final void Disconnect() {
    if (CmdHandler != null) {
      CmdHandler.StopCmdHandler();
      try {
        CmdHandler.Thread.join(1000);
      } catch (InterruptedException e) {
      }
    }
  }

  public final void OnConnectCall(Exception exception, ConnectProgress progress) {
    ConnectEventArgs args = new ConnectEventArgs();
    args.Exception = exception;
    args.Progress = progress;
    OnConnectProgress.Invoke(this, args);
  }

  // public int DisconnectTimeout { get; internal set; }

  public final void OnDisconnect(Exception ex) {
    if (ex != null) {
      Log.exception(ex);
    } else {
      Log.trace("onDisconnect");
    }
    OnConnectCall(ex, ConnectProgress.Disconnect);
  }

  public final void OnQuoteHistory(String symbol, List<Bar> bars) {
    QuoteHistoryEventArgs args = new QuoteHistoryEventArgs();
    args.Symbol = symbol;
    args.Bars = bars;
    OnQuoteHistory.Invoke(this, args);
  }

  // private void onQuoteHist(object args)
  // {
  //    try
  //    {
  //        if (OnQuoteHistory != null)
  //            OnQuoteHistory(this, (QuoteHistoryEventArgs)args);
  //    }
  //    catch (Exception ex)
  //    {
  //        Log.exception(ex);
  //    }
  // }

  /**
   * Subscribe trading instrument.
   *
   * @param symbol Symbol for trading.
   * @throws Exception Not connected.
   */
  public final void Subscribe(String symbol) throws IOException {
    Log.trace("Subscribe");
    if (!Connected()) {
      throw new RuntimeException("Not connected");
    }
    Subscriber.Subscribe(symbol);
  }

  /**
   * Unsubscribe trading instrument.
   *
   * @param symbol Symbol for trading.
   * @throws Exception Not connected.
   */
  public final void Unsubscribe(String symbol) throws IOException {
    Log.trace("Unsubscribe");
    if (!Connected()) {
      throw new RuntimeException("Not connected");
    }
    // if (CalculateTradeProps)
    // {
    //    foreach (var order in GetOpenedOrders())
    //    {
    //        if ((order.Type == Op.Buy || order.Type == Op.Sell) && order.Symbol == symbol)
    //            return;
    //    }
    // }
    Subscriber.Unsubscribe(symbol);
  }

  /**
   * Latest quote for the symbol.
   *
   * @param symbol Symbol for trading.
   * @return Return null if no quotes for specified symbol avalible, otherwise return quote event
   *     arguments.
   * @throws Exception Symbol not subscribed.
   */
  public final Quote GetQuote(String symbol) throws IOException {
    Quote q = Subscriber.GetQuote(symbol);
    if (q != null) {
      if (q.Bid == 0 || q.Ask == 0) {
        return null;
      }
    }
    return q;
  }

  /**
   * Latest quote for the symbol.
   *
   * @param symbol Symbol for trading.
   * @return Return null if no quotes for specified symbol avalible, otherwise return quote event
   *     arguments.
   * @throws Exception Symbol not subscribed.
   */
  public Quote GetQuote(String symbol, int msTimeout)
      throws IOException, TimeoutException, ConnectException {
    if (!Connected()) Connect();
    if (!Symbols.Exist(symbol)) throw new RuntimeException("Symbol " + symbol + " not exist");
    LocalDateTime start = LocalDateTime.now();
    while (true) {
      Quote quote = Subscriber.GetQuote(symbol);
      if (quote != null)
        if (quote.Bid == 0 || quote.Ask == 0) continue;
        else return quote;
      if (Duration.between(start, LocalDateTime.now()).toMillis() > msTimeout)
        throw new TimeoutException("Cannot get quote in " + msTimeout + " ms", Log);
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
      }
    }
  }

  public final Order[] GetOpenedOrders() {
    return Orders.Opened.values().toArray(new Order[0]);
  }

  public final Order[] GetCloseddOrders() {
    return Orders.Closed.values().toArray(new Order[0]);
  }

  public final void OnOrderHisotyCall(OrderHistoryEventArgs args) {
    OnTradeHistory.Invoke(this, args);
  }

  public final void OnQuoteCall(Quote quote) {
    // ServerTime = quote.Time;
    ThreadPool.QueueUserWorkItem(() -> OnCalcProfitThread(quote));
    OnQuote.Invoke(this, quote);
  }

  private void OnCalcProfitThread(Object args) {
    try {
      Quote quote = (Quote) args;
      // Profit.Calculate(quote);
      for (Order item : GetOpenedOrders()) {
        if (item.OrderType == OrderType.Buy || item.OrderType == OrderType.Sell) {
          if (DotNetToJavaStringHelper.stringsEqual(item.Symbol, quote.Symbol)) {
            try {
              OrderProfit.Update(item, quote.Bid, quote.Ask);
            } catch (RuntimeException | IOException ex) {
              Log.exception(ex);
            }
          }
        }
      }
    } catch (RuntimeException ex) {
      Log.exception(ex);
    }
  }

  public final void OnOrderUpdateCall(OrderUpdate[] update) {
    try {
      for (OrderUpdate item : update) {
        Orders.Api_OnOrderUpdate(this, item);
      }
    } catch (RuntimeException ex) {
      Log.exception(ex);
    }
    try {
      for (OrderUpdate item : update) {
        for (OnOrderUpdate waiter : UpdateWaiters) {
          waiter.invoke(this, item);
        }
      }
    } catch (RuntimeException ex) {
      Log.exception(ex);
    }
    try {
      for (Order order : GetOpenedOrders())
        if (order.OrderType == OrderType.Buy || order.OrderType == OrderType.Sell)
          Subscribe(order.Symbol);
    } catch (Exception ex) {
      Log.exception(ex);
    }
    OnOrderUpdate.Invoke(this, update);
  }

  public final void OnOrderProgressCall(OrderProgress[] progr) {
    try {
      for (OrderProgress item : progr) {
        for (OnOrderProgress waiter : ProgressWaiters) {
          waiter.invoke(this, item);
        }
      }
    } catch (RuntimeException ex) {
      Log.exception(ex);
    }
    OnOrderProgress.Invoke(this, progr);
  }

  public FillPolicy GetFillPolicy(String symbol, OrderType type) {
    boolean pendingOrder = true;
    if (type == OrderType.Buy || type == OrderType.Sell || type == OrderType.CloseBy)
      pendingOrder = false;
    FillPolicy fp;
    SymGroup group = Symbols.GetGroup(symbol);
    if (group.TradeType == ExecutionType.Request || group.TradeType == ExecutionType.Instant) {
      if (pendingOrder) fp = FillPolicy.FlashFill;
      else fp = FillPolicy.FillOrKill;
    } else {
      if (pendingOrder) fp = FillPolicy.FlashFill;
      else if (group.FillPolicy == 2) fp = FillPolicy.ImmediateOrCancel;
      else fp = FillPolicy.FillOrKill; // FOK or ANY
    }
    return fp;
  }

  public String[] Subscriptions() {
    return Subscriber.Subscriptions();
  }

  /// <summary>
  /// Request 1 minute bar hsitory for one month back from specifeid date
  /// </summary>
  /// <param name="symbol">Symbol</param>
  /// <param name="year">Year</param>
  /// <param name="month">Month</param>
  /// <param name="day">Day</param>

  /**
   * Request 1 minute bar hsitory for one month back from specifeid date
   *
   * @param symbol
   * @param year
   * @param month
   * @param day
   */
  public void RequestQuoteHistoryMonth(String symbol, int year, int month, int day)
      throws IOException {
    if (!Symbols.Exist(symbol)) throw new RuntimeException(symbol + " not exist");
    QuoteHistory.ReqSend(Connection, symbol, toShort(4, 1, 1), toShort(day, month, year - 1970));
  }

  short toShort(int day, int month, int year) {
    //		[BitfieldLength(5)]
    //		public uint Day;
    //        [BitfieldLength(4)]
    //		public uint Month;
    //        [BitfieldLength(7)]
    //		public uint Year;
    short res = (short) (day | (month << 5) | (year << 9));
    return res;
  }

  /// <summary>
  /// Request 1 minute bar hsitory for today
  /// </summary>
  /// <param name="symbol">Symbol</param>
  public void RequestQuoteHistoryToday(String symbol) throws IOException {
    if (!Symbols.Exist(symbol)) throw new RuntimeException(symbol + " not exist");
    QuoteHistory.ReqStart(Connection, symbol, (byte) 33);
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

  /// <summary>
  /// Tick value in base currency
  /// </summary>
  /// <param name="quote"></param>
  /// <returns></returns>
  public double GetAskTickValue(Quote quote) throws IOException {
    SymbolInfo i = Symbols.GetInfo(quote.Symbol);
    new OrderProfit(this).UpdateSymbolTick(i, quote.Bid, quote.Ask);
    return i.ask_tickvalue;
  }

  /// <summary>
  /// Tick value in base currency
  /// </summary>
  /// <param name="quote"></param>
  /// <returns></returns>
  public double GetBidTickValue(Quote quote) throws IOException {
    SymbolInfo i = Symbols.GetInfo(quote.Symbol);
    new OrderProfit(this).UpdateSymbolTick(i, quote.Bid, quote.Ask);
    return i.bid_tickvalue;
  }

  /// <summary>
  /// Tick value in base currency
  /// </summary>
  /// <param name="symbol"></param>
  /// <returns></returns>
  public double GetTickValue(String symbol) throws IOException, TimeoutException, ConnectException {
    SymbolInfo i = Symbols.GetInfo(symbol);
    Quote quote = GetQuote(symbol, 5000);
    new OrderProfit(this).UpdateSymbolTick(i, quote.Bid, quote.Ask);
    return (i.bid_tickvalue + i.ask_tickvalue) / 2;
  }

  /**
   * Estimate connection time in milliseconds
   *
   * @param host Host
   * @param port Port
   * @param timeoutMs Timeout in milliseconds
   * @return Connection time in milliseconds
   * @throws UnknownHostException If host is unknown
   */
  public static int PingHost(String host, int port, int timeoutMs) throws UnknownHostException {
    LocalDateTime start = LocalDateTime.now();
    int res = -1;
    try {
      try (Socket client = new Socket()) {
        client.connect(new InetSocketAddress(host, port), timeoutMs);
        res = (int) Duration.between(start, LocalDateTime.now()).toMillis();
      }
    } catch (UnknownHostException ex) {
      throw ex;
    } catch (Exception ignored) {
    }
    return res;
  }

  public boolean PingPong() {
    try {
      OrderClose(0, Symbols.Infos[0].Name, 0, -1, OrderType.Buy);
    } catch (ServerException ex) {
      return true;
    } catch (Exception e) {
      return false;
    }
    return false;
  }
}
