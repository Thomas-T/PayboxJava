package com.allocab.paybox.gateway;

import com.allocab.paybox.Result;
import com.allocab.paybox.Transaction;
import com.allocab.paybox.request.Request.Type;
import com.allocab.paybox.request.TransactionRequest;
import com.allocab.paybox.util.Http;

public class TransactionGateway {
  private Http http;

  public TransactionGateway(Http http) {
    this.http = http;
  }

  public Result<Transaction> updateSubscription(TransactionRequest request) {
    return new Result<Transaction>(http.post(request.withType(Type.UPDATE_SUBSCRIPTION)), Transaction.class);
  }

  public Result<Transaction> desenroll(TransactionRequest request) {
    return new Result<Transaction>(http.post(request.withType(Type.DESENROLL)), Transaction.class);
  }

  public Result<Transaction> credit(TransactionRequest request) {
    return new Result<Transaction>(http.post(request.withType(Type.CREDIT)), Transaction.class);
  }

  public Result<Transaction> cancel(TransactionRequest request) {
    return new Result<Transaction>(http.post(request.withType(Type.CANCEL)), Transaction.class);
  }

  public Result<Transaction> refund(TransactionRequest request) {
    return new Result<Transaction>(http.post(request.withType(Type.REFUND)), Transaction.class);
  }

  public Result<Transaction> authorizeAndCapture(TransactionRequest request) {
    return new Result<Transaction>(http.post(request.withType(Type.AUTHORIZE_AND_CAPTURE)), Transaction.class);
  }

  public Result<Transaction> captureAfterAuthorize(TransactionRequest request) {
    return new Result<Transaction>(http.post(request.withType(Type.CAPTURE_AFTER_AUTHORIZE)), Transaction.class);
  }

  public Result<Transaction> authorize(TransactionRequest request) {
    return new Result<Transaction>(http.post(request.withType(Type.AUTHORIZE)), Transaction.class);
  }

  public Result<Transaction> enroll(TransactionRequest request) {
    return new Result<Transaction>(http.post(request.withType(Type.ENROLL)), Transaction.class);
  }
}
