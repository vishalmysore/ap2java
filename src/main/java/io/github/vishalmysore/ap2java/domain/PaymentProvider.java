package io.github.vishalmysore.ap2java.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a payment provider in the AP2 protocol.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentProvider {
    /**
     * Unique identifier for the payment provider.
     */
    private String id;
    
    /**
     * Name of the payment provider.
     */
    private String name;
    
    /**
     * Description of the payment provider.
     */
    private String description;
    
    /**
     * URL for the payment provider's website.
     */
    private String url;
    
    /**
     * API endpoint for the payment provider.
     */
    private String apiEndpoint;
    
    /**
     * Type of payment provider (e.g., "credit_card", "wallet", "bank").
     */
    private String type;
    
    /**
     * Image URL for the payment provider's logo.
     */
    private String logoUrl;
}