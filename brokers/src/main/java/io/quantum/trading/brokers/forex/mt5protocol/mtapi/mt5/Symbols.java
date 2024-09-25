package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Symbols {
  public SymBaseInfo Base;
  public SymGroup[] SymGroups;
  public SymbolInfo[] Infos;
  public HashMap<String, SymbolSessions> Sessions = new HashMap<String, SymbolSessions>();
  public HashMap<String, SymGroup> Groups = new HashMap<String, SymGroup>();

  public final SymbolInfo GetInfo(String symbol) {
    for (SymbolInfo item : Infos) {
      if (DotNetToJavaStringHelper.stringsEqual(item.Name, symbol)) {
        return item;
      }
    }
    throw new RuntimeException("Symbol not found: " + symbol);
  }

  public final SymGroup GetGroup(String symbol) {
    if (!Groups.containsKey(symbol)) {
      throw new RuntimeException("Symbol not found: " + symbol);
    }
    SymGroup res = Groups.get(symbol);
    for (SymGroup slave : SymGroups) {
      String regex = slave.GroupName.replace("\\", "\\\\").replace("*", ".*");
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(res.GroupName);
      if (matcher.matches()) res.CopyValues(slave);
    }
    return res;
  }

  public final SymbolInfo GetInfo(int id) {
    for (SymbolInfo item : Infos) {
      if (item.Id == id) {
        return item;
      }
    }
    throw new RuntimeException("Symbol not found: " + id);
  }

  public final boolean Exist(String symbol) {
    for (SymbolInfo item : Infos) {
      if (DotNetToJavaStringHelper.stringsEqual(item.Name, symbol)) {
        return true;
      }
    }
    return false;
  }

  public final String ExistStartsWith(String symbol) {
    for (SymbolInfo item : Infos) {
      if (item.Name.startsWith(symbol)) {
        return item.Name;
      }
    }
    return null;
  }
}
