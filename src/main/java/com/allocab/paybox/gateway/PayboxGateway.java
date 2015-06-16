package com.allocab.paybox.gateway;

import com.allocab.paybox.util.Http;

public class PayboxGateway {
  private Http http;

  public PayboxGateway(String site, String rang, String cle, String identifiant, String baseUrl) {
    this.http = new Http(baseUrl, site, rang, cle, identifiant);
  }

  public TransactionGateway transaction() {
    return new TransactionGateway(http);
  }

}
