package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Sha256 {

  MessageDigest MD;

  public Sha256() {
    try {
      MD = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public void AddData(byte[] data, int of, int len) {
    MD.update(data, of, len);
  }

  public void AddData(byte[] data) {
    MD.update(data);
  }

  public byte[] GetHash() {
    return MD.digest();
  }
}
