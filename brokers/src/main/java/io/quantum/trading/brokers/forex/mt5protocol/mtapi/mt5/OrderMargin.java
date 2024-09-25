package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.IOException;

public class OrderMargin {
  public class TradesInfo // sizeof 0x328 d
   {
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: public ulong m_nLogin;
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

  public OrderMargin(MT5API api) {
    Api = api;
  }

  protected final double GetBidRate(String pCurrency1, String pCurrency2) throws IOException {
    String cur = pCurrency1 + pCurrency2;
    if (Api.Symbols.Exist(cur)) {
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
    if (DotNetToJavaStringHelper.isNullOrWhiteSpace(pCurrency1)) {
      throw new RuntimeException("pCurrency1 is null or empty");
    }
    if (DotNetToJavaStringHelper.isNullOrWhiteSpace(pCurrency2)) {
      throw new RuntimeException("pCurrency2 is null or empty");
    }
    if (pCurrency1.compareTo(pCurrency2) != 0 || IsRubleCurrency(pCurrency1, pCurrency2)) {
      return 1.0;
    }
    double rate = GetBidRate(pCurrency1, pCurrency2);
    if ((int) rate != 0) {
      return rate;
    }
    double toUSD = GetBidRate(pCurrency1, "USD");
    if (toUSD == 0) {
      return 0;
    }
    double fromUsd = GetBidRate("USD", pCurrency2);
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
    String cur = pCurrency1 + pCurrency2;
    if (Api.Symbols.Exist(cur)) {
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
    if (DotNetToJavaStringHelper.isNullOrWhiteSpace(pCurrency1)) {
      throw new RuntimeException("pCurrency1 is null or empty");
    }
    if (DotNetToJavaStringHelper.isNullOrWhiteSpace(pCurrency2)) {
      throw new RuntimeException("pCurrency2 is null or empty");
    }
    if (pCurrency1.compareTo(pCurrency2) != 0 || IsRubleCurrency(pCurrency1, pCurrency2)) {
      return 1.0;
    }
    double rate = GetAskRate(pCurrency1, pCurrency2);
    if ((int) rate != 0) {
      return rate;
    }
    double toUSD = GetAskRate(pCurrency1, "USD");
    if (toUSD == 0) {
      return 0;
    }
    double fromUSD = GetAskRate("USD", pCurrency2);
    if (fromUSD == 0) {
      return 0;
    }
    return toUSD * fromUSD;
  }

  private double RoundAdd(double value1, double value2, int digits) {
    return Math.round((value1 + value2) * Math.pow(10, digits)) / Math.pow(10, digits);
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

  private double m_dContractSize;
  private double m_dMaintenanceMargin;
  private double m_dInitialMargin;

  private double IntToDouble(int value) {
    double _DP2to32 = 4.294967296e9;
    double res = (double) value;
    if (value < 0) {
      res += _DP2to32;
    }
    return res;
  }

  private double m_dTickSize;
  private double m_dTickValue;
  private double m_dSettlementPrice;
  private double m_dUpperLimit;
  private double m_dLowerLimit;
  private double s1F0;
  private double m_dFaceValue;
  private int sE8;

  public final double CalcDefMargin(
      int nLeverage,
      OrderType type,
      boolean bInitialMargin,
      boolean bPrice,
      double lots,
      double price,
      SymbolInfo sym) {
    double initialMargin = m_dInitialMargin;
    if (!bInitialMargin && m_dMaintenanceMargin != 0) {
      initialMargin = m_dMaintenanceMargin;
    }
    double leverage = IntToDouble(nLeverage);
    double margin = 0;
    switch (sym.CalcMode) {
      case Forex:
        if (initialMargin > 0) {
          margin = (lots * initialMargin) / (m_dContractSize * leverage);
        } else {
          margin = lots / leverage;
        }
        break;
      case Futures:
      case ExchangeFutures:
      case ExchangeMarginOption:
        margin = lots * initialMargin / m_dContractSize;
        break;
      case CFD:
      case ExchangeStocks:
        if (initialMargin > 0) {
          margin = lots * initialMargin / m_dContractSize;
        } else {
          margin = lots * price;
        }
        break;
      case CFDIndex:
        if (initialMargin > 0) {
          margin = lots * initialMargin / m_dContractSize;
        } else if (m_dTickSize != 0) {
          margin = lots * price / m_dTickSize * m_dTickValue;
        }
        break;
      case CFDLeverage:
        if (initialMargin > 0) {
          margin = (lots * initialMargin) / (m_dContractSize * leverage);
        } else {
          margin = lots * price / leverage;
        }
        break;
      case CalcMode5:
        if (initialMargin > 0) {
          margin = lots * initialMargin / m_dContractSize;
        } else {
          margin = lots;
        }
        break;
      case FORTSFutures:
        if (m_dTickSize > 0) {
          double rate = (m_dTickValue / m_dTickSize) * (s1F0 * 0.01 + 1.0);
          switch (type) {
            case Buy:
              if (bPrice) {
                margin = m_dInitialMargin + (price - m_dSettlementPrice) * rate;
              } else {
                margin = m_dInitialMargin + (m_dUpperLimit - m_dSettlementPrice) * rate;
              }
              margin *= lots / m_dContractSize;
              break;
            case Sell:
              if (bPrice) {
                margin = m_dMaintenanceMargin + (m_dSettlementPrice - price) * rate;
              } else {
                margin = m_dMaintenanceMargin + (m_dSettlementPrice - m_dLowerLimit) * rate;
              }
              margin *= lots / m_dContractSize;
              break;
            case BuyLimit:
            case BuyStopLimit:
              margin = m_dInitialMargin + (price - m_dSettlementPrice) * rate;
              margin *= lots / m_dContractSize;
              break;
            case SellLimit:
            case SellStopLimit:
              margin = m_dMaintenanceMargin + (m_dSettlementPrice - price) * rate;
              margin *= lots / m_dContractSize;
              break;
            case BuyStop:
              margin = m_dInitialMargin + (m_dUpperLimit - m_dSettlementPrice) * rate;
              margin *= lots / m_dContractSize;
              break;
            case SellStop:
              margin = m_dInitialMargin + (m_dSettlementPrice - m_dLowerLimit) * rate;
              margin *= lots / m_dContractSize;
              break;
            default:
              margin = lots * initialMargin / m_dContractSize;
              break;
          }
        }
        break;
      case ExchangeBounds:
        if (initialMargin > 0) {
          margin = lots * initialMargin / m_dContractSize;
        } else {
          margin =
              Math.round((lots * price * m_dFaceValue * 0.01) * Math.pow(10, sE8))
                  / Math.pow(10, sE8);
        }
        break;
      case Collateral:
        break;
    }
    return margin;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: private ulong m_lVolume;
  private long m_lVolume; // 218
  private double m_dLots; // 220
  private double m_dOpenPrice; // 228
  private double m_dPrice; // 230
  private double m_dMargin; // 238
  private double m_dBuyMargin; // 240
  private double m_dSellMargin; // 248
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: private ulong m_lBuyVolume;
  private long m_lBuyVolume; // 250
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: private ulong m_lSellVolume;
  private long m_lSellVolume; // 258
  private boolean m_bOrder; // 260
  private double m_dTradeMargin; // 268
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: private ulong m_lTradeVolume;
  private long m_lTradeVolume; // 270
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: private ulong m_lSpreadVolume;
  private long m_lSpreadVolume; // 278
  private OrderType m_TradeType = OrderType.values()[0]; // 280
  private double m_dMarginRate; // 288
  private double s1E8;

  public final double GetTradeMargin(SymbolInfo sym, Order order, TradesInfo trdInfo)
      throws IOException {
    double buyMargin = m_dBuyMargin;
    double sellMargin = m_dSellMargin;
    String m_sProfitCurrency = Api.Symbols.Base.Currency;
    OrderType m_Type = order.OrderType;

    m_dTradeMargin = 0;
    if (sym.CalcMode == CalculationMode.Collateral) {
      m_lTradeVolume = 0;
      m_TradeType = OrderType.Buy;
      m_dMarginRate = 0;
      if (order.OrderType == OrderType.Buy) {
        double profitRate = GetAskProfitRate(m_sProfitCurrency, sym.Name);
        double price = m_dPrice;
        if (price == 0) {
          price = GetPrice(order);
        }
        double volume =
            Math.round((m_dLots * price) * Math.pow(10, sym.Digits)) / Math.pow(10, sym.Digits);
        buyMargin =
            Math.round((volume * profitRate * s1E8) * Math.pow(10, sym.Digits))
                / Math.pow(10, sym.Digits);
        trdInfo.m_dAssets = RoundAdd(trdInfo.m_dAssets, buyMargin, sym.Digits);
        trdInfo.m_dCollateral = RoundAdd(trdInfo.m_dCollateral, buyMargin, sym.Digits);
      }
      if (order.OrderType == OrderType.Sell) {
        double profitRate = GetBidProfitRate(m_sProfitCurrency, sym.Name);
        double price = m_dPrice;
        if (price == 0) {
          price = GetPrice(order);
        }
        double volume =
            Math.round((m_dLots * price) * Math.pow(10, sym.Digits)) / Math.pow(10, sym.Digits);
        sellMargin =
            -Math.round((volume * profitRate) * Math.pow(10, sym.Digits))
                / Math.pow(10, sym.Digits);
        trdInfo.m_dLiabilities = RoundAdd(trdInfo.m_dLiabilities, sellMargin, sym.Digits);
        trdInfo.m_dCollateral = RoundAdd(trdInfo.m_dCollateral, sellMargin, sym.Digits);
      }
      return m_dTradeMargin;
    }

    if (m_Type == OrderType.Buy) {
      buyMargin += m_dMargin;
      if (sym.CalcMode == CalculationMode.FORTSFutures) {
        sellMargin -=
            Math.round(
                    (CalcDefMargin(
                            Api.Account.Leverage,
                            OrderType.Sell,
                            false,
                            true,
                            m_dLots,
                            m_dOpenPrice,
                            sym))
                        * Math.pow(10, sym.Digits))
                / Math.pow(10, sym.Digits);
      } else if (m_dSellMargin != 0 && m_lSellVolume != 0) {
        sellMargin -=
            Math.round(
                    (m_dSellMargin / ULL2DBL(m_lSellVolume) * ULL2DBL(m_lVolume))
                        * Math.pow(10, trdInfo.m_nDigits))
                / Math.pow(10, trdInfo.m_nDigits);
      }
    }
    if (m_Type == OrderType.Sell) {
      sellMargin += m_dMargin;
      if (sym.CalcMode == CalculationMode.FORTSFutures) {
        buyMargin -=
            Math.round(
                    (CalcDefMargin(
                            Api.Account.Leverage,
                            OrderType.Buy,
                            false,
                            true,
                            m_dLots,
                            m_dOpenPrice,
                            sym))
                        * Math.pow(10, sym.Digits))
                / Math.pow(10, sym.Digits);
      } else if (m_dBuyMargin != 0 && m_lBuyVolume != 0) {
        buyMargin -=
            Math.round(
                    (m_dBuyMargin / ULL2DBL(m_lBuyVolume) * ULL2DBL(m_lVolume))
                        * Math.pow(10, trdInfo.m_nDigits))
                / Math.pow(10, trdInfo.m_nDigits);
      }
    }

    double margin = Math.max(buyMargin, sellMargin);
    m_dTradeMargin = margin;
    m_lTradeVolume = 0;
    m_dMarginRate = 0;
    m_TradeType = OrderType.Buy;
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: ulong buyVolume = m_lBuyVolume;
    long buyVolume = m_lBuyVolume;
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: ulong sellVolume = m_lSellVolume;
    long sellVolume = m_lSellVolume;
    if (m_lVolume != 0) {
      if (m_Type == OrderType.Buy) {
        buyVolume += m_lVolume;
      }
      if (m_Type == OrderType.Sell) {
        sellVolume += m_lVolume;
      }
    }
    if (buyVolume > sellVolume) {
      m_TradeType = OrderType.Buy;
      m_lTradeVolume = buyVolume;
    } else if (buyVolume < sellVolume) {
      m_TradeType = OrderType.Sell;
      m_lTradeVolume = sellVolume;
    } else {
      m_TradeType = m_Type;
      m_lTradeVolume = buyVolume;
    }
    if (m_lTradeVolume != 0) {
      m_dMarginRate = margin / ULL2DBL(m_lTradeVolume);
    }
    trdInfo.m_dMargin = RoundAdd(trdInfo.m_dMargin, margin, sym.Digits);
    return m_dTradeMargin;
  }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: double ULL2DBL(ulong value)
  private double ULL2DBL(long value) {
    return (double) value;
  }
}
