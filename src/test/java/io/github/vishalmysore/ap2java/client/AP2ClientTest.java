package io.github.vishalmysore.ap2java.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.vishalmysore.ap2java.domain.PaymentEnabledAgentCard;
import io.github.vishalmysore.ap2java.domain.PaymentRequest;
import io.github.vishalmysore.ap2java.domain.PaymentResponse;
import io.github.vishalmysore.ap2java.domain.PaymentStatus;
import io.github.vishalmysore.ap2java.payment.PaymentProcessor;

class AP2ClientTest {

    private AP2Client ap2Client;
    private PaymentProcessor mockPaymentProcessor;
    private PaymentEnabledAgentCard mockAgentCard;
    private PaymentRequest mockPaymentRequest;

    @BeforeEach
    void setUp() {
        mockPaymentProcessor = mock(PaymentProcessor.class);
        mockAgentCard = mock(PaymentEnabledAgentCard.class);
        mockPaymentRequest = buildMockPaymentRequest();

        ap2Client = new AP2Client(mockPaymentProcessor, mockAgentCard);

        // Setup mocks with different payment statuses for different operations
        when(mockPaymentProcessor.createPayment(any(PaymentRequest.class)))
            .thenReturn(CompletableFuture.completedFuture(buildMockPaymentResponse(PaymentStatus.CREATED)));
        when(mockPaymentProcessor.authorizePayment(any(String.class), any(String.class)))
            .thenReturn(CompletableFuture.completedFuture(buildMockPaymentResponse(PaymentStatus.AUTHORIZED)));
        when(mockPaymentProcessor.capturePayment(any(String.class)))
            .thenReturn(CompletableFuture.completedFuture(buildMockPaymentResponse(PaymentStatus.COMPLETED)));
        when(mockPaymentProcessor.cancelPayment(any(String.class)))
            .thenReturn(CompletableFuture.completedFuture(buildMockPaymentResponse(PaymentStatus.CANCELED)));
        when(mockPaymentProcessor.checkPaymentStatus(any(String.class)))
            .thenReturn(CompletableFuture.completedFuture(buildMockPaymentResponse(PaymentStatus.CREATED)));
    }

    @Test
    void testCreatePayment() {
        CompletableFuture<PaymentResponse> future = ap2Client.createPayment(mockPaymentRequest);
        PaymentResponse response = future.join();

        assertNotNull(response);
        assertEquals("test-payment-123", response.getPaymentId());
        verify(mockPaymentProcessor).createPayment(mockPaymentRequest);
    }

    @Test
    void testAuthorizePayment() {
        String paymentId = "test-payment-123";
        String authCode = "AUTH-123456";
        
        CompletableFuture<PaymentResponse> future = ap2Client.authorizePayment(paymentId, authCode);
        PaymentResponse response = future.join();

        assertNotNull(response);
        assertEquals(PaymentStatus.AUTHORIZED, response.getStatus());
        verify(mockPaymentProcessor).authorizePayment(paymentId, authCode);
    }

    @Test
    void testCapturePayment() {
        String paymentId = "test-payment-123";
        
        CompletableFuture<PaymentResponse> future = ap2Client.capturePayment(paymentId);
        PaymentResponse response = future.join();

        assertNotNull(response);
        assertEquals(PaymentStatus.COMPLETED, response.getStatus());
        verify(mockPaymentProcessor).capturePayment(paymentId);
    }

    @Test
    void testCancelPayment() {
        String paymentId = "test-payment-123";
        
        CompletableFuture<PaymentResponse> future = ap2Client.cancelPayment(paymentId);
        PaymentResponse response = future.join();

        assertNotNull(response);
        assertEquals(PaymentStatus.CANCELED, response.getStatus());
        verify(mockPaymentProcessor).cancelPayment(paymentId);
    }

    @Test
    void testCheckPaymentStatus() {
        String paymentId = "test-payment-123";
        
        CompletableFuture<PaymentResponse> future = ap2Client.checkPaymentStatus(paymentId);
        PaymentResponse response = future.join();

        assertNotNull(response);
        assertEquals("test-payment-123", response.getPaymentId());
        verify(mockPaymentProcessor).checkPaymentStatus(paymentId);
    }

    private PaymentRequest buildMockPaymentRequest() {
        return PaymentRequest.builder()
            .amount(new BigDecimal("100.00"))
            .currencyCode("USD")
            .requestingAgentId("requesting-agent-1")
            .receivingAgentId("receiving-agent-1")
            .description("Test payment")
            .paymentMethod("credit-card")
            .build();
    }

    private PaymentResponse buildMockPaymentResponse(PaymentStatus status) {
        return PaymentResponse.builder()
            .paymentId("test-payment-123")
            .status(status)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .authUrl("https://example.com/auth")
            .errorCode(null)
            .errorMessage(null)
            .receiptUrl("https://example.com/receipt")
            .build();
    }
}