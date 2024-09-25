package io.quantum.trading.brokers.indian.kotak;

import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import okhttp3.logging.HttpLoggingInterceptor;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;

@Log4j2
public class KotakApiClient {

  private OkHttpClient httpClient;
  private Map<String, Object> defaultHeaders = new HashMap<String, Object>();
  private Map<String, Object> defaultQueryParams = new HashMap<String, Object>();
  private String apiAuthorizationHeader = "";
  private String consumerKey = "";
  private boolean debug;
  @Getter private String ip = "127.0.0.1";
  private HttpLoggingInterceptor loggingInterceptor;
  private JSON json = new JSON();
  private KotakTokenResponse token = null;
  private static String LOCK_OBJ = "dummy";
  @Setter private AuthHandler authHandler = null;

  public KotakApiClient(String consumerKey, String consumerSecret) {
    httpClient = new OkHttpClient.Builder().build();

    var consumerCombined = consumerKey + ":" + consumerSecret;
    this.consumerKey = consumerKey;
    apiAuthorizationHeader =
        "Basic "
            + Base64.getEncoder().encodeToString(consumerCombined.getBytes(StandardCharsets.UTF_8));
    addDefaultHeader("User-Agent", "Java");

    try {
      Socket socket = new Socket();
      socket.connect(new InetSocketAddress("google.com", 80));
      ip = socket.getLocalAddress().getHostAddress();
      socket.close();
    } catch (IOException e) {
      log.error("Cant find device IP, using localhost", e);
    }
  }

  public void setDefaultHeaders(Map<String, Object> defaultHeaders) {
    synchronized (LOCK_OBJ) {
      this.defaultHeaders = defaultHeaders;
    }
  }

  public void addDefaultHeader(String key, String value) {
    defaultHeaders.put(key, value);
  }

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    if (debug != this.debug) {
      if (debug) {
        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = httpClient.newBuilder().addInterceptor(loggingInterceptor).build();
      } else {
        httpClient.interceptors().remove(loggingInterceptor);
        loggingInterceptor = null;
      }
    }
    this.debug = debug;
  }

  public String login(KotakLoginPayload payload, Map<String, Object> headers) throws Exception {
    var res = executeRequest(Method.POST, ClientBase.LOGIN_USERID, null, headers, null, payload);
    var loginRes = (KotakLoginResponse) deserializeIfSuccess(res, KotakLoginResponse.class);
    return getSessionToken(payload, loginRes, headers);
  }

  private String getSessionToken(
      KotakLoginPayload payload, KotakLoginResponse kotakLoginResponse, Map<String, Object> headers)
      throws Exception {
    headers.put("oneTimeToken", kotakLoginResponse.getOneTimeToken());
    var res =
        executeRequest(
            Method.POST, ClientBase.ONE_TIME_TOKEN_VALIDATE, null, headers, null, payload);
    var sessionResObj = (JsonObject) deserializeIfSuccess(res, JsonObject.class);
    if (!sessionResObj.has("sessionToken")) {
      throw new Exception(
          "Seesion Token not part of response, Response json: "
              + json.getGson().toJson(sessionResObj));
    }
    return sessionResObj.get("sessionToken").getAsString();
  }

  public KotakTokenResponse getAccessToken(
      String apiUsername, String apiPassword, String refreshToken) throws Exception {
    var formParams = new HashMap<String, Object>();
    formParams.put("grant_type", "password");
    if (apiUsername != null) {
      formParams.put("username", apiUsername);
    }
    if (apiPassword != null) {
      formParams.put("password", apiPassword);
    }
    if (refreshToken != null) {
      formParams.put("refresh_token", refreshToken);
    }

    var headers = new HashMap<String, Object>();
    headers.put("Content-Type", "application/x-www-form-urlencoded");
    headers.put("Authorization", apiAuthorizationHeader);

    var res = executeRequest(Method.POST, ClientBase.TOKEN, null, headers, formParams, null);
    var resString = res.body().string();
    var resToken = (KotakTokenResponse) json.deserialize(resString, KotakTokenResponse.class);
    if (resToken.getAccessToken() != null) {
      token = resToken;
    }
    return resToken;
  }

  public KotakInstrumentLinkResponse getInstrumentLinks() {
    Response res = null;
    try {
      res =
          executeRequest(
              Method.GET, ClientBase.SCRIPT_MASTER_API_BASE + "/filename", null, null, null, null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return deserializeIfSuccess(res, KotakInstrumentLinkResponse.class);
  }

  public double getMargin() {
    Response res = null;
    try {
      res =
          executeRequest(
              Method.GET, ClientBase.MARGIN_API_BASE + "/margin", null, null, null, null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    var resData = deserializeIfSuccess(res, Map.class);
    double margin =
        (double)
            ((Map) (((Map) (((ArrayList) (resData.get("equity"))).get(0))).get("cash")))
                .get("totalMargin");
    return margin;
  }

  public String placeOrder(KotakPlaceOrderPayload payload) {
    Response res = null;
    try {
      res = executeRequest(Method.POST, ClientBase.ORDER_API_BASE, null, null, null, payload);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    var placeOrderResponse = deserializeIfSuccess(res, KotakPlaceOrderResponse.class);
    return String.valueOf(placeOrderResponse.getExchangeResponse().getOrderId());
  }

  public void cancelOrder(String orderId) {
    Response res = null;
    try {
      res =
          executeRequest(
              Method.DELETE, ClientBase.ORDER_API_BASE + "/" + orderId, null, null, null, null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      log.info("Cancel Order response - {}", res.body().string());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<KotakOrderDetailsResponse> getOrderDetails(String orderId) {
    Response res = null;
    try {
      res =
          executeRequest(
              Method.GET, ClientBase.REPORTS_API_BASE + "/orders/", null, null, null, null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    var orderDetails = Arrays.asList(deserializeIfSuccess(res, KotakOrderDetailsResponse[].class));
    orderDetails =
        orderDetails.stream()
            .filter(orderDetail -> orderDetail.getOrderId() == Long.valueOf(orderId))
            .collect(Collectors.toList());
    if (orderDetails.isEmpty()) {
      throw new RuntimeException("OrderId, " + orderId + " is not found");
    } else return orderDetails;
  }

  public <T> T deserializeIfSuccess(Response response, Class<T> returnType) {
    String resString = null;
    try {
      resString = response.body().string();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    var jsonObj = (JsonObject) json.deserialize(resString, JsonObject.class);
    if (jsonObj.has("Success")) {
      var payloadString = json.getGson().toJson(jsonObj.get("Success"));
      return json.deserialize(payloadString, returnType);
    } else if (jsonObj.has("success")) {
      var payloadString = json.getGson().toJson(jsonObj.get("success"));
      return json.deserialize(payloadString, returnType);
    } else {
      throw new RuntimeException("Login Failed, Response json: " + resString);
    }
  }

  private Response executeRequest(
      Method method,
      String path,
      Map<String, Object> queryParams,
      Map<String, Object> headers,
      Map<String, Object> formParams,
      Object body)
      throws IOException {
    Request request = null;
    Request.Builder reqBuilder = null;
    RequestBody reqBody;
    synchronized (LOCK_OBJ) {
      var pathWithDefaultParams = frameUrlWithQueryParams(path, defaultQueryParams);
      var pathWithParams = frameUrlWithQueryParams(pathWithDefaultParams, queryParams);

      reqBuilder = new Request.Builder().url(pathWithParams);

      for (var header : defaultHeaders.entrySet()) {
        reqBuilder.header(header.getKey(), parameterToString(header.getValue()));
      }

      String contentType = "application/json";
      if (headers != null) {
        for (var param : headers.entrySet()) {
          reqBuilder.header(param.getKey(), parameterToString(param.getValue()));
        }

        if (headers.get("Content-Type") != null) {
          contentType = headers.get("Content-Type").toString();
        }
      }

      if (!HttpMethod.permitsRequestBody(method.method)) {
        reqBody = null;
      } else if ("application/x-www-form-urlencoded".equals(contentType)) {
        reqBody = buildRequestBodyFormEncoding(formParams);
      } else if ("multipart/form-data".equals(contentType)) {
        reqBody = buildRequestBodyMultipart(formParams);
      } else if (body == null) {
        if (method == Method.DELETE) {
          reqBody = null;
        } else {
          reqBody = RequestBody.create(MediaType.parse(contentType), "");
        }
      } else {
        reqBody = serialize(body, contentType);
      }

      request = reqBuilder.method(method.method, reqBody).build();
    }
    var response = httpClient.newCall(request).execute();
    var resBodyStr = Objects.requireNonNull(response.body()).string();
    if (isUnAuthorizedRes(response, resBodyStr)) {
      if (authHandler != null) {
        authHandler.login();
        for (var header : defaultHeaders.entrySet()) {
          reqBuilder.header(header.getKey(), parameterToString(header.getValue()));
        }
        request = reqBuilder.method(method.method, reqBody).build();
        response = httpClient.newCall(request).execute();
        resBodyStr = Objects.requireNonNull(response.body()).string();
        if (isUnAuthorizedRes(response, resBodyStr)) {
          throw new RuntimeException("Authentication Failed Second time can't proceed");
        }
      }
    }
    var resContentType = Objects.requireNonNull(response.body()).contentType();
    var resBody = ResponseBody.create(resBodyStr, resContentType);
    return response.newBuilder().body(resBody).build();
  }

  private boolean isUnAuthorizedRes(Response response, String resBodyStr) {
    return response.code() == 401
        || resBodyStr.toLowerCase().contains("Invalid Credentials".toLowerCase())
        || resBodyStr
            .toLowerCase()
            .contains("Session token is either expired or not correct".toLowerCase());
  }

  private RequestBody buildRequestBodyFormEncoding(Map<String, Object> formParams) {
    var formBuilder = new okhttp3.FormBody.Builder();
    for (Map.Entry<String, Object> param : formParams.entrySet()) {
      formBuilder.add(param.getKey(), parameterToString(param.getValue()));
    }
    return formBuilder.build();
  }

  private RequestBody buildRequestBodyMultipart(Map<String, Object> formParams) {
    MultipartBody.Builder mpBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    for (Map.Entry<String, Object> param : formParams.entrySet()) {
      if (param.getValue() instanceof File) {
        File file = (File) param.getValue();
        Headers partHeaders =
            Headers.of(
                "Content-Disposition",
                "form-data; name=\"" + param.getKey() + "\"; filename=\"" + file.getName() + "\"");
        MediaType mediaType = MediaType.parse(guessContentTypeFromFile(file));
        mpBuilder.addPart(partHeaders, RequestBody.create(mediaType, file));
      } else {
        Headers partHeaders =
            Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\"");
        mpBuilder.addPart(
            partHeaders, RequestBody.create(null, parameterToString(param.getValue())));
      }
    }
    return mpBuilder.build();
  }

  private String guessContentTypeFromFile(File file) {
    String contentType = URLConnection.guessContentTypeFromName(file.getName());
    if (contentType == null) {
      return "application/octet-stream";
    } else {
      return contentType;
    }
  }

  private String frameUrlWithQueryParams(String path, Map<String, Object> queryParams) {
    final StringBuilder url = new StringBuilder();
    url.append(path);

    if (queryParams != null && !queryParams.isEmpty()) {
      String prefix = path.contains("?") ? "&" : "?";
      for (var param : queryParams.entrySet()) {
        if (param.getValue() != null) {
          if (prefix != null) {
            url.append(prefix);
            prefix = null;
          } else {
            url.append("&");
          }
          url.append(param.getKey()).append("=").append(parameterToString(param.getValue()));
        }
      }
    }
    return url.toString();
  }

  public String parameterToString(Object param) {
    if (param == null) {
      return "";
    } else if (param instanceof Date
        || param instanceof OffsetDateTime
        || param instanceof LocalDate) {
      String jsonStr = json.serialize(param);
      return jsonStr.substring(1, jsonStr.length() - 1);
    } else if (param instanceof Collection) {
      StringBuilder b = new StringBuilder();
      for (Object o : (Collection) param) {
        if (b.length() > 0) {
          b.append(",");
        }
        b.append(String.valueOf(o));
      }
      return b.toString();
    } else {
      return String.valueOf(param);
    }
  }

  private RequestBody serialize(Object obj, String contentType) {
    if (obj instanceof byte[]) {
      return RequestBody.create(MediaType.parse(contentType), (byte[]) obj);
    } else if (obj instanceof File) {
      return RequestBody.create(MediaType.parse(contentType), (File) obj);
    } else if (isJsonMime(contentType)) {
      String content;
      if (obj != null) {
        content = json.serialize(obj);
      } else {
        content = null;
      }
      return RequestBody.create(MediaType.parse(contentType), content);
    } else {
      throw new RuntimeException("Content type \"" + contentType + "\" is not supported");
    }
  }

  private boolean isJsonMime(String mime) {
    String jsonMime = "(?i)^(application/json|[^;/ \t]+/[^;/ \t]+[+]json)[ \t]*(;.*)?$";
    return mime != null && (mime.matches(jsonMime) || mime.equals("*/*"));
  }

  public static final class ClientBase {
    public static final String TOKEN_API_BASE = "https://tradeapi.kotaksecurities.com";
    public static final String TOKEN = TOKEN_API_BASE + "/token";

    public static final String SESSION_API_BASE =
        "https://tradeapi.kotaksecurities.com/apim/session/1.0/session";

    public static final String LOGIN_USERID = SESSION_API_BASE + "/login/userid";
    public static final String ONE_TIME_TOKEN_VALIDATE = SESSION_API_BASE + "/2FA/oneTimeToken";
    public static final String ORDER_API_BASE =
        "https://tradeapi.kotaksecurities.com/apim/orders/1.0/orders";
    public static final String REPORTS_API_BASE =
        "https://tradeapi.kotaksecurities.com/apim/reports/1.0";
    public static final String SCRIPT_MASTER_API_BASE =
        "https://tradeapi.kotaksecurities.com/apim/scripmaster/1.1";
    public static final String MARGIN_API_BASE =
        "https://tradeapi.kotaksecurities.com/apim/margin/1.0";
  }

  public enum Method {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE");

    private final String method;

    Method(final String method) {
      this.method = method;
    }

    @Override
    public String toString() {
      return method;
    }
  }

  public interface AuthHandler {
    void login();
  }
}
