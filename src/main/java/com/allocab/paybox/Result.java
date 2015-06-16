package com.allocab.paybox;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class Result<T> {
  protected static final Logger log = Logger.getLogger("logger");

  public enum Status {
    OK("00000"), INVALID_TRANSACTION_TYPE("00009"), MANDATORY_FIELD_MISSING("00014"), MISSING_SUBSCRIPTION("00017");

    private String code;

    private Status(String code) {
      this.code = code;
    }

    public static Status fromCode(String code) {
      for (Status status : Status.values()) {
        if (status.code.equals(code)) {
          return status;
        }
      }
      return null;
    }
  }

  private T target;
  private Status status;
  private boolean success;
  private String message;

  public Result() {
  }

  public Result(ResponseWrapper rw, Class<T> klass) {
    log.info(rw.toString());
    success = rw.isSuccess();
    status = Status.fromCode(rw.get("CODEREPONSE"));
    message = (rw.get("COMMENTAIRE"));
    if (rw.isSuccess()) {
      this.target = newInstanceFromResponseWrapper(klass, rw);
    } else {

    }

  }

  public static <T> T newInstanceFromResponseWrapper(Class<T> klass, ResponseWrapper node) {
    Throwable cause = null;
    try {
      return klass.getConstructor(ResponseWrapper.class).newInstance(node);
    } catch (InstantiationException e) {
      cause = e;
    } catch (IllegalAccessException e) {
      cause = e;
    } catch (InvocationTargetException e) {
      cause = e;
    } catch (NoSuchMethodException e) {
      cause = e;
    }

    throw new IllegalArgumentException("Unknown klass: " + klass, cause);
  }

  public boolean isSuccess() {
    return success;
  }

  public String getMessage() {
    return this.message;
  }

  public Status getStatus() {
    return status;
  }

  public T getTarget() {
    return target;
  }

}
