package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.IOException;

class SymbolMargin {
  class HedStatInfo // sizeof 0x30 d
   {
    public long m_lVolume; // 0
    public long m_lOrderVolume; // 8
    public double m_dLots; // 10
    public double m_dPrice; // 18
    public double m_dVolume; // 20
    public double m_dMarginRate; // 28
  }

  class TradesInfo // sizeof 0x328 d
   {
    public long m_nLogin; // 0
    public String s8 = new String(new char[16]);
    public int m_nDigits; // 18
    public String s1C = new String(new char[36]);
    public double m_dBalance; // 40
    public double m_dCredit; // 48
    public double m_dCommission; // 50
    public double m_dBlocked; // 58
    public String s60 = new String(new char[24]);
    public double m_dMargin; // 78
    public double m_dMarginFree; // 80
    public double m_dMarginLevel; // 88
    public int m_nLeverage; // 90
    public int s94;
    public double m_dMarginInitial; // 98
    public double m_dMarginMaintenance; // A0
    public String sA8 = new String(new char[16]);
    public double m_dOrderProfit; // B8
    public double m_dSwap; // C0
    public double m_dOrderCommission; // C8
    public double m_dProfit; // D0
    public double m_dEquity; // D8
    public double m_dAssets; // E0
    public double m_dLiabilities; // E8
    public double m_dCollateral; // F0
    public String sF8 = new String(new char[136]);
    public int s180;
    public String s184 = new String(new char[316]);
    public double s2C0;
    public int s2C8;
    public String s2CC = new String(new char[88]);
  }

  private MT5API Api;
  private String Symbol;
  private HedStatInfo[] m_Deal = new HedStatInfo[2];
  private HedStatInfo[] m_Order = new HedStatInfo[8];
  private double[] m_dMntnMarginRate;
  private double[] m_dInitMarginRate;
  private double m_dInitialMargin;
  private double m_dMaintenanceMargin;
  private TradesInfo m_pTradesInfo = new TradesInfo();
  private double m_dContractSize;
  private double s160;
  private double s1E8;
  private double s1F0;
  private int sE8;
  private int sEC;
  private int sF0;
  private int sF4;
  private CalculationMode m_CalcMode = CalculationMode.values()[0];
  private double m_dTickSize;
  private double m_dTickValue;
  private double m_dFaceValue;
  private double m_dTradeMargin;
  private String m_sProfitCurrency;
  private SymbolInfo m_SymInfo;
  private String m_sCurrency;
  private int s148;
  private String m_sMarginCurrency;

  public SymbolMargin(MT5API api, String symbol) {
    Api = api;
    Symbol = symbol;
    SymGroup gr = api.Symbols.GetGroup(symbol);
    SymbolInfo sym = api.Symbols.GetInfo(symbol);
    m_dContractSize = sym.ContractSize;
    m_dInitMarginRate = gr.InitMarginRate;
    m_dMntnMarginRate = gr.MntnMarginRate;
    m_dInitialMargin = gr.InitialMargin;
    m_dMaintenanceMargin = gr.MaintenanceMargin;
    s160 = gr.s3DC;
    s1E8 = gr.HedgedMargin;
    s1F0 = gr.s3E4;
    sE8 = sym.s468;
    sEC = sym.Precision;
    sF0 = gr.DeviationRate;
    sF4 = gr.RoundRate;
    m_CalcMode = sym.CalcMode;
    m_dTickSize = sym.TickSize;
    m_dTickValue = sym.TickValue;
    m_dFaceValue = sym.FaceValue;
    m_Deal[0] = new HedStatInfo();
    m_Deal[1] = new HedStatInfo();
    for (int i = 0; i < 8; i++) {
      m_Order[i] = new HedStatInfo();
    }
    m_pTradesInfo.m_nLeverage = api.Account.Leverage;
    m_sProfitCurrency = api.Symbols.Base.Currency;
    m_SymInfo = sym;
    m_sCurrency = symbol;
    s148 = gr.s340;
    m_sMarginCurrency = sym.MarginCurrency;
  }

  public final boolean AcceptDeal(DealInternal deal) {
    if (deal.Type == null)
      throw new RuntimeException(
          "Deal.Type is null for " + deal.PositionTicket + ". TypeInt = " + deal.TypeInt);
    if (deal.Type == DealType.DealBuy) {
      m_Deal[0].m_lVolume += deal.Volume;
      m_Deal[0].m_dLots += ULL2DBL(deal.Volume) * deal.OpenPrice;
      m_Deal[0].m_dVolume += ULL2DBL(deal.Volume) * deal.VolumeRate;
      m_Deal[0].m_dPrice = deal.Price;
      m_Deal[0].m_dMarginRate =
          m_dMntnMarginRate[0] != 0 ? m_dMntnMarginRate[0] : m_dInitMarginRate[0];
      return true;
    }
    if (deal.Type == DealType.DealSell) {
      m_Deal[1].m_lVolume += deal.Volume;
      m_Deal[1].m_dLots += ULL2DBL(deal.Volume) * deal.OpenPrice;
      m_Deal[1].m_dVolume += ULL2DBL(deal.Volume) * deal.VolumeRate;
      m_Deal[1].m_dPrice = deal.Price;
      m_Deal[1].m_dMarginRate =
          m_dMntnMarginRate[1] != 0 ? m_dMntnMarginRate[1] : m_dInitMarginRate[1];
      return true;
    }
    return false;
  }

  public final boolean AcceptOrder(OrderInternal order, boolean bOrder) throws IOException {
    if (m_CalcMode == CalculationMode.Collateral) {
      return false;
    }
    OrderType type = order.Type;
    if (order.IsAssociativeDealOrder() || (type == OrderType.CloseBy)) {
      return true;
    }
    if (bOrder) {
      if ((order.s1C8 == 1) && (order.IsLimitOrder() || order.IsStopOrder())) {
        type = order.IsBuyOrder() ? OrderType.Buy : OrderType.Sell;
      } else if ((order.s1C8 == 2) && order.IsStopLimitOrder()) {
        type = order.IsBuyOrder() ? OrderType.BuyLimit : OrderType.SellLimit;
      }
    }
    double mr = GetMarginRate(type, true, m_sCurrency);
    if (mr <= 0) {
      return true;
    }
    double price = order.OpenPrice;
    if (price == 0 && ((type == OrderType.Buy) || (type == OrderType.Sell))) {
      price = order.Price;
    }
    if ((type == OrderType.BuyStopLimit) || (type == OrderType.SellStopLimit)) {
      price = order.OrderPrice;
    }
    double profitRate;
    if ((type == OrderType.Buy)
        || (type == OrderType.BuyStop)
        || (type == OrderType.BuyLimit)
        || (type == OrderType.BuyStopLimit)) {
      profitRate = order.ProfitRate;
      if (profitRate == 0) {
        profitRate = GetAskProfitRate(m_sMarginCurrency, m_sCurrency, price);
      }
      if (profitRate <= 0) {
        return true;
      }
    } else if ((type == OrderType.Sell)
        || (type == OrderType.SellStop)
        || (type == OrderType.SellLimit)
        || (type == OrderType.SellStopLimit)) {
      profitRate = order.ProfitRate;
      if (profitRate == 0) {
        profitRate = GetBidProfitRate(m_sMarginCurrency, m_sCurrency, price);
      }
      if (profitRate <= 0) {
        return true;
      }
    } else {
      return false;
    }
    m_Order[type.getValue()].m_lVolume += order.RequestVolume;
    m_Order[type.getValue()].m_dLots += ULL2DBL(order.RequestVolume) * price;
    m_Order[type.getValue()].m_dVolume += ULL2DBL(order.RequestVolume) * profitRate;
    m_Order[type.getValue()].m_dMarginRate = mr;
    return true;
  }

  public final double GetTradeMargin() throws IOException {
    TradesInfo trdInfo = m_pTradesInfo;
    m_dTradeMargin = 0;
    if (m_Deal[0].m_lVolume != 0) {
      double vr = 1.0 / ULL2DBL(m_Deal[0].m_lVolume);
      m_Deal[0].m_dLots *= vr;
      m_Deal[0].m_dVolume *= vr;
    }
    if (m_Deal[1].m_lVolume != 0) {
      double vr = 1.0 / ULL2DBL(m_Deal[1].m_lVolume);
      m_Deal[1].m_dLots *= vr;
      m_Deal[1].m_dVolume *= vr;
    }
    if (m_CalcMode == CalculationMode.Collateral) {
      // vTickRate rate = new vTickRate();
      if (m_Deal[0].m_lVolume != 0) {
        double profit = GetAskProfitRate(m_sProfitCurrency, m_SymInfo.Name);
        double price = m_Deal[0].m_dPrice;
        CalculationMode calcMode = CalculationMode.Forex;
        if (price == 0) {
          price = GetBid(m_sCurrency);
        }
        double volume = AsLots(m_Deal[0].m_lVolume, m_dContractSize);
        double margin =
            Math.round(
                    (Math.round((volume * price) * Math.pow(10, m_SymInfo.Digits))
                            / Math.pow(10, m_SymInfo.Digits)
                            * profit
                            * s1E8)
                        * Math.pow(10, m_SymInfo.Digits))
                / Math.pow(10, m_SymInfo.Digits);
        trdInfo.m_dAssets = RoundAdd(trdInfo.m_dAssets, margin, m_SymInfo.Digits);
        trdInfo.m_dCollateral = RoundAdd(trdInfo.m_dCollateral, margin, m_SymInfo.Digits);
      }
      if (m_Deal[1].m_lVolume != 0) {
        double profit = GetBidProfitRate(m_sProfitCurrency, m_SymInfo.Name);
        double price = m_Deal[1].m_dPrice;
        CalculationMode calcMode = CalculationMode.Forex;
        if (price == 0) {
          price = GetAsk(m_sCurrency);
        }
        double volume = AsLots(m_Deal[1].m_lVolume, m_dContractSize);
        double margin =
            -Math.round(
                    (Math.round((volume * price) * Math.pow(10, m_SymInfo.Digits))
                            / Math.pow(10, m_SymInfo.Digits)
                            * profit)
                        * Math.pow(10, m_SymInfo.Digits))
                / Math.pow(10, m_SymInfo.Digits);
        trdInfo.m_dLiabilities = RoundAdd(trdInfo.m_dLiabilities, margin, m_SymInfo.Digits);
        trdInfo.m_dCollateral = RoundAdd(trdInfo.m_dCollateral, margin, m_SymInfo.Digits);
      }
      return m_dTradeMargin;
    }
    double buyMargin = 0;
    double sellMargin = 0;
    if ((s148 & 4) != 0) {
      for (int i = 0; i < 8; i++) {
        HedStatInfo stat = m_Order[i];
        if (stat.m_lVolume == 0) {
          continue;
        }
        double vr = 1.0 / ULL2DBL(stat.m_lVolume);
        stat.m_dLots *= vr;
        stat.m_dVolume *= vr;
        if ((i == OrderType.Buy.getValue())
            || (i == OrderType.BuyStop.getValue())
            || (i == OrderType.BuyLimit.getValue())
            || (i == OrderType.BuyStopLimit.getValue())) {
          buyMargin +=
              CalcHedMargin(m_pTradesInfo, stat.m_lVolume, true, false, stat.m_dLots)
                  * stat.m_dVolume
                  * stat.m_dMarginRate;
        }
        if ((i == OrderType.Sell.getValue())
            || (i == OrderType.SellStop.getValue())
            || (i == OrderType.SellLimit.getValue())
            || (i == OrderType.SellStopLimit.getValue())) {
          sellMargin +=
              CalcHedMargin(m_pTradesInfo, stat.m_lVolume, true, false, stat.m_dLots)
                  * stat.m_dVolume
                  * stat.m_dMarginRate;
        }
      }
      double buyDealMargin = 0;
      if (m_Deal[0].m_lVolume != 0) {
        buyDealMargin =
            CalcHedMargin(m_pTradesInfo, m_Deal[0].m_lVolume, false, false, m_Deal[0].m_dLots)
                * m_Deal[0].m_dVolume
                * m_Deal[0].m_dMarginRate;
      }
      double sellDealMargin = 0;
      if (m_Deal[1].m_lVolume != 0) {
        sellDealMargin =
            CalcHedMargin(m_pTradesInfo, m_Deal[1].m_lVolume, false, false, m_Deal[1].m_dLots)
                * m_Deal[1].m_dVolume
                * m_Deal[1].m_dMarginRate;
      }
      buyMargin += buyDealMargin;
      sellMargin += sellDealMargin;
      m_dTradeMargin = Math.max(buyMargin, sellMargin);
    } else {
      double dealMargin = CalculateDealMargin();
      for (int i = 0; i < 8; i++) {
        HedStatInfo stat = m_Order[i];
        if (stat.m_lVolume == 0) {
          continue;
        }
        double vr = 1.0 / ULL2DBL(stat.m_lVolume);
        stat.m_dLots *= vr;
        stat.m_dVolume *= vr;
        if ((i == OrderType.BuyStop.getValue())
            || (i == OrderType.BuyLimit.getValue())
            || (i == OrderType.BuyStopLimit.getValue())) {
          buyMargin +=
              CalcHedMargin(m_pTradesInfo, stat.m_lVolume, true, false, stat.m_dLots)
                  * stat.m_dVolume
                  * stat.m_dMarginRate;
        }
        if ((i == OrderType.SellStop.getValue())
            || (i == OrderType.SellLimit.getValue())
            || (i == OrderType.SellStopLimit.getValue())) {
          sellMargin +=
              CalcHedMargin(m_pTradesInfo, stat.m_lVolume, true, false, stat.m_dLots)
                  * stat.m_dVolume
                  * stat.m_dMarginRate;
        }
      }
      if (m_Order[0].m_lVolume != 0) {
        if (m_Deal[0].m_dMarginRate == 0) {
          m_Deal[0].m_dMarginRate = m_Order[0].m_dMarginRate;
        }
        double vr = 1.0 / ULL2DBL(m_Order[0].m_lVolume + m_Deal[0].m_lVolume);
        double orderAmount = ULL2DBL(m_Order[0].m_lVolume) * m_Order[0].m_dLots;
        double dealAmount = ULL2DBL(m_Deal[0].m_lVolume) * m_Deal[0].m_dLots;
        m_Deal[0].m_dLots = (orderAmount + dealAmount) * vr;
        orderAmount = ULL2DBL(m_Order[0].m_lVolume) * m_Order[0].m_dVolume;
        dealAmount = ULL2DBL(m_Deal[0].m_lVolume) * m_Deal[0].m_dVolume;
        m_Deal[0].m_dVolume = (orderAmount + dealAmount) * vr;
        m_Deal[0].m_lVolume += m_Order[0].m_lVolume;
        m_Deal[0].m_lOrderVolume = m_Order[0].m_lVolume;
      }
      if (m_Order[1].m_lVolume != 0) {
        if (m_Deal[1].m_dMarginRate == 0) {
          m_Deal[1].m_dMarginRate = m_Order[1].m_dMarginRate;
        }
        double vr = 1.0 / ULL2DBL(m_Order[1].m_lVolume + m_Deal[1].m_lVolume);
        double orderAmount = ULL2DBL(m_Order[1].m_lVolume) * m_Order[1].m_dLots;
        double dealAmount = ULL2DBL(m_Deal[1].m_lVolume) * m_Deal[1].m_dLots;
        m_Deal[1].m_dLots = (orderAmount + dealAmount) * vr;
        orderAmount = ULL2DBL(m_Order[1].m_lVolume) * m_Order[1].m_dVolume;
        dealAmount = ULL2DBL(m_Deal[1].m_lVolume) * m_Deal[1].m_dVolume;
        m_Deal[1].m_dVolume = (orderAmount + dealAmount) * vr;
        m_Deal[1].m_lVolume += m_Order[1].m_lVolume;
        m_Deal[1].m_lOrderVolume = m_Order[1].m_lVolume;
      }
      double margin = dealMargin;
      if (m_Order[0].m_lVolume != 0 || m_Order[1].m_lVolume != 0) {
        dealMargin = CalculateDealMargin();
        margin = Math.max(margin, dealMargin);
      }
      m_dTradeMargin = margin + buyMargin + sellMargin;
    }
    trdInfo.m_dMargin = RoundAdd(trdInfo.m_dMargin, m_dTradeMargin, m_SymInfo.Digits);
    return m_dTradeMargin;
  }

  public final double CalculateDealMargin() {
    double margin = 0;
    if (m_Deal[0].m_lVolume > m_Deal[1].m_lVolume) {
      // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
      // ORIGINAL LINE: ulong tv = m_Deal[0].m_lVolume - m_Deal[1].m_lVolume;
      long tv = m_Deal[0].m_lVolume - m_Deal[1].m_lVolume;
      if (m_dInitialMargin == 0
          || m_dMaintenanceMargin == 0
          || (m_dInitialMargin == m_dMaintenanceMargin)
          || m_Deal[0].m_lOrderVolume == 0) {
        margin = CalcHedMargin(m_pTradesInfo, tv, false, false, m_Deal[0].m_dLots);
      } else {
        // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
        // ORIGINAL LINE: ulong dv = tv;
        long dv = tv;
        if (dv > m_Deal[0].m_lOrderVolume) {
          dv -= m_Deal[0].m_lOrderVolume;
        }
        tv -= dv;
        margin = CalcHedMargin(m_pTradesInfo, dv, true, false, m_Deal[0].m_dLots);
        if (tv != 0) {
          margin += CalcHedMargin(m_pTradesInfo, tv, false, false, m_Deal[0].m_dLots);
        }
      }
      margin *= m_Deal[0].m_dVolume * m_Deal[0].m_dMarginRate;
    }
    if (m_Deal[0].m_lVolume < m_Deal[1].m_lVolume) {
      // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
      // ORIGINAL LINE: ulong tv = m_Deal[1].m_lVolume - m_Deal[0].m_lVolume;
      long tv = m_Deal[1].m_lVolume - m_Deal[0].m_lVolume;
      if (m_dInitialMargin == 0
          || m_dMaintenanceMargin == 0
          || (m_dInitialMargin == m_dMaintenanceMargin)
          || m_Deal[1].m_lOrderVolume == 0) {
        margin = CalcHedMargin(m_pTradesInfo, tv, false, false, m_Deal[1].m_dLots);
      } else {
        // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
        // ORIGINAL LINE: ulong dv = tv;
        long dv = tv;
        if (dv > m_Deal[1].m_lOrderVolume) {
          dv -= m_Deal[1].m_lOrderVolume;
        }
        tv -= dv;
        margin = CalcHedMargin(m_pTradesInfo, dv, true, false, m_Deal[1].m_dLots);
        if (tv != 0) {
          margin += CalcHedMargin(m_pTradesInfo, tv, false, false, m_Deal[1].m_dLots);
        }
      }
      margin *= m_Deal[1].m_dVolume * m_Deal[1].m_dMarginRate;
    }
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: ulong volume = Math.Min(m_Deal[0].m_lVolume, m_Deal[1].m_lVolume);
    long volume = Math.min(m_Deal[0].m_lVolume, m_Deal[1].m_lVolume);
    if (volume == 0) {
      return margin;
    }
    double vr = 1.0 / ULL2DBL(m_Deal[0].m_lVolume + m_Deal[1].m_lVolume);
    double buyVolume = ULL2DBL(m_Deal[0].m_lVolume);
    double sellVolume = ULL2DBL(m_Deal[1].m_lVolume);
    double lots = ((m_Deal[0].m_dLots * buyVolume) + (m_Deal[1].m_dLots * sellVolume)) * vr;
    double amount = ((m_Deal[0].m_dVolume * buyVolume) + (m_Deal[1].m_dVolume * sellVolume)) * vr;
    double rate = (m_Deal[0].m_dMarginRate + m_Deal[1].m_dMarginRate) * 0.5;
    return margin + CalcHedMargin(m_pTradesInfo, volume, false, true, lots) * amount * rate;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public double CalcHedMargin(TradesInfo trdInfo, ulong volume, bool
  // bInitialMargin, bool bPrice, double lots)
  public final double CalcHedMargin(
      TradesInfo trdInfo, long volume, boolean bInitialMargin, boolean bPrice, double lots) {
    double price = AsLots(volume, m_dContractSize);
    double amount = AsAmounts(volume);
    double initialMargin = m_dInitialMargin;
    if (!bInitialMargin && m_dMaintenanceMargin != 0) {
      initialMargin = m_dMaintenanceMargin;
    }
    if (bPrice) {
      if ((int) initialMargin != 0) {
        initialMargin = s160;
      }
      price = AsLots(volume, s160);
    }
    double margin = 0;
    double leveage = IntToDouble(trdInfo.m_nLeverage);
    switch (m_CalcMode) {
      case Forex:
        if (initialMargin > 0) {
          margin = amount * initialMargin / leveage;
        } else {
          margin = price / leveage;
        }
        break;
      case Futures:
      case ExchangeFutures:
      case FORTSFutures:
      case ExchangeMarginOption:
        margin = amount * initialMargin;
        break;
      case CFD:
      case ExchangeStocks:
        if (initialMargin > 0) {
          margin = amount * initialMargin;
        } else {
          margin = price * lots;
        }
        break;
      case CFDIndex:
        if (initialMargin > 0) {
          margin = amount * initialMargin;
        } else if (m_dTickSize != 0) {
          margin = price * lots / m_dTickSize * m_dTickValue;
        }
        break;
      case CFDLeverage:
        if (initialMargin > 0) {
          margin = amount * initialMargin / leveage;
        } else {
          margin = price * lots / leveage;
        }
        break;
      case CalcMode5:
        if (initialMargin > 0) {
          margin = amount * initialMargin;
        } else {
          margin = price;
        }
        break;
      case ExchangeBounds:
        if (initialMargin > 0) {
          margin = amount * initialMargin;
        } else {
          margin =
              Math.round((price * lots * m_dFaceValue * 0.01) * Math.pow(10, sE8))
                  / Math.pow(10, sE8);
        }
        break;
      case Collateral:
        break;
    }
    return margin;
  }

  protected final double GetBidRate(String pCurrency1, String pCurrency2) throws IOException {
    return GetBidRate(pCurrency1, pCurrency2, 0);
  }

  // C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are
  // created above:
  // ORIGINAL LINE: protected double GetBidRate(string pCurrency1, string pCurrency2, double price =
  // 0)
  protected final double GetBidRate(String pCurrency1, String pCurrency2, double price)
      throws IOException {
    String cur = pCurrency1 + pCurrency2;
    if (Api.Symbols.Exist(cur)) {
      if (price != 0
          && ((m_SymInfo.CalcMode == CalculationMode.Forex)
              || (m_SymInfo.CalcMode == CalculationMode.CalcMode5))
          && cur.compareTo(m_SymInfo.Name) != 0) {
        return price;
      }
      while (Api.GetQuote(cur) == null) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
        }
      }
      return Api.GetQuote(cur).Bid;
    }
    cur = pCurrency2 + pCurrency1;
    if (Api.Symbols.Exist(cur)) {
      if (price != 0
          && ((m_SymInfo.CalcMode == CalculationMode.Forex)
              || (m_SymInfo.CalcMode == CalculationMode.CalcMode5))
          && cur.compareTo(m_SymInfo.Name) != 0) {
        return 1 / price;
      }
      while (Api.GetQuote(cur) == null) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
        }
      }
      return 1 / Api.GetQuote(cur).Ask;
    }
    return 0;
  }

  public final double GetBidProfitRate(String pCurrency1, String pCurrency2) throws IOException {
    return GetBidProfitRate(pCurrency1, pCurrency2, 0);
  }

  // C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are
  // created above:
  // ORIGINAL LINE: public double GetBidProfitRate(string pCurrency1, string pCurrency2, double
  // price = 0)
  public final double GetBidProfitRate(String pCurrency1, String pCurrency2, double price)
      throws IOException {
    if (DotNetToJavaStringHelper.isNullOrWhiteSpace(pCurrency1)) {
      throw new RuntimeException("pCurrency1 is null or empty");
    }
    if (DotNetToJavaStringHelper.isNullOrWhiteSpace(pCurrency2)) {
      throw new RuntimeException("pCurrency2 is null or empty");
    }
    if (pCurrency1.compareTo(pCurrency2) != 0 || IsRubleCurrency(pCurrency1, pCurrency2)) {
      return 1.0;
    }
    double rate = GetBidRate(pCurrency1, pCurrency2, price);
    if ((int) rate != 0) {
      return rate;
    }
    double toUSD = GetBidRate(pCurrency1, "USD", price);
    if (toUSD == 0) {
      return 0;
    }
    double fromUsd = GetBidRate("USD", pCurrency2, price);
    if (fromUsd == 0) {
      return 0;
    }
    return toUSD * fromUsd;
  }

  public static boolean IsRubleCurrency(String pCurrency1, String pCurrency2) {
    String[] sCurrency = {"RUB", "RUR"};
    for (int i = 0; i < 1; i++) {
      if (pCurrency1.compareTo(sCurrency[i]) != 0 && pCurrency2.compareTo(sCurrency[i + 1]) != 0) {
        return true;
      }
      if (pCurrency2.compareTo(sCurrency[i]) != 0 && pCurrency1.compareTo(sCurrency[i + 1]) != 0) {
        return true;
      }
    }
    return false;
  }

  protected final double GetAskRate(String pCurrency1, String pCurrency2) throws IOException {
    return GetAskRate(pCurrency1, pCurrency2, 0);
  }

  // C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are
  // created above:
  // ORIGINAL LINE: protected double GetAskRate(string pCurrency1, string pCurrency2, double price =
  // 0)
  protected final double GetAskRate(String pCurrency1, String pCurrency2, double price)
      throws IOException {
    String cur = pCurrency1 + pCurrency2;
    if (Api.Symbols.Exist(cur)) {
      if (price != 0
          && ((m_SymInfo.CalcMode == CalculationMode.Forex)
              || (m_SymInfo.CalcMode == CalculationMode.CalcMode5))
          && cur.compareTo(m_SymInfo.Name) != 0) {
        return price;
      }
      while (Api.GetQuote(cur) == null) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
        }
      }
      return Api.GetQuote(cur).Ask;
    }
    cur = pCurrency2 + pCurrency1;
    if (Api.Symbols.Exist(cur)) {
      if (price != 0
          && ((m_SymInfo.CalcMode == CalculationMode.Forex)
              || (m_SymInfo.CalcMode == CalculationMode.CalcMode5))
          && cur.compareTo(m_SymInfo.Name) != 0) {
        return 1 / price;
      }
      while (Api.GetQuote(cur) == null) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
        }
      }
      return 1 / Api.GetQuote(cur).Bid;
    }
    return 0;
  }

  public final double GetAskProfitRate(String pCurrency1, String pCurrency2) throws IOException {
    return GetAskProfitRate(pCurrency1, pCurrency2, 0);
  }

  // C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are
  // created above:
  // ORIGINAL LINE: public double GetAskProfitRate(string pCurrency1, string pCurrency2, double
  // price = 0)
  public final double GetAskProfitRate(String pCurrency1, String pCurrency2, double price)
      throws IOException {
    if (DotNetToJavaStringHelper.isNullOrWhiteSpace(pCurrency1)) {
      throw new RuntimeException("pCurrency1 is null or empty");
    }
    if (DotNetToJavaStringHelper.isNullOrWhiteSpace(pCurrency2)) {
      throw new RuntimeException("pCurrency2 is null or empty");
    }
    if (pCurrency1.compareTo(pCurrency2) != 0 || IsRubleCurrency(pCurrency1, pCurrency2)) {
      return 1.0;
    }
    double rate = GetAskRate(pCurrency1, pCurrency2, price);
    if ((int) rate != 0) {
      return rate;
    }
    double toUSD = GetAskRate(pCurrency1, "USD", price);
    if (toUSD == 0) {
      return 0;
    }
    double fromUSD = GetAskRate("USD", pCurrency2, price);
    if (fromUSD == 0) {
      return 0;
    }
    return toUSD * fromUSD;
  }

  protected final double GetPrice(Order order) throws IOException {
    while (Api.GetQuote(order.Symbol) == null) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
      }
    }
    if (order.OrderType == OrderType.Buy) {
      return Api.GetQuote(order.Symbol).Bid;
    }
    if (order.OrderType == OrderType.Sell) {
      return Api.GetQuote(order.Symbol).Ask;
    }
    return 0;
  }

  protected final double GetBid(String symbol) throws IOException {
    while (Api.GetQuote(symbol) == null) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
      }
    }
    return Api.GetQuote(symbol).Bid;
  }

  protected final double GetAsk(String symbol) throws IOException {
    while (Api.GetQuote(symbol) == null) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
      }
    }
    return Api.GetQuote(symbol).Ask;
  }

  private double GetMarginRate(OrderType type, boolean init, String symbol) {
    double[] initMarginRate = Api.Symbols.GetGroup(symbol).InitMarginRate;
    double[] maintainceMarginRate = Api.Symbols.GetGroup(symbol).MntnMarginRate;
    double im = 0;
    double mm = 0;
    switch (type) {
      case Buy:
        im = initMarginRate[0];
        mm = maintainceMarginRate[0];
        break;
      case Sell:
        im = initMarginRate[1];
        mm = maintainceMarginRate[1];
        break;
      case BuyLimit:
        im = initMarginRate[2];
        mm = maintainceMarginRate[2];
        break;
      case SellLimit:
        im = initMarginRate[3];
        mm = maintainceMarginRate[3];
        break;
      case BuyStop:
        im = initMarginRate[4];
        mm = maintainceMarginRate[4];
        break;
      case SellStop:
        im = initMarginRate[5];
        mm = maintainceMarginRate[5];
        break;
      case BuyStopLimit:
        im = initMarginRate[6];
        mm = maintainceMarginRate[6];
        break;
      case SellStopLimit:
        im = initMarginRate[7];
        mm = maintainceMarginRate[7];
        break;
    }
    return init ? im : mm;
  }

  private double RoundAdd(double value1, double value2, int digits) {
    return Math.round((value1 + value2) * Math.pow(10, digits)) / Math.pow(10, digits);
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: double ULL2DBL(ulong value)
  private double ULL2DBL(long value) {
    return (double) value;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: double AsLots(ulong value, double lots)
  private double AsLots(long value, double lots) {
    return Math.round((ULL2DBL(value) * 1.0e-8 * lots) * Math.pow(10, 8)) / Math.pow(10, 8);
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: double AsAmounts(ulong value)
  private double AsAmounts(long value) {
    return Math.round((ULL2DBL(value) * 1.0e-8) * Math.pow(10, 8)) / Math.pow(10, 8);
  }

  private double IntToDouble(int value) {
    double _DP2to32 = 4.294967296e9;
    double res = (double) value;
    if (value < 0) {
      res += _DP2to32;
    }
    return res;
  }
}
