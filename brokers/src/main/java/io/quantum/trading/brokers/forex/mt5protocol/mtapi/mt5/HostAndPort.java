package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.util.AbstractMap;
import java.util.Map;

public class HostAndPort {
  public static Map.Entry<String, Integer> Parse(String ip) {
    int port;
    int i = 0;
    String host = "";
    while (i < ip.length() && ip.getBytes()[i] != (byte) ':') host += (char) ip.getBytes()[i++];
    if (i == ip.length()) port = 443;
    else {
      i++;
      String strPort = "";
      while (i < ip.length()) strPort += (char) ip.getBytes()[i++];
      port = Integer.parseInt(strPort);
    }
    return new AbstractMap.SimpleEntry<String, Integer>(host.trim(), port);
  }
}
