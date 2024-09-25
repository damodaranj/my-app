package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

class vRSA {
  private long M, N, D, Y;

  // private long P, Q;

  vRSA(long p) {
    // P = p ^ 0x151D8255;
    // Q = p ^ 0x274ECC00;
    M = 0x67789ED4559AF79L;
    N = 0xCCCCCCCCCCCCCCCCL;
    D = 0x1DE7FED38081L;
    // E = 0x5D405B5;
    Y = 0x53290744C4D541L;
  }

  final long ComputePacketKey(byte[] data) {
    return ExpMod64(PrepareKey(data) % N, N, M);
  }

  final long ComputeFileKey(byte[] data) {
    return ExpMod64(PrepareKey(data) % M, D, M);
  }

  final boolean CheckKey(byte[] data) {
    if (data.length < 8) {
      return false;
    }
    int szData = data.length - 8;
    byte[] buf = new byte[szData];
    System.arraycopy(data, 0, buf, 0, szData);
    long dataKey = PrepareKey(buf);
    long origKey = BitConverter.ToInt64(data, szData);
    return ExpMod64(origKey, Y, M) == (dataKey % M);
  }

  private long PrepareKey(byte[] data) {
    if (data.length < 1) {
      return 0;
    }
    long h = 0;
    long pm = 0x123456789L;
    for (int i = 0; i < data.length / 8; i++) {
      long w = BitConverter.ToInt64(data, i * 8);
      h ^= w;
      int lw = (int) w;
      int hw = (int) (w >>> 32);
      long sign = ((hw & 0x80000000) != 0) ? 0xFFFFFFFF00000000L : 0;
      long t = ((long) (hw ^ lw) << 32) | (long) (lw ^ hw);
      t += sign;
      t += (((long) lw << 32) | hw) | sign;
      t += pm;
      t += w;
      pm ^= t;
    }
    int rem = data.length & 7;
    if (rem != 0) {
      long ls = 0;
      long rs = 0;
      long mdc = 0x11F71FB04CBL;
      int last = 0;
      if (rem >= 2) {
        int it = (rem - 2) / 2 + 1;
        last = it * 2;
        int off = data.length - rem;
        int sh = 0;
        for (int i = 0; i < it; i++, sh += 16) {
          ls += (long) ((byte) data[i * 2 + off]) << sh;
          rs += (long) ((byte) data[i * 2 + 1 + off]) << (sh + 8);
        }
      }
      if (last < rem) {
        mdc += (long) ((byte) data[data.length - 1]) << (last << 8);
      }
      long w = (ls + rs + mdc);
      h ^= w;
      int lw = (int) w;
      int hw = (int) (w >>> 32);
      long sign = ((hw & 0x80000000) != 0) ? 0xFFFFFFFF00000000L : 0;
      long t = ((long) (hw ^ lw) << 32) | (long) (lw ^ hw);
      t += sign;
      t += (((long) lw << 32) | hw) | sign;
      t += pm;
      t += w;
      pm ^= t;
    }
    return (((long) data.length * 0x100000001L) ^ pm ^ h) & 0xFFFFFFFFFFFL;
  }

  private long ExpMod64(long rem, long n, long m) {
    long key = 1;
    long prv = rem;
    for (int i = 0; i < 64; i++) {
      if (((n >>> i) & 1) != 0) {
        key = MulMod64(key, prv, m);
      }
      prv = MulMod64(prv, prv, m);
    }
    return key;
  }

  private long MulMod64(long k, long n, long m) {
    long key = 0;
    long prv = k;
    for (int i = 0; i < 64; i++) {
      if (((n >>> i) & 1) != 0) {
        key = (key + prv) % m;
      }
      prv = prv * 2 % m;
    }
    return key;
  }
}
