package com.allocab.paybox.exception;

@SuppressWarnings("serial")
public class UnexpectedException extends PayboxException {
  public UnexpectedException(String message) {
    super(message);
  }

  public UnexpectedException(String message, Throwable cause) {
    super(message, cause);
  }
}
