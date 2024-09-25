package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.util.*;

public class ErrorDescription {
  private static HashMap<Integer, Integer> keys;
  private static HashMap<Integer, String> strings;

  static {
    keys = new HashMap<Integer, Integer>();
    keys.put(0x0, 0x572608);
    keys.put(0x1, 0x572604);
    keys.put(0x2, 0x5725F4);
    keys.put(0x3, 0x5725E0);
    keys.put(0x4, 0x5725D0);
    keys.put(0x5, 0x5725C4);
    keys.put(0x6, 0x5725B4);
    keys.put(0x7, 0x5725A0);
    keys.put(0x8, 0x572588);
    keys.put(0xD, 0x572580);
    keys.put(0xE, 0x57257C);
    keys.put(0x40, 0x572574);
    keys.put(0x41, 0x572564);
    keys.put(0x80, 0x572554);
    keys.put(0x81, 0x572544);
    keys.put(0x82, 0x572530);
    keys.put(0x83, 0x572520);
    keys.put(0x84, 0x57250C);
    keys.put(0x85, 0x5724F8);
    keys.put(0x86, 0x5724E4);
    keys.put(0x87, 0x5724D0);
    keys.put(0x88, 0x5724C4);
    keys.put(0x89, 0x5724B4);
    keys.put(0x8A, 0x5724AC);
    keys.put(0x8B, 0x57249C);
    keys.put(0x8C, 0x57247C);
    keys.put(0x8D, 0x572468);
    keys.put(0x8E, 0x572454);
    keys.put(0x8F, 0x572440);
    keys.put(0x90, 0x572424);
    keys.put(0x91, 0x5723DC);
    keys.put(0x92, 0x57240C);
    keys.put(0x93, 0x5723B0);
    keys.put(0x94, 0x5723A0);
    keys.put(0x95, 0x572390);
    keys.put(0x96, 0x572380);
    strings = new HashMap<Integer, String>();
    strings.put(0x00572380, "Prohibited by FIFO rule");
    strings.put(0x00572390, "Hedge is prohibited");
    strings.put(0x005723A0, "Too many open orders");
    strings.put(0x005723B0, "Expiration for pending orders is disabled");
    strings.put(0x0057240C, "Trade context is busy");
    strings.put(0x005723DC, "Modification denied. Order too close to market");
    strings.put(0x00572424, "Request canceled by client");
    strings.put(0x00572440, "Order is in process");
    strings.put(0x00572454, "Order is accepted");
    strings.put(0x00572468, "Too many requests");
    strings.put(0x0057247C, "Only long positions are allowed");
    strings.put(0x0057249C, "Order is locked");
    strings.put(0x005724AC, "Requote");
    strings.put(0x005724B4, "Broker is busy");
    strings.put(0x005724C4, "Off quotes");
    strings.put(0x005724D0, "Price is changed");
    strings.put(0x005724E4, "Not enough money");
    strings.put(0x005724F8, "Trade is disabled");
    strings.put(0x0057250C, "Market is closed");
    strings.put(0x00572520, "Invalid volume");
    strings.put(0x00572530, "Invalid S/L or T/P");
    strings.put(0x00572544, "Invalid prices");
    strings.put(0x00572554, "Trade timeout");
    strings.put(0x00572564, "Invalid account");
    strings.put(0x00572574, "Account disabled");
    strings.put(0x0057257C, "Invalid one-time password");
    strings.put(0x00572580, "Secret key for one-time password is required");
    strings.put(0x00572588, "Too frequent requests");
    strings.put(0x005725A0, "Not enough rights");
    strings.put(0x005725B4, "No connection");
    strings.put(0x005725C4, "Old version");
    strings.put(0x005725D0, "Server is busy");
    strings.put(0x005725E0, "Invalid parameters");
    strings.put(0x005725F4, "Common error");
    strings.put(0x00572604, "OK");
    strings.put(0x00572608, "Done");
  }

  public static String get(int code) {
    try {
      int key = keys.get(code);
      return strings.get(key);
    } catch (RuntimeException e) {
      return "Unknown server reply " + Integer.toHexString(code).toUpperCase();
    }
  }
}
