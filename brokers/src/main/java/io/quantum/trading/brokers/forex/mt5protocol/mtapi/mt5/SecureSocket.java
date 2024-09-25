package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SecureSocket {
  private Decoder Decoder;
  private Encoder Encoder;
  private Logger Log;
  Socket Sock;
  DataOutputStream Output;
  DataInputStream Input;

  public SecureSocket() {
    Log = new Logger(this);
  }

  //	public SecureSocket(byte[] key)
  //	{
  //		Decoder = new Decoder(key);
  //		Encoder = new Encoder(key);
  //		Log = new Logger(this);
  //	}

  //	public final void SetNewKey(byte[] key)
  //	{
  //		if (Decoder == null)
  //		{
  //			Decoder = new Decoder(key);
  //		}
  //		else
  //		{
  //			Decoder.ChangeKey(key);
  //		}
  //		if (Encoder == null)
  //		{
  //			Encoder = new Encoder(key);
  //		}
  //		else
  //		{
  //			Encoder.ChangeKey(key);
  //		}
  //	}
  //
  //	public final byte[] GetKey()
  //	{
  //		if (Decoder != null)
  //		{
  //			return Decoder.GetKey();
  //		}
  //		if (Encoder != null)
  //		{
  //			return Encoder.GetKey();
  //		}
  //		return null;
  //	}

  //	public final void ResetDecoder()
  //	{
  //		Decoder.Reset();
  //	}
  //
  //	public final void ResetEncoder()
  //	{
  //		Encoder.Reset();
  //	}

  public final void Send(byte[] buf) throws IOException {
    Output.write(buf);
    Output.flush();
  }

  public final byte[] Receive(int count) throws IOException {
    byte[] buf = new byte[count];
    int rest = buf.length;
    while (rest > 0) {
      int len = Input.read(buf, buf.length - rest, rest);
      if (len == 0) {
        throw new RuntimeException("Server disconnected");
      } else if (len == -1) {
        throw new RuntimeException("Server closed the stream");
      } else {
        rest -= len;
      }
    }
    return buf;
  }

  //	public final int SendEnrypt(byte[] hdr, byte[] buf)
  //	{
  //		byte value = 0;
  //		for (int i = 1; i < buf.length; i++)
  //		{
  //			value = (byte)((((value & 0xFF) + (Crypt.CryptKey[(i - 1) & 0xF] & 0xFF)) ^ (buf[i] & 0xFF))
  // & 0xFF);
  //			buf[i] = value;
  //		}
  //		return Sock.Send(buf);
  //	}
  //
  //	public final int SendEnrypt(byte[] buf)
  //	{
  //		byte value = 0;
  //		for (int i = 1; i < buf.length; i++)
  //		{
  //			value = (byte)((((value & 0xFF) + (Crypt.CryptKey[(i - 1) & 0xF] & 0xFF)) ^ (buf[i] & 0xFF))
  // & 0xFF);
  //			buf[i] = value;
  //		}
  //		return Sock.Send(buf);
  //	}
  //
  //	public final byte[] ReceiveDecrypt(int count)
  //	{
  //		byte[] buf = Receive(count);
  //		byte prev = 0;
  //		for (int i = 0; i < buf.length; i++)
  //		{
  //			byte value = (byte)(((prev & 0xFF) + (Crypt.CryptKey[i & 0xF] & 0xFF)) & 0xFF);
  //			prev = buf[i];
  //			(buf[i] & 0xFF) ^= (value & 0xFF);
  //		}
  //		Log.trace("RECV " + ConvertBytes.ToHex(buf));
  //		return buf;
  //	}
  //
  //	public final int SendEncode(byte[] buf)
  //	{
  //		return Sock.Send(Encoder.Encode(buf));
  //	}

  public final byte[] ReceiveDecode(int count) throws IOException {
    byte[] buf = Receive(count);
    return Decoder.Decode(buf);
  }

  public final byte[] ReceiveCopmressed() throws IOException {
    byte[] buf = ReceiveDecode(4);
    int dstlen = BitConverter.ToInt32(buf, 0);
    buf = ReceiveDecode(4);
    int len = BitConverter.ToInt32(buf, 0);
    buf = ReceiveDecode(len);
    return Decompressor.decompress(buf, dstlen);
  }

  final void Connect(String host, int port) throws IOException {
    if (Decoder != null) {
      Decoder.Reset();
    }
    if (Encoder != null) {
      Encoder.Reset();
    }
    Sock = new Socket();
    Sock.connect(new InetSocketAddress(host, port), 30000);
    Sock.setSoTimeout(30000);
    Output = new DataOutputStream(Sock.getOutputStream());
    Input = new DataInputStream(Sock.getInputStream());
  }

  final void Close() {
    try {
      if (Sock != null) {
        Sock.close();
      }
    } catch (Exception ex) {
    }
    try {
      if (Input != null) {
        Input.close();
      }
    } catch (Exception ex) {
    }
    try {
      if (Output != null) {
        Output.close();
      }
    } catch (Exception ex) {
    }
  }

  int avaliable() throws IOException {
    return Input.available();
  }
}
