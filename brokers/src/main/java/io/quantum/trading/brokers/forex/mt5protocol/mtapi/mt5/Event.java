package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

public final class Event<T> {
  public java.util.List<T> listeners = new java.util.ArrayList<T>();

  public void addListener(T unnamedEventHandlerMethod) {
    listeners.add(unnamedEventHandlerMethod);
  }

  public java.util.List<T> listeners() {
    java.util.List<T> allListeners = new java.util.ArrayList<T>();
    allListeners.addAll(listeners);
    return allListeners;
  }

  public void Invoke(MT5API api, ConnectEventArgs args) {
    Runnable func =
        new Runnable() {
          @Override
          public void run() {
            try {
              for (Object obj : listeners) {
                OnConnectProgress event = (OnConnectProgress) obj;
                event.invoke(api, args);
              }
            } catch (RuntimeException ex) {
              api.Log.exception(ex);
            }
          }
        };
    processEvent(api, func);
  }

  public void Invoke(MT5API api, Quote args) {
    Runnable func =
        new Runnable() {
          @Override
          public void run() {
            try {
              for (Object obj : listeners) {
                SymbolInfo info = api.Symbols.GetInfo(args.Symbol);
                args.Spread = (int) Math.round((args.Ask - args.Bid) / info.Points);
                OnQuote event = (OnQuote) obj;
                event.invoke(api, args);
              }
            } catch (RuntimeException ex) {
              api.Log.exception(ex);
            }
          }
        };
    processEvent(api, func);
  }

  public void Invoke(MT5API api, OrderHistoryEventArgs args) {
    Runnable func =
        new Runnable() {
          @Override
          public void run() {
            try {
              for (Object obj : listeners) {
                OnTradeHistory event = (OnTradeHistory) obj;
                event.invoke(api, args);
              }
            } catch (Exception ex) {
              api.Log.exception(ex);
            }
          }
        };
    processEvent(api, func);
  }

  public void Invoke(MT5API api, OrderProgress[] progr) {
    Runnable func =
        new Runnable() {
          @Override
          public void run() {
            try {
              for (OrderProgress item : progr) {
                for (Object obj : listeners) {
                  OnOrderProgress event = (OnOrderProgress) obj;
                  event.invoke(api, item);
                }
              }
            } catch (RuntimeException ex) {
              api.Log.exception(ex);
            }
          }
        };
    processEvent(api, func);
  }

  void processEvent(MT5API api, Runnable func) {
    if (api.ProcessEventsIn == ProcessEvents.ThreadPool) ThreadPool.QueueUserWorkItem(func);
    else if (api.ProcessEventsIn == ProcessEvents.NewThread) new Thread(func).start();
    else func.run();
  }

  public void Invoke(MT5API api, OrderUpdate[] update) {
    Runnable func =
        new Runnable() {
          @Override
          public void run() {
            try {
              for (OrderUpdate item : update) {
                for (Object obj : listeners) {
                  if (item.Type != UpdateType.Cancelling
                      && item.Type != UpdateType.Started
                      && item.Type != UpdateType.Filled
                      && item.Type != UpdateType.Unknown) {
                    OnOrderUpdate event = (OnOrderUpdate) obj;
                    event.invoke(api, item);
                  }
                }
              }
            } catch (RuntimeException ex) {
              api.Log.exception(ex);
            }
          }
        };
    processEvent(api, func);
  }

  public void Invoke(MT5API api, QuoteHistoryEventArgs args) {
    Runnable func =
        new Runnable() {
          @Override
          public void run() {
            try {
              for (Object obj : listeners) {
                OnQuoteHistory event = (OnQuoteHistory) obj;
                event.invoke(api, args);
              }
            } catch (RuntimeException ex) {
              api.Log.exception(ex);
            }
          }
        };
    processEvent(api, func);
  }
}
