package io.github.ap2java.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a payment transaction in the AP2 protocol.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {
    /**
     * Unique identifier for the payment.
     */
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    
    /**
     * The amount of the payment.
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
     * The ID of the agent receiving the payment.
     */
    private String receivingAgentId;
    
    /**
     * The current status of the payment.
     */
    private PaymentStatus status;
    
    /**
     * When the payment was created.
     */
    @Builder.Default
    private Instant createdAt = Instant.now();
    
    /**
     * When the payment was last updated.
     */
    private Instant updatedAt;
    
    /**
     * Additional metadata for the payment.
     */
    private Map<String, Object> metadata;
    
    /**
     * Description or purpose of the payment.
     */
    private String description;
    
    /**
     * Reference ID for external payment systems.
     */
    private String externalReference;
}