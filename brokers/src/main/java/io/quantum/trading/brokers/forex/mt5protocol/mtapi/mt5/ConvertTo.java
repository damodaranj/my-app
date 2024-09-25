package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.time.Duration;

class ConvertTo {
  private static java.time.LocalDateTime StartTime =
      java.time.LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0);

  public static java.time.LocalDateTime DateTime(long time) {
    return StartTime.plusSeconds(time);
  }

  public static java.time.LocalDateTime DateTimeMs(long time) {
    return StartTime.plusNanos(time * 1000000);
  }

  public static long Long(java.time.LocalDateTime time) {
    return Duration.between(StartTime, time).getSeconds();
  }

  public static double LongLongToDouble(int digits, long value) {
    digits = Math.min(digits, 11);
    return Math.round(((double) (value) / DegreeP[digits]) * Math.pow(10, digits))
        / Math.pow(10, digits);
  }

  private static double[] DegreeP = {
    1.0, 1.0e1, 1.0e2, 1.0e3, 1.0e4, 1.0e5, 1.0e6, 1.0e7, 1.0e8, 1.0e9, 1.0e10, 1.0e11, 1.0e12,
    1.0e13, 1.0e14, 1.0e15
  };
}
