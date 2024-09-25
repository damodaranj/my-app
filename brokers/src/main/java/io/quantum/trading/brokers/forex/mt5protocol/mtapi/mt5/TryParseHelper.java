package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

// ----------------------------------------------------------------------------------------
//	Copyright © 2007 - 2017 Tangible Software Solutions Inc.
//	This class can be used by anyone provided that the copyright notice remains intact.
//
//	This class is used to convert some of the C# TryParse methods to Java.
// ----------------------------------------------------------------------------------------
public final class TryParseHelper {
  public static boolean tryParseInt(String s, RefObject<Integer> result) {
    try {
      result.argValue = Integer.parseInt(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static boolean tryParseShort(String s, RefObject<Short> result) {
    try {
      result.argValue = Short.parseShort(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static boolean tryParseLong(String s, RefObject<Long> result) {
    try {
      result.argValue = Long.parseLong(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static boolean tryParseByte(String s, RefObject<Byte> result) {
    try {
      result.argValue = Byte.parseByte(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static boolean tryParseDouble(String s, RefObject<Double> result) {
    try {
      result.argValue = Double.parseDouble(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static boolean tryParseFloat(String s, RefObject<Float> result) {
    try {
      result.argValue = Float.parseFloat(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static boolean tryParseBoolean(String s, RefObject<Boolean> result) {
    try {
      result.argValue = Boolean.parseBoolean(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
