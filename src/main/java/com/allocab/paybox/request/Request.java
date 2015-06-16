package com.allocab.paybox.request;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.joda.time.DateTime;

public class Request {
  protected static final Logger log = Logger.getLogger("logger");

  private static long counter;

  static {
    counter = new DateTime().minusDays(new DateTime().getSecondOfDay()).getMillis();
    String c = "" + counter;
    counter = Long.parseLong(c.substring(c.length() - 9, c.length()));
  }

  public enum Type {
    REFUND("00014"), CANCEL("00005"),

    AUTHORIZE("00051"), CAPTURE_AFTER_AUTHORIZE("00052"), AUTHORIZE_AND_CAPTURE("00053"), CREDIT("00054"), ENROLL(
        "00056"), UPDATE_SUBSCRIPTION("00057"), DESENROLL("00058");
    private String code;

    private Type(String code) {
      this.code = code;
    }

    public String getCode() {
      return this.code;
    }
  }

  public enum Currency {
    EUR("978"), USD("840");
    private String code;

    private Currency(String code) {
      this.code = code;
    }

    public String getCode() {
      return this.code;
    }

    public static Currency getForCode(String code) {
      for (Currency currency : Currency.values()) {
        if (currency.getCode().equals(code)) {
          return currency;
        }
      }
      return null;
    }
  }

  private Type type;
  private String site;
  private String rang;
  private String cle;
  private String identifiant;
  private String date;
  private String numQuestion;
  private String version = "00104";

  public Request() {
    this(0);
  }

  public Request(long num) {
    SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmmss");
    Date now = new Date();
    date = format.format(now);

    if (num > 0) {
      numQuestion = "" + num;
    } else {
      numQuestion = "" + counter;
      counter++;
    }
  }

  public String toQueryString() {
    String str = new StringBuffer().append("TYPE=").append(type.code).append("&VERSION=").append(version)
        .append("&SITE=").append(site).append("&RANG=").append(rang).append("&CLE=").append(cle)
        .append("&IDENTIFIANT=").append(identifiant).append("&DATEQ=").append(date).append("&NUMQUESTION=")
        .append(numQuestion).toString();
    log.info(str);
    return str;
  }

  /**
   * Define type of the transaction (Auth, Capture, Enroll, ....
   * 
   * @param type
   *          an enum {@link Type} defining the type of the request
   * @return the {@link Request}
   */
  public Request withType(Type type) {
    this.type = type;
    return this;
  }

  /**
   * Cle is one of numerous Paybox credentials
   * 
   * @param cle
   * @return the {@link Request}
   */
  public Request withCle(String cle) {
    this.cle = cle;
    return this;
  }

  /**
   * Site is one of numerous Paybox credentials
   * 
   * @param site
   * @return the {@link Request}
   */
  public Request withSite(String site) {
    this.site = site;
    return this;
  }

  /**
   * Rang is one of numerous Paybox credentials
   * 
   * @param rang
   * @return the {@link Request}
   */
  public Request withRang(String rang) {
    this.rang = rang;
    return this;
  }

  /**
   * Identifiant is one of numerous Paybox credentials
   * 
   * @param identifiant
   * @return the {@link Request}
   */
  public Request withIdentifiant(String identifiant) {
    this.identifiant = identifiant;
    return this;
  }

}
