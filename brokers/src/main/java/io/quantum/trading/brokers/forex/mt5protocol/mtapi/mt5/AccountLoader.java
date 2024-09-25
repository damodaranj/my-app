package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.net.ConnectException;
import java.util.*;

public class AccountLoader {
  private Logger Log;
  private MT5API QuoteClient;
  CmdHandler CmdHandler;
  Connection Connection;

  public AccountLoader(MT5API qc, CmdHandler cmdHandler, Connection connection) {
    Log = new Logger(this);
    QuoteClient = qc;
    CmdHandler = cmdHandler;
    Connection = connection;
    QuoteClient.Symbols.Groups.clear();
    QuoteClient.Symbols.Sessions.clear();
    QuoteClient.Orders.Opened.clear();
  }

  public final void Parse(InBuf buf) {
    try {
      Msg status = Msg.forValue(buf.Int());
      if (status != Msg.DONE) {
        throw new RuntimeException("Account info parse status = " + status);
      }
      while (buf.gethasData()) {
        // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
        // ORIGINAL LINE: var cmd = buf.Byte();
        byte cmd = buf.Byte();
        switch ((cmd & 0xFF)) {
          case 0x07: // symbols
            LoadSymbols(buf);
            break;
          case 0x11: // tickers
            LoadTickers(buf);
            break;
          case 0x17: // server
            QuoteClient.ServerDetails = LoadServer(buf);
            break;
          case 0x18: // mail recepients
            MailRecipient[] mr = LoadMailRecepients(buf);
            break;
          case 0x1F: // order
            QuoteClient.Orders.AddOrders(LoadOrders(buf));
            break;
          case 0x24: // deal
            QuoteClient.Orders.AddDeals(LoadDeals(buf));
            break;
          case 0x25: // account
            QuoteClient.Account = LoadAccount(buf);
            break;
          case 0x28: // spreads
            LoadSpreads(buf);
            break;
          case 0x67: // subscriptions
            LoadSubscriptions(buf);
            break;
          case 0x69: // subscription categories
            int size = buf.Int();
            buf.Bytes(0x80 * size);
            break;
          case 0x78: // payments
            LoadPayments(buf);
            break;
          case 0x79: // payments
          case 0x80:
            LoadPayments2(buf);
            break;
          case 0x84:
            LoadSomeAccountData(buf);
            break;
          default:
            break;
        }
      }
      QuoteClient.GotAccountInfo = true;
      Connection.SendPacket((byte) 0xA, new OutBuf());
    } catch (Exception ex) {
      CmdHandler.AccountLoaderException = ex;
      QuoteClient.OnConnectCall(ex, ConnectProgress.Exception);
    }
  }

  private void LoadSubscriptions(InBuf buf) {
    Msg status = Msg.forValue(buf.Int());
    if (status == Msg.OK) return;
    if (status != Msg.DONE) throw new RuntimeException(status.toString());
    int num = buf.Int();
    for (int i = 0; i < num; i++) {
      buf.Bytes(1240); // vSubscriptionInfo
      int size = buf.Int();
      buf.Bytes(size);
      int count = buf.Int();
      for (int j = 0; j < count; j++) buf.Bytes(256);
      count = buf.Int();
      for (int j = 0; j < count; j++) buf.Bytes(256);
      count = buf.Int();
      for (int j = 0; j < count; j++) buf.Bytes(292);
      count = buf.Int();
      for (int j = 0; j < count; j++) buf.Bytes(292);
    }
  }

  private void LoadPayments(InBuf buf) {
    int num = buf.Int();
    for (int i = 0; i < num; i++) {
      buf.Bytes(776);
      int size = buf.Int();
      buf.Bytes(size);
      int count = buf.Int();
      for (int j = 0; j < count; j++) buf.Bytes(528);
      count = buf.Int();
      for (int j = 0; j < count; j++) buf.Bytes(208);
      count = buf.Int();
      for (int j = 0; j < count; j++) buf.Bytes(112);
    }
  }

  private void LoadPayments2(InBuf buf) {
    int num = buf.Int();
    for (int i = 0; i < num; i++) {
      buf.Bytes(20); // vPaymentRec
      int size = buf.Int(); // vPaymentRec
      buf.Bytes(104); // vPaymentRec
      buf.Bytes(size);
    }
  }

  private void LoadSomeAccountData(InBuf buf) {
    buf.Bytes(3084);
    int num = buf.Int();
    for (int i = 0; i < num; i++) buf.Bytes(1288);
  }

  private void LoadSpreads(InBuf buf) {
    Log.trace("LoadSpreads");
    Msg status = Msg.forValue(buf.Int());
    if (status == null) throw new RuntimeException("LoadSpreads status is null");
    ;
    if (status == Msg.OK) {
      return;
    }
    if (status != Msg.DONE) {
      throw new RuntimeException(status.toString());
    }
    int num = buf.Int();
    for (int i = 0; i < num; i++) {
      LoadSpread(buf);
    }
    LoadRemoveList(buf);
  }

  private void LoadSpread(InBuf buf) {
    SpreadInfo si = buf.Struct(new SpreadInfo());
    int num = buf.Int();
    SpreadData[] buy = new SpreadData[num];
    for (int i = 0; i < num; i++) {
      buy[i] = buf.Struct(new SpreadData());
    }
    num = buf.Int();
    SpreadData[] sell = new SpreadData[num];
    for (int i = 0; i < num; i++) {
      sell[i] = buf.Struct(new SpreadData());
    }
  }

  private AccountRec LoadAccount(InBuf buf) {
    return buf.Struct(new AccountRec());
  }

  private ArrayList<DealInternal> LoadDeals(InBuf buf) {
    Log.trace("LoadDeals");
    int updateID = buf.Int();
    int num = buf.Int();
    ArrayList<DealInternal> list = new ArrayList<DealInternal>();
    for (int i = 0; i < num; i++) {
      if (Connection.TradeBuild < 1891) {
        throw new UnsupportedOperationException();
      }
      DealInternal d = buf.Struct(new DealInternal());
      list.add(d);
    }
    return list;
  }

  private ArrayList<OrderInternal> LoadOrders(InBuf buf) {
    Log.trace("LoadOrders");
    int updateID = buf.Int();
    int num = buf.Int();
    ArrayList<OrderInternal> list = new ArrayList<OrderInternal>();
    for (int i = 0; i < num; i++) {
      if (Connection.TradeBuild < 1891) {
        throw new UnsupportedOperationException();
      }
      OrderInternal o = buf.Struct(new OrderInternal());
      list.add(o);
    }
    return list;
  }

  private MailRecipient[] LoadMailRecepients(InBuf buf) {
    Log.trace("LoadMailRecepients");
    return buf.Array(new MailRecipient());
  }

  private Map.Entry<ServerRec, ArrayList<Map.Entry<AccessInfo, AddressRec[]>>> LoadServer(
      InBuf buf) {
    Log.trace("LoadServer");
    ServerRec sr = buf.Struct(new ServerRec());
    int num = buf.Int();
    ArrayList<Map.Entry<AccessInfo, AddressRec[]>> list =
        new ArrayList<Map.Entry<AccessInfo, AddressRec[]>>();
    for (int i = 0; i < num; i++) list.add(LoadAccess(buf));
    return new AbstractMap.SimpleEntry<>(sr, list);
  }

  private Map.Entry<AccessInfo, AddressRec[]> LoadAccess(InBuf buf) {
    AccessInfo ai = buf.Struct(new AccessInfo());
    int num = buf.Int();
    ArrayList<AddressRec> list = new ArrayList<AddressRec>();
    for (int i = 0; i < num; i++) {
      list.add(buf.Struct(new AddressRec()));
    }
    return new AbstractMap.SimpleEntry<>(ai, list.toArray(new AddressRec[0]));
  }

  private void LoadTickers(InBuf buf) {
    Log.trace("LoadTickers");
    int num = buf.Int();
    for (int i = 0; i < num; i++) {
      if (Connection.SymBuild <= 1036) {
        throw new UnsupportedOperationException();
      } else {
        Ticker ticker = buf.Struct(new Ticker());
      }
    }
  }

  private void LoadSymbols(InBuf buf) throws ConnectException {
    Log.trace("LoadSymbols");
    buf.SymBuild = Connection.SymBuild;
    LoadSymBase(buf);
    if (Connection.SymBuild >= 4072) LoadSymXX4072(buf);
    Msg status = Msg.forValue(buf.Int());
    if (status == Msg.OK) {
      Log.trace("DeleteDuplicatedSymbols");
      // DeleteDuplicatedSymbols();
      return;
    }
    if (status != Msg.DONE) {
      throw new RuntimeException(status.toString());
    }
    if (Connection.SymBuild <= 1891) {
      throw new UnsupportedOperationException("SymBuild <= 1891");
    }
    int num = buf.Int();
    QuoteClient.Symbols.Infos = new SymbolInfo[num];
    for (int i = 0; i < num; i++) {
      // var size = Marshal.SizeOf(typeof(SymbolInfo));
      // var bytes = buf.Bytes(size);
      SymbolInfo si = UDT.ReadStruct(buf, new SymbolInfo());
      QuoteClient.Symbols.Infos[i] = si;
      // size = Marshal.SizeOf(typeof(SymGroup));
      // bytes = buf.Bytes(size);
      SymGroup gr = UDT.ReadStruct(buf, new SymGroup());
      QuoteClient.Symbols.Groups.put(si.Name, gr);
      QuoteClient.Symbols.Sessions.put(si.Name, LoadSessions(buf));
      // size = Marshal.SizeOf(typeof(C54));
      // bytes = buf.Bytes(size);
      C54 sc54 = UDT.ReadStruct(buf, new C54());
    }
    //		if (QuoteClient.Symbols.Groups.values().size() < 100 && QuoteClient.Symbols.SymGroups.length
    // < 100)
    //			for(SymGroup main : QuoteClient.Symbols.Groups.values())
    //			{
    //				for (SymGroup slave : QuoteClient.Symbols.SymGroups)
    //				{
    //					String regex = slave.GroupName.replace("\\", "\\\\").replace("*", ".*");
    //					Pattern pattern = Pattern.compile(regex);
    //					Matcher matcher = pattern.matcher(main.GroupName);
    //					if (matcher.matches())
    //						main.CopyValues(slave);
    //				}
    //			}
    LoadRemoveList(buf);
    LoadSymbolSets(buf);
  }

  void LoadSymXX4072(InBuf buf) {
    buf.Bytes(656); // vSymXXInfo
    int num = buf.Int();
    for (int i = 0; i < num; i++) {
      buf.Bytes(932); // vSymYY
      int count = buf.Int();
      for (int j = 0; j < count; j++) buf.Bytes(160);
    }
  }

  private void LoadRemoveList(InBuf buf) {
    int num = buf.Int();
    int[] ar = new int[num]; // m_SymInfo.m_nId
    for (int i = 0; i < num; i++) {
      ar[i] = buf.Int();
    }
  }

  private void LoadSymbolSets(InBuf buf) {
    Msg status = Msg.forValue(buf.Int());
    if (status == Msg.OK) {
      return;
    }
    if (status != Msg.DONE) {
      throw new RuntimeException(status.toString());
    }
    int num = buf.Int();
    for (int i = 0; i < num; i++) {
      // var size = Marshal.SizeOf(typeof(SymbolSet));
      // var bytes = buf.Bytes(size);
      SymbolSet ss = UDT.ReadStruct(buf, new SymbolSet());
    }
  }

  private SymbolSessions LoadSessions(InBuf buf) {
    ArrayList<ArrayList<Session>> quotes = new ArrayList<>();
    ArrayList<ArrayList<Session>> trades = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      int num = buf.Int();
      ArrayList<Session> ses = new ArrayList<Session>();
      for (int j = 0; j < num; j++) {
        Session s = UDT.ReadStruct(buf, new Session());
        ses.add(s);
      }
      quotes.add(ses);
      num = buf.Int();
      ArrayList<Session> tr = new ArrayList<Session>();
      for (int j = 0; j < num; j++) {
        Session s = UDT.ReadStruct(buf, new Session());
        tr.add(s);
      }
      trades.add(tr);
    }
    SymbolSessions ss = new SymbolSessions();
    ss.Quotes = quotes;
    ss.Trades = trades;
    return ss;
  }

  private void LoadSymBase(InBuf buf) throws ConnectException {
    short build = Connection.SymBuild;
    if (build <= 1495)
      throw new ConnectException(
          "SymBuild: " + Connection.SymBuild); // return LoadBuild1495(pBufMan);
    if (build <= 1613)
      throw new ConnectException(
          "SymBuild: " + Connection.SymBuild); // return LoadBuild1613(pBufMan);
    if (build <= 1891)
      throw new ConnectException(
          "SymBuild: " + Connection.SymBuild); // return LoadBuild1891(pBufMan);
    if (build <= 2017) {
      // LoadBuild2017(buf);
      throw new ConnectException("SymBuild: " + Connection.SymBuild);
      // return;
    }
    if (build <= 2124) {
      // LoadBuild2124(buf);
      throw new ConnectException("SymBuild: " + Connection.SymBuild);
      // return;
    }
    if (build <= 2204) {
      // LoadBuild2204(buf);
      throw new ConnectException("SymBuild: " + Connection.SymBuild);
      // return;
    }
    LoadLastBuild(buf);
  }

  private void LoadLastBuild(InBuf buf) {
    QuoteClient.Symbols.Base = UDT.ReadStruct(buf, new SymBaseInfo());
    int num = buf.Int();
    SymGroup[] ar = new SymGroup[num];
    for (int i = 0; i < num; i++) {
      SymGroup gr = UDT.ReadStruct(buf, new SymGroup());
      ar[i] = gr;
    }
    QuoteClient.Symbols.SymGroups = ar;
    num = buf.Int();
    if (num > 0) {
      Log.trace("vSymXX count > 0");
    }
    for (int i = 0; i < num; i++) {
      LoadSymXX(buf);
    }
  }

  private void LoadSymXX(InBuf buf) {
    buf.Bytes(0x38C); // vSymXXInfo
    int num = buf.Int();
    for (int i = 0; i < num; i++) {
      buf.Bytes(0xA0); // vSymYY
    }
  }
}
