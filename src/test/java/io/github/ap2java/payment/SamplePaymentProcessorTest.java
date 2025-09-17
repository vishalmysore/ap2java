package io.github.ap2java.payment;

import io.github.ap2java.domain.Payment;
import io.github.ap2java.domain.PaymentRequest;
import io.github.ap2java.domain.PaymentResponse;
import io.github.ap2java.domain.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the SamplePaymentProcessor class.
 */
public class SamplePaymentProcessorTest {
    
    private SamplePaymentProcessor paymentProcessor;
    private PaymentRequest testPaymentRequest;
    
    @BeforeEach
    void setUp() {
        paymentProcessor = new SamplePaymentProcessor();
        
        testPaymentRequest = PaymentRequest.builder()
                .amount(new BigDecimal("50.00"))
                .currencyCode("USD")
                .requestingAgentId("test-agent-1")
                .receivingAgentId("test-agent-2")
                .description("Test payment")
                .build();
    }
    
    @Test
    void testCreatePayment() throws ExecutionException, InterruptedException {
        PaymentResponse response = paymentProcessor.createPayment(testPaymentRequest).get();
        
        assertNotNull(response);
        assertNotNull(response.getPaymentId());
        assertEquals(PaymentStatus.CREATED, response.getStatus());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getAuthUrl());
    }
    
    @Test
    void testAuthorizePayment() throws ExecutionException, InterruptedException {
        PaymentResponse createResponse = paymentProcessor.createPayment(testPaymentRequest).get();
        PaymentResponse authResponse = paymentProcessor.authorizePayment(createResponse.getPaymentId(), "test-auth-token").get();
        
        assertNotNull(authResponse);
        assertEquals(createResponse.getPaymentId(), authResponse.getPaymentId());
        assertEquals(PaymentStatus.AUTHORIZED, authResponse.getStatus());
        assertNotNull(authResponse.getUpdatedAt());
    }
    
    @Test
    void testCapturePayment() throws ExecutionException, InterruptedException {
        PaymentResponse createResponse = paymentProcessor.createPayment(testPaymentRequest).get();
        paymentProcessor.authorizePayment(createResponse.getPaymentId(), "test-auth-token").get();
        PaymentResponse captureResponse = paymentProcessor.capturePayment(createResponse.getPaymentId()).get();
        
        assertNotNull(captureResponse);
        assertEquals(createResponse.getPaymentId(), captureResponse.getPaymentId());
        assertEquals(PaymentStatus.COMPLETED, captureResponse.getStatus());
        assertNotNull(captureResponse.getUpdatedAt());
        assertNotNull(captureResponse.getReceiptUrl());
    }
    
    @Test
    void testCancelPayment() throws ExecutionException, InterruptedException {
        PaymentResponse createResponse = paymentProcessor.createPayment(testPaymentRequest).get();
        PaymentResponse cancelResponse = paymentProcessor.cancelPayment(createResponse.getPaymentId()).get();
        
        assertNotNull(cancelResponse);
        assertEquals(createResponse.getPaymentId(), cancelResponse.getPaymentId());
        assertEquals(PaymentStatus.CANCELED, cancelResponse.getStatus());
        assertNotNull(cancelResponse.getUpdatedAt());
    }
    
    @Test
    void testRefundPayment() throws ExecutionException, InterruptedException {
        PaymentResponse createResponse = paymentProcessor.createPayment(testPaymentRequest).get();
        paymentProcessor.authorizePayment(createResponse.getPaymentId(), "test-auth-token").get();
        paymentProcessor.capturePayment(createResponse.getPaymentId()).get();
        PaymentResponse refundResponse = paymentProcessor.refundPayment(createResponse.getPaymentId(), "Customer request").get();
        
        assertNotNull(refundResponse);
        assertEquals(createResponse.getPaymentId(), refundResponse.getPaymentId());
        assertEquals(PaymentStatus.REFUNDED, refundResponse.getStatus());
        assertNotNull(refundResponse.getUpdatedAt());
    }
    
    @Test
    void testGetPayment() throws ExecutionException, InterruptedException {
        PaymentResponse createResponse = paymentProcessor.createPayment(testPaymentRequest).get();
        Optional<Payment> paymentOptional = paymentProcessor.getPayment(createResponse.getPaymentId()).get();
        
        assertTrue(paymentOptional.isPresent());
        Payment payment = paymentOptional.get();
        assertEquals(createResponse.getPaymentId(), payment.getId());
        assertEquals(testPaymentRequest.getAmount(), payment.getAmount());
        assertEquals(testPaymentRequest.getCurrencyCode(), payment.getCurrencyCode());
        assertEquals(testPaymentRequest.getDescription(), payment.getDescription());
        assertEquals(PaymentStatus.CREATED, payment.getStatus());
    }
    
    @Test
    void testCheckPaymentStatus() throws ExecutionException, InterruptedException {
        PaymentResponse createResponse = paymentProcessor.createPayment(testPaymentRequest).get();
        paymentProcessor.authorizePayment(createResponse.getPaymentId(), "test-auth-token").get();
        PaymentResponse statusResponse = paymentProcessor.checkPaymentStatus(createResponse.getPaymentId()).get();
        
        assertNotNull(statusResponse);
        assertEquals(createResponse.getPaymentId(), statusResponse.getPaymentId());
        assertEquals(PaymentStatus.AUTHORIZED, statusResponse.getStatus());
    }
    
    @Test
    void testNonExistentPayment() throws ExecutionException, InterruptedException {
        PaymentResponse response = paymentProcessor.checkPaymentStatus("non-existent-id").get();
        
        assertNotNull(response);
        assertEquals("non-existent-id", response.getPaymentId());
        assertEquals(PaymentStatus.FAILED, response.getStatus());
        assertEquals("PAYMENT_NOT_FOUND", response.getErrorCode());
    }
}