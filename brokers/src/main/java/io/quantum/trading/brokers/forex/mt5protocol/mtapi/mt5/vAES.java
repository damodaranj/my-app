package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.util.Arrays;

class vAES {
  private int m_nCipherRnd;
  private int[] m_Ks = new int[64]; // KeySchedule
  private int[] m_Ke = new int[64]; // KeyEncoded
  private int[][] s_tabIT = new int[4][256];
  private int[][] s_tabFT = new int[4][256];
  private int[] InvSBox = new int[256];
  private int[] SBox = new int[256];
  private int[] s_tabIB = new int[256];

  vAES() {
    m_nCipherRnd = 0;
    Arrays.fill(m_Ks, 0);
    Arrays.fill(m_Ke, 0);

    // Arrays.fill(m_Ks, 0, 64);
    // Array.Clear(m_Ke, 0, 64);
  }

  private byte[] EncryptBlock(byte[] data) {
    int[] ind = new int[4];
    long[] w = new long[4];
    ind[0] = m_Ks[0] ^ BitConverter.ToInt32(data, 0);

    ind[1] = m_Ks[1] ^ BitConverter.ToInt32(data, 4);

    ind[2] = m_Ks[2] ^ BitConverter.ToInt32(data, 8);

    ind[3] = m_Ks[3] ^ BitConverter.ToInt32(data, 12);

    w[0] =
        s_tabFT[0][ind[0] & 0xFF]
            ^ s_tabFT[1][(ind[1] >>> 8) & 0xFF]
            ^ s_tabFT[2][(ind[2] >>> 16) & 0xFF]
            ^ s_tabFT[3][(ind[3] >>> 24) & 0xFF]
            ^ m_Ks[4];
    w[0] &= 0xFFFFFFFF;
    w[1] =
        s_tabFT[0][ind[1] & 0xFF]
            ^ s_tabFT[1][(ind[2] >>> 8) & 0xFF]
            ^ s_tabFT[2][(ind[3] >>> 16) & 0xFF]
            ^ s_tabFT[3][(ind[0] >>> 24) & 0xFF]
            ^ m_Ks[5];
    w[1] &= 0xFFFFFFFF;
    w[2] =
        s_tabFT[0][ind[2] & 0xFF]
            ^ s_tabFT[1][(ind[3] >>> 8) & 0xFF]
            ^ s_tabFT[2][(ind[0] >>> 16) & 0xFF]
            ^ s_tabFT[3][(ind[1] >>> 24) & 0xFF]
            ^ m_Ks[6];
    w[2] &= 0xFFFFFFFF;
    w[3] =
        s_tabFT[0][ind[3] & 0xFF]
            ^ s_tabFT[1][(ind[0] >>> 8) & 0xFF]
            ^ s_tabFT[2][(ind[1] >>> 16) & 0xFF]
            ^ s_tabFT[3][(ind[2] >>> 24) & 0xFF]
            ^ m_Ks[7];
    w[3] &= 0xFFFFFFFF;
    for (int i = 0; i < w.length; i++) ind[i] = (int) w[i];
    // System.arraycopy(w, 0, ind, 0, w.length);
    int i;
    for (i = 0; i < m_nCipherRnd - 2; i += 2) {
      w[0] =
          s_tabFT[0][ind[0] & 0xFF]
              ^ s_tabFT[1][(ind[1] >>> 8) & 0xFF]
              ^ s_tabFT[2][(ind[2] >>> 16) & 0xFF]
              ^ s_tabFT[3][(ind[3] >>> 24) & 0xFF]
              ^ m_Ks[i * 4 + 8];

      w[1] =
          s_tabFT[0][ind[1] & 0xFF]
              ^ s_tabFT[1][(ind[2] >>> 8) & 0xFF]
              ^ s_tabFT[2][(ind[3] >>> 16) & 0xFF]
              ^ s_tabFT[3][(ind[0] >>> 24) & 0xFF]
              ^ m_Ks[i * 4 + 9];

      w[2] =
          s_tabFT[0][ind[2] & 0xFF]
              ^ s_tabFT[1][(ind[3] >>> 8) & 0xFF]
              ^ s_tabFT[2][(ind[0] >>> 16) & 0xFF]
              ^ s_tabFT[3][(ind[1] >>> 24) & 0xFF]
              ^ m_Ks[i * 4 + 10];

      w[3] =
          s_tabFT[0][ind[3] & 0xFF]
              ^ s_tabFT[1][(ind[0] >>> 8) & 0xFF]
              ^ s_tabFT[2][(ind[1] >>> 16) & 0xFF]
              ^ s_tabFT[3][(ind[2] >>> 24) & 0xFF]
              ^ m_Ks[i * 4 + 11];
      for (int j = 0; j < w.length; j++) ind[j] = (int) w[j];
      // System.arraycopy(w, 0, ind, 0, w.length);
      w[0] =
          s_tabFT[0][ind[0] & 0xFF]
              ^ s_tabFT[1][(ind[1] >>> 8) & 0xFF]
              ^ s_tabFT[2][(ind[2] >>> 16) & 0xFF]
              ^ s_tabFT[3][(ind[3] >>> 24) & 0xFF]
              ^ m_Ks[i * 4 + 12];

      w[1] =
          s_tabFT[0][ind[1] & 0xFF]
              ^ s_tabFT[1][(ind[2] >>> 8) & 0xFF]
              ^ s_tabFT[2][(ind[3] >>> 16) & 0xFF]
              ^ s_tabFT[3][(ind[0] >>> 24) & 0xFF]
              ^ m_Ks[i * 4 + 13];

      w[2] =
          s_tabFT[0][ind[2] & 0xFF]
              ^ s_tabFT[1][(ind[3] >>> 8) & 0xFF]
              ^ s_tabFT[2][(ind[0] >>> 16) & 0xFF]
              ^ s_tabFT[3][(ind[1] >>> 24) & 0xFF]
              ^ m_Ks[i * 4 + 14];

      w[3] =
          s_tabFT[0][ind[3] & 0xFF]
              ^ s_tabFT[1][(ind[0] >>> 8) & 0xFF]
              ^ s_tabFT[2][(ind[1] >>> 16) & 0xFF]
              ^ s_tabFT[3][(ind[2] >>> 24) & 0xFF]
              ^ m_Ks[i * 4 + 15];

      for (int j = 0; j < w.length; j++) ind[j] = (int) w[j];
      // System.arraycopy(w, 0, ind, 0, w.length);
    }
    byte[] crypt = new byte[16];
    System.arraycopy(
        BitConverter.GetBytes(
            SBox[ind[0] & 0xFF]
                ^ (SBox[(ind[1] >>> 8) & 0xFF] << 8)
                ^ (SBox[(ind[2] >>> 16) & 0xFF] << 16)
                ^ (SBox[(ind[3] >>> 24) & 0xFF] << 24)
                ^ m_Ks[i * 4 + 8]),
        0,
        crypt,
        0,
        BitConverter.GetBytes(
                SBox[ind[0] & 0xFF]
                    ^ (SBox[(ind[1] >>> 8) & 0xFF] << 8)
                    ^ (SBox[(ind[2] >>> 16) & 0xFF] << 16)
                    ^ (SBox[(ind[3] >>> 24) & 0xFF] << 24)
                    ^ m_Ks[i * 4 + 8])
            .length);
    System.arraycopy(
        BitConverter.GetBytes(
            SBox[ind[1] & 0xFF]
                ^ (SBox[(ind[2] >>> 8) & 0xFF] << 8)
                ^ (SBox[(ind[3] >>> 16) & 0xFF] << 16)
                ^ (SBox[(ind[0] >>> 24) & 0xFF] << 24)
                ^ m_Ks[i * 4 + 9]),
        0,
        crypt,
        4,
        BitConverter.GetBytes(
                SBox[ind[1] & 0xFF]
                    ^ (SBox[(ind[2] >>> 8) & 0xFF] << 8)
                    ^ (SBox[(ind[3] >>> 16) & 0xFF] << 16)
                    ^ (SBox[(ind[0] >>> 24) & 0xFF] << 24)
                    ^ m_Ks[i * 4 + 9])
            .length);
    System.arraycopy(
        BitConverter.GetBytes(
            SBox[ind[2] & 0xFF]
                ^ (SBox[(ind[3] >>> 8) & 0xFF] << 8)
                ^ (SBox[(ind[0] >>> 16) & 0xFF] << 16)
                ^ (SBox[(ind[1] >>> 24) & 0xFF] << 24)
                ^ m_Ks[i * 4 + 10]),
        0,
        crypt,
        8,
        BitConverter.GetBytes(
                SBox[ind[2] & 0xFF]
                    ^ (SBox[(ind[3] >>> 8) & 0xFF] << 8)
                    ^ (SBox[(ind[0] >>> 16) & 0xFF] << 16)
                    ^ (SBox[(ind[1] >>> 24) & 0xFF] << 24)
                    ^ m_Ks[i * 4 + 10])
            .length);
    System.arraycopy(
        BitConverter.GetBytes(
            SBox[ind[3] & 0xFF]
                ^ (SBox[(ind[0] >>> 8) & 0xFF] << 8)
                ^ (SBox[(ind[1] >>> 16) & 0xFF] << 16)
                ^ (SBox[(ind[2] >>> 24) & 0xFF] << 24)
                ^ m_Ks[i * 4 + 11]),
        0,
        crypt,
        12,
        BitConverter.GetBytes(
                SBox[ind[3] & 0xFF]
                    ^ (SBox[(ind[0] >>> 8) & 0xFF] << 8)
                    ^ (SBox[(ind[1] >>> 16) & 0xFF] << 16)
                    ^ (SBox[(ind[2] >>> 24) & 0xFF] << 24)
                    ^ m_Ks[i * 4 + 11])
            .length);
    return crypt;
  }

  private long upr(long x) {
    return ((x << 8) | (x >>> (32 - 8))) & 0xFFFFFFFF;
  }

  final void GenerateTables() {
    short[] log = new short[256];
    short[] pow = new short[256];
    log[0] = 0;
    short w = 1;
    for (int i = 0; i < 256; i++) {
      int v = w;
      log[v] = (short) i;
      pow[i] = w;
      w ^= (short) ((v << 1) ^ (((w & 0x80) != 0) ? 0x1B : 0));
      w &= 0xFF;
      // if(w>256)
      //	w-=256;
    }
    pow[255] = 0;
    for (int i = 0; i < 256; i++) {
      short v = pow[255 - log[i]];
      w =
          (short)
              ((((((((v >>> 1) ^ v) >> 1) ^ v) >> 1) ^ v) >> 4)
                  ^ (((((((v << 1) ^ v) << 1) ^ v) << 1) ^ v) << 1)
                  ^ v
                  ^ 0x63);
      w &= 0xFF;
      SBox[i] = w;
      InvSBox[w] = i;
    }
    for (int i = 0; i < 256; i++) {
      short v1 = (short) SBox[i];
      v1 &= 0xFF;
      short v2 = (short) (v1 << 1);
      v2 &= 0xFF;
      if ((v1 & 0x80) != 0) {
        v2 ^= 0x1B;
        v2 &= 0xFF;
      }
      long wt = ((((long) v1 ^ v2) << 24) | (v1 << 16) | (v1 << 8) | v2);
      wt &= 0xFFFFFFFF;
      s_tabFT[0][i] = (int) wt;
      wt = upr(wt);
      s_tabFT[1][i] = (int) wt;
      wt = upr(wt);
      s_tabFT[2][i] = (int) wt;
      wt = upr(wt);
      s_tabFT[3][i] = (int) wt;
      wt = 0;
      short v = (short) InvSBox[i];
      v &= 0xFF;
      if (v != 0) {
        wt =
            (((long) pow[(log[v] + 0x68) % 255] << 24)
                ^ (pow[(log[v] + 0xEE) % 255] << 16)
                ^ (pow[(log[v] + 0xC7) % 255] << 8)
                ^ pow[(log[v] + 0xDF) % 255]);
        wt &= 0xFFFFFFFF;
      }
      s_tabIT[0][i] = (int) wt;
      wt = upr(wt);
      s_tabIT[1][i] = (int) wt;
      wt = upr(wt);
      s_tabIT[2][i] = (int) wt;
      wt = upr(wt);
      s_tabIT[3][i] = (int) wt;
    }
    s_tabIT.toString();
  }

  private int bKs(int index) {
    long ks = m_Ks[index / 4];
    switch (index % 4) {
      case 0:
        return (int) (ks & 0xFF);
      case 1:
        return (int) ((ks >>> 8) & 0xFF);
      case 2:
        return (int) ((ks >>> 16) & 0xFF);
      case 3:
        return (int) ((ks >>> 24) & 0xFF);
    }
    return 0;
  }

  private void EncodeKey(byte[] key, int szKey) {
    if (szKey > 256) {
      return;
    }
    if (SBox[0] == 0) {
      GenerateTables();
    }
    for (int i = 0; i < szKey / 32; i++) {
      m_Ks[i] = BitConverter.ToInt32(key, i * 4);
    }
    short v;
    short w = 1;
    int indKs;
    if (szKey == 128) {
      for (int i = 0; i < 2; i++) {
        m_Ks[i * 20 + 4] =
            (((((SBox[bKs(i * 80 + 12)] << 8) ^ SBox[bKs(i * 80 + 15)]) << 8)
                        ^ SBox[bKs(i * 80 + 14)])
                    << 8)
                ^ SBox[bKs(i * 80 + 13)]
                ^ m_Ks[i * 20]
                ^ w;

        m_Ks[i * 20 + 5] = m_Ks[i * 20 + 1] ^ m_Ks[i * 20 + 4];

        m_Ks[i * 20 + 6] = m_Ks[i * 20 + 1] ^ m_Ks[i * 20 + 2] ^ m_Ks[i * 20 + 4];

        m_Ks[i * 20 + 7] = m_Ks[i * 20 + 3] ^ m_Ks[i * 20 + 6];

        v = w;
        w = (byte) ((v << 1) ^ (((w & 0x80) != 0) ? 0x1B : 0));
        w &= 0xFF;
        m_Ks[i * 20 + 8] =
            (((((SBox[bKs(i * 80 + 28)] << 8) ^ SBox[bKs(i * 80 + 31)]) << 8)
                        ^ SBox[bKs(i * 80 + 30)])
                    << 8)
                ^ SBox[bKs(i * 80 + 29)]
                ^ m_Ks[i * 20 + 4]
                ^ w;

        m_Ks[i * 20 + 9] = m_Ks[i * 20 + 5] ^ m_Ks[i * 20 + 8];

        m_Ks[i * 20 + 10] = m_Ks[i * 20 + 5] ^ m_Ks[i * 20 + 6] ^ m_Ks[i * 20 + 8];

        m_Ks[i * 20 + 11] = m_Ks[i * 20 + 7] ^ m_Ks[i * 20 + 10];

        v = w;
        w = (byte) ((v << 1) ^ (((w & 0x80) != 0) ? 0x1B : 0));
        w &= 0xFF;
        m_Ks[i * 20 + 12] =
            (((((SBox[bKs(i * 80 + 44)] << 8) ^ SBox[bKs(i * 80 + 47)]) << 8)
                        ^ SBox[bKs(i * 80 + 46)])
                    << 8)
                ^ SBox[bKs(i * 80 + 45)]
                ^ m_Ks[i * 20 + 8]
                ^ w;
        m_Ks[i * 20 + 13] = m_Ks[i * 20 + 9] ^ m_Ks[i * 20 + 12];
        m_Ks[i * 20 + 14] = m_Ks[i * 20 + 9] ^ m_Ks[i * 20 + 10] ^ m_Ks[i * 20 + 12];
        m_Ks[i * 20 + 15] = m_Ks[i * 20 + 11] ^ m_Ks[i * 20 + 14];
        v = w;
        w = (byte) ((v << 1) ^ (((w & 0x80) != 0) ? 0x1B : 0));
        w &= 0xFF;
        m_Ks[i * 20 + 16] =
            (((((SBox[bKs(i * 80 + 60)] << 8) ^ SBox[bKs(i * 80 + 63)]) << 8)
                        ^ SBox[bKs(i * 80 + 62)])
                    << 8)
                ^ SBox[bKs(i * 80 + 61)]
                ^ m_Ks[i * 20 + 12]
                ^ w;
        m_Ks[i * 20 + 17] = m_Ks[i * 20 + 13] ^ m_Ks[i * 20 + 16];
        m_Ks[i * 20 + 18] = m_Ks[i * 20 + 13] ^ m_Ks[i * 20 + 14] ^ m_Ks[i * 20 + 16];
        m_Ks[i * 20 + 19] = m_Ks[i * 20 + 15] ^ m_Ks[i * 20 + 18];
        v = w;
        w = (byte) ((v << 1) ^ (((w & 0x80) != 0) ? 0x1B : 0));
        w &= 0xFF;
        m_Ks[i * 20 + 20] =
            (((((SBox[bKs(i * 80 + 76)] << 8) ^ SBox[bKs(i * 80 + 79)]) << 8)
                        ^ SBox[bKs(i * 80 + 78)])
                    << 8)
                ^ SBox[bKs(i * 80 + 77)]
                ^ m_Ks[i * 20 + 16]
                ^ w;
        m_Ks[i * 20 + 21] = m_Ks[i * 20 + 17] ^ m_Ks[i * 20 + 20];
        m_Ks[i * 20 + 22] = m_Ks[i * 20 + 17] ^ m_Ks[i * 20 + 18] ^ m_Ks[i * 20 + 20];
        m_Ks[i * 20 + 23] = m_Ks[i * 20 + 19] ^ m_Ks[i * 20 + 22];
        v = w;
        w = (byte) ((v << 1) ^ (((w & 0x80) != 0) ? 0x1B : 0));
        w &= 0xFF;
      }
      m_nCipherRnd = 10;
      indKs = 80 * 2;
    } else if (szKey == 196) {
      for (int i = 0; i < 2; i++) {
        m_Ks[i * 24 + 6] =
            (((((SBox[bKs(i * 96 + 20)] << 8) ^ SBox[bKs(i * 96 + 23)]) << 8)
                        ^ SBox[bKs(i * 96 + 22)])
                    << 8)
                ^ SBox[bKs(i * 96 + 21)]
                ^ m_Ks[i * 24]
                ^ w;
        m_Ks[i * 24 + 7] = m_Ks[i * 24 + 1] ^ m_Ks[i * 24 + 6];
        m_Ks[i * 24 + 8] = m_Ks[i * 24 + 1] ^ m_Ks[i * 24 + 2] ^ m_Ks[i * 24 + 6];
        m_Ks[i * 24 + 9] = m_Ks[i * 24 + 3] ^ m_Ks[i * 24 + 8];
        m_Ks[i * 24 + 10] = m_Ks[i * 24 + 3] ^ m_Ks[i * 24 + 4] ^ m_Ks[i * 24 + 8];
        m_Ks[i * 24 + 11] = m_Ks[i * 24 + 5] ^ m_Ks[i * 24 + 10];
        w <<= 1;
        w &= 0xFF;
        m_Ks[i * 24 + 12] =
            (((((SBox[bKs(i * 96 + 44)] << 8) ^ SBox[bKs(i * 96 + 47)]) << 8)
                        ^ SBox[bKs(i * 96 + 46)])
                    << 8)
                ^ SBox[bKs(i * 96 + 45)]
                ^ m_Ks[i * 24 + 6]
                ^ w;
        m_Ks[i * 24 + 13] = m_Ks[i * 24 + 7] ^ m_Ks[i * 24 + 12];
        m_Ks[i * 24 + 14] = m_Ks[i * 24 + 7] ^ m_Ks[i * 24 + 8] ^ m_Ks[i * 24 + 12];
        m_Ks[i * 24 + 15] = m_Ks[i * 24 + 9] ^ m_Ks[i * 24 + 14];
        m_Ks[i * 24 + 16] = m_Ks[i * 24 + 9] ^ m_Ks[i * 24 + 10] ^ m_Ks[i * 24 + 14];
        m_Ks[i * 24 + 17] = m_Ks[i * 24 + 11] ^ m_Ks[i * 24 + 16];
        w <<= 1;
        w &= 0xFF;
        m_Ks[i * 24 + 18] =
            (((((SBox[bKs(i * 96 + 68)] << 8) ^ SBox[bKs(i * 96 + 71)]) << 8)
                        ^ SBox[bKs(i * 96 + 70)])
                    << 8)
                ^ SBox[bKs(i * 96 + 69)]
                ^ m_Ks[i * 24 + 12]
                ^ w;
        m_Ks[i * 24 + 19] = m_Ks[i * 24 + 13] ^ m_Ks[i * 24 + 18];
        m_Ks[i * 24 + 20] = m_Ks[i * 24 + 13] ^ m_Ks[i * 24 + 14] ^ m_Ks[i * 24 + 18];
        m_Ks[i * 24 + 21] = m_Ks[i * 24 + 15] ^ m_Ks[i * 24 + 20];
        m_Ks[i * 24 + 22] = m_Ks[i * 24 + 15] ^ m_Ks[i * 24 + 16] ^ m_Ks[i * 24 + 20];
        m_Ks[i * 24 + 23] = m_Ks[i * 24 + 17] ^ m_Ks[i * 24 + 22];
        w <<= 1;
        w &= 0xFF;
        m_Ks[i * 24 + 24] =
            (((((SBox[bKs(i * 96 + 92)] << 8) ^ SBox[bKs(i * 96 + 95)]) << 8)
                        ^ SBox[bKs(i * 96 + 94)])
                    << 8)
                ^ SBox[bKs(i * 96 + 93)]
                ^ m_Ks[i * 24 + 18]
                ^ w;
        m_Ks[i * 24 + 25] = m_Ks[i * 24 + 19] ^ m_Ks[i * 24 + 18];
        m_Ks[i * 24 + 26] = m_Ks[i * 24 + 19] ^ m_Ks[i * 24 + 20] ^ m_Ks[i * 24 + 18];
        m_Ks[i * 24 + 27] = m_Ks[i * 24 + 21] ^ m_Ks[i * 24 + 26];
        m_Ks[i * 24 + 28] = m_Ks[i * 24 + 21] ^ m_Ks[i * 24 + 22] ^ m_Ks[i * 24 + 26];
        m_Ks[i * 24 + 29] = m_Ks[i * 24 + 23] ^ m_Ks[i * 24 + 28];
        w <<= 1;
        w &= 0xFF;
      }
      m_nCipherRnd = 12;
      indKs = 96 * 2;
    } else if (szKey == 256) {
      for (int i = 0; i < 7; i++) {
        m_Ks[i * 8 + 8] =
            (((((SBox[bKs(i * 32 + 28)] << 8) ^ SBox[bKs(i * 32 + 31)]) << 8)
                        ^ SBox[bKs(i * 32 + 30)])
                    << 8)
                ^ SBox[bKs(i * 32 + 29)]
                ^ m_Ks[i * 8]
                ^ w;
        m_Ks[i * 8 + 9] = m_Ks[i * 8 + 1] ^ m_Ks[i * 8 + 8];
        m_Ks[i * 8 + 10] = m_Ks[i * 8 + 1] ^ m_Ks[i * 8 + 2] ^ m_Ks[i * 8 + 8];
        m_Ks[i * 8 + 11] = m_Ks[i * 8 + 3] ^ m_Ks[i * 8 + 10];
        w <<= 1;
        w &= 0xFF;
        m_Ks[i * 8 + 12] =
            (((((SBox[bKs(i * 32 + 44)] << 8) ^ SBox[bKs(i * 32 + 47)]) << 8)
                        ^ SBox[bKs(i * 32 + 46)])
                    << 8)
                ^ SBox[bKs(i * 32 + 45)]
                ^ m_Ks[i * 8 + 4];
        m_Ks[i * 8 + 13] = m_Ks[i * 8 + 5] ^ m_Ks[i * 8 + 12];
        m_Ks[i * 8 + 14] = m_Ks[i * 8 + 5] ^ m_Ks[i * 8 + 6] ^ m_Ks[i * 8 + 12];
        m_Ks[i * 8 + 15] = m_Ks[i * 8 + 7] ^ m_Ks[i * 8 + 14];
      }
      m_nCipherRnd = 14;
      indKs = 32 * 7;
    } else {
      m_nCipherRnd = 0;
      return;
    }
    m_Ke[0] = m_Ks[indKs / 4];
    m_Ke[1] = m_Ks[indKs / 4 + 1];
    m_Ke[2] = m_Ks[indKs / 4 + 2];
    m_Ke[3] = m_Ks[indKs / 4 + 3];
    int ind = 0;
    for (int i = (m_nCipherRnd - 1) * 4; i > 0; i--, ind++) {
      indKs += (((i & 3) != 0) ? 1 : -7) * 4;
      long l =
          (long) s_tabIT[3][SBox[bKs(indKs + 15)]]
              ^ s_tabIT[2][SBox[bKs(indKs + 14)]]
              ^ s_tabIT[1][SBox[bKs(indKs + 13)]]
              ^ s_tabIT[0][SBox[bKs(indKs + 12)]];
      l &= 0xFFFFFFFF;
      m_Ke[ind + 4] = (int) l;
    }
    m_Ke[ind + 4] = m_Ks[indKs / 4 - 4];
    m_Ke[ind + 5] = m_Ks[indKs / 4 - 3];
    m_Ke[ind + 6] = m_Ks[indKs / 4 - 2];
    m_Ke[ind + 7] = m_Ks[indKs / 4 - 1];
  }

  final byte[] EncryptData(byte[] data, byte[] key) {
    EncodeKey(key, key.length * 8);
    byte[] crypt = new byte[(data.length + 15) & ~15];
    byte[] block = new byte[16];
    int ib = 0;
    for (int i = 0; i < data.length / 16; i++, ib += 16) {
      for (int k = 0; k < 16; k++) {
        short b = block[k];
        b &= 0xFF;
        b ^= data[ib + k];
        b &= 0xFF;
        block[k] = (byte) b;
      }
      block = EncryptBlock(block);
      System.arraycopy(block, 0, crypt, ib, 16);
    }
    if ((data.length & 0xF) != 0) {
      for (int i = 0; i < (data.length & 0xF); i++) {
        short b = block[i];
        b &= 0xFF;
        b ^= data[ib + i];
        b &= 0xFF;
        block[i] = (byte) b;
      }
      block = EncryptBlock(block);
      System.arraycopy(block, 0, crypt, ib, 16);
    }
    return crypt;
  }

  byte[] DecryptData(byte[] data, byte[] key) {
    EncodeKey(key, key.length * 8);
    byte[] buf = new byte[data.length];
    byte[] block0 = new byte[16];
    byte[] block1 = new byte[16];
    byte[] block2 = new byte[16];
    byte[] block3 = new byte[16];
    byte[] res = new byte[16];
    int ib = 0;
    for (int i = data.length / 16; i > 0; i--, ib += 16) {
      if ((i & 1) == 0) {
        System.arraycopy(data, ib, block0, 0, 16);
        System.arraycopy(data, ib, block2, 0, 16);
        block2 = DecryptBlock(block2);
        for (int k = 0; k < 16; k++) res[k] = (byte) (block1[k] ^ block2[k]);
      } else {
        System.arraycopy(data, ib, block1, 0, 16);
        System.arraycopy(data, ib, block3, 0, 16);
        block3 = DecryptBlock(block3);
        for (int k = 0; k < 16; k++) res[k] = (byte) (block0[k] ^ block3[k]);
      }
      System.arraycopy(res, 0, buf, ib, 16);
    }
    return buf;
  }

  private byte[] DecryptBlock(byte[] data) {
    long[] ind = new long[4];
    long[] w = new long[4];
    ind[0] = m_Ke[0] ^ BitConverter.ToUInt32(data, 0);
    ind[1] = m_Ke[1] ^ BitConverter.ToUInt32(data, 4);
    ind[2] = m_Ke[2] ^ BitConverter.ToUInt32(data, 8);
    ind[3] = m_Ke[3] ^ BitConverter.ToUInt32(data, 12);
    w[0] =
        s_tabIT[0][(int) (ind[0] & 0xFF)]
            ^ s_tabIT[1][(int) ((ind[3] >> 8) & 0xFF)]
            ^ s_tabIT[2][(int) ((ind[2] >> 16) & 0xFF)]
            ^ s_tabIT[3][(int) ((ind[1] >> 24) & 0xFF)]
            ^ m_Ke[4];
    w[0] &= 0xFFFFFFFF;
    w[1] =
        s_tabIT[0][(int) (ind[1] & 0xFF)]
            ^ s_tabIT[1][(int) ((ind[0] >> 8) & 0xFF)]
            ^ s_tabIT[2][(int) ((ind[3] >> 16) & 0xFF)]
            ^ s_tabIT[3][(int) ((ind[2] >> 24) & 0xFF)]
            ^ m_Ke[5];
    w[1] &= 0xFFFFFFFF;
    w[2] =
        s_tabIT[0][(int) (ind[2] & 0xFF)]
            ^ s_tabIT[1][(int) ((ind[1] >> 8) & 0xFF)]
            ^ s_tabIT[2][(int) ((ind[0] >> 16) & 0xFF)]
            ^ s_tabIT[3][(int) ((ind[3] >> 24) & 0xFF)]
            ^ m_Ke[6];
    w[2] &= 0xFFFFFFFF;
    w[3] =
        s_tabIT[0][(int) (ind[3] & 0xFF)]
            ^ s_tabIT[1][(int) ((ind[2] >> 8) & 0xFF)]
            ^ s_tabIT[2][(int) ((ind[1] >> 16) & 0xFF)]
            ^ s_tabIT[3][(int) ((ind[0] >> 24) & 0xFF)]
            ^ m_Ke[7];
    w[3] &= 0xFFFFFFFF;
    ind = Arrays.copyOf(w, w.length);

    int i;
    for (i = 0; i < m_nCipherRnd - 2; i += 2) {
      w[0] =
          s_tabIT[0][(int) (ind[0] & 0xFF)]
              ^ s_tabIT[1][(int) ((ind[3] >> 8) & 0xFF)]
              ^ s_tabIT[2][(int) ((ind[2] >> 16) & 0xFF)]
              ^ s_tabIT[3][(int) ((ind[1] >> 24) & 0xFF)]
              ^ m_Ke[i * 4 + 8];
      w[0] &= 0xFFFFFFFF;
      w[1] =
          s_tabIT[0][(int) (ind[1] & 0xFF)]
              ^ s_tabIT[1][(int) ((ind[0] >> 8) & 0xFF)]
              ^ s_tabIT[2][(int) ((ind[3] >> 16) & 0xFF)]
              ^ s_tabIT[3][(int) ((ind[2] >> 24) & 0xFF)]
              ^ m_Ke[i * 4 + 9];
      w[1] &= 0xFFFFFFFF;
      w[2] =
          s_tabIT[0][(int) (ind[2] & 0xFF)]
              ^ s_tabIT[1][(int) ((ind[1] >> 8) & 0xFF)]
              ^ s_tabIT[2][(int) ((ind[0] >> 16) & 0xFF)]
              ^ s_tabIT[3][(int) ((ind[3] >> 24) & 0xFF)]
              ^ m_Ke[i * 4 + 10];
      w[2] &= 0xFFFFFFFF;
      w[3] =
          s_tabIT[0][(int) (ind[3] & 0xFF)]
              ^ s_tabIT[1][(int) ((ind[2] >> 8) & 0xFF)]
              ^ s_tabIT[2][(int) ((ind[1] >> 16) & 0xFF)]
              ^ s_tabIT[3][(int) ((ind[0] >> 24) & 0xFF)]
              ^ m_Ke[i * 4 + 11];
      w[3] &= 0xFFFFFFFF;
      ind = Arrays.copyOf(w, w.length);
      w[0] =
          s_tabIT[0][(int) (ind[0] & 0xFF)]
              ^ s_tabIT[1][(int) ((ind[3] >> 8) & 0xFF)]
              ^ s_tabIT[2][(int) ((ind[2] >> 16) & 0xFF)]
              ^ s_tabIT[3][(int) ((ind[1] >> 24) & 0xFF)]
              ^ m_Ke[i * 4 + 12];
      w[0] &= 0xFFFFFFFF;
      w[1] =
          s_tabIT[0][(int) (ind[1] & 0xFF)]
              ^ s_tabIT[1][(int) ((ind[0] >> 8) & 0xFF)]
              ^ s_tabIT[2][(int) ((ind[3] >> 16) & 0xFF)]
              ^ s_tabIT[3][(int) ((ind[2] >> 24) & 0xFF)]
              ^ m_Ke[i * 4 + 13];
      w[1] &= 0xFFFFFFFF;
      w[2] =
          s_tabIT[0][(int) (ind[2] & 0xFF)]
              ^ s_tabIT[1][(int) ((ind[1] >> 8) & 0xFF)]
              ^ s_tabIT[2][(int) ((ind[0] >> 16) & 0xFF)]
              ^ s_tabIT[3][(int) ((ind[3] >> 24) & 0xFF)]
              ^ m_Ke[i * 4 + 14];
      w[2] &= 0xFFFFFFFF;
      w[3] =
          s_tabIT[0][(int) (ind[3] & 0xFF)]
              ^ s_tabIT[1][(int) ((ind[2] >> 8) & 0xFF)]
              ^ s_tabIT[2][(int) ((ind[1] >> 16) & 0xFF)]
              ^ s_tabIT[3][(int) ((ind[0] >> 24) & 0xFF)]
              ^ m_Ke[i * 4 + 15];
      w[3] &= 0xFFFFFFFF;
      ind = Arrays.copyOf(w, w.length);
    }
    byte[] crypt = new byte[16];
    System.arraycopy(
        BitConverter.GetBytes(
            InvSBox[(int) (ind[0] & 0xFF)]
                ^ (InvSBox[(int) ((ind[3] >> 8) & 0xFF)] << 8)
                ^ (InvSBox[(int) ((ind[2] >> 16) & 0xFF)] << 16)
                ^ (InvSBox[(int) ((ind[1] >> 24) & 0xFF)] << 24)
                ^ m_Ke[i * 4 + 8]),
        0,
        crypt,
        0,
        4);
    System.arraycopy(
        BitConverter.GetBytes(
            InvSBox[(int) (ind[1] & 0xFF)]
                ^ (InvSBox[(int) ((ind[0] >> 8) & 0xFF)] << 8)
                ^ (InvSBox[(int) ((ind[3] >> 16) & 0xFF)] << 16)
                ^ (InvSBox[(int) ((ind[2] >> 24) & 0xFF)] << 24)
                ^ m_Ke[i * 4 + 9]),
        0,
        crypt,
        4,
        4);
    System.arraycopy(
        BitConverter.GetBytes(
            InvSBox[(int) (ind[2] & 0xFF)]
                ^ (InvSBox[(int) ((ind[1] >> 8) & 0xFF)] << 8)
                ^ (InvSBox[(int) ((ind[0] >> 16) & 0xFF)] << 16)
                ^ (InvSBox[(int) ((ind[3] >> 24) & 0xFF)] << 24)
                ^ m_Ke[i * 4 + 10]),
        0,
        crypt,
        8,
        4);
    System.arraycopy(
        BitConverter.GetBytes(
            InvSBox[(int) (ind[3] & 0xFF)]
                ^ (InvSBox[(int) ((ind[2] >> 8) & 0xFF)] << 8)
                ^ (InvSBox[(int) ((ind[1] >> 16) & 0xFF)] << 16)
                ^ (InvSBox[(int) ((ind[0] >> 24) & 0xFF)] << 24)
                ^ m_Ke[i * 4 + 11]),
        0,
        crypt,
        12,
        4);
    return crypt;
  }
}
