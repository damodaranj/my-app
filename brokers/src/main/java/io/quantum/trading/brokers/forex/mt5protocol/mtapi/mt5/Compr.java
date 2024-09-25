package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;
//
// public class Compr
// {
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe void DINDEX1(ref uint d, byte* p)
////		{
////			d = DM(DMUL(0x21, DX3(p, 5, 5, 6)) >> 5);
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe uint DX3(byte* p, int s1, int s2, int s3)
////		{
////			return ((DX2((p) + 1, s2, s3) << (s1)) ^ (p)[0]);
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe uint DX2(byte* p, int s1, int s2)
////		{
////			return (((((uint)((p)[2]) << (s2)) ^ (p)[1]) << (s1)) ^ (p)[0]);
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
//	int DMUL(int a, int b)
//		{
//			return ((int)((a) * (b)));
//		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe uint DM(uint v)
////		{
////			return DMS(v, 0);
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe uint DMS(uint v, int s)
////		{
////			return ((uint)(((v) & (D_MASK >> (s))) << (s)));
////		}
//
//	private final int D_BITS = 14;
//
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private uint getD_MASK()
//	private int getD_MASK()
//	{
//
//		return LZO_MASK(D_BITS);
//	}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe uint LZO_MASK(int bits)
////		{
////			return (LZO_SIZE(bits) - 1);
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe uint LZO_SIZE(int bits)
////		{
////			return (1u << (bits));
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe void GINDEX(ref byte* m_pos, uint m_off, byte*[] dict, uint dindex, byte* inn)
////		{
////			m_pos = dict[dindex];
////		}
//
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: readonly uint M4_MAX_OFFSET = 0xbfff;
//	private final int M4_MAX_OFFSET = 0xbfff;
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe bool LZO_CHECK_MPOS_NON_DET(ref byte* m_pos, uint m_off, byte* inn, byte* ip, uint
// max_offset)
////		{
////			m_pos = ip - (uint)PTR_DIFF(ip, m_pos);
////			return PTR_LT(m_pos, inn) || (m_off = (uint)PTR_DIFF(ip, m_pos)) == 0 || m_off > max_offset;
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe bool PTR_LT(byte* a, byte* b)
////		{
////			return a < b;
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe uint PTR_DIFF(byte* a, byte* b)
////		{
////			return (uint)(a - b);
////		}
//
//	private final int M2_MAX_OFFSET = 0x0800;
//
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private uint getD_HIGH()
//	private int getD_HIGH()
//	{
//// C# TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right
// shift operator since the left operand was not confirmed to be of an unsigned type, but you should
// review whether the logical right shift operator (>>>) is more appropriate:
//		return ((getD_MASK() & 0xFFFFFFFFL) >> 1) + 1;
//	}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe void DINDEX2(ref uint d, byte* p)
////		{
////			d = (d & (D_MASK & 0x7ff)) ^ (D_HIGH | 0x1f);
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe void UPDATE_I(byte*[] dict, int drun, uint index, byte* p, byte* inn)
////		{
////			dict[index] = p;
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe void MEMCPY_DS(ref byte* dest, ref byte* src, uint len)
////		{
////			do
////				*dest++ = *src++;
////			while (--len > 0);
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe bool PS(ref byte* m_pos, ref byte* ip)
////		{
////			return *m_pos++ != *ip++;
////		}
//
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: readonly uint M3_MAX_OFFSET = 0x4000;
//	private final int M3_MAX_OFFSET = 0x4000;
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: readonly byte M3_MARKER = 32;
//	private final byte M3_MARKER = 32;
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: readonly byte M4_MARKER = 16;
//	private final byte M4_MARKER = 16;
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: readonly byte M3_MAX_LEN =33;
//	private final byte M3_MAX_LEN = 33;
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: readonly byte M4_MAX_LEN =9;
//	private final byte M4_MAX_LEN = 9;
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe uint DoDeflate(byte* pSrc, uint szSrc, byte* pDst, uint szDst, byte*[] m_pBuffer)
////		{
////			byte* op = pDst;
////			byte* inn = pSrc;
////			byte* ip = inn + 4;
////			byte* ii = inn;
////			byte* in_end = pSrc + szSrc;
////			byte* ip_end = pSrc + szSrc - 13;
////			byte*[] dict = m_pBuffer;
////			if (dict == null)
////				goto end;
////			for (; ;)
////			{
////				byte* m_pos = null;
////				uint m_off = 0;
////				uint dindex = 0;
////				next:
////				DINDEX1(ref dindex, ip);
////				GINDEX(ref m_pos, m_off, dict, dindex, inn);
////				if (LZO_CHECK_MPOS_NON_DET(ref m_pos, m_off, inn, ip, M4_MAX_OFFSET))
////					goto literal;
////				if ((m_off <= M2_MAX_OFFSET) || (m_pos[3] == ip[3]))
////					if ((m_pos[0] != ip[0]) || (m_pos[1] != ip[1]) || (m_pos[2] != ip[2]))
////						goto literal;
////					else
////						goto after_literal;
////				DINDEX2(ref dindex, ip);
////				GINDEX(ref m_pos, m_off, dict, dindex, inn);
////				if (LZO_CHECK_MPOS_NON_DET(ref m_pos, m_off, inn, ip, M4_MAX_OFFSET))
////					goto literal;
////				if ((m_off <= M2_MAX_OFFSET) || (m_pos[3] == ip[3]))
////					if ((m_pos[0] != ip[0]) || (m_pos[1] != ip[1]) || (m_pos[2] != ip[2]))
////						goto literal;
////					else
////						goto after_literal;
////				literal:
////				UPDATE_I(dict, 0, dindex, ip, inn);
////				if (++ip >= ip_end)
////					break;
////				continue;
////				after_literal:
////				UPDATE_I(dict, 0, dindex, ip, inn);
////				uint t = Pd(ip, ii);
////				if (t != 0)
////				{
////					if (t <= 3)
////						op[-2] |= (byte)t;
////					else
////					{
////						if (t <= 18)
////							*op++ = (byte)(t - 3);
////						else
////						{
////							uint tt = t - 18;
////							*op++ = 0;
////							if (tt > 255)
////								DZERO_BLK(op, tt);
////							*op++ = (byte)tt;
////						}
////					}
////					MEMCPY_DS(ref op, ref ii, t); //??
////				}
////				m_pos += 3;
////				ip += 3;
////
////				if (PS(ref m_pos, ref ip) || PS(ref m_pos, ref ip) || PS(ref m_pos, ref ip) || PS(ref m_pos,
// ref ip) || PS(ref m_pos, ref ip) || PS(ref m_pos, ref ip))
////				{
////					--ip;
////					uint m_len = Pd(ip, ii);
////					if (m_off <= M2_MAX_OFFSET)
////					{
////						--m_off;
////						*op++ = (byte)(((m_len - 1) << 5) | ((m_off & 7) << 2));
////						*op++ = (byte)(m_off >> 3);
////					}
////					else if (m_off <= M3_MAX_OFFSET)
////					{
////						--m_off;
////						*op++ = (byte)(M3_MARKER | (m_len - 2));
////						*op++ = (byte)(m_off << 2);
////						*op++ = (byte)(m_off >> 6);
////					}
////					else
////					{
////						m_off -= 0x4000;
////						*op++ = (byte)(M4_MARKER | ((m_off >> 11) & 8) | (m_len - 2));
////						*op++ = (byte)(m_off << 2);
////						*op++ = (byte)(m_off >> 6);
////					}
////				}
////				else
////				{
////					while ((ip < in_end) && (*m_pos == *ip))
////					{
////						m_pos++;
////						ip++;
////					}
////					uint m_len = Pd(ip, ii);
////					if (m_off <= M3_MAX_OFFSET)
////					{
////						m_off -= 1;
////						if (m_len <= M3_MAX_LEN)
////							*op++ = (byte)(M3_MARKER | (m_len - 2));
////						else
////						{
////							m_len -= M3_MAX_LEN;
////							*op++ = (byte)(M3_MARKER | 0);
////							if (m_len > 255)
////								DZERO_BLK(op, m_len);
////							*op++ = (byte)m_len;
////						}
////					}
////					else
////					{
////						m_off -= 0x4000;
////						if (m_len <= M4_MAX_LEN)
////							*op++ = (byte)(M4_MARKER | ((m_off >> 11) & 8) | (m_len - 2));
////						else
////						{
////							m_len -= M4_MAX_LEN;
////							*op++ = (byte)(M4_MARKER | ((m_off >> 11) & 8));
////							if (m_len > 255)
////								DZERO_BLK(op, m_len);
////							*op++ = (byte)m_len;
////						}
////					}
////					*op++ = (byte)(m_off << 2);
////					*op++ = (byte)(m_off >> 6);
////				}
////				ii = ip;
////				goto next;
////			}
////			end:
////			szDst = Pd(op, pDst);
////			return Pd(in_end, ii);
////		}
//
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: byte[] Src;
//	private byte[] Src;
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: byte[] Dst;
//	private byte[] Dst;
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	public unsafe byte[] Deflate(byte[] src)
////		{
////			Src = src;
////			uint sLen = (uint)src.Length;
////			byte[] dstBuf = new byte[sLen];
////			Dst = dstBuf;
////			uint dLen = 0;
////			fixed (byte* s = src, d = dstBuf)
////			{
////				byte*[] m_pBuffer = null;
////				;
////				if (!Deflate(s, sLen, d, &dLen, m_pBuffer))
////					throw new Exception("Cannot deflate");
////			}
////			byte[] data = new byte[dLen];
////			for (int i = 0; i < dLen; i++)
////				data[i] = dstBuf[i];
////			return data;
////		}
//			//+879480 a
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe bool Deflate(byte* pSrc, uint szSrc, byte* pDst, uint* pszDst, byte*[] m_pBuffer)
////		{
////			byte* op = pDst;
////			uint t, szDst = 0;
////			if (szSrc <= 13)
////				t = szSrc;
////			else
////			{
////				if (m_pBuffer == null)
////					m_pBuffer = new byte*[0x4000];
////				if (m_pBuffer == null)
////					return false;
////				t = DoDeflate(pSrc, szSrc, pDst, szDst, m_pBuffer);
////				op += szDst;
////			}
////			if (t != 0)
////			{
////				byte* ii = pSrc + szSrc - t;
////				if ((op == pDst) && (t <= 238))
////					*op++ = (byte)(t + 17);
////				else if (t <= 3)
////					op[-2] |= (byte)t;
////				else if (t <= 18)
////					*op++ = (byte)(t - 3);
////				else
////				{
////					*op++ = 0;
////					uint tt = t - 18;
////					if (tt > 255)
////						DZERO_BLK(op, tt);
////					* op++ = (byte)tt;
////				}
////				do
////					*op++ = *ii++;
////				while (--t > 0);
////			}
////			*(ushort*)op = 17;
////			op += 2;
////			*op++ = 0;
////			*pszDst = Pd(op, pDst);
////			return true;
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe uint Pd(byte* a, byte* b)
////		{
////			return ((uint)((a) - (b)));
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe void DZERO_BLK(byte* s, uint n)
////		{
////			uint sz = (n - 256) / 255 + 1;
////			Memset(s, 0, sz);
////			s += sz;
////			do
////			{
////				n -= 255;
////			}
////			while (--sz > 0);
////		}
//
//// C# TO JAVA CONVERTER TODO TASK: C# 'unsafe' code is not converted by C# to Java Converter:
////	unsafe void Memset(byte* s, byte value, uint size)
////		{
////			for (int i = 0; i < size; i++)
////				s[i] = value;
////		}
//
// }
