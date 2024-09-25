package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.IOException;

class Profit {
  private MT5API Api;

  public Profit(MT5API api) {
    Api = api;
  }

  public final void Calculate(Quote quote) {
    for (Order item : Api.GetOpenedOrders()) {
      if (item.OrderType == OrderType.Buy || item.OrderType == OrderType.Sell) {
        if (DotNetToJavaStringHelper.stringsEqual(item.Symbol, quote.Symbol)) {
          try {
            double closeprice = item.OrderType == OrderType.Buy ? quote.Ask : quote.Bid;
            SymbolInfo sym = Api.Symbols.GetInfo(item.Symbol);
            DealInternal deal = item.DealInternalIn;
            CalculateProfit(
                sym,
                deal.Type == DealType.DealBuy,
                deal.Lots,
                deal.ContractSize,
                deal.OpenPrice,
                closeprice,
                deal);
          } catch (RuntimeException | IOException ex) {
            Api.Log.exception(ex);
          }
        }
      }
    }
  }

  private double AsSize(double value, double lots) {
    return Math.round((lots * value) * Math.pow(10, 8)) / Math.pow(10, 8);
  }

  private double AsLots(double value) {
    return Math.round(((double) value * 1.0e-8) * Math.pow(10, 8)) / Math.pow(10, 8);
  }

  public final void CalculateProfit(
      SymbolInfo sym,
      boolean buy,
      double volume,
      double lots,
      double openPrice,
      double price,
      DealInternal deal)
      throws IOException {
    double profit;
    switch (sym.CalcMode) {
      case Forex:
      case CalcMode5:
        profit = AsSize(volume, lots);
        if (buy) {
          profit =
              Math.round((profit * price) * Math.pow(10, sym.Precision))
                      / Math.pow(10, sym.Precision)
                  - Math.round((profit * openPrice) * Math.pow(10, sym.Precision))
                      / Math.pow(10, sym.Precision);
        } else {
          profit =
              Math.round((profit * openPrice) * Math.pow(10, sym.Precision))
                      / Math.pow(10, sym.Precision)
                  - Math.round((profit * price) * Math.pow(10, sym.Precision))
                      / Math.pow(10, sym.Precision);
        }
        break;
      case Futures:
      case ExchangeFutures:
      case ExchangeOption:
      case ExchangeMarginOption:
        profit = AsLots(volume);
        if (buy) {
          profit *= price - openPrice;
        } else {
          profit *= openPrice - price;
        }
        profit *= sym.TickValue;
        if (sym.TickSize > 0) {
          profit /= sym.TickSize;
        }
        break;
      case FORTSFutures:
        {
          profit = AsLots(volume);
          double v1 = sym.TickValue * openPrice;
          double v2 = sym.TickValue * price;
          if (sym.TickSize > 0) {
            v1 /= sym.TickSize;
            v2 /= sym.TickSize;
          }
          v1 = Math.round(v1 * Math.pow(10, sym.Precision)) / Math.pow(10, sym.Precision);
          v2 = Math.round(v2 * Math.pow(10, sym.Precision)) / Math.pow(10, sym.Precision);
          if (buy) {
            profit *= v2 - v1;
          } else {
            profit *= v1 - v2;
          }
          break;
        }
      case ExchangeBounds:
        profit = AsSize(volume, lots);
        if (buy) {
          profit =
              Math.round((profit * price * sym.FaceValue / 100.0) * Math.pow(10, sym.Precision))
                      / Math.pow(10, sym.Precision)
                  - Math.round(
                          (profit * openPrice * sym.FaceValue / 100.0)
                              * Math.pow(10, sym.Precision))
                      / Math.pow(10, sym.Precision);
        } else {
          profit =
              Math.round((profit * openPrice * sym.FaceValue / 100.0) * Math.pow(10, sym.Precision))
                      / Math.pow(10, sym.Precision)
                  - Math.round(
                          (profit * price * sym.FaceValue / 100.0) * Math.pow(10, sym.Precision))
                      / Math.pow(10, sym.Precision);
        }
        break;
      case Collateral:
        profit = 0;
        break;
      default:
        profit = AsSize(volume, lots);
        if (buy) {
          profit *= price - openPrice;
        } else {
          profit *= openPrice - price;
        }
        break;
    }
    if ((profit > 1.0e11) || (profit < -1.0e11)) {
      profit = 0;
    }
    // if ((price < 0) || ((sym.CalcMode != CalculationMode.Collateral) && price > 0))
    //	profit = 0;
    double bidRate = 0, askRate = 0;
    RefObject<Double> tempRef_bidRate = new RefObject<Double>(bidRate);
    RefObject<Double> tempRef_askRate = new RefObject<Double>(askRate);
    GetAskBidProfitRate(
        sym.ProfitCurrency,
        Api.Symbols.Base.Currency,
        tempRef_bidRate,
        tempRef_askRate,
        sym,
        price);
    askRate = tempRef_askRate.argValue;
    bidRate = tempRef_bidRate.argValue;
    double rate;
    if (((sym.CalcMode == CalculationMode.Forex) || (sym.CalcMode == CalculationMode.CalcMode5))
        && (sym.s550 & 1) == 0) {
      rate = buy ? askRate : bidRate;
    } else {
      rate = (profit > 0) ? bidRate : askRate;
    }
    profit /= rate;
    deal.ProfitRate = rate;
    deal.Profit = profit;
    // return profit;
    // return Math.Round(profit, sym.Precision);
  }

  private void GetAskBidProfitRate(
      String cur1,
      String cur2,
      RefObject<Double> askpr,
      RefObject<Double> bidpr,
      SymbolInfo sym,
      double price)
      throws IOException {
    if (GetBidAskRate(cur1, cur2, askpr, bidpr, sym, price)) {
      return;
    }
    double toUSDAsk = 0;
    double toUSDBid = 0;
    double fromUSDAsk = 0;
    double fromUSDBid = 0;
    RefObject<Double> tempRef_toUSDAsk = new RefObject<Double>(toUSDAsk);
    RefObject<Double> tempRef_toUSDBid = new RefObject<Double>(toUSDBid);
    RefObject<Double> tempRef_fromUSDAsk = new RefObject<Double>(fromUSDAsk);
    RefObject<Double> tempRef_fromUSDBid = new RefObject<Double>(fromUSDBid);
    if (!GetBidAskRate(cur1, "USD", tempRef_toUSDAsk, tempRef_toUSDBid, sym, price)
        || !GetBidAskRate("USD", cur2, tempRef_fromUSDAsk, tempRef_fromUSDBid, sym, price)) {
      fromUSDBid = tempRef_fromUSDBid.argValue;
      fromUSDAsk = tempRef_fromUSDAsk.argValue;
      toUSDBid = tempRef_toUSDBid.argValue;
      toUSDAsk = tempRef_toUSDAsk.argValue;
      bidpr.argValue = 0D;
      askpr.argValue = 0D;
      return;
    } else {
      fromUSDBid = tempRef_fromUSDBid.argValue;
      fromUSDAsk = tempRef_fromUSDAsk.argValue;
      toUSDBid = tempRef_toUSDBid.argValue;
      toUSDAsk = tempRef_toUSDAsk.argValue;
    }
    askpr.argValue = toUSDAsk * fromUSDAsk;
    bidpr.argValue = toUSDBid * fromUSDBid;
  }

  private boolean GetBidAskRate(
      String cur1,
      String cur2,
      RefObject<Double> ask,
      RefObject<Double> bid,
      SymbolInfo sym,
      double price)
      throws IOException {
    String cur = Api.Symbols.ExistStartsWith(cur1 + cur2);
    if (cur != null) {
      if (price != 0
          && ((sym.CalcMode == CalculationMode.Forex)
              || (sym.CalcMode == CalculationMode.CalcMode5))
          && ((sym.s550 & 1) == 0)
          && DotNetToJavaStringHelper.stringsEqual(sym.Name, cur)) {
        bid.argValue = price;
        ask.argValue = price;
        return true;
      }
      Api.Subscribe(cur);
      while (Api.GetQuote(cur) == null) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
        }
      }
      bid.argValue = Api.GetQuote(cur).Bid;
      ask.argValue = Api.GetQuote(cur).Ask;
      return true;
    }
    cur = Api.Symbols.ExistStartsWith(cur2 + cur1);
    if (cur != null) {
      if (price != 0
          && ((sym.CalcMode == CalculationMode.Forex)
              || (sym.CalcMode == CalculationMode.CalcMode5))
          && ((sym.s550 & 1) == 0)
          && DotNetToJavaStringHelper.stringsEqual(sym.Name, cur)) {
        bid.argValue = 1.0 / price;
        ask.argValue = 1.0 / price;
        return true;
      }
      Api.Subscribe(cur);
      while (Api.GetQuote(cur) == null) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
        }
      }
      bid.argValue = Api.GetQuote(cur).Bid;
      ask.argValue = Api.GetQuote(cur).Ask;
      return true;
    }
    bid.argValue = 0D;
    ask.argValue = 0D;
    return false;
  }
}
/*
		double CalcProfit(SymBaseInfo symBase, SymbolInfo sym, OrderType type,
				double openPrice, double price, double volume, double lots, double volumeRate)
	{
		if (symBase.AccMethod == PosAccMethod.Netting)
		{
			switch (sym.CalcMode)
			{
				case CalculationMode.ExchangeStocks:
				case CalculationMode.ExchangeBounds:
					return CalcExchangeProfit(symBase, sym, type, price, volume, volumeRate);
				case CalculationMode.Collateral:
					return CalcCollateralProfit(symBase, sym, price, volume);
			}
		}
		else
		{
			switch (sym.CalcMode)
			{
				case CalculationMode.Forex:
				case CalculationMode.CalcMode5:
					CalcForexProfit(symBase, sym, openPrice, volume, lots, volumeRate);
					break;
				case CalculationMode.Collateral:
					return 0;
			}
		}
		return CalcBaseProfit(symBase, sym, type, openPrice, volume, volumeRate);
	}

	private double CalcBaseProfit(SymBaseInfo symBase, SymbolInfo sym, OrderType type, double openPrice, double volume, double volumeRate)
	{
		throw new NotImplementedException();
	}

	private double CalcCollateralProfit(SymBaseInfo symBase, SymbolInfo sym, double price, double volume)
	{
		throw new NotImplementedException();
	}

	private double CalcExchangeProfit(SymBaseInfo symBase, SymbolInfo sym, OrderType type, double price, double volume, double volumeRate)
	{
		throw new NotImplementedException();
	}

	struct ProfitStruct //sizeof 0x66 d
	{
		public string Currency; //0
		public double Lots; //40
		public int SymDigits; //48
		public int BaseDigits; //49
		public double Profit; //4A
		public double ProfitRate; //52
		public double Margin; //5A
		public int s62;
	}

	Dictionary<string, ProfitStruct> Profits = new Dictionary<string, ProfitStruct>();

	void CalcForexProfit(SymBaseInfo symBase, SymbolInfo sym, double price, double volume, double lots, double volumeRate)
	{
		var baseCurrency = sym.Currency;
		baseCurrency = baseCurrency.Substring(0, 3);
		ProfitStruct profit;
		if (Profits.ContainsKey(baseCurrency))
			profit = Profits[baseCurrency];
		else
		{
			profit = new ProfitStruct();
			profit.SymDigits = sym.Digits;
			profit.BaseDigits = symBase.Digits;
		}
		if (volume>0)
		{
			double vl = Math.Round(volume * lots, 8);
			double nlots = profit.Lots + vl;
			if (nlots > 0)
			{
				profit.ProfitRate = (profit.Lots * profit.ProfitRate + vl * volumeRate) / nlots;
				profit.Lots = nlots;
			}
			else
			{
				profit.ProfitRate = 0;
				profit.Lots = 0;
			}
		}
		profit.Profit = Math.Round(profit.ProfitRate * profit.Lots / Api.Account.Leverage, sym.Digits);
		var quoteCurrency = sym.Currency.Substring(3, 3);
		if (Profits.ContainsKey(quoteCurrency))
			profit = Profits[quoteCurrency];
		else
		{
			profit = new ProfitStruct();
			profit.SymDigits = sym.Digits;
			profit.BaseDigits = symBase.Digits;
		}
		if (volume > 0)
		{
			double vl = Math.Round(-(volume * lots * price), 8);
			double nlots = Math.Round(profit.Lots + vl, 8);
			if (nlots > 0)
			{
				profit.ProfitRate = (profit.Lots * profit.ProfitRate + vl * volumeRate / price) / nlots;
				profit.Lots = nlots;
			}
			else
			{
				profit.ProfitRate = 0;
				profit.Lots = 0;
			}
		}
		profit.Profit = Math.Round(profit.Lots * profit.ProfitRate / Api.Account.Leverage, sym.Digits);
	}

for (int i = 0; i < deals.Count; i++)
		{
			var deal = deals[i];
			SymbolInfo sym;
			try
			{
				sym = Api.Symbols.GetInfo(deal.Symbol);
			}
			catch (Exception ex)
			{
				continue;
			}
			//double dir;
			//if (deal.Type == OrderType.Buy)
			//	dir = 1.0;
			//else if (deal.Type == OrderType.Sell)
			//	dir = -1.0;
			//else
			//	continue;
			while (Api.GetQuote(deal.Symbol) == null)
				Thread.Sleep(1);
			double price = Api.GetQuote(deal.Symbol).Ask;
			if (deal.Type == OrderType.Buy)
				price = Api.GetQuote(deal.Symbol).Bid;
			//CalcProfit(symBase, sym, deal.Type, deal.OpenPrice, price,
			//	deal.Lots * dir, deal.ContractSize, deal.VolumeRate);
			CalculateProfit(sym, deal.Type == OrderType.Buy, deal.Lots, deal.ContractSize, deal.OpenPrice, price, deal);
		}
		//vProfitBase::UpdateProfit(symBase, trdInfo);

*/
