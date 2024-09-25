package io.quantum.trading.brokers;

import io.quantum.trading.exception.ApiError;
import io.quantum.trading.exception.EntityException;
import lombok.Getter;

public class BrokerException extends EntityException {
  @Getter private boolean retryable = true;

  public BrokerException(Throwable cause, ApiError apiError, Object... params) {
    super(cause, apiError, params);
  }

  public BrokerException(ApiError apiError, Object... params) {
    super(apiError, params);
  }

  public BrokerException(boolean retryable, Throwable cause, ApiError apiError, Object... params) {
    super(cause, apiError, params);
    this.retryable = retryable;
  }

  public BrokerException(boolean retryable, BrokerException exception) {
    super(exception);
    this.retryable = retryable;
  }
}
