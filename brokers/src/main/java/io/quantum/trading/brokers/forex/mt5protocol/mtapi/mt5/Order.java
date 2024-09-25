package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

/** Pending, market or history order */
public class Order {
  public long Ticket;
  public double Profit;
  public double Swap;
  public double Commission;
  public double ClosePrice;
  public java.time.LocalDateTime CloseTime = java.time.LocalDateTime.MIN;
  public double CloseVolume;

  public double OpenPrice;
  public java.time.LocalDateTime OpenTime = java.time.LocalDateTime.MIN;
  public double Lots;
  public double ContractSize;
  public long ExpertId;
  public PlacedType PlacedType;
  public OrderType OrderType;
  public DealType DealType;
  public String Symbol;
  public String Comment;
  public OrderState State = OrderState.values()[0];
  public double StopLoss;
  public double TakeProfit;
  public int RequestId;
  public int Digits;
  public double ProfitRate;
  public DealInternal DealInternalIn;
  public DealInternal DealInternalOut;
  public OrderInternal OrderInternal;
  double CloseProfit;

  public Order() {}

  public final void Update(Order order) {
    Ticket = order.Ticket;
    Profit = order.Profit;
    Swap = order.Swap;
    Commission = order.Commission;
    ClosePrice = order.ClosePrice;
    CloseTime = order.CloseTime;
    CloseVolume = order.CloseVolume;
    OpenPrice = order.OpenPrice;
    OpenTime = order.OpenTime;
    Lots = order.Lots;
    ContractSize = order.ContractSize;
    ExpertId = order.ExpertId;
    PlacedType = order.PlacedType;
    OrderType = order.OrderType;
    DealType = order.DealType;
    Symbol = order.Symbol;
    Comment = order.Comment;
    State = order.State;
    StopLoss = order.StopLoss;
    TakeProfit = order.TakeProfit;
    if (order.RequestId != 0) {
      RequestId = order.RequestId;
    }
    Digits = order.Digits;
    ProfitRate = order.ProfitRate;
    DealInternalIn = order.DealInternalIn;
    DealInternalOut = order.DealInternalOut;
    OrderInternal = order.OrderInternal;
  }

  public Order(DealInternal[] deals, int requestId) {
    RequestId = requestId;
    // Console.WriteLine(deals.Length);
    for (DealInternal item : deals) {
      if (item.Direction == Direction.In) {
        DealInternalIn = item;
        OpenTime = ConvertTo.DateTimeMs(item.OpenTimeMs);
        OpenPrice = item.OpenPrice;
        Lots = item.Lots;
        ContractSize = item.ContractSize;
        PlacedType = item.PlacedType;
        Ticket = item.PositionTicket;
        DealType = item.Type;
        if (DealType == DealType.DealSell) OrderType = OrderType.Sell;
        else OrderType = OrderType.Buy;
        ExpertId = item.ExpertId;
        Symbol = item.Symbol;
        Commission = item.Commission;
        Swap = item.Swap;
        Comment = item.Comment;
        StopLoss = item.StopLoss;
        TakeProfit = item.TakeProfit;
        Digits = item.Digits;
        ProfitRate = item.ProfitRate;
      }
      if (item.Direction == Direction.Out) {
        DealInternalOut = item;
        CloseTime = ConvertTo.DateTimeMs(item.OpenTimeMs);
        ClosePrice = item.OpenPrice;
        CloseVolume += item.Lots;
        Commission += item.Commission;
        Profit += item.Profit;
        Swap += item.Swap;
        Digits = item.Digits;
        ProfitRate = item.ProfitRate;
      }
    }
  }

  public final void UpdateOnStop(DealInternal item, boolean unusual) {
    DealInternalOut = item;
    CloseTime = ConvertTo.DateTimeMs(item.OpenTimeMs);
    if (unusual) ClosePrice = item.Price;
    else ClosePrice = item.OpenPrice;
    ClosePrice = item.OpenPrice;
    CloseVolume = item.Lots;
    Commission = item.Commission;
    Profit = item.Profit;
    Swap = item.Swap;
    Digits = item.Digits;
    ProfitRate = item.ProfitRate;
  }

  public Order(DealInternal[] deals) {
    // Console.WriteLine(deals.Length);
    for (DealInternal item : deals) {
      if (item.Direction == Direction.In) {
        DealInternalIn = item;
        OpenTime = ConvertTo.DateTimeMs(item.OpenTimeMs);
        OpenPrice = item.OpenPrice;
        Lots = item.Lots;
        ContractSize = item.ContractSize;
        PlacedType = item.PlacedType;
        if (item.PositionTicket != 0) {
          Ticket = item.PositionTicket;
        }
        DealType = item.Type;
        if (DealType == DealType.DealSell) OrderType = OrderType.Sell;
        else OrderType = OrderType.Buy;
        ExpertId = item.ExpertId;
        Symbol = item.Symbol;
        Commission = item.Commission;
        Swap = item.Swap;
        Comment = item.Comment;
        StopLoss = item.StopLoss;
        TakeProfit = item.TakeProfit;
        Digits = item.Digits;
        ProfitRate = item.ProfitRate;
      }
      if (item.Direction == Direction.Out || item.Direction == Direction.OutBy) {
        DealInternalOut = item;
        CloseTime = ConvertTo.DateTimeMs(item.OpenTimeMs);
        ClosePrice = item.OpenPrice;
        CloseVolume = item.Lots;
        Commission += item.Commission;
        Profit += item.Profit;
        Swap += item.Swap;
        Digits = item.Digits;
        ProfitRate = item.ProfitRate;
        if (item.PositionTicket != 0) {
          Ticket = item.PositionTicket;
        }
      }
    }
  }

  void Update(DealInternal item) {
    if (item.Direction == Direction.In) {
      DealInternalIn = item;
      OpenTime = ConvertTo.DateTimeMs(item.OpenTimeMs);
      OpenPrice = item.OpenPrice;
      Lots = item.Lots;
      ContractSize = item.ContractSize;
      PlacedType = (PlacedType) item.PlacedType;
      Ticket = item.PositionTicket;
      DealType = item.Type;
      if (DealType == DealType.DealSell) OrderType = OrderType.Sell;
      ExpertId = item.ExpertId;
      Symbol = item.Symbol;
      Commission = item.Commission;
      Swap = item.Swap;
      Comment = item.Comment;
      StopLoss = item.StopLoss;
      TakeProfit = item.TakeProfit;
      Digits = item.Digits;
      ProfitRate = item.ProfitRate;
    } else if (item.Direction == Direction.Out) {
      DealInternalOut = item;
      CloseTime = ConvertTo.DateTimeMs(item.OpenTimeMs);
      ClosePrice = item.OpenPrice;
      CloseVolume = item.Lots;
      CloseProfit += item.Profit;
      Commission += item.Commission;
      Swap += item.Swap;
      Digits = item.Digits;
      ProfitRate = item.ProfitRate;
    }
  }

  public Order(DealInternal item) {
    DealInternalIn = item;
    OpenTime = ConvertTo.DateTimeMs(item.OpenTimeMs);
    OpenPrice = item.OpenPrice;
    Lots = item.Lots;
    ContractSize = item.ContractSize;
    PlacedType = item.PlacedType;
    Ticket = item.PositionTicket;
    if (Ticket == 0) {
      Ticket = item.PositionTicket;
    }
    DealType = item.Type;
    if (DealType == DealType.DealSell) OrderType = OrderType.Sell;
    else OrderType = OrderType.Buy;
    ExpertId = item.ExpertId;
    Symbol = item.Symbol;
    Commission = item.Commission;
    Swap = item.Swap;
    Comment = item.Comment;
    Profit = item.Profit;
    StopLoss = item.StopLoss;
    TakeProfit = item.TakeProfit;
    State = OrderState.Filled;
    Digits = item.Digits;
    ProfitRate = item.ProfitRate;
  }

  public Order(OrderInternal item) {
    OrderInternal = item;
    OpenTime = ConvertTo.DateTimeMs(item.OpenTimeMs);
    OpenPrice = item.OpenPrice;
    Lots = item.Lots;
    ContractSize = item.ContractSize;
    PlacedType = item.PlacedType;
    Ticket = item.TicketNumber;
    OrderType = item.Type;
    ExpertId = item.ExpertId;
    Symbol = item.Symbol;
    Comment = item.Comment;
    State = item.State;
    StopLoss = item.StopLoss;
    TakeProfit = item.TakeProfit;
    Digits = item.Digits;
    ProfitRate = item.ProfitRate;
  }

  public Order(
      OrderProgress progr, java.time.LocalDateTime serverTime) // deal executed - OrderSend market
      {
    OpenTime = serverTime;
    OpenPrice = progr.TradeResult.OpenPrice;
    Lots = (double) progr.TradeResult.Volume / 100000000;
    PlacedType = progr.TradeRequest.PlacedType;
    Ticket = progr.TradeResult.TicketNumber;
    OrderType = progr.TradeRequest.OrderType;
    if (progr.TradeRequest.OrderType.toString().startsWith("Buy")) {
      DealType = DealType.DealBuy;
    }
    if (progr.TradeRequest.OrderType.toString().startsWith("Sell")) {
      DealType = DealType.DealSell;
    }
    ExpertId = progr.TradeRequest.ExpertId;
    Symbol = progr.TradeRequest.Currency;
    // Commission =
    // Swap = item.Swap;
    Comment = progr.TradeResult.Comment;
    StopLoss = progr.TradeRequest.StopLoss;
    TakeProfit = progr.TradeRequest.TakeProfit;
    State = OrderState.Filled;
    RequestId = progr.TradeRequest.RequestId;
    Digits = progr.TradeRequest.Digits;
  }

  public Order(
      OrderProgress progr,
      java.time.LocalDateTime serverTime,
      Order order) // deal executed - OrderClose market
      {
    CloseTime = serverTime;
    ClosePrice = progr.TradeResult.OpenPrice;
    CloseVolume = (double) progr.TradeResult.Volume / 100000000;
    // PlacedType = (PlacedType)progr.TradeRequest.PlacedType;
    Ticket = progr.TradeResult.TicketNumber;
    // OrderType = progr.TradeRequest.OrderType;
    // if (progr.TradeRequest.OrderType.ToString().StartsWith("Buy"))
    //    DealType = DealType.DealBuy;
    // if (progr.TradeRequest.OrderType.ToString().StartsWith("Sell"))
    //    DealType = DealType.DealSell;
    // ExpertId = progr.TradeRequest.ExpertId;
    Symbol = progr.TradeRequest.Currency;
    // Commission =
    // Swap = item.Swap;
    Comment = progr.TradeResult.Comment;
    // StopLoss = progr.TradeRequest.StopLoss;
    // TakeProfit = progr.TradeRequest.TakeProfit;
    State = OrderState.Filled;
    RequestId = progr.TradeRequest.RequestId;
    Digits = progr.TradeRequest.Digits;
    if (order != null) {
      OrderInternal = order.OrderInternal;
      DealInternalIn = order.DealInternalIn;
      DealInternalOut = order.DealInternalOut;
      OrderType = order.OrderType;
      DealType = order.DealType;
      ExpertId = order.ExpertId;
      Commission = order.Commission;
      Swap = order.Swap;
      OpenTime = order.OpenTime;
      Lots = order.Lots;
      TakeProfit = order.TakeProfit;
      StopLoss = order.StopLoss;
      PlacedType = order.PlacedType;
      ProfitRate = order.ProfitRate;
    }
  }

  public Order Clone() {
    Order res = new Order();
    res.Update(this);
    return res;
  }
}
