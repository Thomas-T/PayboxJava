package com.allocab.paybox.exception;

public class PayboxException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public PayboxException(String message, Throwable cause) {
    super(message, cause);
  }

  public PayboxException(String message) {
    super(message);
  }

  public PayboxException() {
    super();
  }
}
