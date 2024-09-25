package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

class OrderHistory {
  private Logger Log;
  private MT5API Api;

  public OrderHistory(MT5API api, Logger log) {
    Log = log;
    Api = api;
  }

  public final void Request(
      java.time.LocalDateTime from,
      java.time.LocalDateTime to,
      int existCount,
      long historyTime,
      boolean deals)
      throws IOException {
    OutBuf buf = new OutBuf();
    if (deals) buf.ByteToBuffer((byte) 0x21);
    else buf.ByteToBuffer((byte) 0x20);
    buf.LongLongToBuffer(ConvertTo.Long(from));
    buf.LongLongToBuffer(ConvertTo.Long(to));
    if (existCount > 0) {
      buf.LongToBuffer(1);
      buf.LongLongToBuffer(ConvertTo.Long(from));
      buf.LongLongToBuffer(ConvertTo.Long(to));
      buf.LongLongToBuffer(historyTime);
      buf.LongToBuffer(existCount);
      buf.LongToBuffer(0);
      buf.LongToBuffer(0);
      buf.LongToBuffer(0);
    } else buf.LongToBuffer(0);
    Api.Connection.SendPacket((byte) 0x65, buf);
  }

  public final void Parse(InBuf buf) {
    byte cmd = buf.Byte();
    if (cmd == 0x20) {
      int[] action = new int[1];
      List<OrderInternal> res = ParseOrders(buf, action);
      ArrayList<Order> list = new ArrayList<Order>();
      for (int i = 0; i < res.size(); i++) {
        Order order = new Order(res.get(i));
        list.add(order);
      }
      OrderHistoryEventArgs args = new OrderHistoryEventArgs();
      args.Action = action[0];
      args.Orders = list;
      args.InternalOrders = res;
      Api.OnOrderHisotyCall(args);
    } else if (cmd == 0x21) {
      int[] action = new int[1];
      HashMap<Long, List<DealInternal>> res = ParseDeals(buf, action);
      List<Order> list = new LinkedList<Order>();
      for (long key : res.keySet()) {
        List<DealInternal> value = res.get(key);
        if (value.size() > 1) {
          if (key == 0) // balance
          for (DealInternal deal : value) list.add(new Order(deal));
          else list.add(new Order(value.toArray(new DealInternal[0])));
        }
        if (res.get(key).size() == 1)
          if (value.get(0).Type != DealType.DealBuy && value.get(0).Type != DealType.DealSell)
            list.add(new Order(value.get(0)));
      }
      List<DealInternal> deals = new LinkedList<>();
      for (long key : res.keySet()) deals.addAll(res.get(key));
      OrderHistoryEventArgs args = new OrderHistoryEventArgs();
      args.Action = action[0];
      args.Orders = list;
      args.InternalDeals = deals;
      Api.OnOrderHisotyCall(args);
    } else
      throw new RuntimeException("Unknown Trade Parse Cmd = 0x" + String.format("%X", cmd & 0xFF));
  }

  private HashMap<Long, List<DealInternal>> ParseDeals(InBuf buf, int[] action) {
    int updId = buf.Int();
    int num = buf.Int();
    HashMap<Long, List<DealInternal>> res = new HashMap<Long, List<DealInternal>>();
    for (int i = 0; i < num; i++) {
      LocalDateTime time = ConvertTo.DateTime(buf.Long());
      action[0] = buf.Int();
      if (action[0] == 1) continue;
      if (action[0] == 4) {
        // RemoveItem(time);
        // continue;
      }
      if (res == null) res = ParseReceivedDeals(action[0], buf);
      else {
        HashMap<Long, List<DealInternal>> map = ParseReceivedDeals(action[0], buf);
        for (long key : map.keySet())
          if (!res.containsKey(key)) res.put(key, map.get(key));
          else res.get(key).addAll(map.get(key));
      }
    }
    return res;
  }

  private List<OrderInternal> ParseOrders(InBuf buf, int[] action) {
    int updId = buf.Int();
    int num = buf.Int();
    List<OrderInternal> res = new LinkedList<>();
    for (int i = 0; i < num; i++) {
      LocalDateTime time = ConvertTo.DateTime(buf.Long());
      action[0] = buf.Int();
      if (action[0] == 1) continue;
      if (action[0] == 4) {
        // RemoveItem(time);
        continue;
      }
      res.addAll(Arrays.asList(ParseReceivedOrders(action[0], buf)));
    }
    return res;
  }

  private HashMap<Long, List<DealInternal>> ParseReceivedDeals(int action, InBuf buf) {
    if (action == 0) {
      int num = buf.Int();
      long[] tickets = new long[num];
      for (int i = 0; i < num; i++) tickets[i] = buf.Long();
      if (Api.Connection.TradeBuild <= 1892) throw new RuntimeException("TradeBuild <= 1892");
      HashMap<Long, List<DealInternal>> res = buf.ArrayDeal();
      buf.Bytes(16);
      return res;
    } else {
      Msg status = Msg.forValue(buf.Int());
      HashMap<Long, List<DealInternal>> res = buf.ArrayDeal();
      return res;
    }
  }

  private OrderInternal[] ParseReceivedOrders(int action, InBuf buf) {
    if (action == 0) {
      int num = buf.Int();
      long[] tickets = new long[num];
      for (int i = 0; i < num; i++) tickets[i] = buf.Long();
      if (Api.Connection.TradeBuild <= 1892) throw new RuntimeException("TradeBuild <= 1892");
      OrderInternal[] res = buf.Array(new OrderInternal());
      buf.Bytes(16);
      return res;
    } else {
      Msg status = Msg.forValue(buf.Int());
      OrderInternal[] res = buf.Array(new OrderInternal());
      return res;
    }
  }

  private <T extends FromBufReader> ArrayList<T> ParseBuf(InBuf buf, T t) // extends FromBufReader
      {
    // buf.Int();
    int updId = buf.Int();
    int num = buf.Int();
    ArrayList<T> res = new ArrayList<T>();
    for (int i = 0; i < num; i++) {
      java.time.LocalDateTime time = ConvertTo.DateTime(buf.Long());
      int action = buf.Int();
      if (action == 1) {
        continue;
      }
      if (action == 4) {
        // RemoveItem(time);
        continue;
      }
      // if ((action != 0) && (action != 0xE))
      // {
      long[] tickets = null;
      RefObject<long[]> tempRef_tickets = new RefObject<long[]>(tickets);
      for (Object item : ParseReceivedData(action, buf, tempRef_tickets, t)) res.add((T) item);
      tickets = tempRef_tickets.argValue;
      // }
    }
    return res;
  }

  // private void ParseOrders(InBuf buf)
  // {
  //	int updId = buf.Int();
  //	int num = buf.Int();
  //	for (int i = 0; i < num; i++)
  //	{
  //		var time = ConvertTo.DateTime(buf.Long());
  //		int action = buf.Int();
  //		if (action == 1)
  //			continue;
  //		if (action == 4)
  //		{
  //			//RemoveItem(time);
  //			continue;
  //		}
  //		if ((action != 0) && (action != 0xE))
  //		{
  //			ParseReceivedData(action, buf);
  //		}
  //	}
  // }

  private <T extends FromBufReader> T[] ParseReceivedData(
      int action, InBuf buf, RefObject<long[]> tickets, T t) {
    int num = buf.Int();
    tickets.argValue = new long[num];
    for (int i = 0; i < num; i++) {
      tickets.argValue[i] = buf.Long();
    }
    if (Api.Connection.TradeBuild <= 1892) {
      throw new UnsupportedOperationException("TradeBuild <= 1892");
    }
    T[] res = buf.<T>Array(t);
    // DealInternal[] res = new DealInternal[num];
    // for (int i = 0; i < num; i++)
    //	res[i] = LoadDeal(buf);
    if (action != 0) {
      return res;
    }
    buf.Bytes(16);
    return res;
  }

  private DealInternal LoadDeal(InBuf buf) {
    if (Api.Connection.TradeBuild <= 1892) {
      throw new UnsupportedOperationException("TradeBuild <= 1892");
    }
    return buf.Struct(new DealInternal());
  }
}
