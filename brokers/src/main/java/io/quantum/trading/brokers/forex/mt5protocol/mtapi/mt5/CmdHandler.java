package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/// #define TRIAL

class CmdHandler {
  public Exception AccountLoaderException = null;
  private MT5API QuoteClient;
  private Logger Log;
  public Thread Thread;
  private static final AtomicLong THREAD_COUNTER = new AtomicLong(0);

  boolean Stop = false;
  private boolean Stoped = false;

  private java.time.LocalDateTime LastPing = java.time.LocalDateTime.MIN;

  public CmdHandler(MT5API quoteClient) {
    QuoteClient = quoteClient;
    Log = quoteClient.Log;
    LastPing = java.time.LocalDateTime.now();
  }

  public final void StartCmdHandler(Connection con, long user) {
    Thread =
        new Thread(
            new Runnable() {
              public void run() {
                Run(con);
              }
            });
    Thread.setName(String.format("MT5CmdHandler-%s-%s", user, THREAD_COUNTER.incrementAndGet()));
    Thread.start();
  }

  public final boolean getRunning() {
    return Thread.isAlive() && Stop == false;
  }

  private HashMap<Byte, ArrayList<Byte>> Packets = new HashMap<Byte, ArrayList<Byte>>();
  private final short PackCompress = 1;
  private final short PackComplete = 2;

  private void Run(Object obj) {
    Connection con = (Connection) obj;
    java.time.LocalDateTime pingTime1 = java.time.LocalDateTime.now().plusSeconds(10);
    java.time.LocalDateTime pingTime2 = java.time.LocalDateTime.now().plusSeconds(20);
    byte cmd = 0;
    try {
      while (!Stop && !QuoteClient.TimeoutDuringConnect) {
        io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.InBuf buf = con.RecievePacket();
        QuoteClient.TimeOfLastMessageFromServer = LocalDateTime.now();
        cmd = buf.Hdr.Type;
        if ((buf.Hdr.Flags & PackCompress) > 0) {
          con.Decompress(buf);
        }
        if (Packets.containsKey(cmd)) {
          if ((buf.Hdr.Flags & PackComplete) > 0) {
            ByteLists.addPrimitiveArrayToList(buf.ToBytes(), Packets.get(cmd));
            buf.SetBuf(ByteLists.toArray(Packets.get(cmd)));
            Packets.remove(cmd);
          } else {
            ByteLists.addPrimitiveArrayToList(buf.ToBytes(), Packets.get(cmd));
            continue;
          }
        } else {
          if ((buf.Hdr.Flags & PackComplete) > 0) {
          } else {
            Packets.put(cmd, new ArrayList<Byte>());
            ByteLists.addPrimitiveArrayToList(buf.ToBytes(), Packets.get(cmd));
            continue;
          }
        }
        switch (cmd) {
          case 0x65:
            Log.trace("Cmd TradeHistory");
            QuoteClient.OrderHistory.Parse(buf);
            break;
          case 0x66:
            Log.trace("Cmd QuoteHistory");
            (new QuoteHistory(QuoteClient)).Parse(buf);
            break;
          case 0xC:
            Log.trace("Cmd AccountInfo");
            (new AccountLoader(QuoteClient, this, con)).Parse(buf);
            break;
          case 0x32:
            Log.trace("ParseTicks");
            QuoteClient.Subscriber.Parse(buf);
            break;
          case 0x36:
            Log.trace("RecieveMail");
            break;
          case 0x37:
            Log.trace("ParseTrades");
            QuoteClient.Orders.ParseTrades(buf);
            break;
          case 0x33:
            Log.trace("ParseSubscribeSymbols");
            QuoteClient.Subscriber.ParseSymbolData(buf);
            break;
          case 0xA:
            Log.trace("Cmd Ping");
            break;
          case 0x6C:
            Log.trace("Cmd TradeResult");
            ParseResult(buf);
            break;
          default:
            Log.trace("Unknown cmd = " + String.format("%X", cmd & 0xFF));
            break;
        }
        if (Duration.between(LastPing, LocalDateTime.now()).getSeconds() > 10) {
          QuoteClient.Connection.SendPacket((byte) 0xA, new OutBuf());
          LastPing = java.time.LocalDateTime.now();
        }
      }
      Stop = true;
      con.Disconnect();
      QuoteClient.OnDisconnect(null);
    } catch (Exception ex) {
      Log.exception(ex);
      Stop = true;
      QuoteClient.OnDisconnect(ex);
      try {
        con.Close();
      } catch (Exception exc) {
      }
    }
  }

  public final void StopCmdHandler() {
    Stop = true;
  }

  private void ParseResult(InBuf buf) {
    Msg status = Msg.forValue(buf.Int());
  }

  public void stop() {
    Stop = true;
  }
}
