package io.quantum.trading.brokers.indian.fyers;

import lombok.Data;

@Data
public class FyersAuthCodeValidateResponse {
  private String s;
  private String code;

  private String message;

  private String access_token;

  private String refresh_token;
}
