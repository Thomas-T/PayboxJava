package com.allocab.paybox.gateway;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.allocab.paybox.Result;
import com.allocab.paybox.Transaction;
import com.allocab.paybox.request.Request;
import com.allocab.paybox.request.TransactionRequest;

public class PayboxGatewayTest {

  private PayboxGateway gateway;

  @Before
  public void initGateway() {
    gateway = new PayboxGateway("1999888", "32", "1999888I", "107904482", "https://preprod-ppps.paybox.com/PPPS.php");
  }

  @Test
  public void testInit() {
    assertNotNull(gateway);
  }

  @Test
  public void testTransactionAuthorizationMissingFields() {
    Result<Transaction> result = gateway.transaction().authorize(new TransactionRequest());
    assertNotNull(result);
    assertTrue(!result.isSuccess());
    assertEquals("Mandatory+values+missing+keyword:7+Type:18", result.getMessage());
    assertEquals(Result.Status.MANDATORY_FIELD_MISSING, result.getStatus());
  }

  @Test
  public void testTransactionAuthorizationMissingSubscription() {
    Result<Transaction> result = gateway.transaction().authorize(
        new TransactionRequest().withCvv("123").withDateval("0218").withMontant("1000")
            .withDevise(Request.Currency.EUR).withPorteur("porteur").withRefabonne("refabonne")
            .withReference("reference"));
    assertNotNull(result);
    assertTrue(!result.isSuccess());
    assertEquals("PAYBOX:Abonn�inexistant", result.getMessage());
    assertEquals(Result.Status.MISSING_SUBSCRIPTION, result.getStatus());
  }

  @Test
  public void testTransactionEnroll() {
    Result<Transaction> result = gateway.transaction().enroll(
        new TransactionRequest().withCvv("123").withDateval("0220").withMontant("1000")
            .withDevise(Request.Currency.EUR).withPorteur("1111222233334444")
            .withRefabonne("AC" + new Date().getTime()).withReference("ACEnrollementNoCapture"));
    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertEquals("Demandetrait�eavecsucc�s", result.getMessage());
    assertEquals(Result.Status.OK, result.getStatus());
    assertNotNull(result.getTarget());
    assertNotNull(result.getTarget().getNumAppel());
    assertNotNull(result.getTarget().getNumTrans());
    assertNotNull(result.getTarget().getPorteur());
    assertNotNull(result.getTarget().getRefAbonne());
  }

  @Test
  public void testTransactionEnrollAndCapture() {

    String cvv = "123";
    String dateval = "0220";

    String refabonne = "AC" + new Date().getTime();

    Result<Transaction> enroll = gateway.transaction().enroll(
        new TransactionRequest().withCvv(cvv).withDateval(dateval).withMontant("1000").withDevise(Request.Currency.EUR)
            .withPorteur("1111222233334444").withRefabonne(refabonne).withReference("ACEnrollement"));
    assertNotNull(enroll);
    assertTrue(enroll.isSuccess());
    assertEquals("Demandetrait�eavecsucc�s", enroll.getMessage());
    assertEquals(Result.Status.OK, enroll.getStatus());
    assertNotNull(enroll.getTarget());
    assertNotNull(enroll.getTarget().getNumAppel());
    assertNotNull(enroll.getTarget().getNumTrans());
    assertNotNull(enroll.getTarget().getPorteur());
    assertNotNull(enroll.getTarget().getRefAbonne());

    Result<Transaction> result = gateway.transaction().authorize(
        new TransactionRequest().withCvv(cvv).withDateval(dateval).withMontant("1000").withDevise(Request.Currency.EUR)
            .withPorteur(enroll.getTarget().getPorteur()).withRefabonne(refabonne)
            .withReference("ACPaymentAuthorizationAfterEnroll"));
    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertEquals("Demandetrait�eavecsucc�s", result.getMessage());
    assertEquals(Result.Status.OK, result.getStatus());
  }

  @Test
  public void testTransactionEnrollAuhtorizeAndCapture() {

    String cvv = "123";
    String dateval = "0220";

    String refabonne = "AC" + new Date().getTime();

    Result<Transaction> enroll = gateway.transaction().enroll(
        new TransactionRequest().withCvv(cvv).withDateval(dateval).withMontant("1000").withDevise(Request.Currency.EUR)
            .withPorteur("1111222233334444").withRefabonne(refabonne).withReference("ACEnrollement"));
    assertNotNull(enroll);
    assertTrue(enroll.isSuccess());
    assertEquals("Demandetrait�eavecsucc�s", enroll.getMessage());
    assertEquals(Result.Status.OK, enroll.getStatus());
    assertNotNull(enroll.getTarget());
    assertNotNull(enroll.getTarget().getNumAppel());
    assertNotNull(enroll.getTarget().getNumTrans());
    assertNotNull(enroll.getTarget().getPorteur());
    assertNotNull(enroll.getTarget().getRefAbonne());

    Result<Transaction> auth = gateway.transaction().authorize(
        new TransactionRequest().withCvv(cvv).withDateval(dateval).withMontant("1000").withDevise(Request.Currency.EUR)
            .withPorteur(enroll.getTarget().getPorteur()).withRefabonne(refabonne)
            .withReference("ACPaymentAuthorizationToCapture"));
    assertNotNull(auth);
    assertTrue(auth.isSuccess());
    assertEquals("Demandetrait�eavecsucc�s", auth.getMessage());
    assertEquals(Result.Status.OK, auth.getStatus());

    Result<Transaction> capture = gateway.transaction().captureAfterAuthorize(
        new TransactionRequest().withCvv(cvv).withDateval(dateval).withMontant("900").withDevise(Request.Currency.EUR)
            .withNumAppel(auth.getTarget().getNumAppel()).withNumTrans(auth.getTarget().getNumTrans())
            .withPorteur(enroll.getTarget().getPorteur()).withRefabonne(refabonne)
            .withReference("ACPaymentCatpureAfterAuthorization"));
    assertNotNull(capture);
    assertTrue(capture.isSuccess());
    assertEquals("Demandetrait�eavecsucc�s", capture.getMessage());
    assertEquals(Result.Status.OK, capture.getStatus());

    Result<Transaction> cancel = gateway.transaction().refund(
        new TransactionRequest().withCvv(cvv).withDateval(dateval).withMontant("500").withDevise(Request.Currency.EUR)
            .withNumAppel(capture.getTarget().getNumAppel()).withNumTrans(capture.getTarget().getNumTrans())
            .withPorteur(auth.getTarget().getPorteur()).withRefabonne(refabonne).withReference("ACCancel"));
    assertNotNull(cancel);
    assertTrue(cancel.isSuccess());
    assertEquals("Demandetrait�eavecsucc�s", cancel.getMessage());
    assertEquals(Result.Status.OK, cancel.getStatus());
  }

  @Test
  public void testTransactionEnrollAndCaptureDirectly() {

    String cvv = "123";
    String dateval = "0220";

    String refabonne = "AC" + new Date().getTime();

    Result<Transaction> enroll = gateway.transaction().enroll(
        new TransactionRequest().withCvv(cvv).withDateval(dateval).withMontant("1000").withDevise(Request.Currency.EUR)
            .withPorteur("1111222233334444").withRefabonne(refabonne).withReference("ACEnrollement"));
    assertNotNull(enroll);
    assertTrue(enroll.isSuccess());
    assertEquals("Demandetrait�eavecsucc�s", enroll.getMessage());
    assertEquals(Result.Status.OK, enroll.getStatus());
    assertNotNull(enroll.getTarget());
    assertNotNull(enroll.getTarget().getNumAppel());
    assertNotNull(enroll.getTarget().getNumTrans());
    assertNotNull(enroll.getTarget().getPorteur());
    assertNotNull(enroll.getTarget().getRefAbonne());

    Result<Transaction> result = gateway.transaction().captureAfterAuthorize(
        new TransactionRequest().withCvv(cvv).withDateval(dateval).withMontant("800").withDevise(Request.Currency.EUR)
            .withNumAppel(enroll.getTarget().getNumAppel()).withNumTrans(enroll.getTarget().getNumTrans())
            .withPorteur(enroll.getTarget().getPorteur()).withRefabonne(refabonne)
            .withReference("ACPaymentAuthorizationAfterEnroll"));
    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertEquals("Demandetrait�eavecsucc�s", result.getMessage());
    assertEquals(Result.Status.OK, result.getStatus());

    Result<Transaction> cancel = gateway.transaction().refund(
        new TransactionRequest().withCvv(cvv).withDateval(dateval).withMontant("500").withDevise(Request.Currency.EUR)
            .withNumAppel(result.getTarget().getNumAppel()).withNumTrans(result.getTarget().getNumTrans())
            .withPorteur(enroll.getTarget().getPorteur()).withRefabonne(refabonne).withReference("ACCancel"));
    assertNotNull(cancel);
    assertTrue(cancel.isSuccess());
    assertEquals("Demandetrait�eavecsucc�s", cancel.getMessage());
    assertEquals(Result.Status.OK, cancel.getStatus());

  }

}
