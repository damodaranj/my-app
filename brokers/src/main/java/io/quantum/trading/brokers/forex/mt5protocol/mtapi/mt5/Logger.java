package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
// #if TradingAPI
// #else

import java.io.PrintWriter;
import java.io.StringWriter;

/** Log output. */
public class Logger {

  /** New message event. */
  public static Event<OnMsgHandler> OnMsg = new Event<OnMsgHandler>();

  /** Enable output debug info in the exception messages. */
  public static boolean DebugInfo = false;

  private Object Parent;

  /**
   * Initialize new Logger.
   *
   * @param parent Parent object
   */
  public Logger(Object parent) {
    Parent = parent;
  }

  /**
   * Trace message.
   *
   * @param msg Message
   */
  public final void trace(String msg) {
    if (OnMsg != null) {
      // onMsg(new object[] { msg, MsgType.Trace });
      ThreadPool.QueueUserWorkItem(() -> onMsg(new Object[] {msg, MsgType.Trace}));
    }
  }

  private void onMsg(Object obj) {
    try {
      Object[] args = (Object[]) obj;
      for (OnMsgHandler listener : OnMsg.listeners()) {
        listener.invoke(Parent, (String) args[0], (MsgType) args[1]);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }

  /**
   * Debug message.
   *
   * @param msg Message
   */
  public final void debug(String msg) {
    if (OnMsg != null) {
      ThreadPool.QueueUserWorkItem(() -> onMsg(new Object[] {msg, MsgType.Debug}));
    }
  }

  /**
   * Information message.
   *
   * @param msg Message
   */
  public final void info(String msg) {
    if (OnMsg != null) {
      ThreadPool.QueueUserWorkItem(() -> onMsg(new Object[] {msg, MsgType.Info}));
    }
  }

  /**
   * Warning message.
   *
   * @param msg Message
   */
  public final void warn(String msg) {
    if (OnMsg != null) {
      ThreadPool.QueueUserWorkItem(() -> onMsg(new Object[] {msg, MsgType.Warn}));
    }
  }

  /**
   * Error message.
   *
   * @param msg Message
   */
  public final void error(String msg) {
    if (OnMsg != null) {
      ThreadPool.QueueUserWorkItem(() -> onMsg(new Object[] {msg, MsgType.Error}));
    }
  }

  /**
   * Exception message.
   *
   * @param ex Exception
   */
  public final void exception(Exception ex) {
    if (OnMsg != null) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      ex.printStackTrace(pw);
      String sStackTrace = sw.toString();
      ThreadPool.QueueUserWorkItem(
          () ->
              onMsg(
                  new Object[] {
                    ex.getMessage() + System.lineSeparator() + sStackTrace, MsgType.Exception
                  }));
    }
  }
}
// #endif
