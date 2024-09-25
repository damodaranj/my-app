// package io.quantum.trading.brokers.prostocks;
//
// import com.noren.javaapi.NorenApiJava;
// import io.quantum.trading.brokers.BrokerException;
// import io.quantum.trading.brokers.OrderDetail;
// import io.quantum.trading.brokers.zerodha.ZerodhaBroker;
// import io.quantum.trading.entities.BrokerConfigDetails;
// import io.quantum.trading.util.ThreadFactoryHelper;
// import lombok.extern.slf4j.Slf4j;
// import org.joda.time.DateTime;
// import org.joda.time.format.DateTimeFormat;
// import org.joda.time.format.DateTimeFormatter;
// import org.json.JSONObject;
//
// import java.text.SimpleDateFormat;
// import java.util.concurrent.Executors;
// import java.util.concurrent.ScheduledExecutorService;
// import java.util.concurrent.TimeUnit;
//
// @Slf4j
// public class ProStocksBroker extends ZerodhaBroker {
//    private static NorenApiJava norenClient;
//    private static String identifier = "Dotconn-Java-SDK";
//
//    private static final ScheduledExecutorService LOGIN_POOL =
// Executors.newSingleThreadScheduledExecutor(new ThreadFactoryHelper("prostocks-login-"));
//
//    private BrokerConfigDetails.ProStocksBrokerConfigDetails brokerConfigDetails;
//
//    public ProStocksBroker(BrokerConfigDetails.ProStocksBrokerConfigDetails
// proStocksBrokerConfigDetails,
//            BrokerConfigDetails.ZerodhaBrokerConfigDetails zerodhaBrokerConfigDetails, boolean
// dryRun) {
//        super(zerodhaBrokerConfigDetails, dryRun);
//        this.brokerConfigDetails = proStocksBrokerConfigDetails;
//
//        norenClient = new NorenApiJava(proStocksBrokerConfigDetails.getBrokerUrl());
//        this.login();
//        LOGIN_POOL.scheduleAtFixedRate(this::login, 10, 10, TimeUnit.MINUTES);
//    }
//
//    private void login() {
//        var res = norenClient.login(brokerConfigDetails.getUserId(),
// brokerConfigDetails.getPassword(),
//                brokerConfigDetails.getTwoFactor(), brokerConfigDetails.getVendorCode(),
// brokerConfigDetails.getApiKey(),
//                identifier);
//        var resObj = new JSONObject(res);
//        if (!resObj.getString("stat").equalsIgnoreCase("ok")) {
//            throw new RuntimeException("Login failed response: " + res);
//        }
//    }
//
//    private String getProStocksTradingSymbol(String tradingSymbol) {
//        var ins = getInstruments().stream()
//                .filter(instrument -> instrument.getTradingsymbol().equals(tradingSymbol))
//                .findFirst().get();
//        var sf = new SimpleDateFormat("ddMMMyy");
//        var type = ins.getTradingsymbol().endsWith("PE") ? "P" : "C" ;
//        var tsym = "";
//        if (ins.getInstrument_type().equals("EQ")) {
//            tsym = tradingSymbol + "-EQ";
//        } else {
//            tsym = ins.getName() + sf.format(ins.getExpiry()) + type + ins.getStrike();
//        }
//        log.info("Matched Prostock's tsym {} for Zerodha's trading symbol {}", tsym.toUpperCase(),
// tradingSymbol);
//        return tsym.toUpperCase();
//    }
//
//    @Override
//    public String placeOrder(String tradingSymbol, double quantity, String transactionType, String
// tag, double limitPrice) {
//        var tempTransactionType = transactionType.equals("BUY") ? "B" : "S";
//        try {
//            tradingSymbol = getProStocksTradingSymbol(tradingSymbol);
//            log.info("Placing order for tradingSymbol - {}; transactionType - {}; quantity - {};
// tag - {}",
//                    tradingSymbol, tempTransactionType, quantity, tag);
//            var res = norenClient.place_order(tempTransactionType, "I", "NFO", tradingSymbol,
//                    (int) quantity, (int) quantity, "MKT", 0.0, tag, null, null,
//                    null, null, null, null);
//            if (!res.getString("stat").equalsIgnoreCase("ok")) {
//                throw new RuntimeException("Placing order failed response: " + res);
//            }
//            return res.getString("norenordno");
//        } catch (Exception e) {
//            throw new BrokerException("Error while placing order", e);
//        }
//
//    }
//
//    @Override
//    public OrderDetail getOrderDetail(String orderId) {
//        while (true) {
//            try {
//                var orderHistory = norenClient.get_order_history(orderId);
//                var finalOrder = (JSONObject) orderHistory.get(0);
//                log.info("Order History - {}", orderHistory);
//                log.info("Final Order Detail - {}", finalOrder);
//                DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm:ss dd-MM-yyyy");
//                var avgPrice = finalOrder.has("avgprc") ?
// Double.parseDouble(finalOrder.getString("avgprc")) : -1;
//                return OrderDetail.builder()
//                        .orderId(finalOrder.getString("norenordno"))
//                        .openTimestamp(dtf.parseDateTime(finalOrder.getString("norentm")))
//                        .avgPrice(avgPrice)
//                        .status(fromStatus(finalOrder.getString("status")))
//                        .build();
//            } catch (Throwable e) {
//                log.error("Error while checking order, {}. Checking the order details again",
// orderId, e);
//            }
//        }
//    }
// }
