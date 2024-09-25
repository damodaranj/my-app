package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.IOException;

class OrderProfit {
  private MT5API QC;

  public OrderProfit(MT5API qc) {
    QC = qc;
  }

  public final void Update(Order order, double bid, double ask) throws IOException {
    SymbolInfo sym = QC.Symbols.GetInfo(order.Symbol);
    UpdateSymbolTick(sym, bid, ask);
    if (sym.CalcMode == CalculationMode.Futures
        || sym.CalcMode == CalculationMode.ExchangeFutures
        || sym.CalcMode == CalculationMode.FORTSFutures) {
      double price = 0.0;
      if (order.OrderType == OrderType.Buy) {
        price =
            (QC.GetQuote(order.Symbol).Bid - order.OpenPrice) * sym.bid_tickvalue / sym.tick_size;
      } else if (order.OrderType == OrderType.Sell) {
        price =
            (order.OpenPrice - QC.GetQuote(order.Symbol).Ask) * sym.ask_tickvalue / sym.tick_size;
      }
      order.Profit = price * order.Lots;
    } else {
      if (order.OrderType == OrderType.Buy) {
        order.Profit =
            (Math.round((QC.GetQuote(order.Symbol).Bid) * Math.pow(10, sym.Digits))
                        / Math.pow(10, sym.Digits)
                    - Math.round((order.OpenPrice) * Math.pow(10, sym.Digits))
                        / Math.pow(10, sym.Digits))
                * order.Lots
                * (0.0 == sym.ask_tickvalue ? 1.0 : sym.ask_tickvalue);
      } else if (order.OrderType == OrderType.Sell) {
        order.Profit =
            (Math.round((order.OpenPrice) * Math.pow(10, sym.Digits)) / Math.pow(10, sym.Digits)
                    - Math.round((QC.GetQuote(order.Symbol).Ask) * Math.pow(10, sym.Digits))
                        / Math.pow(10, sym.Digits))
                * order.Lots
                * (0.0 == sym.ask_tickvalue ? 1.0 : sym.ask_tickvalue);
      }
    }
    order.Profit = Math.round(order.Profit * Math.pow(10, sym.Digits)) / Math.pow(10, sym.Digits);
  }

  public final boolean GetTickRate(String symbol, Quote rate) throws IOException {
    if (!QC.Symbols.Exist(symbol)) {
      return false;
    }
    while (QC.GetQuote(symbol) == null) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
      }
    }
    Quote q = QC.GetQuote(symbol);
    rate.Bid = q.Bid;
    rate.Ask = q.Ask;
    return true;
  }

  private boolean memcmp(String str1, int ind, String str2, int count) {
    return memcmp(str1.substring(3), str2, count);
  }

  private boolean memcmp(String str1, String str2, int count) {
    for (int i = 0; i < count; i++) {
      if (i == str1.length()) {
        if (str1.length() == str2.length()) {
          return true;
        } else {
          return false;
        }
      }
      if (i == str2.length()) {
        if (str1.length() == str2.length()) {
          return true;
        } else {
          return false;
        }
      }
      if (str1.charAt(i) != str2.charAt(i)) {
        return false;
      }
    }
    return true;
  }

  private void memcpy(RefObject<String> dst, int dstind, String src, int srcind, int count) {
    String res = dst.argValue.substring(0, dstind);
    for (int i = 0; i < count; i++) {
      if (i + srcind >= src.length()) {
        break;
      } else {
        res += src.charAt(i + srcind);
      }
    }
    for (int i = dstind + count; i < dst.argValue.length(); i++) {
      res += dst.argValue.charAt(i);
    }
    dst.argValue = res;
  }

  private void memcpy(RefObject<String> dst, String src, int count) {
    String res = "";
    for (int i = 0; i < count; i++) {
      res += src.charAt(i);
    }
    for (int i = count; i < dst.argValue.length(); i++) {
      res += dst.argValue.charAt(i);
    }
    dst.argValue = res;
  }

  boolean UpdateSymbolTick(SymbolInfo sym, double bid, double ask) throws IOException {
    Quote rate = new Quote();
    ;
    String sym_symbol = sym.Name;
    String cur1 = sym_symbol;
    String cur2 = "";
    if (sym.CalcMode == CalculationMode.Forex) {
      if (memcmp(cur1, QC.Symbols.Base.Currency, 3)) {
        sym.bid_tickvalue = sym.ContractSize / bid;
        sym.ask_tickvalue = sym.ContractSize / ask;
      } else if (memcmp(cur1, 3, QC.Symbols.Base.Currency, 3)) {
        sym.bid_tickvalue = sym.ContractSize;
        sym.ask_tickvalue = sym.ContractSize;
      } else {
        RefObject<String> tempRef_cur2 = new RefObject<String>(cur2);
        memcpy(tempRef_cur2, QC.Symbols.Base.Currency, 3);
        cur2 = tempRef_cur2.argValue;
        RefObject<String> tempRef_cur22 = new RefObject<String>(cur2);
        memcpy(tempRef_cur22, 3, cur1, 3, 3);
        cur2 = tempRef_cur22.argValue;
        RefObject<String> tempRef_cur23 = new RefObject<String>(cur2);
        memcpy(tempRef_cur23, 6, sym_symbol, 6, 6);
        cur2 = tempRef_cur23.argValue;
        if (GetTickRate(cur2, rate)) {
          sym.bid_tickvalue = sym.ContractSize / rate.Bid;
          sym.ask_tickvalue = sym.ContractSize / rate.Ask;
        } else {
          RefObject<String> tempRef_cur24 = new RefObject<String>(cur2);
          memcpy(tempRef_cur24, 0, cur1, 3, 3);
          cur2 = tempRef_cur24.argValue;
          RefObject<String> tempRef_cur25 = new RefObject<String>(cur2);
          memcpy(tempRef_cur25, 3, QC.Symbols.Base.Currency, 0, 3);
          cur2 = tempRef_cur25.argValue;
          if (GetTickRate(cur2, rate)) {
            sym.bid_tickvalue = sym.ContractSize * rate.Bid;
            sym.ask_tickvalue = sym.ContractSize * rate.Ask;
          } else {
            RefObject<String> tempRef_cur26 = new RefObject<String>(cur2);
            memcpy(tempRef_cur26, "USD", 3);
            cur2 = tempRef_cur26.argValue;
            RefObject<String> tempRef_cur27 = new RefObject<String>(cur2);
            memcpy(tempRef_cur27, 3, cur1, 3, 3);
            cur2 = tempRef_cur27.argValue;
            if (GetTickRate(cur2, rate)) {
              sym.bid_tickvalue = sym.ContractSize / rate.Bid;
              sym.ask_tickvalue = sym.ContractSize / rate.Ask;
            } else {
              RefObject<String> tempRef_cur28 = new RefObject<String>(cur2);
              memcpy(tempRef_cur28, 0, cur1, 3, 3);
              cur2 = tempRef_cur28.argValue;
              RefObject<String> tempRef_cur29 = new RefObject<String>(cur2);
              memcpy(tempRef_cur29, 3, "USD", 0, 3);
              cur2 = tempRef_cur29.argValue;
              if (GetTickRate(cur2, rate)) {
                sym.bid_tickvalue = sym.ContractSize * rate.Bid;
                sym.ask_tickvalue = sym.ContractSize * rate.Ask;
              }
            }
            RefObject<String> tempRef_cur210 = new RefObject<String>(cur2);
            memcpy(tempRef_cur210, "USD", 3);
            cur2 = tempRef_cur210.argValue;
            RefObject<String> tempRef_cur211 = new RefObject<String>(cur2);
            memcpy(tempRef_cur211, 3, QC.Symbols.Base.Currency, 0, 3);
            cur2 = tempRef_cur211.argValue;
            if (GetTickRate(cur2, rate)) {
              sym.bid_tickvalue *= rate.Bid;
              sym.ask_tickvalue *= rate.Ask;
            } else {
              RefObject<String> tempRef_cur212 = new RefObject<String>(cur2);
              memcpy(tempRef_cur212, QC.Symbols.Base.Currency, 3);
              cur2 = tempRef_cur212.argValue;
              RefObject<String> tempRef_cur213 = new RefObject<String>(cur2);
              memcpy(tempRef_cur213, 3, "USD", 0, 3);
              cur2 = tempRef_cur213.argValue;
              if (GetTickRate(cur2, rate)) {
                sym.bid_tickvalue /= rate.Bid;
                sym.ask_tickvalue /= rate.Ask;
              }
            }
          }
        }
      }
      return (sym.bid_tickvalue > 0)
          && (sym.bid_tickvalue < Double.MAX_VALUE)
          && (sym.ask_tickvalue > 0)
          && (sym.ask_tickvalue < Double.MAX_VALUE);
    }
    double dTick = (sym.CalcMode == CalculationMode.Futures) ? sym.TickValue : sym.ContractSize;
    sym.bid_tickvalue = dTick;
    sym.ask_tickvalue = dTick;
    if (!DotNetToJavaStringHelper.stringsEqual(sym.ProfitCurrency, QC.Symbols.Base.Currency)) {
      RefObject<String> tempRef_cur214 = new RefObject<String>(cur2);
      memcpy(tempRef_cur214, QC.Symbols.Base.Currency, 3);
      cur2 = tempRef_cur214.argValue;
      RefObject<String> tempRef_cur215 = new RefObject<String>(cur2);
      memcpy(tempRef_cur215, 3, sym.ProfitCurrency, 0, 3);
      cur2 = tempRef_cur215.argValue;
      if (cur2.length() > 6) {
        cur2 = cur2.substring(0, 6); // cur2[6] = 0;
      }
      if (GetTickRate(cur2, rate)) {
        sym.bid_tickvalue /= rate.Bid;
        sym.ask_tickvalue = sym.bid_tickvalue;
      } else {
        RefObject<String> tempRef_cur23 = new RefObject<String>(cur2);
        memcpy(tempRef_cur23, 6, sym_symbol, 6, 6);
        cur2 = tempRef_cur23.argValue;
        if (GetTickRate(cur2, rate)) {
          sym.bid_tickvalue /= rate.Bid;
          sym.ask_tickvalue = sym.bid_tickvalue;
        } else {
          RefObject<String> tempRef_cur216 = new RefObject<String>(cur2);
          memcpy(tempRef_cur216, sym.ProfitCurrency, 3);
          cur2 = tempRef_cur216.argValue;
          RefObject<String> tempRef_cur217 = new RefObject<String>(cur2);
          memcpy(tempRef_cur217, 3, QC.Symbols.Base.Currency, 0, 3);
          cur2 = tempRef_cur217.argValue;
          if (GetTickRate(cur2, rate)) {
            sym.bid_tickvalue *= rate.Bid;
            sym.ask_tickvalue = sym.bid_tickvalue;
          }
          RefObject<String> tempRef_cur218 = new RefObject<String>(cur2);
          memcpy(tempRef_cur218, "USD", 3);
          cur2 = tempRef_cur218.argValue;
          RefObject<String> tempRef_cur219 = new RefObject<String>(cur2);
          memcpy(tempRef_cur219, 3, sym.ProfitCurrency, 0, 3);
          cur2 = tempRef_cur219.argValue;
          if (GetTickRate(cur2, rate)) {
            sym.bid_tickvalue /= rate.Bid;
            sym.ask_tickvalue = sym.bid_tickvalue;
          } else {
            RefObject<String> tempRef_cur220 = new RefObject<String>(cur2);
            memcpy(tempRef_cur220, sym.ProfitCurrency, 3);
            cur2 = tempRef_cur220.argValue;
            RefObject<String> tempRef_cur221 = new RefObject<String>(cur2);
            memcpy(tempRef_cur221, 3, "USD", 0, 3);
            cur2 = tempRef_cur221.argValue;
            if (GetTickRate(cur2, rate)) {
              sym.bid_tickvalue *= rate.Bid;
              sym.ask_tickvalue *= rate.Ask;
              // ->94111D
            } else {
              sym.bid_tickvalue = 0;
              sym.ask_tickvalue = 0;
              return false;
            }
          }
          RefObject<String> tempRef_cur222 = new RefObject<String>(cur2);
          memcpy(tempRef_cur222, "USD", 3);
          cur2 = tempRef_cur222.argValue;
          RefObject<String> tempRef_cur223 = new RefObject<String>(cur2);
          memcpy(tempRef_cur223, 3, QC.Symbols.Base.Currency, 0, 3);
          cur2 = tempRef_cur223.argValue;
          if (GetTickRate(cur2, rate)) {
            sym.bid_tickvalue *= rate.Bid;
            sym.ask_tickvalue *= rate.Ask;
          } else {
            RefObject<String> tempRef_cur224 = new RefObject<String>(cur2);
            memcpy(tempRef_cur224, QC.Symbols.Base.Currency, 3);
            cur2 = tempRef_cur224.argValue;
            RefObject<String> tempRef_cur225 = new RefObject<String>(cur2);
            memcpy(tempRef_cur225, 3, "USD", 0, 3);
            cur2 = tempRef_cur225.argValue;
            if (GetTickRate(cur2, rate)) {
              sym.bid_tickvalue /= rate.Bid;
              sym.ask_tickvalue /= rate.Ask;
            } else {
              sym.bid_tickvalue = 0;
              sym.ask_tickvalue = 0;
              return false;
            }
          }
        }
      }
    }
    return (sym.bid_tickvalue > 0)
        && (sym.bid_tickvalue < Double.MAX_VALUE)
        && (sym.ask_tickvalue > 0)
        && (sym.ask_tickvalue < Double.MAX_VALUE);
  }
}
