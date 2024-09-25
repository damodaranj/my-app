package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

class UDT {
  // public static T ReadStruct<T>(byte[] data, int offset)
  // {
  //    int size = Marshal.SizeOf(typeof(T));
  //    IntPtr ptr = Marshal.AllocHGlobal(size);
  //    Marshal.Copy(data, offset, ptr, size);
  //    T temp = (T)Marshal.PtrToStructure(ptr, typeof(T));
  //    Marshal.FreeHGlobal(ptr);
  //    return temp;
  // }

  // public static T ReadStruct<T>(byte[] data, int offset, int size)
  // {
  //	IntPtr ptr = Marshal.AllocHGlobal(size);
  //	Marshal.Copy(data, offset, ptr, size);
  //	T temp = (T)Marshal.PtrToStructure(ptr, typeof(T));
  //	Marshal.FreeHGlobal(ptr);
  //	return temp;
  // }

  // C# TO JAVA CONVERTER TODO TASK: The C# 'new()' constraint has no equivalent in Java:
  // ORIGINAL LINE: public static T ReadStruct<T>(InBuf buf) where T : FromBufReader, new()
  public static <T extends FromBufReader> T ReadStruct(InBuf buf, T t) {
    return (T) t.ReadFromBuf(buf);
  }

  // C# TO JAVA CONVERTER TODO TASK: The C# 'new()' constraint has no equivalent in Java:
  // ORIGINAL LINE: public static T ReadStruct<T>(byte[] data, int offset, int size) where T :
  // FromBufReader, new()
  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  public static <T extends FromBufReader> T ReadStruct(byte[] data, int offset, int size, T t) {
    InBuf buf = new InBuf(data, offset);
    return (T) t.ReadFromBuf(buf);
  }

  // public static byte[] GetBytes(object obj)
  // {
  //	int size;
  //	if (obj is TradeRequest)
  //		size = 800;
  //	else
  //		throw new Exception("Unknown type");
  //	byte[] buffer = new byte[size];
  //	IntPtr ptr = Marshal.AllocHGlobal(size);
  //	Marshal.StructureToPtr(obj, ptr, false);
  //	Marshal.Copy(ptr, buffer, 0, size);
  //	Marshal.FreeHGlobal(ptr);
  //	return buffer;
  // }

  // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
  // ORIGINAL LINE: public static byte[] GetBytes(TradeRequest req)
  public static byte[] GetBytes(TradeRequest req) {
    OutBuf buf = new OutBuf();
    req.WriteToBuf(buf);
    return ByteLists.toArray(buf.List);
  }

  static String readString(byte[] buf, int of, int len) {
    ArrayList<Byte> res = new ArrayList<Byte>();
    for (int i = 0; i < len; i += 2) {
      if (buf[of + i] == 0 && buf[of + i + 1] == 0) break;
      res.add(buf[of + i]);
      res.add(buf[of + i + 1]);
    }
    byte[] array = new byte[res.size()];
    for (int i = 0; i < res.size(); i++) array[i] = res.get(i);
    try {
      return new String(array, java.nio.charset.StandardCharsets.UTF_16LE.toString());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  static String readStringASCII(byte[] buf, int of, int len) {
    ArrayList<Byte> res = new ArrayList<Byte>();
    for (int i = 0; i < len; i += 1) {
      if (buf[of + i] == 0) // && buf[of + i + 1] == 0
      if (i > 0) break;
      res.add(buf[of + i]);
      // res.Add(buf[of + i + 1]);
    }
    if (res.size() > 0) if (res.get(0) == 0) res.remove(0);
    byte[] array = new byte[res.size()];
    for (int i = 0; i < res.size(); i++) array[i] = res.get(i);
    String r = null;
    try {
      r = new String(array, Charset.forName("ASCII").toString());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return r;
  }
}
