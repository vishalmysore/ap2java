package io.github.vishalmysore.ap2java.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.domain.Message;
import io.github.vishalmysore.a2a.domain.Task;
import io.github.vishalmysore.ap2java.domain.EnhancedMessage;
import io.github.vishalmysore.ap2java.domain.EnhancedTask;
import io.github.vishalmysore.ap2java.domain.PaymentEnabledAgentCard;
import io.github.vishalmysore.ap2java.domain.PaymentRequest;
import io.github.vishalmysore.ap2java.domain.PaymentResponse;
import io.github.vishalmysore.ap2java.domain.PaymentStatus;
import io.github.vishalmysore.ap2java.payment.PaymentProcessor;

public class AP2ClientMessageTest {

    private AP2Client ap2Client;
    private PaymentProcessor mockPaymentProcessor;
    private PaymentEnabledAgentCard mockAgentCard;

    @BeforeEach
    void setUp() {
        mockPaymentProcessor = mock(PaymentProcessor.class);
        mockAgentCard = mock(PaymentEnabledAgentCard.class);

        ap2Client = new AP2Client(mockPaymentProcessor, mockAgentCard);

        // Setup mocks for all payment operations
        PaymentResponse mockResponse = PaymentResponse.builder()
                .paymentId("test-payment-123")
                .status(PaymentStatus.CREATED)
                .build();

        when(mockPaymentProcessor.createPayment(any(PaymentRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(mockResponse));
        when(mockPaymentProcessor.authorizePayment(any(String.class), any(String.class)))
                .thenReturn(CompletableFuture.completedFuture(mockResponse));
        when(mockPaymentProcessor.capturePayment(any(String.class)))
                .thenReturn(CompletableFuture.completedFuture(mockResponse));
        when(mockPaymentProcessor.cancelPayment(any(String.class)))
                .thenReturn(CompletableFuture.completedFuture(mockResponse));
        when(mockPaymentProcessor.checkPaymentStatus(any(String.class)))
                .thenReturn(CompletableFuture.completedFuture(mockResponse));
    }

    @Test
    void testGetAgentCard() {
        AgentCard agentCard = ap2Client.getAgentCard();
        assertNotNull(agentCard);
        assertSame(mockAgentCard, agentCard);
    }

    @Test
    void testProcessMessageCreatePayment() {
        EnhancedMessage message = new EnhancedMessage();
        message.setId("msg-123");
        message.setMethod("createPayment");
        
        Map<String, Object> params = new HashMap<>();
        params.put("amount", 100.0);
        params.put("currency", "USD");
        message.setParams(params);
        
        Task task = ap2Client.processMessage(message);
        
        assertNotNull(task);
        assertEquals("msg-123", task.getId());
    }
    
    @Test
    void testProcessMessageAuthorizePayment() {
        EnhancedMessage message = new EnhancedMessage();
        message.setId("msg-456");
        message.setMethod("authorizePayment");
        
        Map<String, Object> params = new HashMap<>();
        params.put("paymentId", "payment-123");
        params.put("authToken", "auth-123");
        message.setParams(params);
        
        Task task = ap2Client.processMessage(message);
        
        assertNotNull(task);
        assertEquals("msg-456", task.getId());
    }
    
    @Test
    void testProcessMessageCapturePayment() {
        EnhancedMessage message = new EnhancedMessage();
        message.setId("msg-789");
        message.setMethod("capturePayment");
        
        Map<String, Object> params = new HashMap<>();
        params.put("paymentId", "payment-123");
        message.setParams(params);
        
        Task task = ap2Client.processMessage(message);
        
        assertNotNull(task);
        assertEquals("msg-789", task.getId());
    }
    
    @Test
    void testProcessMessageCancelPayment() {
        EnhancedMessage message = new EnhancedMessage();
        message.setId("msg-101");
        message.setMethod("cancelPayment");
        
        Map<String, Object> params = new HashMap<>();
        params.put("paymentId", "payment-123");
        message.setParams(params);
        
        Task task = ap2Client.processMessage(message);
        
        assertNotNull(task);
        assertEquals("msg-101", task.getId());
    }
    
    @Test
    void testProcessMessageCheckPaymentStatus() {
        EnhancedMessage message = new EnhancedMessage();
        message.setId("msg-102");
        message.setMethod("checkPaymentStatus");
        
        Map<String, Object> params = new HashMap<>();
        params.put("paymentId", "payment-123");
        message.setParams(params);
        
        Task task = ap2Client.processMessage(message);
        
        assertNotNull(task);
        assertEquals("msg-102", task.getId());
    }
    
    @Test
    void testProcessMessageUnsupportedMethod() {
        EnhancedMessage message = new EnhancedMessage();
        message.setId("msg-103");
        message.setMethod("unsupportedMethod");
        
        Task task = ap2Client.processMessage(message);
        
        assertNotNull(task);
        assertEquals("msg-103", task.getId());
        assertTrue(task instanceof EnhancedTask);
    }
    
    @Test
    void testProcessRegularMessage() {
        Message message = mock(Message.class);
        when(message.getId()).thenReturn("msg-104");
        
        Task task = ap2Client.processMessage(message);
        
        assertNotNull(task);
        assertEquals("msg-104", task.getId());
    }
}