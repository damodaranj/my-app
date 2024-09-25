package io.quantum.trading.brokers.forex.mt5protocol.mtapi.mt5;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

class LoginIdWebServer {
  class Run implements Runnable {
    String Url;
    byte[] Data;
    boolean LoginIdData;

    long Id;
    IOException Ex;

    @Override
    public void run() {
      try {
        URL obj = new URL(Url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "FIREFOX");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        String str = Base64.getEncoder().encodeToString(Data);
        if (LoginIdData) str = "loginiddata" + str;
        wr.writeBytes(str);
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
        if (response.toString().equals("OK")) return;
        if (!Character.isDigit(response.toString().charAt(0)))
          throw new IOException("LoginIdWebServer: " + response.toString());
        Id = new BigInteger(response.toString(), 10).longValue();
      } catch (IOException e) {
        Ex = new IOException("LoginIdWebServer: " + e.getMessage());
      }
    }
  }

  long Decode(String url, String guid, byte[] data, int timeout, Logger log)
      throws IOException, TimeoutException {

    Run run = new Run();
    run.Url = url + "?guid=" + guid;
    run.Data = data;
    run.LoginIdData = false;
    Thread th = new Thread(run);
    th.start();
    try {
      th.join(timeout);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    if (th.isAlive())
      throw new TimeoutException("No reply from login id web server in " + timeout + "ms", log);
    if (run.Ex != null) throw run.Ex;
    return run.Id;
  }
}
