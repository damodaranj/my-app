package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.util.Enumeration;
import javax.net.ssl.KeyManagerFactory;

public class OrderSender {
  private Connection Connection;

  public OrderSender(Connection connection) {
    Connection = connection;
  }

  public final void Send(TradeRequest req) throws IOException {
    OutBuf buf = new OutBuf();
    buf.Add((byte) (0 & 0xFF));
    WriteRequest(buf, req, 96, Connection.TradeBuild);
    if (Connection.TradeBuild <= 1891) {
      req.Lots /= 10000;
      req.s174 /= 10000;
    }
    ProtectTradeRequest(req, buf);
    Connection.SendCompress((byte) 0x6C, buf);
  }

  private void ProtectTradeRequest(TradeRequest req, OutBuf buf) {
    if (Connection.TradeBuild > 1349) {
      CryptRequestProtectKey(req, buf);
    } else {
      CryptRequestAccountLogon(req, buf);
    }
  }

  private void CryptRequestProtectKey(TradeRequest req, OutBuf buf) {
    if (Connection.ProtectKey == null) {
      if ((Connection.QC.Account.TradeFlags & 8) != 0) throw new RuntimeException("Investor mode");
      else {
        try {
          KeyManagerFactory kmf = javax.net.ssl.KeyManagerFactory.getInstance("SunX509");
          KeyStore keystore = KeyStore.getInstance("PKCS12");
          keystore.load(
              new ByteArrayInputStream(Connection.QC.PfxFile),
              Connection.QC.PfxFilePassword.toCharArray());
          kmf.init(keystore, Connection.QC.PfxFilePassword.toCharArray());
          Enumeration<String> aliases = keystore.aliases();
          PrivateKey key = null;
          while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            key = (PrivateKey) keystore.getKey(alias, Connection.QC.PfxFilePassword.toCharArray());
          }
          if (key == null) throw new RuntimeException("RSA private key not found");
          Signature privateSignature = Signature.getInstance("SHA1withRSA");
          privateSignature.initSign(key);
          privateSignature.update(UDT.GetBytes(req));
          byte[] sign = privateSignature.sign();
          Connection.reverse(sign);
          buf.ByteToBuffer((byte) 0x2C);
          buf.LongToBuffer(sign.length);
          buf.DataToBuffer(sign);
          return;
        } catch (Exception ex) {
          throw new RuntimeException("Invalid cerificate: " + ex.getMessage());
        }
      }
    }
    // byte[] key = {(byte) 0xd1, 0x68, 0x5a, 0x59, (byte) 0xde, 0x51, (byte) 0xf9, (byte) 0xdd,
    // 0x65, (byte) 0x83, (byte) 0xd7, 0x1b, (byte) 0x89, 0x18, (byte) 0xbe, (byte) 0xa9, 0x4c,
    // 0x0a, 0x6b, 0x7d, (byte) 0xfb, 0x66, (byte) 0xab, (byte) 0x8a, 0x53, 0x0a, (byte) 0xeb, 0x7e,
    // 0x6b, 0x68, 0x31, 0x50};
    byte[] key = new byte[32];
    System.arraycopy(Connection.ProtectKey, 0, key, 0, Connection.ProtectKey.length);
    byte[] mod1 = new byte[64];
    System.arraycopy(key, 0, mod1, 0, key.length);
    byte[] mod2 = new byte[64];
    System.arraycopy(key, 0, mod2, 0, key.length);
    for (int i = 0; i < 64; i++) {
      mod1[i] ^= 0x36;
      mod2[i] ^= 0x5C;
    }
    byte[] data = UDT.GetBytes(req);
    byte[] hash1 = new byte[32];
    Sha256 sha = new Sha256();
    sha.AddData(mod1);
    sha.AddData(data);
    copyTo(sha.GetHash(), hash1);
    sha = new Sha256();
    sha.AddData(mod2);
    sha.AddData(hash1);
    byte[] hash2 = new byte[32];
    copyTo(sha.GetHash(), hash2);
    buf.Add((byte) (0x55 & 0xFF));
    buf.Add((int) 32);
    buf.Add(hash2);
  }

  void copyTo(byte[] src, byte[] dst) {
    System.arraycopy(src, 0, dst, 0, src.length);
  }

  private void CryptRequestAccountLogon(TradeRequest req, OutBuf buf) {
    throw new UnsupportedOperationException();
  }

  private void WriteRequest(OutBuf buf, TradeRequest req, int mode, int tradeBuild) {
    switch (mode) {
      case 0:
      case 4:
      case 5:
      case 7:
      case 8:
      case 9:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 66:
      case 67:
      case 69:
      case 73:
      case 96:
      case 97:
      case 98:
      case 99:
      case 100:
      case 101:
        if (tradeBuild > 1891) {
          break;
        }
        throw new UnsupportedOperationException();
    }
    buf.Add(UDT.GetBytes(req));
  }
}
