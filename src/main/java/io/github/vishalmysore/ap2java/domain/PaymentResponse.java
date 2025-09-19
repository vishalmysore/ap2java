package io.github.vishalmysore.ap2java.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Represents a payment response in the AP2 protocol.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse {
    /**
     * The ID of the payment.
     */
    private String paymentId;
    
    /**
     * The current status of the payment.
     */
    private PaymentStatus status;
    
    /**
     * Timestamp when the payment was created.
     */
    private Instant createdAt;
    
    /**
     * Timestamp when the payment was last updated.
     */
    private Instant updatedAt;
    
    /**
     * URL for payment authentication if needed.
     */
    private String authUrl;
    
    /**
     * URL for payment verification.
     */
    private String verificationUrl;
    
    /**
     * Any error code if payment failed.
     */
    private String errorCode;
    
    /**
     * Error message if payment failed.
     */
    private String errorMessage;
    
    /**
     * Additional data specific to the payment processor.
     */
    private Map<String, Object> processorData;
    
    /**
     * Receipt URL if payment is completed.
     */
    private String receiptUrl;
}