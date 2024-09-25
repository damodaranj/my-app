// package io.quantum.trading.brokers.kotak;
//
// import com.fasterxml.jackson.databind.MappingIterator;
// import com.fasterxml.jackson.dataformat.csv.CsvMapper;
// import com.fasterxml.jackson.dataformat.csv.CsvSchema;
// import io.quantum.trading.brokers.OrderDetail;
// import io.quantum.trading.brokers.OrderDetail.BrokerOrderStatus;
// import io.quantum.trading.brokers.zerodha.ZerodhaBroker;
// import io.quantum.trading.entities.BrokerConfigDetails.KotakBrokerConfigDetails;
// import io.quantum.trading.entities.BrokerConfigDetails.ZerodhaBrokerConfigDetails;
// import lombok.SneakyThrows;
// import lombok.extern.slf4j.Slf4j;
// import org.joda.time.DateTime;
//
// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
//
// @Slf4j
// public class KotakBroker extends ZerodhaBroker {
//    private KotakApiClient kotakApiClient;
//    private List<KotakInstrument> kotakInstruments;
//    private boolean dryRun;
//
//    public KotakBroker(KotakBrokerConfigDetails brokerConfigDetails,
//                       ZerodhaBrokerConfigDetails zerodhaBrokerConfigDetails, boolean dryRun) {
//        super(zerodhaBrokerConfigDetails, dryRun);
//        this.dryRun = dryRun;
//
//        kotakApiClient = new KotakApiClient(brokerConfigDetails.getConsumerKey(),
// brokerConfigDetails.getConsumerSecret());
//        kotakApiClient.setDebug(false);
//        kotakApiClient.setAuthHandler(() -> kotakLogin(brokerConfigDetails));
//        refreshKotakInstruments();
//    }
//
//    @SneakyThrows
//    private void kotakLogin(KotakBrokerConfigDetails brokerConfigDetails) {
//        var apiUsername = brokerConfigDetails.getUserId();
//        var apiPassword = brokerConfigDetails.getTradeApiPassword();
//        var consumerKey = brokerConfigDetails.getConsumerKey();
//        var accPassword = brokerConfigDetails.getAccPassword();
//        var tokenRes = kotakApiClient.getAccessToken(apiUsername, apiPassword, null);
//        Map<String, Object> defaultHeaders = new HashMap<>();
//        defaultHeaders.put("Authorization", "Bearer " + tokenRes.getAccessToken());
//        defaultHeaders.put("consumerKey", consumerKey);
//        defaultHeaders.put("ip", kotakApiClient.getIp());
//
//        var payload = new KotakLoginPayload();
//        payload.setUserid(apiUsername);
//        payload.setPassword(accPassword);
//        var sessionToken = kotakApiClient.login(payload, defaultHeaders);
//        defaultHeaders.put("sessionToken", sessionToken);
//
//        kotakApiClient.setDefaultHeaders(defaultHeaders);
//    }
//
//    @SneakyThrows
//    private void refreshKotakInstruments() {
//        var instrumentLinks = kotakApiClient.getInstrumentLinks();
//        var fnoUrl = instrumentLinks.getFno();
//        var cashUrl = instrumentLinks.getCash();
//        kotakInstruments = new ArrayList<>();
//        var client = HttpClient.newBuilder().build();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(new URI(fnoUrl))
//                .GET()
//                .build();
//        HttpResponse<String> response = client.send(request,
// HttpResponse.BodyHandlers.ofString());
//
//        CsvSchema bootstrap = CsvSchema.emptySchema().withHeader().withColumnSeparator('|');
//        CsvMapper csvMapper = new CsvMapper();
//        MappingIterator<KotakInstrument> mappingIterator =
// csvMapper.readerFor(KotakInstrument.class)
//                .with(bootstrap)
//                .readValues(response.body());
//        var fnoInstruments = mappingIterator.readAll();
//        kotakInstruments.addAll(fnoInstruments);
//
//        // Cash instruments
//        request = HttpRequest.newBuilder()
//                .uri(new URI(cashUrl))
//                .GET()
//                .build();
//        response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        mappingIterator = csvMapper.readerFor(KotakInstrument.class)
//                .with(bootstrap)
//                .readValues(response.body());
////        var cashInstruments = mappingIterator.readAll();
////        kotakInstruments.addAll(cashInstruments);
//
//        log.info("Refreshed the instruments. Total count is {}", kotakInstruments.size());
//    }
//
//    @Override
//    public double getBalance() {
//        return kotakApiClient.getMargin();
//    }
//
//    @Override
//    public boolean isLoggedIn() {
//        try {
//            getBalance();
//            return true;
//        } catch (Throwable t) {
//            log.warn("Getting balance failed with exception. Concluding that the broker is not
// logged in", t);
//            return false;
//        }
//    }
//
//    @Override
//    public String placeOrder(String tradingSymbol, double quantity, String transactionType, String
// tag, double limitPrice) {
//        var zerodhaInstrument = getInstruments().stream()
//                .filter(instrument -> instrument.tradingsymbol.equals(tradingSymbol))
//                .findFirst()
//                .get();
//        var kotakInstrument = kotakInstruments.stream()
//                .filter(instrument ->
// instrument.getExchangeToken().equals(String.valueOf(zerodhaInstrument.getExchange_token())))
//                .findFirst()
//                .get();
//        log.info("Zerodha tradingSymbol - {}; Kotak instrumrntToken - {}", tradingSymbol,
// kotakInstrument.getInstrumentToken());
//        // Place Kotak order
//        var placeOrderPayload = KotakPlaceOrderPayload.builder()
//                .instrumentToken(Integer.parseInt(kotakInstrument.getInstrumentToken()))
//                .transactionType(transactionType)
//                .quantity((int) quantity)
//                .price(limitPrice) // limit order
//                .product("MIS")
//                .validity("GFD")
//                .variety("REGULAR")
//                .disclosedQuantity(0)
//                .triggerPrice(0)
//                .tag(tag)
//                .build();
//        return kotakApiClient.placeOrder(placeOrderPayload);
//    }
//
//    @Override
//    public OrderDetail getOrderDetail(String orderId) {
//        log.info("Getting details for orderId, {}", orderId);
//        while (true) {
//            try {
//                var orders = kotakApiClient.getOrderDetails(orderId);
//                var finalOrder = orders.get(orders.size() - 1);
//                return OrderDetail.builder()
//                        .orderId(String.valueOf(finalOrder.getOrderId()))
//                        .openTimestamp(new DateTime(finalOrder.getOrderTimestamp()))
//                        .avgPrice(finalOrder.getPrice())
//                        .status(fromStatus(finalOrder.getStatus()))
//                        .build();
//            } catch (Exception e) {
//                log.error("Error while checking order, {}. Checking the order details again",
// orderId, e);
//            }
//        }
//    }
//
//    protected BrokerOrderStatus fromStatus(String status) {
//        BrokerOrderStatus orderStatus;
//        switch (status) {
//            case "TRAD":
//                orderStatus = BrokerOrderStatus.COMPLETE;
//                break;
//            case "CAN":
//                orderStatus = BrokerOrderStatus.CANCELLED;
//                break;
////            case "REJECTED":
////                orderStatus = BrokerOrderStatus.REJECTED;
////                break;
//            default:
//                orderStatus = BrokerOrderStatus.OPEN;
//                break;
//        }
//        return orderStatus;
//    }
//
//    @Override
//    public void cancelOrder(String orderId) {
//        if (orderId.equals(DUMMY_TRADE)) return;
//        log.info("Cancelling order, {}", orderId);
//        kotakApiClient.cancelOrder(orderId);
//    }
//
//    @Override
//    public double pollUntilOrderCancel(String orderId) {
//        if (orderId.equals(DUMMY_TRADE)) return -1;
//        while (true) {
//            log.info("Polling to verify cancel order status for orderId, {}", orderId);
//            try {
//                var orders = kotakApiClient.getOrderDetails(orderId);
//                if (orders.size() == 0) {
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        // ignore
//                    }
//                    continue;
//                }
//                var finalOrder = orders.get(orders.size() - 1);
//                if (fromStatus(finalOrder.getStatus()) == BrokerOrderStatus.CANCELLED) {
//                    return finalOrder.getPrice();
//                }
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    // ignore
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("Error while checking order, " + orderId, e);
//            }
//        }
//    }
//
//    @Override
//    public double pollUntilOrderCompletion(String orderId) {
//        if (orderId.equals(DUMMY_TRADE)) return -1;
//        while (true) {
//            log.info("Polling to verify order status for orderId, {}", orderId);
//            try {
//                var orders = kotakApiClient.getOrderDetails(orderId);
//                if (orders.size() == 0) {
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        // ignore
//                    }
//                    continue;
//                }
//                var finalOrder = orders.get(orders.size() - 1);
//                if (fromStatus(finalOrder.getStatus()) == BrokerOrderStatus.COMPLETE) {
//                    return finalOrder.getPrice();
//                }
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    // ignore
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("Error while checking order, " + orderId, e);
//            }
//        }
//    }
// }
