package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

class LoginId {

  public static class GlobalMembers {
    public static long[] t00000B98 = {
      0x0000000000000001L,
      0x0000000000000002L,
      0x0000000000000004L,
      0x0000000000000008L,
      0x0000000000000010L,
      0x0000000000000020L,
      0x0000000000000040L,
      0x0000000000000080L,
      0x0000000000000100L,
      0x0000000000000200L,
      0x0000000000000400L,
      0x0000000000000800L,
      0x0000000000001000L,
      0x0000000000002000L,
      0x0000000000004000L,
      0x0000000000008000L,
      0x0000000000010000L,
      0x0000000000020000L,
      0x0000000000040000L,
      0x0000000000080000L,
      0x0000000000100000L,
      0x0000000000200000L,
      0x0000000000400000L,
      0x0000000000800000L,
      0x0000000001000000L,
      0x0000000002000000L,
      0x0000000004000000L,
      0x0000000008000000L,
      0x0000000010000000L,
      0x0000000020000000L,
      0x0000000040000000L,
      0x0000000080000000L,
      0x0000000100000000L,
      0x0000000200000000L,
      0x0000000400000000L,
      0x0000000800000000L,
      0x0000001000000000L,
      0x0000002000000000L,
      0x0000004000000000L,
      0x0000008000000000L,
      0x0000010000000000L,
      0x0000020000000000L,
      0x0000040000000000L,
      0x0000080000000000L,
      0x0000100000000000L,
      0x0000200000000000L,
      0x0000400000000000L,
      0x0000800000000000L,
      0x0001000000000000L,
      0x0002000000000000L,
      0x0004000000000000L,
      0x0008000000000000L,
      0x0010000000000000L,
      0x0020000000000000L,
      0x0040000000000000L,
      0x0080000000000000L,
      0x0100000000000000L,
      0x0200000000000000L,
      0x0400000000000000L,
      0x0800000000000000L,
      0x1000000000000000L,
      0x2000000000000000L,
      0x4000000000000000L,
      0x8000000000000000L
    };
  }

  long s0;
  long s8;
  long s10;
  long s18;
  byte[] s20 = new byte[1024];
  int s420;
  int _align;
  long s428;

  public long Decode(byte[] data) {
    long[] d = new long[data.length / 8];
    for (int i = 0; i < d.length; i++) {
      d[i] = BitConverter.ToUInt64(data, i * 8);
    }
    return Decode(d, (int) d.length, 0);
  }

  long Decode(long[] pData, int szData, int arg8) {
    long[] data = new long[53];

    // ORIGINAL LINE: long& v0 = data[0];
    long v0 = data[0];

    // ORIGINAL LINE: long& v8 = data[1];
    long v8 = data[1];

    // ORIGINAL LINE: long& v10 = data[2];
    long v10 = data[2];

    // ORIGINAL LINE: long& v18 = data[3];
    long v18 = data[3];

    // ORIGINAL LINE: long& v20 = data[4];
    long v20 = data[4];

    // ORIGINAL LINE: long& v28 = data[5];
    long v28 = data[5];

    // ORIGINAL LINE: long& v30 = data[6];
    long v30 = data[6];

    // ORIGINAL LINE: long& v38 = data[7];
    long v38 = data[7];

    // ORIGINAL LINE: long& v40 = data[8];
    long v40 = data[8];

    // ORIGINAL LINE: long& v48 = data[9];
    long v48 = data[9];

    // ORIGINAL LINE: long& v50 = data[10];
    long v50 = data[10];

    // ORIGINAL LINE: long& v58 = data[11];
    long v58 = data[11];

    // ORIGINAL LINE: long& v60 = data[12];
    long v60 = data[12];

    // ORIGINAL LINE: long& v68 = data[13];
    long v68 = data[13];

    // ORIGINAL LINE: long& v70 = data[14];
    long v70 = data[14];

    // ORIGINAL LINE: long& v78 = data[15];
    long v78 = data[15];

    // ORIGINAL LINE: long& v80 = data[16];
    long v80 = data[16];

    // ORIGINAL LINE: long& v88 = data[17];
    long v88 = data[17];

    // ORIGINAL LINE: long& v90 = data[18];
    long v90 = data[18];

    // ORIGINAL LINE: long& v98 = data[19];
    long v98 = data[19];

    // ORIGINAL LINE: long& vA0 = data[20];
    long vA0 = data[20];

    // ORIGINAL LINE: long& vA8 = data[21];
    long vA8 = data[21];

    // ORIGINAL LINE: long& vB0 = data[22];
    long vB0 = data[22];

    // ORIGINAL LINE: long& vB8 = data[23];
    long vB8 = data[23];

    // ORIGINAL LINE: long& vC0 = data[24];
    long vC0 = data[24];

    // ORIGINAL LINE: long& vC8 = data[25];
    long vC8 = data[25];

    // ORIGINAL LINE: long& vD0 = data[26];
    long vD0 = data[26];

    // ORIGINAL LINE: long& vD8 = data[27];
    long vD8 = data[27];

    // ORIGINAL LINE: long& vE0 = data[28];
    long vE0 = data[28];

    // ORIGINAL LINE: long& vE8 = data[29];
    long vE8 = data[29];

    // ORIGINAL LINE: long& vF0 = data[30];
    long vF0 = data[30];

    // ORIGINAL LINE: long& vF8 = data[31];
    long vF8 = data[31];

    // ORIGINAL LINE: long& v100 = data[32];
    long v100 = data[32];

    // ORIGINAL LINE: long& v108 = data[33];
    long v108 = data[33];

    // ORIGINAL LINE: long& v110 = data[34];
    long v110 = data[34];

    // ORIGINAL LINE: long& v118 = data[35];
    long v118 = data[35];

    // ORIGINAL LINE: long& v120 = data[36];
    long v120 = data[36];

    // ORIGINAL LINE: long& v128 = data[37];
    long v128 = data[37];

    // ORIGINAL LINE: long& v130 = data[38];
    long v130 = data[38];

    // ORIGINAL LINE: long& v138 = data[39];
    long v138 = data[39];

    // ORIGINAL LINE: long& v140 = data[40];
    long v140 = data[40];

    // ORIGINAL LINE: long& v148 = data[41];
    long v148 = data[41];

    // ORIGINAL LINE: long& v150 = data[42];
    long v150 = data[42];

    // ORIGINAL LINE: long& v158 = data[43];
    long v158 = data[43];

    // ORIGINAL LINE: long& v160 = data[44];
    long v160 = data[44];

    // ORIGINAL LINE: long& v168 = data[45];
    long v168 = data[45];

    // ORIGINAL LINE: long& v170 = data[46];
    long v170 = data[46];

    // ORIGINAL LINE: long& v178 = data[47];
    long v178 = data[47];

    // ORIGINAL LINE: long& v180 = data[48];
    long v180 = data[48];

    // ORIGINAL LINE: long& v188 = data[49];
    long v188 = data[49];

    // ORIGINAL LINE: long& v190 = data[50];
    long v190 = data[50];

    // ORIGINAL LINE: long& v198 = data[51];
    long v198 = data[51];

    // ORIGINAL LINE: long& v1A0 = data[52];
    long v1A0 = data[52];
    //	LONGLONG v0, v8, v10, v18, v20, v28, v30, v38, v40, v48, v58, v60, v68, v78, v80, v88, v90,
    // v98, vA0, vA8, vB0, vB8, vC0, vC8,
    //		vD0, vD8, vE0, vF0, vF8, v100, v108, v110, v118, v128, v138, v140, v148, v150, v158, v160,
    // v168, v170, v178, v180, v190, v198;
    // BEGIN_PARSE (��� ��������� �� END_PARSE ������ ���� ������������� � �� ��������� ������������
    // (� style))
    v158 = pData[0];
    v140 = 0;
    s8 = 0;
    while (true) {
      l00C144D7:
      v70 = v158;
      vD0 = v158;
      vC8 = pData[(int) s8];
      v88 = 0;
      v98 = 0;
      for (int i = 0; i < 0x40; i++) {
        vA0 = Convert.ToUInt64((vC8 & GlobalMembers.t00000B98[i]) != 0);
        v78 = Convert.ToUInt64((vD0 & GlobalMembers.t00000B98[i]) != 0);
        v48 = vA0 ^ v78 ^ v98;
        v98 =
            ((Convert.ToBoolean(vA0) & Convert.ToBoolean(v78))
                    || (Convert.ToBoolean(vA0) & Convert.ToBoolean(v98)))
                ? 1
                : (v78 & v98);
        v68 = v48 != 0L ? ~0L : 0L;
        v88 |= GlobalMembers.t00000B98[i] & v68;
      }
      vD0 = v88;
      vC8 = ~v70 + 1;
      v88 = 0;
      v98 = 0;
      for (int i = 0; i < 0x40; i++) {
        vA0 = Convert.ToUInt64((vD0 & GlobalMembers.t00000B98[i]) != 0);
        v78 = Convert.ToUInt64((vC8 & GlobalMembers.t00000B98[i]) != 0);
        vD8 = vA0 ^ v78 ^ v98;
        v98 = (Convert.ToBoolean(vA0 & v78) || Convert.ToBoolean(vA0 & v98)) ? 1 : (v78 & v98);
        v58 = vD8 != 0L ? ~0L : 0L;
        v88 |= GlobalMembers.t00000B98[i] & v58;
      }
      v100 = 0x1C;
      v138 = 0x15;
      // MAKELONGLONG(HIDWORD(v88), LODWORD(v88));
      v150 =
          ((long)
              ((((((((long) (v88)) >>> 32) & 0xffffffff))))
                  | (((long) ((((((long) (v88)) & 0xffffffff))))) << 32)));
      v128 = ~v138 + 1;
      v78 = 0;
      vB0 = 0;
      for (int i = 0; i < 0x40; i++) {
        vB8 = Convert.ToUInt64((v100 & GlobalMembers.t00000B98[i]) != 0);
        v98 = Convert.ToUInt64((v128 & GlobalMembers.t00000B98[i]) != 0);
        v90 = vB8 ^ v98 ^ vB0;
        vB0 = (Convert.ToBoolean(vB8 & v98) || Convert.ToBoolean(vB0 & vB8)) ? 1 : (v98 & vB0);
        v68 = v90 != 0 ? ~0L : 0L;
        v78 |= GlobalMembers.t00000B98[i] & v68;
      }
      v150 =
          ((long)
              (((((((long) (v150)) & 0xffffffff)) >>> 0x15))
                  | (((long)
                          ((((((((long) (v150)) >>> 32) & 0xffffffff)) >>> 0x15)
                              | (((((long) (v150)) & 0xffffffff)) << (32 - 0x15)))))
                      << 32)));
      v0 = v78 + 1;
      v18 = ~0L << ((int) (((long) (v0)) & 0xffffffff));
      v20 =
          ((long)
              ((((((((long) (v150)) >>> 32) & 0xffffffff))))
                  | (((long) ((((((long) (v150)) & 0xffffffff))))) << 32)));
      v140 = v20 & ~v18;
      vC8 = 0xD8;
      vA8 = v140;
      vD0 = ~vC8 + 1;
      vC0 = 0;
      vB8 = 0;
      for (int i = 0; i < 0x40; i++) {
        v88 = Convert.ToUInt64((vA8 & GlobalMembers.t00000B98[i]) != 0);
        vA0 = Convert.ToUInt64((vD0 & GlobalMembers.t00000B98[i]) != 0);
        v68 = v88 ^ vA0 ^ vB8;
        vB8 = (Convert.ToBoolean(v88 & vA0) || Convert.ToBoolean(vB8 & v88)) ? 1 : (vA0 & vB8);
        v60 = v68 != 0 ? ~0L : 0L;
        vC0 |= GlobalMembers.t00000B98[i] & v60;
      }
      if (vC0 == 0) {
        v18 = s0;
        s0 = 0;
        return v18;
      }
      s10 = pData[((int) (((long) (s8 + 1)) & 0xffffffff))];
      v60 = s8;
      v68 = 2;
      do {
        v68 = v60 ^ v68;
        v60 = (v60 & ~v68) << 1;
      } while (v60 != 0);
      s18 = pData[((int) (((long) (v68)) & 0xffffffff))];
      v100 = 0x1C;
      v138 = 0x15;
      v150 =
          ((long)
              ((((((((long) (s10)) >>> 32) & 0xffffffff))))
                  | (((long) ((((((long) (s10)) & 0xffffffff))))) << 32)));
      v128 = ~v138 + 1;
      v78 = 0;
      vB0 = 0;
      for (int i = 0; i < 0x40; i++) {
        vB8 = Convert.ToUInt64((v100 & GlobalMembers.t00000B98[i]) != 0);
        v98 = Convert.ToUInt64((v128 & GlobalMembers.t00000B98[i]) != 0);
        v90 = vB8 ^ v98 ^ vB0;
        vB0 = (Convert.ToBoolean(vB8 & v98) || Convert.ToBoolean(vB0 & vB8)) ? 1 : (v98 & vB0);
        v68 = v90 != 0 ? ~0L : 0;
        v78 |= GlobalMembers.t00000B98[i] & v68;
      }
      v150 =
          ((long)
              (((((((long) (v150)) & 0xffffffff)) >>> 0x15))
                  | (((long)
                          ((((((((long) (v150)) >>> 32) & 0xffffffff)) >>> 0x15)
                              | (((((long) (v150)) & 0xffffffff)) << (32 - 0x15)))))
                      << 32)));
      v0 = v78 + 1;
      v18 = ~0L << ((int) (((long) (v0)) & 0xffffffff));
      v20 =
          ((long)
              ((((((((long) (v150)) >>> 32) & 0xffffffff))))
                  | (((long) ((((((long) (v150)) & 0xffffffff))))) << 32)));
      v68 = v20 & ~v18;
      v80 = ~(v68 ^ ~0xF5L);
      if (v80 == 0) {
        s10 = s0;
      }
      vC8 = 0x54;
      vA8 = v140;
      vD0 = ~vC8 + 1;
      vC0 = 0;
      vB8 = 0;
      for (int i = 0; i < 0x40; i++) {
        v88 = Convert.ToUInt64((vA8 & GlobalMembers.t00000B98[i]) != 0);
        vA0 = Convert.ToUInt64((vD0 & GlobalMembers.t00000B98[i]) != 0);
        v78 = v88 ^ vA0 ^ vB8;
        vB8 = (Convert.ToBoolean(v88 & vA0) || Convert.ToBoolean(v88 & vB8)) ? 1 : (vB8 & vA0);
        v68 = v78 != 0 ? ~0L : 0L;
        vC0 |= GlobalMembers.t00000B98[i] & v68;
      }
      if (vC0 == 0) {
        s0 = s10 & s18;
      }
      v128 = 0x70;
      vC8 = v140;
      v100 = ~v128 + 1;
      vA0 = 0;
      v90 = 0;
      for (int i = 0; i < 0x40; i++) {
        v78 = Convert.ToUInt64((vC8 & GlobalMembers.t00000B98[i]) != 0);
        vB8 = Convert.ToUInt64((v100 & GlobalMembers.t00000B98[i]) != 0);
        v58 = v78 ^ vB8 ^ v90;
        v90 = (Convert.ToBoolean(v78 & vB8) || Convert.ToBoolean(v78 & v90)) ? 1 : (vB8 & v90);
        v68 = v58 != 0 ? ~0L : 0;
        vA0 |= GlobalMembers.t00000B98[i] & v68;
      }
      if (vA0 == 0) {
        s0 = s10 | s18;
      }
      v100 = 0x91;
      vD0 = v140;
      vC8 = ~v100 + 1;
      v88 = 0;
      v98 = 0;
      for (int i = 0; i < 0x40; i++) {
        vA0 = Convert.ToUInt64((vD0 & GlobalMembers.t00000B98[i]) != 0);
        v78 = Convert.ToUInt64((vC8 & GlobalMembers.t00000B98[i]) != 0);
        v68 = vA0 ^ v78 ^ v98;
        v98 = (Convert.ToBoolean(v78 & vA0) || Convert.ToBoolean(vA0 & v98)) ? 1 : (v78 & v98);
        v60 = v68 != 0 ? ~0L : 0;
        v88 |= GlobalMembers.t00000B98[i] & v60;
      }
      if (v88 == 0) {
        s0 = s10 ^ s18;
      }
      v100 = 0xAB;
      vD0 = v140;
      vC8 = ~v100 + 1;
      v88 = 0;
      v98 = 0;
      for (int i = 0; i < 0x40; i++) {
        vA0 = Convert.ToUInt64((vD0 & GlobalMembers.t00000B98[i]) != 0);
        v78 = Convert.ToUInt64((vC8 & GlobalMembers.t00000B98[i]) != 0);
        v58 = vA0 ^ v78 ^ v98;
        v98 = (Convert.ToBoolean(vA0 & v78) || Convert.ToBoolean(vA0 & v98)) ? 1 : (v78 & v98);
        v68 = v58 != 0 ? ~0L : 0;
        v88 |= GlobalMembers.t00000B98[i] & v68;
      }
      if (v88 == 0) {
        v58 = s10;
        v60 = s18;
        do {
          v60 = v58 ^ v60;
          v58 = (v58 & ~v60) << 1;
        } while (v58 != 0);
        s0 = v60;
      }
      vD0 = 0xA9;
      vB0 = v140;
      vA8 = ~vD0 + 1;
      vE0 = 0;
      v78 = 0;
      for (int i = 0; i < 0x40; i++) {
        vC0 = Convert.ToUInt64((vB0 & GlobalMembers.t00000B98[i]) != 0);
        v88 = Convert.ToUInt64((vA8 & GlobalMembers.t00000B98[i]) != 0);
        v68 = vC0 ^ v88 ^ v78;
        v78 = (Convert.ToBoolean(vC0 & v88) || Convert.ToBoolean(vC0 & v78)) ? 1 : (v88 & v78);
        v58 = v68 != 0 ? ~0L : 0;
        vE0 |= GlobalMembers.t00000B98[i] & v58;
      }
      if (vE0 == 0) {
        v40 = s10;
        v48 = ~s18 + 1;
        do {
          v48 = v40 ^ v48;
          v40 = (v40 & ~v48) << 1;
        } while (v40 != 0);
        s0 = v48;
      }
      vD0 = 0xB1;
      vB0 = v140;
      vA8 = ~vD0 + 1;
      vE0 = 0;
      v78 = 0;
      for (int i = 0; i < 0x40; i++) {
        vC0 = Convert.ToUInt64((vB0 & GlobalMembers.t00000B98[i]) != 0);
        v88 = Convert.ToUInt64((vA8 & GlobalMembers.t00000B98[i]) != 0);
        vA0 = vC0 ^ v88 ^ v78;
        v78 = (Convert.ToBoolean(vC0 & v88) || Convert.ToBoolean(vC0 & v78)) ? 1 : (v88 & v78);
        v68 = vA0 != 0 ? ~0L : 0;
        vE0 |= GlobalMembers.t00000B98[i] & v68;
      }
      if (vE0 == 0) {
        v150 = Long.remainderUnsigned(s18, 24);
        v70 = s10;
        v148 = 0;
        while (true) {
          v98 = v148;
          vB8 = v150;
          v90 = ~vB8 + 1;
          v160 = 0;
          v178 = 1;
          v170 = 1;
          v168 = 0;
          vB0 = 0;
          vA8 = 0;
          vD0 = 0;
          vC8 = 0;
          for (int i = 0; i < 0x40; i++) {
            vB0 = Convert.ToUInt64((v98 & GlobalMembers.t00000B98[i]) != 0);
            vA8 = Convert.ToUInt64((vB8 & GlobalMembers.t00000B98[i]) != 0);
            vD0 = Convert.ToUInt64((v90 & GlobalMembers.t00000B98[i]) != 0);
            if (vB0 != 0 && vA8 == 0) {
              v160 = 0;
            }
            if (vB0 == 0 && vA8 != 0) {
              v160 = 1;
            }
            v180 = vB0 ^ vD0 ^ vC8;
            vC8 = (Convert.ToBoolean(vB0 & vD0) || Convert.ToBoolean(vB0 & vC8)) ? 1 : (vD0 & vC8);
            if (v180 != 0) {
              v170 = 0;
              if (i <= 7) {
                v178 = Convert.ToUInt64(v178 == 0);
              }
            }
          }
          if (vB0 == 0 && vA8 != 0 && v180 != 0) {
            v168 = 1;
          }
          if (vB0 != 0 && vA8 == 0 && v180 == 0) {
            v168 = 1;
          }
          if (v160 == 0) {
            s0 = v70;
            break;
          }
          vD0 = v70;
          vC8 = v70;
          v88 = 0;
          v98 = 0;
          for (int i = 0; i < 0x40; i++) {
            vA0 = Convert.ToUInt64((vC8 & GlobalMembers.t00000B98[i]) != 0);
            v78 = Convert.ToUInt64((vD0 & GlobalMembers.t00000B98[i]) != 0);
            v48 = vA0 ^ v78 ^ v98;
            v98 = (Convert.ToBoolean(vA0 & v78) || Convert.ToBoolean(vA0 & v98)) ? 1 : (v78 & v98);
            v68 = v48 != 0 ? ~0L : 0;
            v88 |= GlobalMembers.t00000B98[i] & v68;
          }
          v70 = v88;
          v148++;
        }
      }
      v80 = v140 ^ 0xC8;
      if (v80 == 0) {
        v1A0 = Long.remainderUnsigned(s18, 24);
        v190 = s10;
        v100 = 0;
        v160 = 0;
        while (true) {
          v110 = v160;
          vF0 = 0x40;
          vE0 = ~vF0 + 1;
          v178 = 0;
          v170 = 1;
          v180 = 1;
          v198 = 0;
          vC0 = 0;
          v88 = 0;
          vA0 = 0;
          v78 = 0;
          for (int i = 0; i < 0x40; i++) {
            vC0 = Convert.ToUInt64((v110 & GlobalMembers.t00000B98[i]) != 0);
            v88 = Convert.ToUInt64((vF0 & GlobalMembers.t00000B98[i]) != 0);
            vA0 = Convert.ToUInt64((vE0 & GlobalMembers.t00000B98[i]) != 0);
            if (vC0 != 0 && v88 == 0) {
              v178 = 0;
            }
            if (vC0 == 0 && v88 != 0) {
              v178 = 1;
            }
            v168 = vC0 ^ vA0 ^ v78;
            v78 = (Convert.ToBoolean(vC0 & vA0) || Convert.ToBoolean(vC0 & v78)) ? 1 : (vA0 & v78);
            if (v168 != 0) {
              v180 = 0;
              if (i <= 7) {
                v170 = Convert.ToUInt64(v170 == 0);
              }
            }
          }
          if (vC0 == 0 && v88 != 0 && v168 != 0) {
            v198 = 1;
          }
          if (vC0 != 0 && v88 == 0 && v168 == 0) {
            v198 = 1;
          }
          if (v178 == 0) {
            s0 = v100;
            break;
          }
          v80 = v190 & GlobalMembers.t00000B98[(int) v160];
          if (v80 != 0 && (v160 >= v1A0)) {
            vD0 = v1A0;
            vB0 = v160;
            vA8 = ~vD0 + 1;
            vE0 = 0;
            v78 = 0;
            for (int i = 0; i < 0x40; i++) {
              vC0 = Convert.ToUInt64((vB0 & GlobalMembers.t00000B98[i]) != 0);
              v88 = Convert.ToUInt64((vA8 & GlobalMembers.t00000B98[i]) != 0);
              vA0 = vC0 ^ v88 ^ v78;
              v78 =
                  (Convert.ToBoolean(vC0 & v88) || Convert.ToBoolean(vC0 & v78)) ? 1 : (v88 & v78);
              v58 = vA0 != 0 ? ~0L : 0;
              vE0 |= GlobalMembers.t00000B98[i] & v58;
            }
            v100 ^= GlobalMembers.t00000B98[(int) vE0];
          }
          v160++;
        }
      }
      s8 += 3;
    } //	goto l00C144D7; // repid main cycle
    // END_PARSE
  }

  public long DecodeData(byte[] data) {
    long[] d = new long[data.length / 8];
    for (int i = 0; i < d.length; i++) {
      d[i] = BitConverter.ToUInt64(data, i * 8);
    }
    return DecodeData(d, d.length);
  }

  // +4012D0 gp
  public long DecodeData(long[] pData, int szData) {
    long id = 0;
    for (int i = 0; i < szData; i += 3) {
      byte depth = (byte) (pData[i] >>> 14);
      long value = pData[i + 2];
      if (((pData[i + 1] & 0x3FC000) ^ 0x14C000) != 0) {
        id = pData[i + 1];
      }
      switch (depth) {
        case 0x12:
          id |= value;
          break;
        case 0x3A:
          id -= value;
          break;
        case 0x40:
          id >>>= (int) Long.remainderUnsigned(value, 24);
          break;
        case 0x79:
          id ^= value;
          break;
        case (byte) 0x8B:
          id += value;
          break;
        case (byte) 0xB5:
          id = (id & 0xffffffffL) | (((value >>> 32) & 0xffffffffL) << 32);
          break;
        case (byte) 0xC2:
          id <<= (int) Long.remainderUnsigned(value, 24);
          break;
        case (byte) 0xE1:
          id &= value;
          break;
        default:
          return 0;
      }
    }
    return id;
  }
}
