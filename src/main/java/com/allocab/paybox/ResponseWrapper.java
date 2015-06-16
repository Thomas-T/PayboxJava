package com.allocab.paybox;

import java.util.HashMap;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class ResponseWrapper extends HashMap<String, String> {
  protected static final Logger log = Logger.getLogger("logger");

  public ResponseWrapper(StringBuffer sb) {
    String resp = sb.toString();
    if (resp == null || resp.equals("")) {
      return;
    }

    resp = resp.replaceAll("\\s+", "");
    log.info(resp);

    String[] params = resp.split("&");
    for (String pair : params) {
      String[] kv = pair.split("=");
      if (kv.length == 2) {
        this.put(kv[0], kv[1]);
      }

    }
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("\n");
    sb.append("---------");
    sb.append("\n");
    for (String str : this.keySet()) {
      sb.append(str).append(":").append(this.get(str));
      sb.append("\n");
    }
    sb.append("---//----");
    return sb.toString();
  }

  public boolean isSuccess() {
    return this.get("CODEREPONSE") != null && this.get("CODEREPONSE").equals("00000");
  }
}
