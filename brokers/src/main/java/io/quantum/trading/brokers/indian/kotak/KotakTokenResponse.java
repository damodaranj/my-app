package io.quantum.trading.brokers.indian.kotak;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class KotakTokenResponse {
  @SerializedName("access_token")
  private String accessToken;

  @SerializedName("refresh_token")
  private String refreshToken;

  private String scope;

  @SerializedName("token_type")
  private String tokenType;

  @SerializedName("expires_in")
  private int expiresIn;
}
