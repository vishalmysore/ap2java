package io.github.vishalmysore.ap2java.examples;

import io.github.vishalmysore.ap2java.client.AP2Client;

import io.github.vishalmysore.ap2java.domain.*;
import io.github.vishalmysore.ap2java.payment.PaymentProcessor;
import io.github.vishalmysore.ap2java.payment.SamplePaymentProcessor;
import io.github.vishalmysore.a2a.domain.Authentication;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Simple example demonstrating the AP2 protocol usage.
 */
@Slf4j
public class SimplePaymentExample {
    
    public static void main(String[] args) {
        log.info("Starting AP2 simple payment example");
        
        try {
            // Set up the payment-enabled agent card
            PaymentEnabledAgentCard agentCard = createAgentCard();
            
            // Create a payment processor
            PaymentProcessor paymentProcessor = new SamplePaymentProcessor();
            
            // Create an AP2 client
            AP2Client ap2Client = new AP2Client(paymentProcessor, agentCard);
            
            // Create a payment request
            PaymentRequest paymentRequest = createPaymentRequest();
            
            // Create a payment
            PaymentResponse paymentResponse = ap2Client.createPayment(paymentRequest).get();
            log.info("Payment created: {}", paymentResponse);
            
            // Authorize the payment
            paymentResponse = ap2Client.authorizePayment(paymentResponse.getPaymentId(), "auth-token-123").get();
            log.info("Payment authorized: {}", paymentResponse);
            
            // Capture the payment
            paymentResponse = ap2Client.capturePayment(paymentResponse.getPaymentId()).get();
            log.info("Payment captured: {}", paymentResponse);
            
            // Check the payment status
            paymentResponse = ap2Client.checkPaymentStatus(paymentResponse.getPaymentId()).get();
            log.info("Final payment status: {}", paymentResponse);
            
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error in payment example", e);
        }
        
        log.info("AP2 simple payment example completed");
    }
    
    /**
     * Create a sample payment-enabled agent card.
     *
     * @return The agent card.
     */
    private static PaymentEnabledAgentCard createAgentCard() {
        PaymentEnabledAgentCard agentCard = new PaymentEnabledAgentCard();
        
        // Set basic properties
        agentCard.setName("Payment Agent");
        agentCard.setDescription("An agent that can process payments");
        agentCard.setUrl("https://example.com/payment-agent");
        agentCard.setVersion("1.0.0");
        
        // Set capabilities
        EnhancedCapabilities capabilities = new EnhancedCapabilities();
        capabilities.setStreaming(true);
        capabilities.setPushNotifications(true);
        capabilities.setCustomCapability("paymentEnabled", true);
        agentCard.setCapabilities(capabilities);
        
        // Set authentication
        Authentication auth = new Authentication();
        auth.setBearerAuth("api-key-123");
        agentCard.setAuthentication(auth);
        
        // Set payment properties
        agentCard.setPaymentProvider(createPaymentProvider());
        agentCard.setSupportedPaymentMethods(new String[]{"credit_card", "wallet", "bank_transfer"});
        agentCard.setSupportedCurrencies(new String[]{"USD", "EUR", "GBP"});
        agentCard.setCanReceivePayments(true);
        agentCard.setCanMakePayments(true);
        agentCard.setRequiresAuthentication(true);
        
        return agentCard;
    }
    
    /**
     * Create a sample payment provider.
     *
     * @return The payment provider.
     */
    private static PaymentProvider createPaymentProvider() {
        return PaymentProvider.builder()
                .id("sample-provider")
                .name("Sample Payment Provider")
                .description("A sample payment provider for demonstration")
                .url("https://example.com/payment-provider")
                .apiEndpoint("https://api.example.com/payments")
                .type("multi")
                .logoUrl("https://example.com/logo.png")
                .build();
    }
    
    /**
     * Create a sample payment request.
     *
     * @return The payment request.
     */
    private static PaymentRequest createPaymentRequest() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("orderId", UUID.randomUUID().toString());
        metadata.put("customerName", "John Doe");
        
        return PaymentRequest.builder()
                .amount(new BigDecimal("99.99"))
                .currencyCode("USD")
                .requestingAgentId("agent-123")
                .receivingAgentId("merchant-456")
                .description("Purchase of Premium Service")
                .metadata(metadata)
                .externalReference("INV-2023-12345")
                .callbackUrl("https://example.com/callback")
                .paymentMethod("credit_card")
                .build();
    }
}