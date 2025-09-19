package io.github.vishalmysore.ap2java.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Represents a payment request in the AP2 protocol.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRequest {
    /**
     * The amount to be charged.
     */
    private BigDecimal amount;
    
    /**
     * The currency code for the payment (e.g., USD, EUR).
     */
    private String currencyCode;
    
    /**
     * The ID of the agent requesting the payment.
     */
    private String requestingAgentId;
    
    /**
     * The ID of the agent that will receive the payment.
     */
    private String receivingAgentId;
    
    /**
     * Description or purpose of the payment.
     */
    private String description;
    
    /**
     * Additional metadata for the payment.
     */
    private Map<String, Object> metadata;
    
    /**
     * Reference to external payment systems if applicable.
     */
    private String externalReference;
    
    /**
     * Callback URL for payment notifications.
     */
    private String callbackUrl;
    
    /**
     * Payment method to be used (e.g., "credit_card", "wallet").
     */
    private String paymentMethod;
}