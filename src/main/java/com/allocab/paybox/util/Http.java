package com.allocab.paybox.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Logger;

import com.allocab.paybox.ResponseWrapper;
import com.allocab.paybox.exception.AuthenticationException;
import com.allocab.paybox.exception.AuthorizationException;
import com.allocab.paybox.exception.DownForMaintenanceException;
import com.allocab.paybox.exception.NotFoundException;
import com.allocab.paybox.exception.ServerException;
import com.allocab.paybox.exception.UnexpectedException;
import com.allocab.paybox.request.Request;

public class Http {
  protected static final Logger log = Logger.getLogger("logger");

  enum RequestMethod {
    POST;
  }

  private String baseMerchantURL;
  private String site;
  private String rang;
  private String cle;
  private String identifiant;

  public Http(String baseMerchantURL, String site, String rang, String cle, String identifiant) {
    this.baseMerchantURL = baseMerchantURL;
    this.site = site;
    this.rang = rang;
    this.cle = cle;
    this.identifiant = identifiant;
  }

  public ResponseWrapper post(Request request) {
    return httpRequest(RequestMethod.POST, request);
  }

  private ResponseWrapper httpRequest(RequestMethod requestMethod, Request request) {
    HttpURLConnection connection = null;
    try {
      connection = buildConnection(requestMethod);
      OutputStream outputStream = null;
      try {
        outputStream = connection.getOutputStream();
        outputStream.write(request.withCle(cle).withIdentifiant(identifiant).withRang(rang).withSite(site)
            .toQueryString().getBytes("UTF-8"));
      } finally {
        if (outputStream != null) {
          outputStream.close();
        }
      }

      log.info("responseCode : " + connection.getResponseCode());

      throwExceptionIfErrorStatusCode(connection.getResponseCode(), null);

      InputStream responseStream = null;
      try {
        responseStream = connection.getResponseCode() == 422 ? connection.getErrorStream() : connection
            .getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream, "UTF-8"));
        String line;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
          sb.append(line);
        }
        return new ResponseWrapper(sb);

      } finally {
        if (responseStream != null) {
          responseStream.close();
        }
      }
    } catch (IOException e) {
      throw new UnexpectedException(e.getMessage(), e);
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }

  }

  private HttpURLConnection buildConnection(RequestMethod requestMethod) throws java.io.IOException {
    URL url = new URL(baseMerchantURL);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(requestMethod.toString());
    connection.addRequestProperty("User-Agent", "Paybox Java");
    connection.setDoOutput(true);
    connection.setReadTimeout(60000);
    return connection;
  }

  public static void throwExceptionIfErrorStatusCode(int statusCode, String message) {
    String decodedMessage = null;
    if (message != null) {
      try {
        decodedMessage = URLDecoder.decode(message, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }

    if (isErrorCode(statusCode)) {
      switch (statusCode) {
      case 401:
        throw new AuthenticationException();
      case 403:
        throw new AuthorizationException(decodedMessage);
      case 404:
        throw new NotFoundException();
      case 500:
        throw new ServerException();
      case 503:
        throw new DownForMaintenanceException();
      default:
        throw new UnexpectedException("Unexpected HTTP_RESPONSE " + statusCode);

      }
    }
  }

  private static boolean isErrorCode(int responseCode) {
    return responseCode != 200 && responseCode != 201 && responseCode != 422;
  }

}
