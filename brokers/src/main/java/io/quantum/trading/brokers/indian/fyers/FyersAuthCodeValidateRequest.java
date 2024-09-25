package io.quantum.trading.brokers.indian.fyers;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class FyersAuthCodeValidateRequest {
  @NonNull private String grant_type;
  @NonNull private String appIdHash;
  @NonNull private String code;
}
