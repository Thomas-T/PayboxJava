package com.allocab.paybox.request;

import java.lang.reflect.Field;

public class TransactionRequest extends Request {

  private String numTrans;
  private String numAppel;
  private String montant;
  private String reference;
  private String refabonne;
  private Currency devise;
  private String porteur;
  private String dateval;
  private String cvv;

  public TransactionRequest() {
    this(0);
  }

  public TransactionRequest(long num) {
    super(num);
  }

  /**
   * NumTrans is provided when linking to previous operation (eg: cancelling a
   * capture, capturing an authorization)
   * 
   * @param numTrans
   * @return the {@link TransactionRequest}
   */
  public TransactionRequest withNumTrans(String numTrans) {
    this.numTrans = numTrans;
    return this;
  }

  /**
   * NumAppel is provided when linking to previous operation (eg: cancelling a
   * capture, capturing an authorization)
   * 
   * @param numAppel
   * @return the {@link TransactionRequest}
   */
  public TransactionRequest withNumAppel(String numAppel) {
    this.numAppel = numAppel;
    return this;
  }

  /**
   * The amount of the transaction in cents, if capture, its the amount
   * captured, if refund, the amount to refund, eg: $19 is 1900
   * 
   * @param montant
   * @return the {@link TransactionRequest}
   */
  public TransactionRequest withMontant(String montant) {
    this.montant = montant;
    return this;
  }

  /**
   * The label that will be linked to the operation in Paybox Admin
   * 
   * @param reference
   * @return the {@link TransactionRequest}
   */
  public TransactionRequest withReference(String reference) {
    this.reference = reference;
    return this;
  }

  /**
   * The id of the user when the card has been enrolled or the id to define for
   * an enrollment
   * 
   * @param refabonne
   * @return the {@link TransactionRequest}
   */
  public TransactionRequest withRefabonne(String refabonne) {
    this.refabonne = refabonne;
    return this;
  }

  /**
   * The currency for the transaction
   * 
   * @param devise
   *          an enum {@link Currency}
   * @return the {@link TransactionRequest}
   */
  public TransactionRequest withDevise(Currency devise) {
    this.devise = devise;
    return this;
  }

  /**
   * The number of the card to enroll, or the hashcode returned by paybox when
   * card has been enrolled
   * 
   * @param porteur
   * @return the {@link TransactionRequest}
   */
  public TransactionRequest withPorteur(String porteur) {
    this.porteur = porteur;
    return this;
  }

  /**
   * The validity date of the card
   * 
   * @param dateval
   * @return the {@link TransactionRequest}
   */
  public TransactionRequest withDateval(String dateval) {
    this.dateval = dateval;
    return this;
  }

  /**
   * The 3 digits (4 for AMEX) behind the card
   * 
   * @param dateval
   * @return the {@link TransactionRequest}
   */
  public TransactionRequest withCvv(String cvv) {
    this.cvv = cvv;
    return this;
  }

  @Override
  public String toQueryString() {
    StringBuffer sb = new StringBuffer().append(super.toQueryString());
    for (Field field : this.getClass().getDeclaredFields()) {
      // log.info(field.getName());
      Object o;
      try {
        o = field.get(this);
        if (o != null) {
          sb.append("&");
          sb.append(field.getName().toUpperCase());
          sb.append("=");
          if (o instanceof Currency) {
            sb.append(((Currency) o).getCode());
          } else {
            sb.append(o);
          }

        }
      } catch (IllegalArgumentException | IllegalAccessException e) {
        log.severe(e.getMessage());
      }
    }
    log.info(sb.toString());
    return sb.toString();
  }

}
