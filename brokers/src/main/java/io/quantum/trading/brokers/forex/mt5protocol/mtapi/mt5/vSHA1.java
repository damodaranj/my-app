package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

class vSHA1 {
  private int[] Regs = new int[5];
  private int nBitCount = 0;
  private int dwData = 0;
  private byte[] dwBlock = new byte[64];
  private int dwCount = 0;
  private int dbCount = 0;

  vSHA1() {
    nBitCount = 0;
    dwData = 0;
    dwCount = 0;
    dbCount = 0;
    Regs[0] = 0x67452301;
    Regs[1] = 0xEFCDAB89;
    Regs[2] = 0x98BADCFE;
    Regs[3] = 0x10325476;
    Regs[4] = 0xC3D2E1F0;
    // Array.Clear(dwBlock, 0, 16);
    for (int i = 0; i < 16; i++) dwBlock[i] = 0;
  }

  final void HashData(byte[] data) {
    byte[] tmp = new byte[0];
    for (int i = 0; i < data.length; i++) {
      dwData = (dwData << 8) + ((int) data[i] & 0xFF);
      nBitCount += 8;
      if (++dbCount >= 4) {
        dbCount = 0;
        System.arraycopy(
            BitConverter.GetBytes(dwData),
            0,
            dwBlock,
            dwCount * 4,
            BitConverter.GetBytes(dwData).length);
        if (++dwCount >= 16) {
          dwCount = 0;
          Transform(dwBlock);
        }
        dwData = 0;
      }
      tmp = dwBlock;
    }
    tmp.toString();
  }

  final byte[] FinalizeHash() {
    int bitCnt = nBitCount;
    dwData = (dwData << 8) + 0x80;
    while (true) {
      nBitCount += 8;
      if (++dbCount >= 4) {
        dbCount = 0;
        System.arraycopy(
            BitConverter.GetBytes(dwData),
            0,
            dwBlock,
            dwCount * 4,
            BitConverter.GetBytes(dwData).length);
        if (++dwCount >= 16) {
          dwCount = 0;
          Transform(dwBlock);
        }
        dwData = 0;
      }
      if ((dbCount == 0) && (dwCount == 14)) {
        break;
      }
      dwData <<= 8;
    }
    System.arraycopy(
        BitConverter.GetBytes(0), 0, dwBlock, dwCount * 4, BitConverter.GetBytes(0).length);
    if (++dwCount >= 16) {
      dwCount = 0;
      Transform(dwBlock);
    }
    System.arraycopy(
        BitConverter.GetBytes(bitCnt),
        0,
        dwBlock,
        dwCount * 4,
        BitConverter.GetBytes(bitCnt).length);
    if (++dwCount >= 16) {
      dwCount = 0;
      Transform(dwBlock);
    }
    return new byte[] {
      (byte) (Regs[0] >>> 24),
      (byte) (Regs[0] >>> 16),
      (byte) (Regs[0] >>> 8),
      (byte) (Regs[0] >>> 0),
      (byte) (Regs[1] >>> 24),
      (byte) (Regs[1] >>> 16),
      (byte) (Regs[1] >>> 8),
      (byte) (Regs[1] >>> 0),
      (byte) (Regs[2] >>> 24),
      (byte) (Regs[2] >>> 16),
      (byte) (Regs[2] >>> 8),
      (byte) (Regs[2] >>> 0),
      (byte) (Regs[3] >>> 24),
      (byte) (Regs[3] >>> 16),
      (byte) (Regs[3] >>> 8),
      (byte) (Regs[3] >>> 0),
      (byte) (Regs[4] >>> 24),
      (byte) (Regs[4] >>> 16),
      (byte) (Regs[4] >>> 8),
      (byte) (Regs[4] >>> 0)
    };
  }

  final byte[] ComputeHash(byte[] data) {
    int len = data.length;
    int left = 0;
    if (len >= 64) {
      byte[] block = new byte[64];
      for (int i = 0; i < len / 64; i++) {
        for (int k = 0; k < 64; k++) {
          block[k] = data[i * 64 + k];
        }
        Transform(block);
        left += 64;
      }
    }
    int rem = len % 64;
    if (rem > 0) {
      byte[] block = new byte[64];
      for (int k = 0; k < rem; k++) {
        block[k] = data[left + k];
      }
      Transform(block);
    }
    return new byte[] {
      (byte) (Regs[0] >>> 0),
      (byte) (Regs[0] >>> 8),
      (byte) (Regs[0] >>> 16),
      (byte) (Regs[0] >>> 24),
      (byte) (Regs[1] >>> 0),
      (byte) (Regs[1] >>> 8),
      (byte) (Regs[1] >>> 16),
      (byte) (Regs[1] >>> 24),
      (byte) (Regs[2] >>> 0),
      (byte) (Regs[2] >>> 8),
      (byte) (Regs[2] >>> 16),
      (byte) (Regs[2] >>> 24),
      (byte) (Regs[3] >>> 0),
      (byte) (Regs[3] >>> 8),
      (byte) (Regs[3] >>> 16),
      (byte) (Regs[3] >>> 24),
      (byte) (Regs[4] >>> 0),
      (byte) (Regs[4] >>> 8),
      (byte) (Regs[4] >>> 16),
      (byte) (Regs[4] >>> 24)
    };
  }

  private int SHA1Shift(int bits, int word) {
    return ((word << bits) | (word >>> (32 - bits)));
  }

  private void Transform(byte[] data) {
    int temp;
    int[] W = new int[80];
    for (int i = 0; i < 16; i++) {
      W[i] = BitConverter.ToInt32(data, i * 4);
    }
    for (int i = 16; i < 80; i++) {
      W[i] = SHA1Shift(1, W[i - 3] ^ W[i - 8] ^ W[i - 14] ^ W[i - 16]);
    }
    int A = Regs[0];
    int B = Regs[1];
    int C = Regs[2];
    int D = Regs[3];
    int E = Regs[4];
    for (int i = 0; i < 20; i++) {
      temp = SHA1Shift(5, A) + ((B & C) | (~B & D)) + E + W[i] + 0x5A827999;
      E = D;
      D = C;
      C = SHA1Shift(30, B);
      B = A;
      A = temp;
    }
    for (int i = 20; i < 40; i++) {
      temp = SHA1Shift(5, A) + (B ^ C ^ D) + E + W[i] + 0x6ED9EBA1;
      E = D;
      D = C;
      C = SHA1Shift(30, B);
      B = A;
      A = temp;
    }
    for (int i = 40; i < 60; i++) {
      temp = SHA1Shift(5, A) + ((B & C) | (B & D) | (C & D)) + E + W[i] + 0x8F1BBCDC;
      E = D;
      D = C;
      C = SHA1Shift(30, B);
      B = A;
      A = temp;
    }
    for (int i = 60; i < 80; i++) {
      temp = SHA1Shift(5, A) + (B ^ C ^ D) + E + W[i] + 0xCA62C1D6;
      E = D;
      D = C;
      C = SHA1Shift(30, B);
      B = A;
      A = temp;
    }
    Regs[0] += A;
    Regs[1] += B;
    Regs[2] += C;
    Regs[3] += D;
    Regs[4] += E;
  }
}
