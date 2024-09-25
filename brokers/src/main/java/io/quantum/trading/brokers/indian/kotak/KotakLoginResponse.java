package io.quantum.trading.brokers.indian.kotak;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class KotakLoginResponse {
  @SerializedName("Status")
  private String status;

  private String userid;
  private String accessCodeTime;
  private int authLevel;
  private String biometric;
  private String clientType;
  private String emailId;
  private String message;
  private String mpin;
  private int nomineeFlag;
  private String oneTimeToken;
  private String phoneNo;
}
