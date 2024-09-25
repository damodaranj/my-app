package io.quantum.trading.brokers.indian.kotak;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
public class KotakInstrument {
  private String instrumentToken;
  private String instrumentName;
  private String name;
  private double lastPrice;

  @JsonFormat(pattern = "ddMMMyy", timezone = "Asia/Kolkata")
  private Date expiry;

  private double strike;
  private double tickSize;
  private int lotSize;
  private String instrumentType;
  private String segment;
  private String exchange;
  private String isin;
  private String multiplier;
  private String exchangeToken;
  private String optionType;
}
