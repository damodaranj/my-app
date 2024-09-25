package io.quantum.trading.brokers.forex.mt5protocol; // import cn.hutool.core.util.StrUtil;

import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.MT5API;
import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.OrderType;
import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.SymbolInfo;
import io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.ThreadPool;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author hipil
 */
public class Copier5ApiTrader extends MT5API {

  public int i;

  public static ScheduledExecutorService scheduledExecutorService =
      Executors.newScheduledThreadPool(1000);

  ScheduleTask scheduleTask;
  String invalidVolumeCode = "INVALID_VOLUME";
  String marketCloseCode = "MARKET_CLOSED";
  String noMoneyCode = "NO_MONEY";
  String investorMode = "Investor mode";
  String eurusd = "EURUSD";

  public Copier5ApiTrader(long user, String password, String host, Integer port) {
    super(user, password, host, port);
  }

  public void connect2Broker() throws Exception {
    try {
      this.OnConnectProgress.addListener(
          (mt5Api, connectEventArgs) -> {
            if (connectEventArgs != null) {
              //                    System.out.println(mt5Api.User + "'s progress is " +
              // connectEventArgs.Progress);
            }
          });
      this.OnOrderUpdate.addListener(
          (mt5Api, orderUpdate) -> {
            if (orderUpdate != null) {
              System.out.println(mt5Api.User + " " + orderUpdate.Type);
              try {
                mt5Api.RequestDealHistory(
                    LocalDateTime.now().minus(1, ChronoUnit.MONTHS), LocalDateTime.now());
              } catch (IOException e) {
                e.printStackTrace();
              }
            } else {
              System.out.println("orderUpdate is null.");
            }
          });
      this.OnTradeHistory.addListener((mt5Api, orderHistoryEventArgs) -> {});
      this.Connect();
      // List<String> eurusds = Arrays.stream(this.Symbols.Infos).map(info ->
      // info.Name).filter(symbol -> StrUtil.containsAnyIgnoreCase(symbol,
      // "EURUSD")).sorted(Comparator.comparingInt(String::length)).collect(Collectors.toList());
      // eurusd = eurusds.get(0);
      for (SymbolInfo item : Symbols.Infos) {
        if (item.Name.contains("EURUSD") || item.Name.contains("eurusd")) {
          eurusd = item.Name;
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    } finally {
      // scheduledExecutorService.scheduleWithFixedDelay(new ScheduleTask(this), 30, 30,
      // TimeUnit.SECONDS);
      ThreadPool.QueueUserWorkItem(new ScheduleTask(this));
    }
  }

  private class ScheduleTask implements Runnable {
    Copier5ApiTrader copier5ApiTrader;

    public ScheduleTask(Copier5ApiTrader copier5ApiTrader) {
      this.copier5ApiTrader = copier5ApiTrader;
    }

    @Override
    public void run() {
      while (true) {
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        try {
          // copier5ApiTrader.ExecutionTimeout = 30000;
          copier5ApiTrader.OrderSend(eurusd, 0.0, 0.0, OrderType.Buy);
        } catch (Exception exception) {
          if (invalidVolumeCode.equalsIgnoreCase(exception.getMessage())) {
            //                    System.out.println(i + "  " + copier5ApiTrader.User +
            // invalidVolumeCode);
          } else if (marketCloseCode.equalsIgnoreCase(exception.getMessage())) {
            //                    System.out.println(i + "  " + copier5ApiTrader.User +
            // marketCloseCode);
          } else if (noMoneyCode.equalsIgnoreCase(exception.getMessage())) {
            //                    System.out.println(i + "  " + copier5ApiTrader.User +
            // noMoneyCode);
          } else if (exception.getMessage().contains(investorMode)) {
            //                    System.out.println(i + "  " + copier5ApiTrader.User +
            // investorMode);
          } else {
            exception.printStackTrace();
            try {
              copier5ApiTrader.Disconnect();
              copier5ApiTrader.Connect();
              // List<String> eurusds = Arrays.stream(copier5ApiTrader.Symbols.Infos).map(info ->
              // info.Name).filter(symbol -> StrUtil.containsAnyIgnoreCase(symbol,
              // "EURUSD")).sorted(Comparator.comparingInt(String::length)).collect(Collectors.toList());
              // eurusd = eurusds.get(0);
              for (SymbolInfo item : Symbols.Infos) {
                if (item.Name.contains("EURUSD") || item.Name.contains("eurusd")) {
                  eurusd = item.Name;
                  break;
                }
              }
              System.out.println(i + "  " + copier5ApiTrader.User + " Reconnected");
            } catch (Exception e) {
              e.printStackTrace();
              System.out.println(i + "  " + copier5ApiTrader.User + " Not Reconnected");
            }
          }
        }
      }
    }
  }

  public static void main(String[] args) {
    List<Integer> exclude = new LinkedList<>();
    exclude.add(8);
    exclude.add(11);
    exclude.add(12);
    exclude.add(72);
    exclude.add(74);
    exclude.add(124);
    exclude.add(128);
    long[] user = {
      2000072
    }; // , 2000038, 2000112, 2000099, 2000113, 2000100, 2000114, 2000064, 2000095, 2000096,
    // 2000123, 2000136, 2000135, 2000076, 2000079, 3000212, 2000147, 3000213, 2000124, 2000025,
    // 2000048, 2000110, 2000044, 2000167, 2000155, 2000170, 2000050, 2000179, 2000178, 2000115,
    // 2000173, 2000191, 2000193, 2000192, 2000206, 2000119, 2000085, 2000198, 2000200, 2000211,
    // 2000213, 2000215, 3000311, 3000312, 3000314, 3000315, 3000316, 3000317, 3000318, 3000319,
    // 3000320, 3000321, 3000322, 3000324, 3000323, 3000325, 3000326, 3000327, 3000328, 3000329,
    // 3000330, 3000331, 3000333, 3000334, 3000335, 3000336, 3000337, 3000338, 3000340, 3000339,
    // 3000341, 3000342, 3000343, 3000344, 3000345, 3000346, 3000347, 3000348, 3000349, 3000350,
    // 3000351, 3000352, 3000353, 3000354, 3000358, 3000355, 3000356, 3000357, 3000359, 3000361,
    // 3000362, 3000363, 3000364, 3000366, 3000367, 3000368, 3000369, 3000370, 3000365, 3000371,
    // 3000372, 3000373, 3000375, 3000376, 3000377, 3000378, 3000379, 3000380, 3000374, 3000383,
    // 3000384, 3000385, 3000386, 3000387, 3000388, 3000389, 3000390, 3000391, 3000392, 3000382,
    // 3000397, 3000398, 3000399, 3000400, 3000401, 3000402, 3000403, 3000404, 2000890, 2000176,
    // 2000236};
    String[] password =
        new String[] {
          "L9Cl3SP7",
          "noah1234",
          "lcl1987222",
          "lcl1987222",
          "L80008ll",
          "L80008ll",
          "zhu880203",
          "segvr12c",
          "wangyuhu666",
          "wanggang233451",
          "yue550077",
          "wangyuhu666",
          "wangyuhu666",
          "wei282828",
          "fny212514",
          "abcd1234",
          "noah1234",
          "abcd1234",
          "zhang911119",
          "bruce123",
          "xinhanyang1",
          "qq84681067",
          "qq84681067",
          "lizishequ123",
          "zhaobin1990",
          "didi1258",
          "noah1234",
          "G7D8pqdQ",
          "E7NOzjKg",
          "jjyy1105",
          "lk198320",
          "W5S6ot8T",
          "B9aVecfl",
          "T4KArcyU",
          "M9n1udDE",
          "noah1234",
          "XM891121",
          "H5T76t0V",
          "V1f6HC2F",
          "P3C5zUSK",
          "X7sRYt9w",
          "56215487cc",
          "esqvs7bq",
          "yj5krdtw",
          "1vacddtr",
          "dweo1pej",
          "vhf0vejq",
          "4tasbgkm",
          "8sfmykqe",
          "ve0yptue",
          "nj4zjwdx",
          "nkysf0se",
          "yrkaq6et",
          "k3mewuan",
          "dffnf4oa",
          "xkha1grf",
          "mrnwlho1",
          "3fyunscf",
          "msruod8w",
          "bumy4dmo",
          "kjxlqhr2",
          "a6hgvprm",
          "jqiug2ku",
          "hbfwzv1n",
          "cf5oytmt",
          "tmm5nklh",
          "mwydek7t",
          "0horilej",
          "tkqglk0s",
          "4rubwgji",
          "nazq0qqa",
          "dsg5iddw",
          "bmldja0r",
          "vj7gjpnn",
          "hk7roqor	",
          "8covjpan",
          "8vzszuid",
          "yibc3hfb",
          "dlweg2ou",
          "6yfomspn",
          "xyv6mmwq",
          "7nvvykbj",
          "tzcwk3no",
          "skiojv7o",
          "pj1cyqyh",
          "q8zcbyvk",
          "jvnv1lgh",
          "v4fmrrpc",
          "e0vuyvvo",
          "gugk6ccd",
          "f2cykfoi",
          "6rraodgq",
          "fiafsm4c",
          "wqvx3lgr",
          "nulruph4",
          "jhub0hjg",
          "vsynhqt2",
          "vnipvck6",
          "b4nemfnf",
          "xttilvd1",
          "llpziiz0",
          "hpxh7jcx",
          "xv1rdnvw",
          "jnbmuxi0",
          "bdmrxj6n",
          "r2sbjjqg",
          "xpries5a",
          "ooxvej0a",
          "efhcgk8f",
          "anncxd8v",
          "vmy3txlv",
          "czdmjf4g",
          "vfrzkdw7",
          "qmiuo7pf",
          "fqfkkuh4",
          "chbr1fay",
          "qsar7fqo",
          "mxmpxbg7",
          "hxzm5eis",
          "x5sjaltl",
          "c6gfztjw",
          "n5eztwpo",
          "jqcwh6eg",
          "ypm3buje",
          " jufyddq6",
          "sse4smoj",
          "0gzxplzv",
          "gnftu7gt",
          "245533566",
          "wcq666888",
          "J5fmHKSe",
          "aa080524"
        };
    for (int index = 0; index < user.length; index++) {
      if (exclude.contains(index)) {
        continue;
      }
      Copier5ApiTrader copier5ApiTrader;
      copier5ApiTrader = new Copier5ApiTrader(user[index], password[index], "89.187.118.51", 1951);
      //            copier5ApiTrader = new Copier5ApiTrader(user[index], password[index],
      // "8.208.77.179", 443);
      copier5ApiTrader.i = index;
      long start = System.currentTimeMillis(), end;
      try {
        copier5ApiTrader.connect2Broker();
        System.out.println(
            index
                + "  "
                + copier5ApiTrader.User
                + " connected. time consuming:"
                + (System.currentTimeMillis() - start) / 1000.0);
      } catch (Exception e) {
        if (e.getMessage().contains("INVALID_ACCOUNT")) {
          exclude.add(index);
          System.out.println(index);
        }
        e.printStackTrace();
        System.out.println(
            index
                + "  "
                + copier5ApiTrader.User
                + " not connected.time consuming:"
                + (System.currentTimeMillis() - start) / 1000.0);
      }
    }
  }
}
