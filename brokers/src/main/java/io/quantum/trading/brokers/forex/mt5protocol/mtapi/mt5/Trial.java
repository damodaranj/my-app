package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import javax.crypto.*;

class Trial {
  private static String Res = null;

  static void check(String guid) {
    try {
      if (true) return;
      if (Res == null) {
        URL obj = new URL("https://trial.mtapi.io/NewCheckMT5Java?guid=" + guid);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        PrintWriter wr = new PrintWriter(con.getOutputStream());
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();
        Res = response.toString();
      }
      if (!Res.startsWith("OK")) throw new Exception(Res);
    } catch (Exception ex) {
      throw new RuntimeException("Trial check exception: " + ex.getMessage());
    }
  }
}
