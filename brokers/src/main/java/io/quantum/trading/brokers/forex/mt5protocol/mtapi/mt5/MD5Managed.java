// package mtapi.mt5;
//
// import java.util.Arrays;
//
/// **
// MD5Managed: A HashAlgorithm implementation that acts as a thin wrapper
// around a C# translation of the MD5 reference implementation. The C code
// has been translated as closely as possible so that most of the original
// structure remains and comparisons between the two are straightforward.
//
//
// Derived from the RSA Data Security, Inc. MD5 Message-Digest Algorithm.
//
// Specification:
// RFC1321 - The MD5 Message-Digest Algorithm
// http: //www.faqs.org/rfcs/rfc1321.html
//
// Original license:
// Copyright (C) 1991-2, RSA Data Security, Inc. Created 1991. All
// rights reserved.
//
// License to copy and use this software is granted provided that it
// is identified as the "RSA Data Security, Inc. MD5 Message-Digest
// Algorithm" in all material mentioning or referencing this software
// or this function.
//
// License is also granted to make and use derivative works provided
// that such works are identified as "derived from the RSA Data
// Security, Inc. MD5 Message-Digest Algorithm" in all material
// mentioning or referencing the derived work.
//
// RSA Data Security, Inc. makes no representations concerning either
// the merchantability of this software or the suitability of this
// software for any particular purpose. It is provided "as is"
// without express or implied warranty of any kind.
//
// These notices must be retained in any copies of any part of this
// documentation and/or software.
//
// */
// public class MD5Managed
// {
//	// Current context
//	private final MD5_CTX _context = new MD5_CTX();
//	// Last hash result
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private readonly byte[] _digest = new byte[16];
//	private final byte[] _digest = new byte[16];
//	// True if HashCore has been called
//	private boolean _hashCoreCalled;
//	// True if HashFinal has been called
//	private boolean _hashFinalCalled;
//
//	/**
//	 Initializes a new instance.
//	 */
//	public MD5Managed()
//	{
//		InitializeVariables(true);
//	}
//
//	/**
//	 Initializes internal state.
//	 */
//	public final void Initialize(boolean clearConetext)
//	{
//		InitializeVariables(clearConetext);
//	}
//
//	/**
//	 Initializes variables.
//	 */
//	private void InitializeVariables(boolean clearConetext)
//	{
//		MD5Init(_context, clearConetext);
//		_hashCoreCalled = false;
//		_hashFinalCalled = false;
//	}
//
//	/**
//	 Updates the hash code with the data provided.
//
//	 @param array Data to hash.
//	 @param ibStart Start position.
//	 @param cbSize Number of bytes.
//	 */
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: public void HashCore(byte[] array, int ibStart, int cbSize)
//	public final void HashCore(byte[] array, int ibStart, int cbSize)
//	{
//		if (null == array)
//		{
//			throw new IllegalArgumentException("array");
//		}
//
//		if (_hashFinalCalled)
//		{
//			throw new RuntimeException("Hash not valid for use in specified state.");
//		}
//		_hashCoreCalled = true;
//		MD5Update(_context, array, (int)ibStart, (int)cbSize);
//	}
//
//	/**
//	 Finalizes the hash code and returns it.
//
//	 @return
//	 */
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: public byte[] HashFinal()
//	public final byte[] HashFinal()
//	{
//		_hashFinalCalled = true;
//		MD5Final(_digest, _context);
//		return getHash();
//	}
//
//	/**
//	 Returns the hash as an array of bytes.
//	 */
//	//[SuppressMessage("Microsoft.Design", "CA1065:DoNotRaiseExceptionsInUnexpectedLocations",
// Justification = "Matching .NET behavior by throwing here.")]
//	//[SuppressMessage("Microsoft.Usage", "CA2201:DoNotRaiseReservedExceptionTypes", Justification =
// "Matching .NET behavior by throwing NullReferenceException.")]
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: public byte[] getHash()
//	public final byte[] getHash()
//	{
//		if (!_hashCoreCalled)
//		{
//			throw new NullPointerException();
//		}
//		if (!_hashFinalCalled)
//		{
//			// Note: Not CryptographicUnexpectedOperationException because that can't be instantiated on
// Silverlight 4
//			throw new RuntimeException("Hash must be finalized before the hash value is retrieved.");
//		}
//
//		return _digest;
//	}
//
//	// Return size of hash in bits.
//	public final int getHashSize()
//	{
//		return _digest.length * 8;
//	}
//
//	///////////////////////////////////////////////
//	// MD5 reference implementation begins here. //
//	///////////////////////////////////////////////
//
//	/* MD5 context. */
//	private static class MD5_CTX
//	{
//		//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: public readonly uint[] state;
//		public int[] state; // state (ABCD)
//		//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: public readonly uint[] count;
//		public int[] count; // number of bits, modulo 2^64 (lsb first)
//		//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: public readonly byte[] buffer;
//		public byte[] buffer; // input buffer
//
//		public MD5_CTX()
//		{
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: state = new uint[4];
//			state = new int[4];
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: count = new uint[2];
//			count = new int[2];
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: buffer = new byte[64];
//			buffer = new byte[64];
//		}
//
//		public final void Clear()
//		{
//			Arrays.fill(state, 0);
//			Arrays.fill(count, 0);
//			Arrays.fill(buffer, (byte)0);
//		}
//	}
//
//	/* Constants for MD5Transform routine. */
//	private static final int S11 = 7;
//	private static final int S12 = 12;
//	private static final int S13 = 17;
//	private static final int S14 = 22;
//	private static final int S21 = 5;
//	private static final int S22 = 9;
//	private static final int S23 = 14;
//	private static final int S24 = 20;
//	private static final int S31 = 4;
//	private static final int S32 = 11;
//	private static final int S33 = 16;
//	private static final int S34 = 23;
//	private static final int S41 = 6;
//	private static final int S42 = 10;
//	private static final int S43 = 15;
//	private static final int S44 = 21;
//
//	//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static byte[] PADDING;
//	private static byte[] PADDING;
//
//	//[SuppressMessage("Microsoft.Performance", "CA1810:InitializeReferenceTypeStaticFieldsInline",
// Justification = "More compact this way")]
//	static
//	{
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: PADDING = new byte[64];
//		PADDING = new byte[64];
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: PADDING[0] = 0x80;
//		PADDING[0] = (byte)0x80;
//	}
//
//	/* F, G, H and I are basic MD5 functions. */
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static uint F(uint x, uint y, uint z)
//	private static int F(int x, int y, int z)
//	{
//		return (((x) & (y)) | ((~x) & (z)));
//	}
//	//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static uint G(uint x, uint y, uint z)
//	private static int G(int x, int y, int z)
//	{
//		return (((x) & (z)) | ((y) & (~z)));
//	}
//	//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static uint H(uint x, uint y, uint z)
//	private static int H(int x, int y, int z)
//	{
//		return ((x) ^ (y) ^ (z));
//	}
//	//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static uint I(uint x, uint y, uint z)
//	private static int I(int x, int y, int z)
//	{
//		return ((y) ^ ((x) | (~z)));
//	}
//
//	/* ROTATE_LEFT rotates x left n bits. */
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static uint ROTATE_LEFT(uint x, int n)
//	private static int ROTATE_LEFT(int x, int n)
//	{
//// C# TO JAVA CONVERTER WARNING: The right shift operator was replaced by Java's logical right
// shift operator since the left operand was originally of an unsigned type, but you should confirm
// this replacement:
//		return (((x) << (n)) | ((x) >>> (32 - (n))));
//	}
//
//	/* FF, GG, HH, and II transformations for rounds 1, 2, 3, and 4.
//	   Rotation is separate from addition to prevent recomputation. */
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static void FF(ref uint a, uint b, uint c, uint d, uint x, int s, uint
// ac)
//	private static void FF(mtapi.mt5.RefObject<Integer> a, int b, int c, int d, int x, int s, int ac)
//	{
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: (a) += F((b), (c), (d)) + (x) + (uint)(ac);
//		(a.argValue) += F((b), (c), (d)) + (x) + (int)(ac);
//		(a.argValue) = ROTATE_LEFT((a.argValue), (s));
//		(a.argValue) += (b);
//	}
//	//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static void GG(ref uint a, uint b, uint c, uint d, uint x, int s, uint
// ac)
//	private static void GG(mtapi.mt5.RefObject<Integer> a, int b, int c, int d, int x, int s, int ac)
//	{
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: (a) += G((b), (c), (d)) + (x) + (uint)(ac);
//		(a.argValue) += G((b), (c), (d)) + (x) + (int)(ac);
//		(a.argValue) = ROTATE_LEFT((a.argValue), (s));
//		(a.argValue) += (b);
//	}
//	//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static void HH(ref uint a, uint b, uint c, uint d, uint x, int s, uint
// ac)
//	private static void HH(mtapi.mt5.RefObject<Integer> a, int b, int c, int d, int x, int s, int ac)
//	{
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: (a) += H((b), (c), (d)) + (x) + (uint)(ac);
//		(a.argValue) += H((b), (c), (d)) + (x) + (int)(ac);
//		(a.argValue) = ROTATE_LEFT((a.argValue), (s));
//		(a.argValue) += (b);
//	}
//	//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static void II(ref uint a, uint b, uint c, uint d, uint x, int s, uint
// ac)
//	private static void II(mtapi.mt5.RefObject<Integer> a, int b, int c, int d, int x, int s, int ac)
//	{
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: (a) += I((b), (c), (d)) + (x) + (uint)(ac);
//		(a.argValue) += I((b), (c), (d)) + (x) + (int)(ac);
//		(a.argValue) = ROTATE_LEFT((a.argValue), (s));
//		(a.argValue) += (b);
//	}
//
//	/* MD5 initialization. Begins an MD5 operation, writing a new context. */
//	private static void MD5Init(MD5_CTX context, boolean clearConetext) // context
//	{
//		context.count[0] = context.count[1] = 0;
//
//		/* Load magic initialization constants. */
//		if (clearConetext)
//		{
//			context.state[0] = 0x67452301;
//			context.state[1] = (int)0xefcdab89;
//			context.state[2] = (int)0x98badcfe;
//			context.state[3] = 0x10325476;
//		}
//	}
//
//	/* MD5 block update operation. Continues an MD5 message-digest
//	   operation, processing another message block, and updating the
//	   context. */
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static void MD5Update(MD5_CTX context, byte[] input, uint inputIndex,
// uint inputLen)
//	private static void MD5Update(MD5_CTX context, byte[] input, int inputIndex, int inputLen) //
// length of input block -  Starting index for input block -  input block -  context
//	{
//		/* Compute number of bytes mod 64 */
//// C# TO JAVA CONVERTER WARNING: The right shift operator was replaced by Java's logical right
// shift operator since the left operand was originally of an unsigned type, but you should confirm
// this replacement:
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: uint index = (uint)((context.count[0] >> 3) & 0x3F);
//		int index = (int)((context.count[0] >>> 3) & 0x3F);
//
//		/* Update number of bits */
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: if ((context.count[0] += ((uint)inputLen << 3)) < ((uint)inputLen << 3))
//		if ((context.count[0] += ((int)inputLen << 3)) < ((int)inputLen << 3))
//		{
//			context.count[1]++;
//		}
//// C# TO JAVA CONVERTER WARNING: The right shift operator was replaced by Java's logical right
// shift operator since the left operand was originally of an unsigned type, but you should confirm
// this replacement:
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: context.count[1] += ((uint)inputLen >> 29);
//		context.count[1] += ((int)inputLen >>> 29);
//
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: uint partLen = 64 - index;
//		int partLen = 64 - index;
//
//		/* Transform as many times as possible. */
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: uint i = 0;
//		int i = 0;
//		if (inputLen >= partLen)
//		{
//			System.arraycopy(input, (int)inputIndex, context.buffer, (int)index, (int)partLen);
//			MD5Transform(context.state, context.buffer, 0);
//
//			for (i = partLen; i + 63 < inputLen; i += 64)
//			{
//				MD5Transform(context.state, input, inputIndex + i);
//			}
//
//			index = 0;
//		}
//
//		/* Buffer remaining input */
//		System.arraycopy(input, (int)(inputIndex + i), context.buffer, (int)index, (int)(inputLen - i));
//	}
//
//	/* MD5 finalization. Ends an MD5 message-digest operation, writing the
//	   the message digest and zeroizing the context. */
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static void MD5Final(byte[] digest, MD5_CTX context)
//	private static void MD5Final(byte[] digest, MD5_CTX context) // context -  message digest
//	{
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: byte[] bits = new byte[8];
//		byte[] bits = new byte[8];
//
//		/* Save number of bits */
//		Encode(bits, context.count, 8);
//
//		/* Pad out to 56 mod 64. */
//// C# TO JAVA CONVERTER WARNING: The right shift operator was replaced by Java's logical right
// shift operator since the left operand was originally of an unsigned type, but you should confirm
// this replacement:
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: uint index = (uint)((context.count[0] >> 3) & 0x3f);
//		int index = (int)((context.count[0] >>> 3) & 0x3f);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: uint padLen = (index < 56) ? (56 - index) : (120 - index);
//		int padLen = (index < 56) ? (56 - index) : (120 - index);
//		MD5Update(context, PADDING, 0, padLen);
//
//		/* Append length (before padding) */
//		MD5Update(context, bits, 0, 8);
//
//		/* Store state in digest */
//		Encode(digest, context.state, 16);
//
//		/* Zeroize sensitive information. */
//		//context.Clear();
//	}
//
//	/* MD5 basic transformation. Transforms state based on block. */
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static void MD5Transform(uint[] state, byte[] block, uint blockIndex)
//	private static void MD5Transform(int[] state, byte[] block, int blockIndex)
//	{
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: uint a = state[0], b = state[1], c = state[2], d = state[3];
//		int a = state[0], b = state[1], c = state[2], d = state[3];
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: uint[] x = new uint[16];
//		int[] x = new int[16];
//
//		Decode(x, block, blockIndex, 64);
//
//		/* Round 1 */
//		mtapi.mt5.RefObject<Integer> tempRef_a = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref a, b, c, d, x[0], S11, 0xd76aa478);
//		FF(tempRef_a, b, c, d, x[0], S11, 0xd76aa478); // 1
//		a = tempRef_a.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref d, a, b, c, x[1], S12, 0xe8c7b756);
//		FF(tempRef_d, a, b, c, x[1], S12, 0xe8c7b756); // 2
//		d = tempRef_d.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref c, d, a, b, x[2], S13, 0x242070db);
//		FF(tempRef_c, d, a, b, x[2], S13, 0x242070db); // 3
//		c = tempRef_c.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref b, c, d, a, x[3], S14, 0xc1bdceee);
//		FF(tempRef_b, c, d, a, x[3], S14, 0xc1bdceee); // 4
//		b = tempRef_b.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_a2 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref a, b, c, d, x[4], S11, 0xf57c0faf);
//		FF(tempRef_a2, b, c, d, x[4], S11, 0xf57c0faf); // 5
//		a = tempRef_a2.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d2 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref d, a, b, c, x[5], S12, 0x4787c62a);
//		FF(tempRef_d2, a, b, c, x[5], S12, 0x4787c62a); // 6
//		d = tempRef_d2.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c2 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref c, d, a, b, x[6], S13, 0xa8304613);
//		FF(tempRef_c2, d, a, b, x[6], S13, 0xa8304613); // 7
//		c = tempRef_c2.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b2 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref b, c, d, a, x[7], S14, 0xfd469501);
//		FF(tempRef_b2, c, d, a, x[7], S14, 0xfd469501); // 8
//		b = tempRef_b2.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_a3 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref a, b, c, d, x[8], S11, 0x698098d8);
//		FF(tempRef_a3, b, c, d, x[8], S11, 0x698098d8); // 9
//		a = tempRef_a3.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d3 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref d, a, b, c, x[9], S12, 0x8b44f7af);
//		FF(tempRef_d3, a, b, c, x[9], S12, 0x8b44f7af); // 10
//		d = tempRef_d3.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c3 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref c, d, a, b, x[10], S13, 0xffff5bb1);
//		FF(tempRef_c3, d, a, b, x[10], S13, 0xffff5bb1); // 11
//		c = tempRef_c3.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b3 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref b, c, d, a, x[11], S14, 0x895cd7be);
//		FF(tempRef_b3, c, d, a, x[11], S14, 0x895cd7be); // 12
//		b = tempRef_b3.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_a4 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref a, b, c, d, x[12], S11, 0x6b901122);
//		FF(tempRef_a4, b, c, d, x[12], S11, 0x6b901122); // 13
//		a = tempRef_a4.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d4 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref d, a, b, c, x[13], S12, 0xfd987193);
//		FF(tempRef_d4, a, b, c, x[13], S12, 0xfd987193); // 14
//		d = tempRef_d4.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c4 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref c, d, a, b, x[14], S13, 0xa679438e);
//		FF(tempRef_c4, d, a, b, x[14], S13, 0xa679438e); // 15
//		c = tempRef_c4.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b4 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: FF(ref b, c, d, a, x[15], S14, 0x49b40821);
//		FF(tempRef_b4, c, d, a, x[15], S14, 0x49b40821); // 16
//		b = tempRef_b4.argValue;
//
//		/* Round 2 */
//		mtapi.mt5.RefObject<Integer> tempRef_a5 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref a, b, c, d, x[1], S21, 0xf61e2562);
//		GG(tempRef_a5, b, c, d, x[1], S21, 0xf61e2562); // 17
//		a = tempRef_a5.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d5 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref d, a, b, c, x[6], S22, 0xc040b340);
//		GG(tempRef_d5, a, b, c, x[6], S22, 0xc040b340); // 18
//		d = tempRef_d5.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c5 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref c, d, a, b, x[11], S23, 0x265e5a51);
//		GG(tempRef_c5, d, a, b, x[11], S23, 0x265e5a51); // 19
//		c = tempRef_c5.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b5 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref b, c, d, a, x[0], S24, 0xe9b6c7aa);
//		GG(tempRef_b5, c, d, a, x[0], S24, 0xe9b6c7aa); // 20
//		b = tempRef_b5.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_a6 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref a, b, c, d, x[5], S21, 0xd62f105d);
//		GG(tempRef_a6, b, c, d, x[5], S21, 0xd62f105d); // 21
//		a = tempRef_a6.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d6 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref d, a, b, c, x[10], S22, 0x02441453);
//		GG(tempRef_d6, a, b, c, x[10], S22, 0x02441453); // 22
//		d = tempRef_d6.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c6 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref c, d, a, b, x[15], S23, 0xd8a1e681);
//		GG(tempRef_c6, d, a, b, x[15], S23, 0xd8a1e681); // 23
//		c = tempRef_c6.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b6 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref b, c, d, a, x[4], S24, 0xe7d3fbc8);
//		GG(tempRef_b6, c, d, a, x[4], S24, 0xe7d3fbc8); // 24
//		b = tempRef_b6.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_a7 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref a, b, c, d, x[9], S21, 0x21e1cde6);
//		GG(tempRef_a7, b, c, d, x[9], S21, 0x21e1cde6); // 25
//		a = tempRef_a7.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d7 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref d, a, b, c, x[14], S22, 0xc33707d6);
//		GG(tempRef_d7, a, b, c, x[14], S22, 0xc33707d6); // 26
//		d = tempRef_d7.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c7 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref c, d, a, b, x[3], S23, 0xf4d50d87);
//		GG(tempRef_c7, d, a, b, x[3], S23, 0xf4d50d87); // 27
//		c = tempRef_c7.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b7 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref b, c, d, a, x[8], S24, 0x455a14ed);
//		GG(tempRef_b7, c, d, a, x[8], S24, 0x455a14ed); // 28
//		b = tempRef_b7.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_a8 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref a, b, c, d, x[13], S21, 0xa9e3e905);
//		GG(tempRef_a8, b, c, d, x[13], S21, 0xa9e3e905); // 29
//		a = tempRef_a8.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d8 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref d, a, b, c, x[2], S22, 0xfcefa3f8);
//		GG(tempRef_d8, a, b, c, x[2], S22, 0xfcefa3f8); // 30
//		d = tempRef_d8.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c8 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref c, d, a, b, x[7], S23, 0x676f02d9);
//		GG(tempRef_c8, d, a, b, x[7], S23, 0x676f02d9); // 31
//		c = tempRef_c8.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b8 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: GG(ref b, c, d, a, x[12], S24, 0x8d2a4c8a);
//		GG(tempRef_b8, c, d, a, x[12], S24, 0x8d2a4c8a); // 32
//		b = tempRef_b8.argValue;
//
//		/* Round 3 */
//		mtapi.mt5.RefObject<Integer> tempRef_a9 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref a, b, c, d, x[5], S31, 0xfffa3942);
//		HH(tempRef_a9, b, c, d, x[5], S31, 0xfffa3942); // 33
//		a = tempRef_a9.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d9 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref d, a, b, c, x[8], S32, 0x8771f681);
//		HH(tempRef_d9, a, b, c, x[8], S32, 0x8771f681); // 34
//		d = tempRef_d9.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c9 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref c, d, a, b, x[11], S33, 0x6d9d6122);
//		HH(tempRef_c9, d, a, b, x[11], S33, 0x6d9d6122); // 35
//		c = tempRef_c9.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b9 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref b, c, d, a, x[14], S34, 0xfde5380c);
//		HH(tempRef_b9, c, d, a, x[14], S34, 0xfde5380c); // 36
//		b = tempRef_b9.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_a10 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref a, b, c, d, x[1], S31, 0xa4beea44);
//		HH(tempRef_a10, b, c, d, x[1], S31, 0xa4beea44); // 37
//		a = tempRef_a10.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d10 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref d, a, b, c, x[4], S32, 0x4bdecfa9);
//		HH(tempRef_d10, a, b, c, x[4], S32, 0x4bdecfa9); // 38
//		d = tempRef_d10.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c10 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref c, d, a, b, x[7], S33, 0xf6bb4b60);
//		HH(tempRef_c10, d, a, b, x[7], S33, 0xf6bb4b60); // 39
//		c = tempRef_c10.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b10 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref b, c, d, a, x[10], S34, 0xbebfbc70);
//		HH(tempRef_b10, c, d, a, x[10], S34, 0xbebfbc70); // 40
//		b = tempRef_b10.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_a11 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref a, b, c, d, x[13], S31, 0x289b7ec6);
//		HH(tempRef_a11, b, c, d, x[13], S31, 0x289b7ec6); // 41
//		a = tempRef_a11.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d11 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref d, a, b, c, x[0], S32, 0xeaa127fa);
//		HH(tempRef_d11, a, b, c, x[0], S32, 0xeaa127fa); // 42
//		d = tempRef_d11.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c11 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref c, d, a, b, x[3], S33, 0xd4ef3085);
//		HH(tempRef_c11, d, a, b, x[3], S33, 0xd4ef3085); // 43
//		c = tempRef_c11.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b11 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref b, c, d, a, x[6], S34, 0x04881d05);
//		HH(tempRef_b11, c, d, a, x[6], S34, 0x04881d05); // 44
//		b = tempRef_b11.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_a12 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref a, b, c, d, x[9], S31, 0xd9d4d039);
//		HH(tempRef_a12, b, c, d, x[9], S31, 0xd9d4d039); // 45
//		a = tempRef_a12.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d12 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref d, a, b, c, x[12], S32, 0xe6db99e5);
//		HH(tempRef_d12, a, b, c, x[12], S32, 0xe6db99e5); // 46
//		d = tempRef_d12.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c12 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref c, d, a, b, x[15], S33, 0x1fa27cf8);
//		HH(tempRef_c12, d, a, b, x[15], S33, 0x1fa27cf8); // 47
//		c = tempRef_c12.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b12 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: HH(ref b, c, d, a, x[2], S34, 0xc4ac5665);
//		HH(tempRef_b12, c, d, a, x[2], S34, 0xc4ac5665); // 48
//		b = tempRef_b12.argValue;
//
//		/* Round 4 */
//		mtapi.mt5.RefObject<Integer> tempRef_a13 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref a, b, c, d, x[0], S41, 0xf4292244);
//		II(tempRef_a13, b, c, d, x[0], S41, 0xf4292244); // 49
//		a = tempRef_a13.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d13 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref d, a, b, c, x[7], S42, 0x432aff97);
//		II(tempRef_d13, a, b, c, x[7], S42, 0x432aff97); // 50
//		d = tempRef_d13.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c13 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref c, d, a, b, x[14], S43, 0xab9423a7);
//		II(tempRef_c13, d, a, b, x[14], S43, 0xab9423a7); // 51
//		c = tempRef_c13.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b13 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref b, c, d, a, x[5], S44, 0xfc93a039);
//		II(tempRef_b13, c, d, a, x[5], S44, 0xfc93a039); // 52
//		b = tempRef_b13.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_a14 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref a, b, c, d, x[12], S41, 0x655b59c3);
//		II(tempRef_a14, b, c, d, x[12], S41, 0x655b59c3); // 53
//		a = tempRef_a14.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d14 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref d, a, b, c, x[3], S42, 0x8f0ccc92);
//		II(tempRef_d14, a, b, c, x[3], S42, 0x8f0ccc92); // 54
//		d = tempRef_d14.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c14 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref c, d, a, b, x[10], S43, 0xffeff47d);
//		II(tempRef_c14, d, a, b, x[10], S43, 0xffeff47d); // 55
//		c = tempRef_c14.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b14 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref b, c, d, a, x[1], S44, 0x85845dd1);
//		II(tempRef_b14, c, d, a, x[1], S44, 0x85845dd1); // 56
//		b = tempRef_b14.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_a15 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref a, b, c, d, x[8], S41, 0x6fa87e4f);
//		II(tempRef_a15, b, c, d, x[8], S41, 0x6fa87e4f); // 57
//		a = tempRef_a15.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d15 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref d, a, b, c, x[15], S42, 0xfe2ce6e0);
//		II(tempRef_d15, a, b, c, x[15], S42, 0xfe2ce6e0); // 58
//		d = tempRef_d15.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c15 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref c, d, a, b, x[6], S43, 0xa3014314);
//		II(tempRef_c15, d, a, b, x[6], S43, 0xa3014314); // 59
//		c = tempRef_c15.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b15 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref b, c, d, a, x[13], S44, 0x4e0811a1);
//		II(tempRef_b15, c, d, a, x[13], S44, 0x4e0811a1); // 60
//		b = tempRef_b15.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_a16 = new mtapi.mt5.RefObject<Integer>(a);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref a, b, c, d, x[4], S41, 0xf7537e82);
//		II(tempRef_a16, b, c, d, x[4], S41, 0xf7537e82); // 61
//		a = tempRef_a16.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_d16 = new mtapi.mt5.RefObject<Integer>(d);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref d, a, b, c, x[11], S42, 0xbd3af235);
//		II(tempRef_d16, a, b, c, x[11], S42, 0xbd3af235); // 62
//		d = tempRef_d16.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_c16 = new mtapi.mt5.RefObject<Integer>(c);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref c, d, a, b, x[2], S43, 0x2ad7d2bb);
//		II(tempRef_c16, d, a, b, x[2], S43, 0x2ad7d2bb); // 63
//		c = tempRef_c16.argValue;
//		mtapi.mt5.RefObject<Integer> tempRef_b16 = new mtapi.mt5.RefObject<Integer>(b);
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: II(ref b, c, d, a, x[9], S44, 0xeb86d391);
//		II(tempRef_b16, c, d, a, x[9], S44, 0xeb86d391); // 64
//		b = tempRef_b16.argValue;
//
//		state[0] += a;
//		state[1] += b;
//		state[2] += c;
//		state[3] += d;
//
//		/* Zeroize sensitive information. */
//		Arrays.fill(x, 0);
//	}
//
//	/* Encodes input (UINT4) into output (unsigned char). Assumes len is
//	   a multiple of 4. */
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static void Encode(byte[] output, uint[] input, uint len)
//	private static void Encode(byte[] output, int[] input, int len)
//	{
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: for (uint i = 0, j = 0; j < len; i++, j += 4)
//		for (int i = 0, j = 0; j < len; i++, j += 4)
//		{
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: output[j] = (byte)(input[i] & 0xff);
//			output[j] = (byte)(input[i] & 0xff);
//// C# TO JAVA CONVERTER WARNING: The right shift operator was replaced by Java's logical right
// shift operator since the left operand was originally of an unsigned type, but you should confirm
// this replacement:
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: output[j + 1] = (byte)((input[i] >> 8) & 0xff);
//			output[j + 1] = (byte)((input[i] >>> 8) & 0xff);
//// C# TO JAVA CONVERTER WARNING: The right shift operator was replaced by Java's logical right
// shift operator since the left operand was originally of an unsigned type, but you should confirm
// this replacement:
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: output[j + 2] = (byte)((input[i] >> 16) & 0xff);
//			output[j + 2] = (byte)((input[i] >>> 16) & 0xff);
//// C# TO JAVA CONVERTER WARNING: The right shift operator was replaced by Java's logical right
// shift operator since the left operand was originally of an unsigned type, but you should confirm
// this replacement:
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: output[j + 3] = (byte)((input[i] >> 24) & 0xff);
//			output[j + 3] = (byte)((input[i] >>> 24) & 0xff);
//		}
//	}
//
//	/* Decodes input (unsigned char) into output (UINT4). Assumes len is
//	   a multiple of 4. */
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: private static void Decode(uint[] output, byte[] input, uint inputIndex, uint
// len)
//	private static void Decode(int[] output, byte[] input, int inputIndex, int len)
//	{
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: for (uint i = 0, j = 0; j < len; i++, j += 4)
//		for (int i = 0, j = 0; j < len; i++, j += 4)
//		{
//// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//// ORIGINAL LINE: output[i] = ((uint)input[inputIndex + j]) | (((uint)input[inputIndex + j + 1])
// << 8) | (((uint)input[inputIndex + j + 2]) << 16) | (((uint)input[inputIndex + j + 3]) << 24);
//			output[i] = ((int)input[inputIndex + j]) | (((int)input[inputIndex + j + 1]) << 8) |
// (((int)input[inputIndex + j + 2]) << 16) | (((int)input[inputIndex + j + 3]) << 24);
//		}
//	}
// }
