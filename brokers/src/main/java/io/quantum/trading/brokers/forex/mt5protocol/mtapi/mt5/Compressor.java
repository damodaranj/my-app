package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import static io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.String8.dataDistance;
import static io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5.String8.nnc;

public class Compressor {

  public static byte[] compress(byte[] bytes) {
    int srcLen = bytes.length;
    String8 src = new String8(bytes);
    int len = srcLen;
    String8 dst = new String8(len);
    IntContainer dstLen = IntContainer.fromData(0);
    {
      String8 p = dst;
      if (len > 0xD) {
        len = compressData(src, len, dst, dstLen);
        p = dst.shift(dstLen.get());
      }
      if (len > 0) {
        String8 from = String8.from(src, srcLen - len);
        if (dataAddress(p) == dataAddress(dst) && len <= 0xEE) {
          (p = nnc(p).shift(1)).set(-1, (byte) (len + 0x11));
        } else if (len <= 3) {
          p.set(-2, (byte) (Byte.toUnsignedInt(p.get(-2)) | Byte.toUnsignedInt((byte) len)));
        } else if (len <= 0x12) {
          (p = nnc(p).shift(1)).set(-1, (byte) (len - 3));
        } else {
          p = nnc(p).shift(1);
          int cnt = len - 0x12;
          if (cnt > 0xFF) {
            long lbl_U = 0x80808081L * (long) (cnt - 1);
            int bl = (int) (lbl_U >>> 39);
            p = nnc(p).shift(bl);
            for (int i = 0; i < bl; i++) {
              cnt -= 0xFF;
            }
          }
          (p = nnc(p).shift(1)).set(-1, (byte) cnt);
        }
        for (int i = 0; i < len; i++) {
          (p = nnc(p).shift(1)).set(-1, (from = nnc(from).shift(1)).get(-1));
        }
        p.toString();
      }
      (p = p.shift(1)).set(-1, (byte) 0x11);
      p = p.shift(2);
      dstLen.set((int) dataDistance(dst, p));
    }
    byte[] data = new byte[dstLen.get()];
    for (int i = 0; i < dstLen.get(); i++) {
      data[i] = dst.get(i);
    }
    return data;
  }

  public static int compressData(String8 src, int sizeSrc, String8 dst, IntContainer sizeDst) {
    String8[] buf = new String8[0x4000];
    String8 to = dst;
    String8 from = nnc(src).shift(4);
    String8 start = src;
    String8 end = nnc(src).shift(sizeSrc);
    int index = 0;

    final int posNextStep = 1, posCheckEnd = 2;
    positionLoop:
    for (int pos = 0; true; )
      switch (pos) {
        default:
          forLoop:
          for (; true; pos = 0)
            switch (pos) {
              default:
                index =
                    ((Byte.toUnsignedInt(from.get(3)) << 6 ^ Byte.toUnsignedInt(from.get(2))) << 5
                                ^ Byte.toUnsignedInt(from.get(1)))
                            << 5
                        ^ Byte.toUnsignedInt(from.get(0));
                index += index << 5;
                index = index >> 5 & 0x3FFF;
                String8 ptr = buf[index];
                if (ptr == null) {
                  pos = posNextStep;
                  continue positionLoop;
                }
                if (dataAddress(ptr) < dataAddress(src)) {
                  pos = posNextStep;
                  continue positionLoop;
                }
                sizeSrc = (int) dataDistance(ptr, from);
                if (sizeSrc == 0 || sizeSrc > 0xBFFF) {
                  pos = posNextStep;
                  continue positionLoop;
                }
                if (sizeSrc > 0x800 && ptr.get(3) != from.get(3)) {
                  index &= 0x7FF;
                  index ^= 0x201F;
                  ptr = buf[index];
                  if (dataAddress(ptr) < dataAddress(src)) {
                    pos = posNextStep;
                    continue positionLoop;
                  }
                  sizeSrc = (int) dataDistance(ptr, from);
                  if (sizeSrc == 0
                      || sizeSrc > 0xBFFF
                      || sizeSrc > 0x800 && ptr.get(3) != from.get(3)) {
                    pos = posNextStep;
                    continue positionLoop;
                  }
                }
                if (ptr.get(0) != from.get(0)
                    || ptr.get(1) != from.get(1)
                    || ptr.get(2) != from.get(2)) {
                  pos = posNextStep;
                  continue positionLoop;
                }
                buf[index] = nnc(from);
                int look = (int) dataDistance(start, from);
                if (look != 0) {
                  if (look <= 3) {
                    to.set(
                        -2,
                        (byte) (Byte.toUnsignedInt(to.get(-2)) | Byte.toUnsignedInt((byte) look)));
                  } else if (look <= 0x12) {
                    (to = nnc(to).shift(1)).set(-1, (byte) (look - 3));
                  } else {
                    (to = nnc(to).shift(1)).set(-1, (byte) 0);
                    int cnt = look - 0x12;
                    if (cnt > 0xFF) {
                      long lbl_U = 0x80808081L * (long) (cnt - 1);
                      int bl = (int) (lbl_U >>> 39);
                      to = nnc(to).shift(bl);
                      for (int i = 0; i < bl; i++) {
                        cnt -= 0xFF;
                      }
                    }
                    (to = nnc(to).shift(1)).set(-1, (byte) cnt);
                  }
                  look = look;
                  for (int i = 0; i < look; i++) {
                    (to = nnc(to).shift(1)).set(-1, (start = nnc(start).shift(1)).get(-1));
                  }
                }
                from.Index += 3;
                if ((from = nnc(from).shift(1)).get(-1) == ptr.get(3)
                    && (from = nnc(from).shift(1)).get(-1) == ptr.get(4)
                    && (from = nnc(from).shift(1)).get(-1) == ptr.get(5)
                    && (from = nnc(from).shift(1)).get(-1) == ptr.get(6)
                    && (from = nnc(from).shift(1)).get(-1) == ptr.get(7)
                    && (from = nnc(from).shift(1)).get(-1) == ptr.get(8)) {
                  ptr.Index += 9;
                  while (dataAddress(from) < dataAddress(end)) {
                    if (ptr.get() != from.get()) {
                      break;
                    }
                    ptr.Index++;
                    from.Index++;
                  }
                  look = (int) dataDistance(start, from);
                  if (sizeSrc <= 0x4000) {
                    sizeSrc--;
                    if (look <= 0x21) {
                      (to = nnc(to).shift(1)).set(-1, (byte) (look - 2 | 0x20));
                    } else {
                      (to = nnc(to).shift(1)).set(-1, (byte) 0x20);
                      int cnt = look - 0x21;
                      if (cnt > 0xFF) {
                        long lbl_U = 0x80808081L * (long) (cnt - 1);
                        int bl = (int) (lbl_U >>> 39);
                        to = nnc(to).shift(bl);
                        for (int i = 0; i < bl; i++) {
                          cnt -= 0xFF;
                        }
                      }
                      (to = nnc(to).shift(1)).set(-1, (byte) cnt);
                    }
                  } else {
                    sizeSrc -= 0x4000;
                    if (look <= 9) {
                      (to = nnc(to).shift(1)).set(-1, (byte) (sizeSrc >> 11 & 8 | look - 2 | 0x10));
                    } else {
                      (to = nnc(to).shift(1)).set(-1, (byte) (sizeSrc >> 11 & 8 | 0x10));
                      int cnt = look - 9;
                      if (cnt > 0xFF) {
                        long lbl_U = 0x80808081L * (long) (cnt - 1);
                        int bl = (int) (lbl_U >>> 39);
                        to = nnc(to).shift(bl);
                        for (int i = 0; i < bl; i++) {
                          cnt -= 0xFF;
                        }
                      }
                      (to = nnc(to).shift(1)).set(-1, (byte) cnt);
                    }
                  }
                  (to = nnc(to).shift(1)).set(-1, (byte) (sizeSrc << 2));
                  (to = nnc(to).shift(1)).set(-1, (byte) (sizeSrc >> 6));
                } else {
                  look = (int) dataDistance(start, from = nnc(from).shift(-1));
                  if (sizeSrc <= 0x800) {
                    sizeSrc--;
                    (to = nnc(to).shift(1)).set(-1, (byte) (look + 7 << 5 | (sizeSrc & 7) << 2));
                    (to = nnc(to).shift(1)).set(-1, (byte) (sizeSrc >> 3));
                  } else if (sizeSrc <= 0x4000) {
                    (to = nnc(to).shift(1)).set(-1, (byte) (look - 2 | 0x20));
                    sizeSrc--;
                    (to = nnc(to).shift(1)).set(-1, (byte) (sizeSrc << 2));
                    (to = nnc(to).shift(1)).set(-1, (byte) (sizeSrc >> 6));
                  } else {
                    sizeSrc -= 0x4000;
                    (to = nnc(to).shift(1)).set(-1, (byte) (sizeSrc >> 11 & 8 | look - 2 | 0x10));
                    (to = nnc(to).shift(1)).set(-1, (byte) (sizeSrc << 2));
                    (to = nnc(to).shift(1)).set(-1, (byte) (sizeSrc >> 6));
                  }
                }
                start = nnc(from);
                pos = posCheckEnd;
                continue positionLoop;
            }
        case posNextStep:
          buf[index] = nnc(from);
          from.Index++;
        case posCheckEnd:
          pos = 0;
          if (dataAddress(from) >= dataAddress(nnc(end).shift(-0xD))) {
            sizeDst.set(to.Index - dst.Index);
            return end.Index - start.Index;
          }
      }
  }

  private static int dataAddress(String8 s) {
    return s.Index;
  }

  //    public static void dzeroBlk(String8[] s_U, long[] n_U) {
  //        {
  //            long sz_U = Long.divideUnsigned(n_U[0] - 256, 255) + 1;
  //            s_U[0].fill(0, (int)sz_U, (byte)0);
  //            s_U[0] = nnc(s_U[0]).shift((int)sz_U);
  //            do {
  //                n_U[0] = n_U[0] - 255;
  //            } while(Long.compareUnsigned(--sz_U, 0) > 0);
  //        }
  //    }
  //
  //    public static long pd_U(String8 a_U, String8 b_U) {
  //        return dataDistance(b_U, a_U);
  //    }
  //    public static boolean compress(String8 pSrc_U, int szSrc_U, String8 pDst_U, int[] pszDst_U)
  // {
  //        Container<String8> mPBuffer_U = new Container<String8>(0x4000){};
  //        if(pSrc_U == null || Long.compareUnsigned(szSrc_U, 1) < 0 || pDst_U == null || pszDst_U
  // == null) {
  //            return false;
  //        }
  //        String8[] op_U = {pDst_U};
  //        int t_U, szDst_U = 0;
  //        if(Long.compareUnsigned(szSrc_U, 13) <= 0) {
  //            t_U = szSrc_U;
  //        } else {
  //            String8.from(mPBuffer_U).fill(0, 0x4000 * 4, (byte)0);
  //            t_U = compressData(pSrc_U, szSrc_U, pDst_U, szDst_U);
  //            op_U[0] = nnc(op_U[0]).shift((int)szDst_U);
  //        }
  //        if(t_U != 0) {
  //            String8 ii_U = nnc(pSrc_U).shift((int)(szSrc_U - t_U));
  //            if(dataAddress(op_U[0]) == dataAddress(pDst_U) && Long.compareUnsigned(t_U, 238) <=
  // 0) {
  //                (op_U[0] = nnc(op_U[0]).shift(1)).set(-1, (byte)(t_U + 17));
  //            } else if(Long.compareUnsigned(t_U, 3) <= 0) {
  //                op_U[0].set(-2, (byte)(Byte.toUnsignedInt(op_U[0].get(-2)) |
  // Byte.toUnsignedInt((byte)t_U)));
  //            } else if(Long.compareUnsigned(t_U, 18) <= 0) {
  //                (op_U[0] = nnc(op_U[0]).shift(1)).set(-1, (byte)(t_U - 3));
  //            } else {
  //                (op_U[0] = nnc(op_U[0]).shift(1)).set(-1, (byte)0);
  //                long[] tt_U = {t_U - 18};
  //                if(Long.compareUnsigned(tt_U[0], 255) > 0) {
  //                    dzeroBlk(op_U, tt_U);
  //                }
  //                (op_U[0] = nnc(op_U[0]).shift(1)).set(-1, (byte)tt_U[0]);
  //            }
  //            do {
  //                (op_U[0] = nnc(op_U[0]).shift(1)).set(-1, (ii_U = nnc(ii_U).shift(1)).get(-1));
  //            } while(Long.compareUnsigned(--t_U, 0) > 0);
  //        }
  //        ShortContainer.from(op_U[0]).set((short)17);
  //        op_U[0] = nnc(op_U[0]).shift(2);
  //        (op_U[0] = nnc(op_U[0]).shift(1)).set(-1, (byte)0);
  //        pszDst_U[0] = pd_U(op_U[0], pDst_U);
  //        return true;
  //    }

  //    public static int doDeflate(String8 pSrc_U, long szSrc_U, String8 pDst_U, long[] szDst_U) {
  //        String8 op_U = null;
  //        String8 ip_U = null;
  //        String8 ii_U = null;
  //        String8 inEnd_U = null;
  //        byte[] mPos_U = null;
  //        long dindex_U = 0;
  //
  //        final int posNext = 1, posTryMatch = 2, posLiteral = 3, posEnd = 4;
  //        positionLoop:
  //        for(int pos = 0; true;) switch(pos) {
  //            default:
  //                String8 mPBuffer_U = null;
  //                op_U = pDst_U;
  //                String8 in_U = pSrc_U;
  //                ip_U = nnc(in_U).shift(4);
  //                ii_U = in_U;
  //                inEnd_U = nnc(pSrc_U).shift((int)szSrc_U);
  //                String8 ipEnd_U = nnc(pSrc_U).shift((int)(szSrc_U - 13));
  //                Container<String8> dict_U = Container.from(new TypeInfo<Container<String8>>(){},
  // mPBuffer_U);
  //                if(dict_U == null) {
  //                    pos = posEnd;
  //                    continue positionLoop;
  //                }
  //            case posNext:
  //            case posTryMatch:
  //            case posLiteral:
  //                forLoop:
  //                for(;; pos = 0) switch(pos) {
  //                    default:
  //                        long mOff_U = 0;
  //                    case posNext:
  //                        if(true) {
  //                            pos = posLiteral;
  //                            continue positionLoop;
  //                        }
  //                        if(true) {
  //                            pos = posTryMatch;
  //                            continue positionLoop;
  //                        }
  //                        if(true) {
  //                            pos = posLiteral;
  //                            continue positionLoop;
  //                        }
  //                        if(true) {
  //                            pos = posTryMatch;
  //                            continue positionLoop;
  //                        }
  //                        pos = posLiteral;
  //                        continue positionLoop;
  //                    case posTryMatch:
  //                    case posLiteral:
  //                        if(true) {
  //                            if(true) {
  //                                break forLoop;
  //                            }
  //                            continue;
  //                        }
  //
  //                        if(true) {
  //                        } else if(true) {
  //                        } else {
  //                        }
  //                        ii_U = ip_U;
  //                        pos = posNext;
  //                        continue positionLoop;
  //                }
  //            case posEnd:
  //                szDst_U[0] = pd_U(op_U, pDst_U);
  //                return (int)pd_U(inEnd_U, ii_U);
  //        }
  //    }

}
