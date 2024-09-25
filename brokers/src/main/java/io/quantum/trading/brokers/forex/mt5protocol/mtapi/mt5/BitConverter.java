package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

class BitConverter {
  public static byte[] GetBytes(short v) {
    byte[] writeBuffer = new byte[2];
    writeBuffer[0] = (byte) ((v >>> 0) & 0xFF);
    writeBuffer[1] = (byte) ((v >>> 8) & 0xFF);
    return writeBuffer;
  }

  public static byte[] GetBytes(int v) {
    byte[] writeBuffer = new byte[4];
    writeBuffer[3] = (byte) ((v >>> 24) & 0xFF);
    writeBuffer[2] = (byte) ((v >>> 16) & 0xFF);
    writeBuffer[1] = (byte) ((v >>> 8) & 0xFF);
    writeBuffer[0] = (byte) ((v >>> 0) & 0xFF);
    return writeBuffer;
  }

  private static byte[] getBytes(long v) {

    byte[] writeBuffer = new byte[8];
    writeBuffer[7] = (byte) ((v >>> 56) & 0xFF);
    writeBuffer[6] = (byte) ((v >>> 48) & 0xFF);
    writeBuffer[5] = (byte) ((v >>> 40) & 0xFF);
    writeBuffer[4] = (byte) ((v >>> 32) & 0xFF);
    writeBuffer[3] = (byte) ((v >>> 24) & 0xFF);
    writeBuffer[2] = (byte) ((v >>> 16) & 0xFF);
    writeBuffer[1] = (byte) ((v >>> 8) & 0xFF);
    writeBuffer[0] = (byte) ((v >>> 0) & 0xFF);
    return writeBuffer;
  }

  public static byte[] getBytesLong(long v) {
    byte[] writeBuffer = new byte[8];
    writeBuffer[7] = (byte) ((v >>> 56) & 0xFF);
    writeBuffer[6] = (byte) ((v >>> 48) & 0xFF);
    writeBuffer[5] = (byte) ((v >>> 40) & 0xFF);
    writeBuffer[4] = (byte) ((v >>> 32) & 0xFF);
    writeBuffer[3] = (byte) ((v >>> 24) & 0xFF);
    writeBuffer[2] = (byte) ((v >>> 16) & 0xFF);
    writeBuffer[1] = (byte) ((v >>> 8) & 0xFF);
    writeBuffer[0] = (byte) ((v >>> 0) & 0xFF);
    return writeBuffer;
  }

  public static byte[] getBytes(float v) {
    return getBytes(Float.floatToRawIntBits(v));
  }

  public static byte[] GetBytes(double v) {
    return getBytes(Double.doubleToRawLongBits(v));
  }

  public static byte[] GetBytes(long v) {
    return getBytesLong(v);
  }

  public static byte[] getBytes(String v) {
    if (v == null) {
      v = "";
    }
    byte[] buf = new byte[4 + v.length()];
    System.arraycopy(getBytes(v.length()), 0, buf, 0, 4);
    byte[] vb = v.getBytes(StandardCharsets.UTF_8);
    System.arraycopy(vb, 0, buf, 4, vb.length);
    return buf;
  }

  public static String toString(byte[] data, int offset) {
    int length = ToInt32(data, offset);
    return new String(data, offset + 4, length, StandardCharsets.UTF_8);
  }

  public static float toFloat(byte[] data, int offset) {
    return Float.intBitsToFloat(ToInt32(data, offset));
  }

  public static double ToDouble(byte[] data, int offset) {
    return Double.longBitsToDouble(ToInt64(data, offset));
  }

  public static short ToInt16(byte[] data, int offset) {
    return (short) (data[offset] & 0xFF | (data[offset + 1] & 0xFF) << 8);
  }

  public static int ToInt32(byte[] data, int offset) {
    return (data[offset] & 0xFF)
        | ((data[offset + 1] & 0xFF) << 8)
        | ((data[offset + 2] & 0xFF) << 16)
        | ((data[offset + 3] & 0xFF) << 24);
  }

  public static long ToUInt32(byte[] data, int offset) {
    return ((long) data[offset] & 0xFF)
        | ((data[offset + 1] & 0xFF) << 8)
        | ((data[offset + 2] & 0xFF) << 16)
        | ((data[offset + 3] & 0xFF) << 24);
  }

  public static long ToInt64(byte[] data, int offset) {
    return (((long) (data[offset + 7] & 0xff) << 56)
        | ((long) (data[offset + 6] & 0xff) << 48)
        | ((long) (data[offset + 5] & 0xff) << 40)
        | ((long) (data[offset + 4] & 0xff) << 32)
        | ((long) (data[offset + 3] & 0xff) << 24)
        | ((long) (data[offset + 2] & 0xff) << 16)
        | ((long) (data[offset + 1] & 0xff) << 8)
        | (data[offset] & 0xff));
  }

  public static byte[] asByteArray(UUID uuid) {
    long msb = uuid.getMostSignificantBits();
    long lsb = uuid.getLeastSignificantBits();
    byte[] buffer = new byte[16];

    for (int i = 0; i < 8; i++) {
      buffer[i] = (byte) (msb >>> 8 * (7 - i));
    }
    for (int i = 8; i < 16; i++) {
      buffer[i] = (byte) (lsb >>> 8 * (7 - i));
    }
    return buffer;
  }

  public static UUID toUUID(byte[] byteArray) {
    return toUUID(byteArray, 0);
  }

  private static UUID toUUID(byte[] byteArray, int offset) {
    long msb = 0;
    long lsb = 0;

    for (int i = offset; i < offset + 8; i++) {
      msb = (msb << 8) | (byteArray[i] & 0xff);
    }

    for (int i = offset + 8; i < offset + 16; i++) {
      lsb = (lsb << 8) | (byteArray[i] & 0xff);
    }

    return new UUID(msb, lsb);
  }

  public static long ToUInt64(byte[] bytes, int i) {
    return ToInt64(bytes, i);
  }

  public static short ToUInt16(byte[] bytes, int i) {
    return ToInt16(bytes, i);
  }
}
