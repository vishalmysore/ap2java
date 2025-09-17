package io.github.ap2java.examples.secure;

import io.github.ap2java.client.AP2Client;
import io.github.ap2java.domain.PaymentEnabledAgentCard;
import io.github.ap2java.domain.PaymentRequest;
import io.github.ap2java.domain.PaymentResponse;
import io.github.ap2java.domain.PaymentStatus;
import io.github.ap2java.payment.PaymentProcessor;
import io.github.ap2java.payment.SamplePaymentProcessor;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * A simplified secure payment example that demonstrates the basic AP2 flow.
 * This example uses the standard SamplePaymentProcessor but could be extended
 * to include mandate verification and credentials.
 */
public class SimpleSecureExample {

    public static void main(String[] args) {
        try {
            // Create a basic payment processor
            PaymentProcessor processor = new SamplePaymentProcessor();
            
            // Create agent card
            PaymentEnabledAgentCard agentCard = new PaymentEnabledAgentCard();
            
            // Initialize AP2 client
            AP2Client ap2Client = new AP2Client(processor, agentCard);
            
            // Create payment request
            PaymentRequest paymentRequest = createSamplePaymentRequest();
            
            // Process the payment
            System.out.println("Creating payment...");
            PaymentResponse response = ap2Client.createPayment(paymentRequest).get();
            
            // Check the result
            if (response.getStatus() == PaymentStatus.CREATED) {
                System.out.println("Payment created successfully!");
                System.out.println("Payment ID: " + response.getPaymentId());
                
                // Authorize the payment
                System.out.println("Authorizing payment...");
                response = ap2Client.authorizePayment(response.getPaymentId(), "auth-token").get();
                
                if (response.getStatus() == PaymentStatus.AUTHORIZED) {
                    System.out.println("Payment authorized successfully!");
                    
                    // Capture the payment
                    System.out.println("Capturing payment...");
                    response = ap2Client.capturePayment(response.getPaymentId()).get();
                    
                    if (response.getStatus() == PaymentStatus.COMPLETED) {
                        System.out.println("Payment captured successfully!");
                    } else {
                        System.out.println("Failed to capture payment: " + response.getStatus());
                    }
                } else {
                    System.out.println("Failed to authorize payment: " + response.getStatus());
                }
            } else {
                System.out.println("Failed to create payment: " + response.getStatus());
            }
            
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error processing payment: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static PaymentRequest createSamplePaymentRequest() {
        return PaymentRequest.builder()
                .amount(new BigDecimal("50.00"))
                .currencyCode("USD")
                .requestingAgentId("agent-123")
                .receivingAgentId("merchant-456")
                .description("Secure payment example")
                .externalReference(UUID.randomUUID().toString())
                .build();
    }
}