package io.github.ap2java.client;

import io.github.ap2java.domain.EnhancedMessage;
import io.github.ap2java.domain.EnhancedTask;
import io.github.ap2java.domain.PaymentEnabledAgentCard;
import io.github.ap2java.domain.PaymentRequest;
import io.github.ap2java.domain.PaymentResponse;
import io.github.ap2java.payment.PaymentProcessor;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.domain.Message;
import io.github.vishalmysore.a2a.domain.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * Client for interacting with payment-enabled agents.
 */
@Slf4j
public class AP2Client {
    
    private static final String METHOD_CREATE_PAYMENT = "createPayment";
    private static final String METHOD_AUTHORIZE_PAYMENT = "authorizePayment";
    private static final String METHOD_CAPTURE_PAYMENT = "capturePayment";
    private static final String METHOD_CANCEL_PAYMENT = "cancelPayment";
    private static final String METHOD_CHECK_STATUS = "checkPaymentStatus";
    
    private final PaymentProcessor paymentProcessor;
    private final PaymentEnabledAgentCard agentCard;
    
    /**
     * Constructor.
     *
     * @param paymentProcessor The payment processor implementation.
     * @param agentCard        The payment-enabled agent card.
     */
    public AP2Client(PaymentProcessor paymentProcessor, PaymentEnabledAgentCard agentCard) {
        this.paymentProcessor = paymentProcessor;
        this.agentCard = agentCard;
    }
    
    /**
     * Create a new payment.
     *
     * @param paymentRequest The payment request.
     * @return A future with the payment response.
     */
    public CompletableFuture<PaymentResponse> createPayment(PaymentRequest paymentRequest) {
        log.info("Creating payment: {}", paymentRequest);
        return paymentProcessor.createPayment(paymentRequest);
    }
    
    /**
     * Authorize a payment.
     *
     * @param paymentId The ID of the payment to authorize.
     * @param authToken The authorization token.
     * @return A future with the payment response.
     */
    public CompletableFuture<PaymentResponse> authorizePayment(String paymentId, String authToken) {
        log.info("Authorizing payment: {}", paymentId);
        return paymentProcessor.authorizePayment(paymentId, authToken);
    }
    
    /**
     * Capture a payment.
     *
     * @param paymentId The ID of the payment to capture.
     * @return A future with the payment response.
     */
    public CompletableFuture<PaymentResponse> capturePayment(String paymentId) {
        log.info("Capturing payment: {}", paymentId);
        return paymentProcessor.capturePayment(paymentId);
    }
    
    /**
     * Cancel a payment.
     *
     * @param paymentId The ID of the payment to cancel.
     * @return A future with the payment response.
     */
    public CompletableFuture<PaymentResponse> cancelPayment(String paymentId) {
        log.info("Cancelling payment: {}", paymentId);
        return paymentProcessor.cancelPayment(paymentId);
    }
    
    /**
     * Check the status of a payment.
     *
     * @param paymentId The ID of the payment to check.
     * @return A future with the payment response.
     */
    public CompletableFuture<PaymentResponse> checkPaymentStatus(String paymentId) {
        log.info("Checking payment status: {}", paymentId);
        return paymentProcessor.checkPaymentStatus(paymentId);
    }
    
    /**
     * Get the agent card associated with this client.
     *
     * @return The agent card.
     */
    public AgentCard getAgentCard() {
        return agentCard;
    }
    
    /**
     * Process a message from another agent.
     * This method is intended to be used when integrating with the a2ajava messaging system.
     *
     * @param message The message to process.
     * @return A task representing the operation.
     */
    public Task processMessage(Message message) {
        log.info("Processing message: {}", message);
        
        // Convert to enhanced message if it's not already
        EnhancedMessage enhancedMessage;
        if (message instanceof EnhancedMessage) {
            enhancedMessage = (EnhancedMessage) message;
        } else {
            enhancedMessage = new EnhancedMessage();
            // Copy properties from original message
            enhancedMessage.setId(message.getId());
            enhancedMessage.setRole(message.getRole());
            enhancedMessage.setParts(message.getParts());
            enhancedMessage.setMetadata(message.getMetadata());
        }
        
        // Create a task to track the operation
        EnhancedTask task = new EnhancedTask();
        task.setId(enhancedMessage.getId());
        
        // Process the message based on its method
        String method = enhancedMessage.getMethod();
        
        if (METHOD_CREATE_PAYMENT.equals(method)) {
            // Extract payment request from message
            PaymentRequest paymentRequest = extractPaymentRequest(message);
            
            // Create the payment
            createPayment(paymentRequest)
                    .thenAccept(response -> {
                        // Update task with response
                        task.setCompleted(true);
                        task.setResult(response);
                    })
                    .exceptionally(ex -> {
                        // Handle error
                        task.setCompleted(true);
                        task.setError(ex.getMessage());
                        return null;
                    });
        } else if (METHOD_AUTHORIZE_PAYMENT.equals(method)) {
            // Extract parameters from message
            String paymentId = extractString(message, "paymentId");
            String authToken = extractString(message, "authToken");
            
            // Authorize the payment
            authorizePayment(paymentId, authToken)
                    .thenAccept(response -> {
                        // Update task with response
                        task.setCompleted(true);
                        task.setResult(response);
                    })
                    .exceptionally(ex -> {
                        // Handle error
                        task.setCompleted(true);
                        task.setError(ex.getMessage());
                        return null;
                    });
        } else if (METHOD_CAPTURE_PAYMENT.equals(method)) {
            // Extract parameters from message
            String paymentId = extractString(message, "paymentId");
            
            // Capture the payment
            capturePayment(paymentId)
                    .thenAccept(response -> {
                        // Update task with response
                        task.setCompleted(true);
                        task.setResult(response);
                    })
                    .exceptionally(ex -> {
                        // Handle error
                        task.setCompleted(true);
                        task.setError(ex.getMessage());
                        return null;
                    });
        } else if (METHOD_CANCEL_PAYMENT.equals(method)) {
            // Extract parameters from message
            String paymentId = extractString(message, "paymentId");
            
            // Cancel the payment
            cancelPayment(paymentId)
                    .thenAccept(response -> {
                        // Update task with response
                        task.setCompleted(true);
                        task.setResult(response);
                    })
                    .exceptionally(ex -> {
                        // Handle error
                        task.setCompleted(true);
                        task.setError(ex.getMessage());
                        return null;
                    });
        } else if (METHOD_CHECK_STATUS.equals(method)) {
            // Extract parameters from message
            String paymentId = extractString(message, "paymentId");
            
            // Check payment status
            checkPaymentStatus(paymentId)
                    .thenAccept(response -> {
                        // Update task with response
                        task.setCompleted(true);
                        task.setResult(response);
                    })
                    .exceptionally(ex -> {
                        // Handle error
                        task.setCompleted(true);
                        task.setError(ex.getMessage());
                        return null;
                    });
        } else {
            // Unsupported method
            task.setCompleted(true);
            task.setError("Unsupported method: " + method);
        }
        
        return task;
    }
    
    /**
     * Helper method to extract a payment request from a message.
     *
     * @param message The message containing the payment request.
     * @return The extracted payment request.
     */
    private PaymentRequest extractPaymentRequest(Message message) {
        // Convert to enhanced message if it's not already
        EnhancedMessage enhancedMessage;
        if (message instanceof EnhancedMessage) {
            enhancedMessage = (EnhancedMessage) message;
        } else {
            enhancedMessage = new EnhancedMessage();
            // Copy properties from original message
            enhancedMessage.setId(message.getId());
            enhancedMessage.setRole(message.getRole());
            enhancedMessage.setParts(message.getParts());
            enhancedMessage.setMetadata(message.getMetadata());
        }
        
        // In a real implementation, this would deserialize the message payload
        // into a PaymentRequest object. For simplicity, we return a dummy object.
        return PaymentRequest.builder().build();
    }
    
    /**
     * Helper method to extract a string parameter from a message.
     *
     * @param message The message containing the parameter.
     * @param key     The parameter key.
     * @return The extracted string value.
     */
    private String extractString(Message message, String key) {
        // Convert to enhanced message if it's not already
        EnhancedMessage enhancedMessage;
        if (message instanceof EnhancedMessage) {
            enhancedMessage = (EnhancedMessage) message;
        } else {
            enhancedMessage = new EnhancedMessage();
            // Copy properties from original message
            enhancedMessage.setId(message.getId());
            enhancedMessage.setRole(message.getRole());
            enhancedMessage.setParts(message.getParts());
            enhancedMessage.setMetadata(message.getMetadata());
        }
        // In a real implementation, this would extract the parameter from the message.
        // For simplicity, we return a dummy value.
        return "dummy-value";
    }
}