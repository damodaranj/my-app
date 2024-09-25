package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class OpnedClosedOrders {
  private Logger Log;
  private MT5API Api;

  ConcurrentHashMap<Long, Order> Opened = new ConcurrentHashMap();
  ConcurrentHashMap<Long, Order> Closed = new ConcurrentHashMap();

  public OpnedClosedOrders(MT5API api, Logger log) {
    Api = api;
    Log = log;
  }

  String Proc;

  public final synchronized void Api_OnOrderUpdate(MT5API sender, OrderUpdate update) {
    Proc = "0 ";
    try {
      Process(sender, update);
      Proc += "36 ";
    } catch (Exception ex) {
      Log.warn(ex.getMessage());
    }
  }

  void Process(MT5API sender, OrderUpdate update) {
    Proc += "1 ";
    if (update.OrderInternal != null) {
      if (update.OrderInternal.State == OrderState.Placed) {
        Proc += "2 ";
        if (update.OrderInternal.DealTicket == 0) {
          Order order = Opened.get(update.OrderInternal.TicketNumber);
          if (order != null) {
            Proc += "3 ";
            order.Update(new Order(update.OrderInternal));
            update.Type = UpdateType.PendingModify;
          } else {
            Proc += "4 ";
            order = new Order(update.OrderInternal);
            Opened.put(update.OrderInternal.TicketNumber, order);
            update.Type = UpdateType.PendingOpen;
          }
          update.Order = order;
        }
      } else if (update.OrderInternal.State == OrderState.Cancelled
          || update.OrderInternal.State == OrderState.Expired
          || update.OrderInternal.State == OrderState.Rejected) {
        Proc += "5 ";
        Order order = Opened.get(update.OrderInternal.TicketNumber);
        if (order != null) {
          Proc += "6 ";
          order.Update(new Order(update.OrderInternal));
          Closed.put(update.OrderInternal.TicketNumber, order);
          while (Closed.size() > 10) Closed.remove(Collections.min(Closed.keySet()));
          Opened.remove(update.OrderInternal.TicketNumber);
          update.Order = order;
        } else update.Order = new Order(update.OrderInternal);
        Proc += "7 ";
        if (update.OrderInternal.State == OrderState.Expired) update.Type = UpdateType.Expired;
        else if (update.OrderInternal.State == OrderState.Rejected)
          update.Type = UpdateType.Rejected;
        else update.Type = UpdateType.PendingClose;
      } else if (update.OrderInternal.State == OrderState.Started) {
        Proc += "8 ";
        update.Order = new Order(update.OrderInternal);
        update.Type = UpdateType.Started;
      } else if (update.OrderInternal.State == OrderState.Filled) {
        Proc += "9 ";
        Order order = Opened.get(update.OrderInternal.TicketNumber);
        if (order != null) update.Order = order;
        else update.Order = new Order(update.OrderInternal);
        update.Type = UpdateType.Filled;
      } else if (update.OrderInternal.State == OrderState.RequestCancelling) {
        Proc += "10 ";
        Order order = Opened.get(update.OrderInternal.TicketNumber);
        if (order != null) update.Order = order;
        else update.Order = new Order(update.OrderInternal);
        update.Type = UpdateType.Cancelling;
      }
    }
    if (update.Deal != null) {
      Proc += "11 ";
      if (update.Deal.Type == DealType.Balance) {
        Proc += "12 ";
        long ticket = update.Deal.PositionTicket;
        update.Order = new Order(update.Deal);
        update.Type = UpdateType.Balance;
        Closed.put(ticket, update.Order);
        while (Closed.size() > 10) Closed.remove(Collections.min(Closed.keySet()));
      } else {
        Proc += "14 ";
        long ticket = update.Deal.PositionTicket;
        int closeByTicket = 0;
        if (update.Deal.Comment.contains("by #"))
          closeByTicket =
              Integer.parseInt(
                  update.Deal.Comment.substring(
                      update.Deal.Comment.indexOf("by #") + "by #".length()));
        if (ticket == 0) {
          Proc += "15 ";
          ticket = update.OppositeDeal.PositionTicket;
          if (update.Deal.OpenTimeMs != update.OppositeDeal.OpenTimeMs) {
            Proc += "16 ";
            Order openedOrder = Opened.get(ticket);
            if (openedOrder != null) {
              Proc += "17 ";
              int digits = Api.Symbols.GetInfo(update.OppositeDeal.Symbol).Digits;
              double mul = Math.pow(10, digits);
              if (Math.round(update.OppositeDeal.Price * mul) / mul
                      == Math.round(update.OppositeDeal.StopLoss * mul) / mul
                  || update.Deal.PlacedType == PlacedType.OnSL) {
                Proc += "18 ";
                openedOrder.UpdateOnStop(update.OppositeDeal, true);
                update.Order = openedOrder;
                update.Type = UpdateType.OnStopLoss;
                Closed.put(ticket, update.Order);
                while (Closed.size() > 10) Closed.remove(Collections.min(Closed.keySet()));
                Opened.remove(ticket);
              } else if (Math.round(update.OppositeDeal.Price * mul) / mul
                      == Math.round(update.OppositeDeal.TakeProfit * mul) / mul
                  || update.Deal.PlacedType == PlacedType.OnTP) {
                Proc += "19 ";
                openedOrder.UpdateOnStop(update.OppositeDeal, true);
                update.Order = openedOrder;
                update.Type = UpdateType.OnTakeProfit;
                Closed.put(ticket, update.Order);
                while (Closed.size() > 10) Closed.remove(Collections.min(Closed.keySet()));
                Opened.remove(ticket);
              } else {
                Proc += "20 ";
                openedOrder.Update(new Order(update.OppositeDeal));
                update.Order = openedOrder;
                update.Type = UpdateType.MarketModify;
              }
            }
          }
        } else if (update.Deal.OpenTimeMs == update.OppositeDeal.OpenTimeMs
            && Api.AccountMethod() == AccMethod.Hedging) {
          Proc += "21 ";
          Order order = Opened.get(ticket);
          if (order != null) order.Update(new Order(update.Deal));
          else {
            order = new Order(update.Deal);
            Opened.put(ticket, order);
          }
          update.Order = order;
          update.Type = UpdateType.MarketOpen;

        } else {
          Proc += "23 ";
          if (Api.AccountMethod() == AccMethod.Netting
              || Api.AccountMethod() == AccMethod.Default) {
            long nettingTicket = update.Deal.PositionTicket;
            for (Order item : Opened.values())
              if (item.Symbol.equals(update.Deal.Symbol))
                if (item.OrderType == OrderType.Buy || item.OrderType == OrderType.Sell) {
                  nettingTicket = item.Ticket;
                  break;
                }
            Order order = Opened.get(nettingTicket);
            if (nettingTicket > 0 && order != null) // in case of exvents not sorted by time
            {
              Proc += "27 ";
              if (nettingTicket <= update.OppositeDeal.TicketNumber) {
                Order open = order.Clone();
                order.Update(new Order(update.OppositeDeal));
                order.Ticket = update.OppositeDeal.TicketNumber;
                Opened.remove(nettingTicket);
                if (update.OppositeDeal.Volume > 0) {
                  Opened.put(order.Ticket, order);
                  update.Type = UpdateType.MarketOpen;
                  update.Order = new Order(update.Deal);
                } else {
                  Proc += "28 ";
                  update.Type = UpdateType.MarketClose;
                  Order value = Closed.get(ticket);
                  if (value != null) value.Update(open);
                  else {
                    Closed.put(ticket, open);
                    while (Closed.size() > 10) Closed.remove(Collections.min(Closed.keySet()));
                  }
                  update.Order = new Order(new DealInternal[] {update.Deal, update.OppositeDeal});
                }
              }
            } else {
              Order newOrder = new Order(update.Deal);
              Order oldOrder = Opened.get(ticket);
              if (oldOrder != null) oldOrder.Update(newOrder);
              else Opened.put(ticket, newOrder);
              update.Type = UpdateType.MarketOpen;
              update.Order = newOrder;
            }
          } else if (Opened.containsKey(ticket)) {
            Order open = Opened.get(ticket);
            if (open != null) {
              Proc += "24 ";
              if (update.Deal.Direction == Direction.Out
                  && (update.Deal.PlacedType == PlacedType.OnSL
                      || update.Deal.PlacedType == PlacedType.OnTP
                      || update.Deal.PlacedType == PlacedType.OnStopOut)) {
                Proc += "25 ";
                if (update.Deal.PlacedType == PlacedType.OnSL) update.Type = UpdateType.OnStopLoss;
                else if (update.Deal.PlacedType == PlacedType.OnTP)
                  update.Type = UpdateType.OnTakeProfit;
                else update.Type = UpdateType.OnStopOut;
                open.UpdateOnStop(update.Deal, false);
                Closed.put(ticket, open);
                while (Closed.size() > 10) Closed.remove(Collections.min(Closed.keySet()));
                Opened.remove(ticket);
                update.Order = open;
              } else if (update.Deal.Direction == Direction.In) {

              } else {
                Proc += "26 ";
                double closeLots = new Order(update.Deal).Lots;
                double mul = Math.pow(10, 8);
                if ((double) Math.round(open.Lots * mul) / mul == closeLots) {
                  open.Lots = 0;
                  Opened.remove(ticket);
                  Opened.remove(update.Deal.OrderTicket); // CLOSE BY
                  Opened.remove(closeByTicket); // CLOSE BY
                  update.Type = UpdateType.MarketClose;
                } else {
                  open.Lots -= closeLots;
                  update.Type = UpdateType.PartialClose;
                }
                open.Update(update.Deal);
                update.Order = open;
                if (update.Type == UpdateType.MarketClose)
                  update.Order.Profit = update.Order.CloseProfit;
                Proc += "30 ";
                Order value = Closed.get(ticket);
                if (value != null) value.Update(open);
                else {
                  Closed.put(ticket, open);
                  while (Closed.size() > 10) Closed.remove(Collections.min(Closed.keySet()));
                }
              }
            }
          } else if (Opened.containsKey(update.OppositeDeal.PositionTicket)) {
            Order order = Opened.get(update.OppositeDeal.PositionTicket);
            if (order != null) {
              Proc += "32 ";
              order.Update(new Order(update.OppositeDeal));
              update.Order = order;
              update.Type = UpdateType.MarketModify;
            }
          } else if (Api.AccountMethod() == AccMethod.Netting
              || Api.AccountMethod() == AccMethod.Default)
            Opened.put(ticket, new Order(update.OppositeDeal));
        }
      }
      // round to 2 decimal to retain precision with double
      long factor = (long) Math.pow(10, 2);
      double value =
          (Api.Account.Balance + update.Deal.Profit + update.Deal.Swap + update.Deal.Commission)
              * factor;
      long tmp = Math.round(value);
      value = (double) tmp / factor;
      Api.Account.Balance = value;
    }
    Proc += "33 ";
    if (update.Order != null)
      update.Order = update.Order.Clone(); // to avoid changes by further events during processing
    Proc += "34 ";
  }

  public final void AddDeals(ArrayList<DealInternal> deals) {
    for (DealInternal item : deals) {
      Opened.put(item.TicketNumber, new Order(item));
    }
  }

  public final void AddOrders(ArrayList<OrderInternal> orders) {
    for (OrderInternal item : orders) {
      Opened.put(item.TicketNumber, new Order(item));
    }
  }

  // internal Order[] GetOpenedOrders()
  // {
  //    Log.trace("GetOpenedOrders");
  //    if (Orders == null || Deals == null)
  //        throw new Exception("Opened orders and deals not loaded yet");
  //    List<Order> res = new List<Order>();
  //    foreach (var item in Orders)
  //        res.Add(new Order(item));
  //    foreach (var item in Deals)
  //        res.Add(new Order(item));
  //    return res.ToArray();
  // }

  public final void ParseTrades(InBuf buf) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: var cmd = buf.Byte();
    byte cmd = buf.Byte();
    switch ((cmd & 0xFF)) {
      case 7: // symbol configuration
        UpdateSymbols(buf);
        break;
      case 8: // symbol group configuration
        UpdateSymbolGroup(buf);
        break;
      case 9: // group configuration
        UpdateSymbolsBase(buf);
        break;
      case 0x11: // tickers
        UpdateTickers(buf);
        break;
      case 0x13: // users
        UpdateAccount(buf);
        break;
      case 0x1F: // orders
        UpdateOrders(buf);
        break;
      case 0x20: // history of orders
        UpdateHistoryOrders(buf);
        break;
      case 0x21: // all deals
        UpdateDeals(buf);
        break;
      case 0x23: // request
        UpdateTradeRequest(buf);
        break;
      case 0x28: // spread config
        UpdateSpreads(buf);
        break;
      default:
        String.valueOf(cmd);
        break;
    }
  }

  private void UpdateSpreads(InBuf buf) {
    Log.trace("UpdateSpreads");
  }

  private void UpdateTradeRequest(InBuf buf) {
    Log.trace("UpdateTradeRequest");
    int num = buf.Int();
    OrderProgress[] array = new OrderProgress[num];
    int count = buf.getLeft() / num - 1212; // size TransactionInfo + TradeRequest + TradeResult 476
    for (int i = 0; i < num; i++) {
      OrderProgress progress = new OrderProgress();
      progress.OrderUpdate = buf.Struct(new TransactionInfo());
      if (Api.Connection.TradeBuild <= 1891) {
        throw new UnsupportedOperationException("TradeBuild <= 1891");
      }
      progress.TradeRequest = buf.Struct(new TradeRequest());
      progress.TradeResult = buf.Struct(new TradeResult());
      // progress.DealsResult = buf.Struct<DealsResult>();
      buf.Bytes(count);
      array[i] = progress;

      long closeByTicket = progress.TradeRequest.ByCloseTicket;
      if (0 != closeByTicket
          && null != progress.TradeResult
          && progress.TradeResult.Status == Msg.REQUEST_EXECUTED) {
        Opened.remove(closeByTicket);
      }
    }
    Api.OnOrderProgressCall(array);
  }

  private void UpdateDeals(InBuf buf) {
    Log.trace("UpdateDeals");
    int num = buf.Int();
    OrderUpdate[] ar = new OrderUpdate[num];
    if (Api.Connection.TradeBuild <= 1892) {
      throw new UnsupportedOperationException("TradeBuild <= 1892");
    }
    for (int i = 0; i < num; i++) {
      OrderUpdate ou = new OrderUpdate();
      ou.Trans = buf.Struct(new TransactionInfo());
      ou.Deal = buf.Struct(new DealInternal());
      ou.OppositeDeal = buf.Struct(new DealInternal());
      PumpDeals5D8 s5 = buf.Struct(new PumpDeals5D8());
      PumpDeals698 s6 = buf.Struct(new PumpDeals698());
      if (Api.Connection.TradeBuild <= 1241) {
        continue;
      }
      DealInternal[] deals = buf.Array(new DealInternal());
      DealInternal[] opposite = buf.Array(new DealInternal());
      ar[i] = ou;
    }
    Api.OnOrderUpdateCall(ar);
  }

  private void UpdateHistoryOrders(InBuf buf) {
    Log.trace("UpdateHistoryOrders");
  }

  private void UpdateOrders(InBuf buf) {
    Log.trace("UpdateOrders");
    int num = buf.Int();
    OrderUpdate[] ar = new OrderUpdate[num];
    for (int i = 0; i < num; i++) {
      if (Api.Connection.TradeBuild <= 1891) {
        throw new UnsupportedOperationException("TradeBuild <= 1891");
      }
      OrderUpdate ou = new OrderUpdate();
      ou.Trans = buf.Struct(new TransactionInfo());
      ou.OrderInternal = buf.Struct(new OrderInternal());
      ar[i] = ou;
    }
    Api.OnOrderUpdateCall(ar);
  }

  private void UpdateAccount(InBuf buf) {
    Log.trace("UpdateAccount");
  }

  private void UpdateTickers(InBuf buf) {
    Log.trace("UpdateTickers");
  }

  private void UpdateSymbolsBase(InBuf buf) {
    Log.trace("UpdateSymbolsBase");
  }

  private void UpdateSymbolGroup(InBuf buf) {
    Log.trace("UpdateSymbolGroup");
  }

  private void UpdateSymbols(InBuf buf) {
    Log.trace("UpdateSymbols");
  }
}
