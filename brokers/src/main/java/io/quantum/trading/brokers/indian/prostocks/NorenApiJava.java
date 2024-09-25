package io.quantum.trading.brokers.indian.prostocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.json.GsonJsonParser;

@Slf4j
public class NorenApiJava {
  String _host;
  NorenRequests _api;
  private String _userid;
  private String _actid;
  private String _key;
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final GsonJsonParser GSON_JSON_PARSER = new GsonJsonParser();

  public NorenApiJava(String host) {
    this._host = host;
    this._api = new NorenRequests(host);
  }

  public String login(
      String userid,
      String password,
      String twoFA,
      String vendor_code,
      String api_secret,
      String imei) {
    String url = this._api.routes.get("authorize");
    JSONObject jsonObject = new JSONObject();
    String passwordsha = this._api.sha256(password);
    String appkey = userid + "|" + api_secret;
    String appkeysha = this._api.sha256(appkey);
    jsonObject.put("source", "API");
    jsonObject.put("apkversion", "1.0.0");
    jsonObject.put("uid", userid);
    jsonObject.put("pwd", passwordsha);
    jsonObject.put("factor2", twoFA);
    jsonObject.put("vc", vendor_code);
    jsonObject.put("appkey", appkeysha);
    jsonObject.put("imei", imei);
    String response = this._api.post(url, jsonObject);
    JSONObject jsonResp = new JSONObject(response);
    String stat = jsonResp.getString("stat").toString();
    if ("Ok".equals(stat)) {
      this._userid = userid;
      this._actid = userid;
      this._key = jsonResp.getString("susertoken").toString();
    }

    return response;
  }

  public JSONObject search(String exchange, String searchtext) {
    String url = this._api.routes.get("searchscrip");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("uid", this._userid);
    jsonObject.put("exch", exchange);
    jsonObject.put("stext", this._api.encodeValue(searchtext));
    String response = this._api.post(url, this._key, jsonObject);
    JSONObject jsonResp = new JSONObject(response);
    return jsonResp;
  }

  public JSONArray get_order_book() {
    String url = this._api.routes.get("orderbook");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("uid", this._userid);
    String response = this._api.post(url, this._key, jsonObject);
    System.out.println(response);
    if (response.charAt(0) == '[') {
      JSONArray jsonResp = new JSONArray(response);
      return jsonResp;
    } else {
      return null;
    }
  }

  public JSONArray get_order_history(String norenorderno) {
    String url = this._api.routes.get("singleorderhistory");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("uid", this._userid);
    jsonObject.put("norenordno", norenorderno);
    String response = this._api.post(url, this._key, jsonObject);
    log.info("Get order history response - {}", response);
    if (response.charAt(0) == '[') {
      response = removeDuplicates(response);
      JSONArray jsonResp = new JSONArray(response);
      return jsonResp;
    } else {
      return null;
    }
  }

  @SneakyThrows
  public static String removeDuplicates(String inputJson) {
    var list = GSON_JSON_PARSER.parseList(inputJson);
    return OBJECT_MAPPER.writeValueAsString(list);
  }

  public JSONArray get_trade_book() {
    String url = this._api.routes.get("tradebook");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("uid", this._userid);
    jsonObject.put("actid", this._actid);
    String response = this._api.post(url, this._key, jsonObject);
    System.out.println(response);
    if (response.charAt(0) == '[') {
      JSONArray jsonResp = new JSONArray(response);
      return jsonResp;
    } else {
      return null;
    }
  }

  public JSONObject get_quotes(String exchange, String token) {
    String url = this._api.routes.get("getquotes");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("uid", this._userid);
    jsonObject.put("exch", exchange);
    jsonObject.put("token", token);

    String response = this._api.post(url, this._key, jsonObject);
    JSONObject jsonResp = new JSONObject(response);
    return jsonResp;
  }

  public JSONObject place_order(
      String buy_or_sell,
      String product_type,
      String exchange,
      String tradingsymbol,
      Integer quantity,
      Integer discloseqty,
      String price_type,
      Double price,
      String remarks,
      Double trigger_price,
      String retention,
      String amo,
      Double bookloss_price,
      Double bookprofit_price,
      Double trail_price) {
    String url = this._api.routes.get("placeorder");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("uid", this._userid);
    jsonObject.put("ordersource", "API");
    jsonObject.put("actid", this._actid);
    jsonObject.put("trantype", buy_or_sell);
    jsonObject.put("prd", product_type);
    jsonObject.put("exch", exchange);
    jsonObject.put("tsym", this._api.encodeValue(tradingsymbol));
    jsonObject.put("qty", Integer.toString(quantity));
    jsonObject.put("dscqty", Integer.toString(discloseqty));
    jsonObject.put("prctyp", price_type);
    jsonObject.put("prc", Double.toString(price));
    if (null != trigger_price) {
      jsonObject.put("trgprc", Double.toString(trigger_price));
    }

    if (null == retention) {
      retention = "DAY";
    }

    jsonObject.put("ret", retention);
    jsonObject.put("remarks", remarks);
    if (null != amo) {
      jsonObject.put("amo", amo);
    }

    String response = this._api.post(url, this._key, jsonObject);
    JSONObject jsonResp = new JSONObject(response);
    return jsonResp;
  }

  public JSONObject modify_order(
      String orderno,
      String exchange,
      String tradingsymbol,
      Integer newquantity,
      String newprice_type,
      Double newprice,
      Double newtrigger_price,
      Double bookloss_price,
      Double bookprofit_price,
      Double trail_price) {
    String url = this._api.routes.get("modifyorder");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("uid", this._userid);
    jsonObject.put("ordersource", "API");
    jsonObject.put("actid", this._actid);
    jsonObject.put("norenordno", orderno);
    jsonObject.put("exch", exchange);
    jsonObject.put("tsym", this._api.encodeValue(tradingsymbol));
    jsonObject.put("qty", Integer.toString(newquantity));
    jsonObject.put("prctyp", newprice_type);
    jsonObject.put("prc", Double.toString(newprice));
    if (newprice_type.equals("SL-LMT") || "SL-MKT".equals(newprice_type)) {
      if (newtrigger_price == null) {
        return null;
      }

      jsonObject.put("trgprc", Double.toString(newtrigger_price));
    }

    if (newtrigger_price != null) {
      jsonObject.put("blprc", Double.toString(bookloss_price));
    }

    if (trail_price != null) {
      jsonObject.put("trailprc", Double.toString(trail_price));
    }

    if (bookprofit_price != null) {
      jsonObject.put("bpprc", Double.toString(bookprofit_price));
    }

    String response = this._api.post(url, this._key, jsonObject);
    JSONObject jsonResp = new JSONObject(response);
    return jsonResp;
  }

  public JSONObject cancel_order(String orderno) {
    String url = this._api.routes.get("cancelorder");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("uid", this._userid);
    jsonObject.put("ordersource", "API");
    jsonObject.put("norenordno", orderno);
    String response = this._api.post(url, this._key, jsonObject);
    JSONObject jsonResp = new JSONObject(response);
    return jsonResp;
  }
}
