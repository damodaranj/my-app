package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import static io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.String8.dataDistance;
import static io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.String8.nnc;

class Decompressor {

  static byte[] decompress(byte[] src, int maxSize) {
    int dLen = maxSize;
    byte[] data;
    byte[] dst = new byte[dLen + 0x800];
    String8 s = String8.fromData(src);
    String8 d = String8.fromData(dst);
    int[] dLenPointer = new int[] {dLen};
    int res = new Decompressor().inflate(s, src.length, d, dLenPointer);
    byte[] buf = new byte[dLenPointer[0]];
    for (int i = 0; i < dLenPointer[0]; i++) buf[i] = d.get(i);
    return buf;
  }

  public static final int M2_MAX_OFFSET = 0x0800;

  private static long pd_U(String8 a_U, String8 b_U) {
    return dataDistance(b_U, a_U);
  }

  private static void memcpyDs(String8[] dest_U, String8[] src_U, long[] len_U) {
    do {
      (dest_U[0] = String8.nnc(dest_U[0]).shift(1))
          .set(-1, (src_U[0] = nnc(src_U[0]).shift(1)).get(-1));
    } while (Long.compareUnsigned(len_U[0] = len_U[0] - 1, 0) > 0);
  }

  static int inflate(String8 pSrc_U, int szSrc_U, String8 pDst_U, int[] pszDst_U) {
    //	if (!pSrc || (szSrc < 3) || !pszDst || !pDst || (*pszDst < 1))
    //		return(0);
    long[] t_U = null;
    String8[] ip_U = null;
    String8[] op_U = null;
    String8 ipEnd_U = null;
    String8 opEnd_U = null;
    String8[] mPos_U = null;

    final int posFirstLiteralRun = 1,
        posMatch = 2,
        posCopyMatch = 3,
        posMatchDone = 4,
        posMatchNext = 5,
        posEofFound = 6,
        posInputOverrun = 7,
        posOutputOverrun = 8,
        posLookbehindOverrun = 9;
    positionLoop:
    for (int pos = 0; true; )
      switch (pos) {
        default:
          t_U = new long[1];
          ip_U = new String8[] {pSrc_U};
          op_U = new String8[] {pDst_U};
          ipEnd_U = nnc(pSrc_U).shift(szSrc_U);
          opEnd_U = nnc(pDst_U).shift(pszDst_U[0]);
          mPos_U = new String8[1];
          pszDst_U[0] = 0;
          if (ipEnd_U.get(-1) != 0
              || ipEnd_U.get(-2) != 0
              || Byte.toUnsignedInt(ipEnd_U.get(-3)) != 17) {
            return 0;
          }
          if (Byte.toUnsignedInt(ip_U[0].get()) > 17) {
            t_U[0] = Byte.toUnsignedInt((ip_U[0] = nnc(ip_U[0]).shift(1)).get(-1)) - 17;
            if (Long.compareUnsigned(t_U[0], 4) < 0) {
              pos = posMatchNext;
              continue positionLoop;
            }
            if (Long.compareUnsigned(dataDistance(op_U[0], opEnd_U), t_U[0]) < 0) {
              pos = posOutputOverrun;
              continue positionLoop;
            }
            if (Long.compareUnsigned(dataDistance(ip_U[0], ipEnd_U), t_U[0] + 1) < 0) {
              pos = posInputOverrun;
              continue positionLoop;
            }
            memcpyDs(op_U, ip_U, t_U);
            pos = posFirstLiteralRun;
            continue positionLoop;
          }
        case posFirstLiteralRun:
        case posMatch:
        case posCopyMatch:
        case posMatchDone:
        case posMatchNext:
          forLoop:
          for (; ; pos = 0)
            switch (pos) {
              default:
                // if(dataAddress(ip_U[0]) >= dataAddress(ipEnd_U))
                if (dataDistance(ip_U[0], ipEnd_U) <= 0) {
                  break forLoop;
                }
                t_U[0] = Byte.toUnsignedLong((ip_U[0] = nnc(ip_U[0]).shift(1)).get(-1));
                if (Long.compareUnsigned(t_U[0], 16) >= 0) {
                  pos = posMatch;
                  continue positionLoop;
                }
                if (t_U[0] == 0) {
                  if (Long.compareUnsigned(dataDistance(ip_U[0], ipEnd_U), (long) 0) < 0) {
                    pos = posInputOverrun;
                    continue positionLoop;
                  }
                  while (Byte.toUnsignedInt(ip_U[0].get()) == 0) {
                    t_U[0] = t_U[0] + 255;
                    ip_U[0] = nnc(ip_U[0]).shift(1);
                    if (Long.compareUnsigned(dataDistance(ip_U[0], ipEnd_U), (long) 0) < 0) {
                      pos = posInputOverrun;
                      continue positionLoop;
                    }
                  }
                  t_U[0] =
                      t_U[0] + (15 + Byte.toUnsignedInt((ip_U[0] = nnc(ip_U[0]).shift(1)).get(-1)));
                }
                if (Long.compareUnsigned(dataDistance(op_U[0], opEnd_U), t_U[0] + 3) < 0) {
                  pos = posOutputOverrun;
                  continue positionLoop;
                }
                if (Long.compareUnsigned(dataDistance(ip_U[0], ipEnd_U), t_U[0] + 4) < 0) {
                  pos = posInputOverrun;
                  continue positionLoop;
                }
                (op_U[0] = nnc(op_U[0]).shift(1))
                    .set(-1, (ip_U[0] = nnc(ip_U[0]).shift(1)).get(-1));
                (op_U[0] = nnc(op_U[0]).shift(1))
                    .set(-1, (ip_U[0] = nnc(ip_U[0]).shift(1)).get(-1));
                (op_U[0] = nnc(op_U[0]).shift(1))
                    .set(-1, (ip_U[0] = nnc(ip_U[0]).shift(1)).get(-1));
                memcpyDs(op_U, ip_U, t_U);
              case posFirstLiteralRun:
                t_U[0] = Byte.toUnsignedLong((ip_U[0] = nnc(ip_U[0]).shift(1)).get(-1));
                if (Long.compareUnsigned(t_U[0], 16) >= 0) {
                  pos = posMatch;
                  continue positionLoop;
                }
                mPos_U[0] = nnc(op_U[0]).shift(-(1 + M2_MAX_OFFSET));
                mPos_U[0] = nnc(mPos_U[0]).shift((int) -(t_U[0] >>> 2));
                mPos_U[0] =
                    nnc(mPos_U[0])
                        .shift(
                            -(Byte.toUnsignedInt((ip_U[0] = nnc(ip_U[0]).shift(1)).get(-1)) << 2));
                // if(dataAddress(mPos_U[0]) < dataAddress(pDst_U)) {
                if (dataDistance(mPos_U[0], pDst_U) > 0) {
                  pos = posLookbehindOverrun;
                  continue positionLoop;
                }
                if (Long.compareUnsigned(dataDistance(op_U[0], opEnd_U), (long) 3) < 0) {
                  pos = posOutputOverrun;
                  continue positionLoop;
                }
                (op_U[0] = nnc(op_U[0]).shift(1))
                    .set(-1, (mPos_U[0] = nnc(mPos_U[0]).shift(1)).get(-1));
                (op_U[0] = nnc(op_U[0]).shift(1))
                    .set(-1, (mPos_U[0] = nnc(mPos_U[0]).shift(1)).get(-1));
                (op_U[0] = nnc(op_U[0]).shift(1))
                    .set(-1, (mPos_U[0] = nnc(mPos_U[0]).shift(1)).get(-1));
                pos = posMatchDone;
                continue positionLoop;
              case posMatch:
              case posCopyMatch:
              case posMatchDone:
              case posMatchNext:
                forLoop2:
                for (; ; pos = 0)
                  switch (pos) {
                    default:
                      if (Long.compareUnsigned(t_U[0], 64) >= 0) {
                        mPos_U[0] = nnc(op_U[0]).shift(-1);
                        mPos_U[0] = nnc(mPos_U[0]).shift((int) -(t_U[0] >>> 2 & 7));
                        mPos_U[0] =
                            nnc(mPos_U[0])
                                .shift(
                                    -(Byte.toUnsignedInt((ip_U[0] = nnc(ip_U[0]).shift(1)).get(-1))
                                        << 3));
                        t_U[0] = (t_U[0] >>> 5) - 1;
                        pos = posCopyMatch;
                        continue positionLoop;
                      } else if (Long.compareUnsigned(t_U[0], 32) >= 0) {
                        t_U[0] = t_U[0] & 31;
                        if (t_U[0] == 0) {
                          if (Long.compareUnsigned(dataDistance(ip_U[0], ipEnd_U), (long) 0) < 0) {
                            pos = posInputOverrun;
                            continue positionLoop;
                          }
                          while (Byte.toUnsignedInt(ip_U[0].get()) == 0) {
                            t_U[0] = t_U[0] + 255;
                            ip_U[0] = nnc(ip_U[0]).shift(1);
                            if (Long.compareUnsigned(dataDistance(ip_U[0], ipEnd_U), (long) 0)
                                < 0) {
                              pos = posInputOverrun;
                              continue positionLoop;
                            }
                          }
                          t_U[0] =
                              t_U[0]
                                  + (31
                                      + Byte.toUnsignedInt(
                                          (ip_U[0] = nnc(ip_U[0]).shift(1)).get(-1)));
                        }
                        mPos_U[0] = nnc(op_U[0]).shift(-1);
                        mPos_U[0] =
                            nnc(mPos_U[0])
                                .shift(
                                    -((Byte.toUnsignedInt(ip_U[0].get(0)) >> 2)
                                        + (Byte.toUnsignedInt(ip_U[0].get(1)) << 6)));
                        ip_U[0] = nnc(ip_U[0]).shift(2);
                      } else if (Long.compareUnsigned(t_U[0], 16) >= 0) {
                        mPos_U[0] = op_U[0];
                        mPos_U[0] = nnc(mPos_U[0]).shift((int) -((t_U[0] & 8) << 11));
                        t_U[0] = t_U[0] & 7;
                        if (t_U[0] == 0) {
                          if (Long.compareUnsigned(dataDistance(ip_U[0], ipEnd_U), (long) 0) < 0) {
                            pos = posInputOverrun;
                            continue positionLoop;
                          }
                          while (Byte.toUnsignedInt(ip_U[0].get()) == 0) {
                            t_U[0] = t_U[0] + 255;
                            ip_U[0] = nnc(ip_U[0]).shift(1);
                            if (Long.compareUnsigned(dataDistance(ip_U[0], ipEnd_U), (long) 0)
                                < 0) {
                              pos = posInputOverrun;
                              continue positionLoop;
                            }
                          }
                          t_U[0] =
                              t_U[0]
                                  + (7
                                      + Byte.toUnsignedInt(
                                          (ip_U[0] = nnc(ip_U[0]).shift(1)).get(-1)));
                        }
                        mPos_U[0] =
                            nnc(mPos_U[0])
                                .shift(
                                    -((Byte.toUnsignedInt(ip_U[0].get(0)) >> 2)
                                        + (Byte.toUnsignedInt(ip_U[0].get(1)) << 6)));
                        ip_U[0] = nnc(ip_U[0]).shift(2);
                        // if(dataAddress(mPos_U[0]) == dataAddress(op_U[0])) {
                        if (dataDistance(mPos_U[0], op_U[0]) == 0) {
                          pos = posEofFound;
                          continue positionLoop;
                        }
                        mPos_U[0] = nnc(mPos_U[0]).shift(-0x4000);
                      } else {
                        mPos_U[0] = nnc(op_U[0]).shift(-1);
                        mPos_U[0] = nnc(mPos_U[0]).shift((int) -(t_U[0] >>> 2));
                        mPos_U[0] =
                            nnc(mPos_U[0])
                                .shift(
                                    -(Byte.toUnsignedInt((ip_U[0] = nnc(ip_U[0]).shift(1)).get(-1))
                                        << 2));
                        // if(dataAddress(mPos_U[0]) < dataAddress(pDst_U)) {
                        if (dataDistance(mPos_U[0], pDst_U) > 0) {
                          pos = posLookbehindOverrun;
                          continue positionLoop;
                        }
                        if (Long.compareUnsigned(dataDistance(op_U[0], opEnd_U), (long) 2) < 0) {
                          pos = posOutputOverrun;
                          continue positionLoop;
                        }
                        (op_U[0] = nnc(op_U[0]).shift(1))
                            .set(-1, (mPos_U[0] = nnc(mPos_U[0]).shift(1)).get(-1));
                        (op_U[0] = nnc(op_U[0]).shift(1))
                            .set(-1, (mPos_U[0] = nnc(mPos_U[0]).shift(1)).get(-1));
                        pos = posMatchDone;
                        continue positionLoop;
                      }
                    case posCopyMatch:
                      // if(dataAddress(mPos_U[0]) < dataAddress(pDst_U)) {
                      if (dataDistance(mPos_U[0], pDst_U) > 0) {
                        pos = posLookbehindOverrun;
                        continue positionLoop;
                      }
                      if (Long.compareUnsigned(dataDistance(op_U[0], opEnd_U), t_U[0] + 2) < 0) {
                        pos = posOutputOverrun;
                        continue positionLoop;
                      }
                      (op_U[0] = nnc(op_U[0]).shift(1))
                          .set(-1, (mPos_U[0] = nnc(mPos_U[0]).shift(1)).get(-1));
                      (op_U[0] = nnc(op_U[0]).shift(1))
                          .set(-1, (mPos_U[0] = nnc(mPos_U[0]).shift(1)).get(-1));
                      memcpyDs(op_U, mPos_U, t_U);
                    case posMatchDone:
                      t_U[0] = Byte.toUnsignedInt(ip_U[0].get(-2)) & 3;
                      if (t_U[0] == 0) {
                        break forLoop2;
                      }
                    case posMatchNext:
                      if (Long.compareUnsigned(dataDistance(op_U[0], opEnd_U), t_U[0]) < 0) {
                        pos = posOutputOverrun;
                        continue positionLoop;
                      }
                      if (Long.compareUnsigned(dataDistance(ip_U[0], ipEnd_U), t_U[0] + 1) < 0) {
                        pos = posInputOverrun;
                        continue positionLoop;
                      }
                      memcpyDs(op_U, ip_U, t_U);
                      t_U[0] = Byte.toUnsignedLong((ip_U[0] = nnc(ip_U[0]).shift(1)).get(-1));
                      // if(dataAddress(ip_U[0]) >= dataAddress(ipEnd_U))
                      if (dataDistance(ip_U[0], ipEnd_U) <= 0) {
                        break forLoop2;
                      }
                  }
            }
        case posEofFound:
          pszDst_U[0] = (int) pd_U(op_U[0], pDst_U);
          return dataDistance(ip_U[0], ipEnd_U) == 0 ? 1 : 0;
        case posInputOverrun:
        case posOutputOverrun:
        case posLookbehindOverrun:
          return 0;
      }
  }
}
