package com.allocab.paybox;

import java.lang.reflect.Field;
import java.util.logging.Logger;

public class Transaction {
  protected static final Logger log = Logger.getLogger("logger");

  private String numTrans;
  private String numAppel;
  private String refAbonne;
  private String porteur;

  public Transaction(ResponseWrapper rw) {
    for (Field field : this.getClass().getDeclaredFields()) {
      try {
        field.set(this, rw.get(field.getName().toUpperCase()));
      } catch (IllegalArgumentException | IllegalAccessException e) {
        log.severe(e.getMessage());
      }
    }
  }

  public String getNumTrans() {
    return numTrans;
  }

  public String getNumAppel() {
    return numAppel;
  }

  public String getRefAbonne() {
    return refAbonne;
  }

  public String getPorteur() {
    return porteur;
  }

}
