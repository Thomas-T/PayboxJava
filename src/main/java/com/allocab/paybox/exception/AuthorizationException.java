package com.allocab.paybox.exception;

@SuppressWarnings("serial")
public class AuthorizationException extends PayboxException {
  public AuthorizationException(String message) {
    super(message);
  }
}
