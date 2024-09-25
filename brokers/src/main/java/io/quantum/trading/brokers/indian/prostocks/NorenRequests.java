package io.quantum.trading.brokers.indian.prostocks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

public class NorenRequests {
  OkHttpClient client = new OkHttpClient();
  NorenRoutes routes = new NorenRoutes();
  public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  public NorenRequests(String host) {
    NorenRoutes._host = host;
  }

  public String encodeValue(String value) {
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException var3) {
      return var3.toString();
    }
  }

  private static String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder(2 * hash.length);

    for (int i = 0; i < hash.length; ++i) {
      String hex = Integer.toHexString(255 & hash[i]);
      if (hex.length() == 1) {
        hexString.append('0');
      }

      hexString.append(hex);
    }

    return hexString.toString();
  }

  String sha256(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
      return bytesToHex(encodedhash);
    } catch (NoSuchAlgorithmException var4) {
      return input;
    }
  }

  String run(String url) {
    try {
      Request request = (new Builder()).url(url).build();
      Response response = this.client.newCall(request).execute();
      return response.body().string();
    } catch (IOException var4) {
      var4.printStackTrace();
      return var4.toString();
    }
  }

  String post(String url, JSONObject jsnObj) {
    String json = "jData=" + jsnObj.toString();
    System.out.println(url + " " + json);
    RequestBody body = RequestBody.create(json, JSON);
    Request request = (new Builder()).url(url).post(body).build();

    try {
      Response response = this.client.newCall(request).execute();
      return response.body().string();
    } catch (IOException var7) {
      var7.printStackTrace();
      return var7.toString();
    }
  }

  String post(String url, String key, JSONObject jsnObj) {
    String var10000 = jsnObj.toString();
    String json = "jData=" + var10000 + "&jKey=" + key;
    System.out.println(url + " " + json);
    RequestBody body = RequestBody.create(json, JSON);
    Request request = (new Builder()).url(url).post(body).build();

    try {
      Response response = this.client.newCall(request).execute();
      return response.body().string();
    } catch (IOException var8) {
      var8.printStackTrace();
      return var8.toString();
    }
  }
}
