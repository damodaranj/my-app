package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SHA256Native {
  MessageDigest MD;

  SHA256Native() {
    try {
      MD = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
    }
  }

  byte[] ComputeHash(byte[] bytes) {
    return MD.digest(bytes);
  }
}
