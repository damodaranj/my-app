package io.quantum.trading.brokers.indian.kotak;

import lombok.Data;

@Data
public class KotakLoginPayload {
  private String userid;
  private String password;
}
