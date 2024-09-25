package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

class Subscriber {
  private MT5API QuoteClient;
  private final ConcurrentHashMap<String, Quote> Quotes = new ConcurrentHashMap<String, Quote>();
  private Logger Log;

  public Subscriber(MT5API quoteClient, Logger log) {
    Log = log;
    QuoteClient = quoteClient;
  }

  public final Quote GetQuote(String symbol) throws IOException {
    if (!Quotes.containsKey(symbol)) {
      Quotes.put(symbol, new Quote(symbol));
      Request();
    }
    Quote res = Quotes.get(symbol);
    if (res.Ask == 0 || res.Bid == 0) return null;
    else return res;
  }

  public final boolean Subscribed(String symbol) {
    return Quotes.containsKey(symbol);
  }

  public final void Subscribe(String symbol) throws IOException {
    if (!Quotes.containsKey(symbol)) {
      Quotes.put(symbol, new Quote(symbol));
      Request();
    }
  }

  public String[] Subscriptions() {
    return Quotes.keySet().toArray(new String[0]);
  }

  public final void Subscribe(String[] symbols) throws IOException {
    boolean needRequest = false;
    for (String symbol : symbols) {
      if (!Quotes.containsKey(symbol)) {
        Quotes.put(symbol, new Quote(symbol));
        needRequest = true;
      }
    }
    if (needRequest) Request();
  }

  public final void Unsubscribe(String symbol) throws IOException {
    boolean found = false;
    for (Order order : QuoteClient.GetOpenedOrders()) {
      if ((order.OrderType == OrderType.Buy || order.OrderType == OrderType.Sell)
          && order.Symbol == symbol) found = true;
    }
    if (!found)
      if (Quotes.containsKey(symbol)) {
        Quotes.remove(symbol);
        Request();
      }
  }

  private void Request() throws IOException {
    OutBuf buf = new OutBuf();
    buf.ByteToBuffer((byte) 9);
    buf.IntToBuffer(Quotes.keySet().size());
    // C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
    for (String item : Quotes.keySet()) {
      SymbolInfo si = QuoteClient.Symbols.GetInfo(item);
      buf.IntToBuffer(si.Id);
    }
    QuoteClient.Connection.SendPacket((byte) 0x69, buf);
  }

  public final void Parse(InBuf buf) {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: var bytes = buf.ToBytes();
    byte[] bytes = buf.ToBytes();
    BitReaderQuotes br = new BitReaderQuotes(bytes, bytes.length * 8);
    br.Initialize((byte) 2, (byte) 3);
    while (true) {
      TickRec rec = new TickRec();
      RefObject<Integer> tempRef_Id = new RefObject<Integer>(rec.Id);
      if (!br.GetInt(tempRef_Id)) {
        rec.Id = tempRef_Id.argValue;
        break;
      } else {
        rec.Id = tempRef_Id.argValue;
      }
      RefObject<Long> tempRef_Time = new RefObject<Long>(rec.Time);
      if (!br.GetLong(tempRef_Time)) {
        rec.Time = tempRef_Time.argValue;
        break;
      } else {
        rec.Time = tempRef_Time.argValue;
      }
      RefObject<Long> tempRef_UpdateMask = new RefObject<Long>(rec.UpdateMask);
      // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
      // ORIGINAL LINE: if (!br.GetULong(out rec.UpdateMask))
      if (!br.GetULong(tempRef_UpdateMask)) {
        rec.UpdateMask = tempRef_UpdateMask.argValue;
        break;
      } else {
        rec.UpdateMask = tempRef_UpdateMask.argValue;
      }
      if ((rec.UpdateMask & 1) != 0) {
        RefObject<Long> tempRef_Bid = new RefObject<Long>(rec.Bid);
        if (!br.GetLong(tempRef_Bid)) {
          rec.Bid = tempRef_Bid.argValue;
          break;
        } else {
          rec.Bid = tempRef_Bid.argValue;
        }
      }
      if ((rec.UpdateMask & 2) != 0) {
        RefObject<Long> tempRef_Ask = new RefObject<Long>(rec.Ask);
        if (!br.GetLong(tempRef_Ask)) {
          rec.Ask = tempRef_Ask.argValue;
          break;
        } else {
          rec.Ask = tempRef_Ask.argValue;
        }
      }
      if ((rec.UpdateMask & 4) != 0) {
        RefObject<Long> tempRef_Last = new RefObject<Long>(rec.Last);
        if (!br.GetLong(tempRef_Last)) {
          rec.Last = tempRef_Last.argValue;
          break;
        } else {
          rec.Last = tempRef_Last.argValue;
        }
      }
      // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
      // ORIGINAL LINE: ulong volume = 0;
      long volume = 0;
      if ((rec.UpdateMask & 8) != 0) {
        RefObject<Long> tempRef_volume = new RefObject<Long>(volume);
        // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
        // ORIGINAL LINE: if (!br.GetULong(out volume))
        if (!br.GetULong(tempRef_volume)) {
          volume = tempRef_volume.argValue;
          break;
        } else {
          volume = tempRef_volume.argValue;
        }
      }
      if ((rec.UpdateMask & 0x10) != 0) {
        RefObject<Long> tempRef_s3C = new RefObject<Long>(rec.s3C);
        if (!br.GetLong(tempRef_s3C)) {
          rec.s3C = tempRef_s3C.argValue;
          break;
        } else {
          rec.s3C = tempRef_s3C.argValue;
        }
      }
      if ((rec.UpdateMask & 0x20) != 0) {
        RefObject<Short> tempRef_BankId = new RefObject<Short>(rec.BankId);
        if (!br.GetShort(tempRef_BankId)) {
          rec.BankId = tempRef_BankId.argValue;
          break;
        } else {
          rec.BankId = tempRef_BankId.argValue;
          rec.BankId = -1;
        }
      }
      if ((rec.UpdateMask & 0x40) != 0) {
        RefObject<Long> tempRef_TimeMs = new RefObject<Long>(rec.TimeMs);
        if (!br.GetLong(tempRef_TimeMs)) {
          rec.TimeMs = tempRef_TimeMs.argValue;
          break;
        } else {
          rec.TimeMs = tempRef_TimeMs.argValue;
        }
        rec.TimeMs += rec.Time * 1000;
      } else {
        rec.TimeMs = rec.Time * 1000;
      }
      if ((rec.UpdateMask & 0x80) != 0) {
        RefObject<Long> tempRef_s44 = new RefObject<Long>(rec.s44);
        if (!br.GetLong(tempRef_s44)) {
          rec.s44 = tempRef_s44.argValue;
          break;
        } else {
          rec.s44 = tempRef_s44.argValue;
        }
      }
      // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
      // ORIGINAL LINE: ulong volumeEx = 0;
      long volumeEx = 0;
      if ((rec.UpdateMask & 0x100) != 0) {
        RefObject<Long> tempRef_volumeEx = new RefObject<Long>(volumeEx);
        // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
        // ORIGINAL LINE: if (!br.GetULong(out volumeEx))
        if (!br.GetULong(tempRef_volumeEx)) {
          volumeEx = tempRef_volumeEx.argValue;
          break;
        } else {
          volumeEx = tempRef_volumeEx.argValue;
        }
      }
      if ((rec.UpdateMask & 0x108) != 0) {
        rec.Volume = volume * 100000000 + volumeEx;
      }
      br.SkipRecords(rec.UpdateMask, 0x100);
      br.AlignBitPosition(1);
      SymbolInfo info = QuoteClient.Symbols.GetInfo(rec.Id);
      Quote quote;
      if (!Quotes.containsKey(info.Name)) {
        continue;
      } else {
        quote = Quotes.get(info.Name);
        if (quote == null) {
          quote = new Quote();
          quote.Symbol = info.Name;
          Quotes.put(quote.Symbol, quote);
        }
      }
      RecToQuote(rec, quote);
      if (quote.Bid != 0) {
        if (quote.Ask != 0) {
          if (QuoteClient.ServerDetails.getKey().ServerName.startsWith("LandFX")) {
            double dif = 2 * Math.pow(10, -QuoteClient.Symbols.GetInfo(quote.Symbol).Digits);
            quote.Bid += dif;
            quote.Ask -= dif;
          }
          QuoteClient.OnQuoteCall(quote);
        }
      }
    }
  }

  private void RecToQuote(TickRec rec, Quote quote) {
    SymbolInfo info = QuoteClient.Symbols.GetInfo(rec.Id);
    quote.Time = ConvertTo.DateTimeMs(rec.TimeMs);
    quote.BankId = rec.BankId;
    quote.UpdateMask = rec.UpdateMask;
    // if ((rec.UpdateMask & 1)!=0)
    if (rec.Bid != 0) {
      quote.Bid = ConvertTo.LongLongToDouble(info.Digits, rec.Bid);
    }
    // if ((rec.UpdateMask & 2)!=0)
    if (rec.Ask != 0) {
      quote.Ask = ConvertTo.LongLongToDouble(info.Digits, rec.Ask);
    }
    // if ((rec.UpdateMask & 4) != 0)
    if (rec.Last != 0) {
      quote.Last = ConvertTo.LongLongToDouble(info.Digits, rec.Last);
    }
    // if ((rec.UpdateMask & 8) != 0)
    if (rec.Volume != 0) {
      quote.Volume = rec.Volume;
    }
    // if ((rec.UpdateMask & 8) != 0x80)
    //    quote.s44 = rec.s44;
  }

  private void RecToQuote(SymbolMarketData rec, Quote quote) {
    SymbolInfo info = QuoteClient.Symbols.GetInfo(rec.Id);
    quote.Time = ConvertTo.DateTimeMs(rec.TimeMs);
    quote.UpdateMask = rec.UpdateMask;
    // if ((rec.UpdateMask & 1)!=0)
    if (rec.Bid != 0) {
      quote.Bid = ConvertTo.LongLongToDouble(info.Digits, rec.Bid);
    }
    // if ((rec.UpdateMask & 2)!=0)
    if (rec.Ask != 0) {
      quote.Ask = ConvertTo.LongLongToDouble(info.Digits, rec.Ask);
    }
    // if ((rec.UpdateMask & 4) != 0)
    if (rec.Last != 0) {
      quote.Last = ConvertTo.LongLongToDouble(info.Digits, rec.Last);
    }
    // if ((rec.UpdateMask & 8) != 0)
    if (rec.Volume != 0) {
      quote.Volume = rec.Volume;
    }
    // if ((rec.UpdateMask & 8) != 0x80)
    //    quote.s44 = rec.s44;
  }

  public void ParseSymbolData(InBuf buf) {
    byte[] bytes = buf.ToBytes();
    BitReaderQuotes br = new BitReaderQuotes(bytes, bytes.length * 8);
    br.Initialize((byte) 2, (byte) 3);
    while (true) {
      SymbolMarketData rec = new SymbolMarketData();
      RefObject<Integer> ir = new RefObject<Integer>();
      if (!br.GetInt(ir)) break;
      else rec.Id = ir.argValue;
      RefObject<Long> lr = new RefObject<Long>();
      if (!br.GetULong(lr)) break;
      else rec.UpdateMask = lr.argValue;
      if (!br.GetLong(lr)) break;
      else rec.Time = lr.argValue;
      if ((rec.UpdateMask & 1) != 0)
        if (!br.GetLong(lr)) break;
        else rec.Bid = lr.argValue;
      if ((rec.UpdateMask & 2) != 0)
        if (!br.GetLong(lr)) break;
        else rec.BidHigh = lr.argValue;
      if ((rec.UpdateMask & 4) != 0)
        if (!br.GetLong(lr)) break;
        else rec.BidLow = lr.argValue;
      if ((rec.UpdateMask & 8) != 0)
        if (!br.GetLong(lr)) break;
        else rec.Ask = lr.argValue;
      if ((rec.UpdateMask & 0x10) != 0)
        if (!br.GetLong(lr)) break;
        else rec.AskHigh = lr.argValue;
      if ((rec.UpdateMask & 0x20) != 0)
        if (!br.GetLong(lr)) break;
        else rec.AskLow = lr.argValue;
      if ((rec.UpdateMask & 0x40) != 0)
        if (!br.GetLong(lr)) break;
        else rec.Last = lr.argValue;
      if ((rec.UpdateMask & 0x80) != 0)
        if (!br.GetLong(lr)) break;
        else rec.LastHigh = lr.argValue;
      if ((rec.UpdateMask & 0x100) != 0)
        if (!br.GetLong(lr)) break;
        else rec.LastLow = lr.argValue;
      if ((rec.UpdateMask & 0x200) != 0)
        if (!br.GetULong(lr)) break;
        else rec.Volume = lr.argValue;
      if ((rec.UpdateMask & 0x400) != 0)
        if (!br.GetULong(lr)) break;
        else rec.VolumeHigh = lr.argValue;
      if ((rec.UpdateMask & 0x800) != 0)
        if (!br.GetULong(lr)) break;
        else rec.VolumeLow = lr.argValue;
      if ((rec.UpdateMask & 0x1000) != 0)
        if (!br.GetULong(lr)) break;
        else rec.BuyVolume = lr.argValue;
      if ((rec.UpdateMask & 0x2000) != 0)
        if (!br.GetULong(lr)) break;
        else rec.SellVolume = lr.argValue;
      if ((rec.UpdateMask & 0x4000) != 0)
        if (!br.GetULong(lr)) break;
        else rec.BuyOrders = lr.argValue;
      if ((rec.UpdateMask & 0x8000) != 0)
        if (!br.GetULong(lr)) break;
        else rec.SellOrders = lr.argValue;
      if ((rec.UpdateMask & 0x10000) != 0)
        if (!br.GetLong(lr)) break;
        else rec.Deals = lr.argValue;
      if ((rec.UpdateMask & 0x20000) != 0)
        if (!br.GetULong(lr)) break;
        else rec.DealsVolume = lr.argValue;
      if ((rec.UpdateMask & 0x40000) != 0)
        if (!br.GetLong(lr)) break;
        else rec.Turnover = lr.argValue;
      if ((rec.UpdateMask & 0x80000) != 0)
        if (!br.GetLong(lr)) break;
        else rec.OpenInterest = lr.argValue;
      if ((rec.UpdateMask & 0x100000) != 0)
        if (!br.GetLong(lr)) break;
        else rec.OpenPrice = lr.argValue;
      if ((rec.UpdateMask & 0x200000) != 0)
        if (!br.GetLong(lr)) break;
        else rec.ClosePrice = lr.argValue;
      if ((rec.UpdateMask & 0x400000) != 0)
        if (!br.GetLong(lr)) break;
        else rec.AverageWeightPrice = lr.argValue;
      if ((rec.UpdateMask & 0x800000) != 0)
        if (!br.GetLong(lr)) break;
        else rec.PriceChange = lr.argValue;
      if ((rec.UpdateMask & 0x1000000) != 0)
        if (!br.GetLong(lr)) break;
        else rec.PriceVolatility = lr.argValue;
      if ((rec.UpdateMask & 0x2000000) != 0)
        if (!br.GetLong(lr)) break;
        else rec.PriceTheoretical = lr.argValue;
      if ((rec.UpdateMask & 0x8000000) != 0)
        if (!br.GetLong(lr)) break;
        else rec.PriceChange = lr.argValue;
      if ((rec.UpdateMask & 0x4000000) != 0)
        if (!br.GetLong(lr)) {
          rec.TimeMs += rec.Time * 1000;
          break;
        } else rec.TimeMs = rec.Time * 1000;
      if ((rec.UpdateMask & 0x10000000) != 0) if (!br.GetLong(lr)) break;

      if ((rec.UpdateMask & 0x20000000) != 0) if (!br.GetLong(lr)) break;

      if ((rec.UpdateMask & 0x40000000) != 0) if (!br.GetLong(lr)) break;

      if ((rec.UpdateMask & 0x80000000) != 0) if (!br.GetLong(lr)) break;

      if ((rec.UpdateMask & 0x100000000L) != 0) if (!br.GetLong(lr)) break;

      if ((rec.UpdateMask & 0x200000000L) != 0) if (!br.GetLong(lr)) break;

      if ((rec.UpdateMask & 0x400000000L) != 0) if (!br.GetLong(lr)) break;

      if ((rec.UpdateMask & 0x800000000L) != 0) if (!br.GetLong(lr)) break;

      if ((rec.UpdateMask & 0x1000000000L) != 0) if (!br.GetLong(lr)) break;

      if ((rec.UpdateMask & 0x2000000000L) != 0) if (!br.GetLong(lr)) break;

      if ((rec.UpdateMask & 0x4000000000L) != 0) if (!br.GetLong(lr)) break;

      if ((rec.UpdateMask & 0x8000000000L) != 0) if (!br.GetLong(lr)) break;

      if ((rec.UpdateMask & 0x10000000000L) != 0) if (!br.GetLong(lr)) break;

      br.SkipRecords(rec.UpdateMask, 0x10000000000L);
      if ((rec.UpdateMask & 0x4000000000000000L) != 0) {
        long mask;
        if (!br.GetULong(lr)) break;
        br.SkipRecords(lr.argValue, 0);
      }
      if ((rec.UpdateMask & 0x8000000000000000L) != 0) {
        long mask;
        if (!br.GetULong(lr)) break;
        br.SkipRecords(lr.argValue, 0);
      }
      br.AlignBitPosition(1);
      SymbolInfo info;
      try {
        info = QuoteClient.Symbols.GetInfo(rec.Id);
      } catch (Exception ex) {
        continue;
      }

      Quote quote;

      if (!Quotes.containsKey(info.Name)) {
        continue;
      } else {
        quote = Quotes.get(info.Name);
        if (quote == null) {
          quote = new Quote();
          quote.Symbol = info.Name;
          Quotes.put(quote.Symbol, quote);
        }
      }
      RecToQuote(rec, quote);
      if (quote.Bid != 0) {
        if (quote.Ask != 0) {
          if (QuoteClient.ServerDetails.getKey().ServerName.startsWith("LandFX")) {
            double dif = 2 * Math.pow(10, -QuoteClient.Symbols.GetInfo(quote.Symbol).Digits);
            quote.Bid += dif;
            quote.Ask -= dif;
          }
          QuoteClient.OnQuoteCall(quote);
        }
      }
    }
  }
}
