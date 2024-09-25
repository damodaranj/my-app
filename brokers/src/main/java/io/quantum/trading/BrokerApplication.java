package io.quantum.trading;

import io.quantum.trading.util.DateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrokerApplication {
  public static void main(String[] args) {
    DateUtil.setTimeZone();
    SpringApplication.run(BrokerApplication.class, args);
  }
}
