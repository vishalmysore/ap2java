package io.github.vishalmysore.ap2java.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class PaymentDomainTest {
    
    @Test
    void testPaymentBuilder() {
        Instant now = Instant.now();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("orderNumber", "ORDER-123");
        
        Payment payment = Payment.builder()
                .id("payment-123")
                .amount(new BigDecimal("100.00"))
                .currencyCode("USD")
                .requestingAgentId("agent-1")
                .receivingAgentId("agent-2")
                .status(PaymentStatus.CREATED)
                .createdAt(now)
                .metadata(metadata)
                .description("Test payment")
                .externalReference("EXT-REF-123")
                .build();
        
        assertEquals("payment-123", payment.getId());
        assertEquals(new BigDecimal("100.00"), payment.getAmount());
        assertEquals("USD", payment.getCurrencyCode());
        assertEquals("agent-1", payment.getRequestingAgentId());
        assertEquals("agent-2", payment.getReceivingAgentId());
        assertEquals(PaymentStatus.CREATED, payment.getStatus());
        assertEquals(now, payment.getCreatedAt());
        assertEquals(metadata, payment.getMetadata());
        assertEquals("Test payment", payment.getDescription());
        assertEquals("EXT-REF-123", payment.getExternalReference());
        assertNull(payment.getUpdatedAt());
        
        // Test setters
        Instant updated = Instant.now();
        payment.setStatus(PaymentStatus.PROCESSING);
        payment.setUpdatedAt(updated);
        
        assertEquals(PaymentStatus.PROCESSING, payment.getStatus());
        assertEquals(updated, payment.getUpdatedAt());
    }
    
    @Test
    void testPaymentRequestBuilder() {
        PaymentRequest request = PaymentRequest.builder()
                .amount(new BigDecimal("50.00"))
                .currencyCode("EUR")
                .requestingAgentId("agent-1")
                .receivingAgentId("agent-2")
                .description("Test payment request")
                .paymentMethod("credit-card")
                .build();
        
        assertEquals(new BigDecimal("50.00"), request.getAmount());
        assertEquals("EUR", request.getCurrencyCode());
        assertEquals("agent-1", request.getRequestingAgentId());
        assertEquals("agent-2", request.getReceivingAgentId());
        assertEquals("Test payment request", request.getDescription());
        assertEquals("credit-card", request.getPaymentMethod());
        
        // Test default values
        assertNull(request.getMetadata());
        assertNull(request.getExternalReference());
        assertNull(request.getCallbackUrl());
    }
    
    @Test
    void testPaymentResponseBuilder() {
        Instant now = Instant.now();
        Instant updated = now.plusSeconds(60);
        
        PaymentResponse response = PaymentResponse.builder()
                .paymentId("payment-123")
                .status(PaymentStatus.AUTHORIZED)
                .createdAt(now)
                .updatedAt(updated)
                .authUrl("https://test.com/auth")
                .errorCode(null)
                .errorMessage(null)
                .receiptUrl("https://test.com/receipt")
                .build();
        
        assertEquals("payment-123", response.getPaymentId());
        assertEquals(PaymentStatus.AUTHORIZED, response.getStatus());
        assertEquals(now, response.getCreatedAt());
        assertEquals(updated, response.getUpdatedAt());
        assertEquals("https://test.com/auth", response.getAuthUrl());
        assertNull(response.getErrorCode());
        assertNull(response.getErrorMessage());
        assertEquals("https://test.com/receipt", response.getReceiptUrl());
        assertNull(response.getVerificationUrl());
        assertNull(response.getProcessorData());
    }
    
    @Test
    void testPaymentStatusValues() {
        // Test that all enum values are accessible
        assertNotNull(PaymentStatus.CREATED);
        assertNotNull(PaymentStatus.PROCESSING);
        assertNotNull(PaymentStatus.REQUIRES_AUTH);
        assertNotNull(PaymentStatus.AUTHORIZED);
        assertNotNull(PaymentStatus.COMPLETED);
        assertNotNull(PaymentStatus.FAILED);
        assertNotNull(PaymentStatus.CANCELED);
        assertNotNull(PaymentStatus.REFUNDED);
        assertNotNull(PaymentStatus.ERROR);
    }
}