package io.github.ap2java.mandate;

import io.github.ap2java.credentials.VerifiableCredential;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * A simplified version of the IntentMandate class for the AP2 protocol.
 * This class represents a long-standing permission for an agent to make payments
 * with certain constraints.
 */
public class IntentMandate {
    
    private String id;
    private String requestingAgentId;
    private String receivingAgentId;
    private Instant createdAt;
    private Instant expiresAt;
    private boolean requiresHumanApproval;
    private BigDecimal maxAmountPerPayment;
    private List<String> allowedCategories;
    private VerifiableCredential credential;
    
    /**
     * Default constructor
     */
    public IntentMandate() {
        this.allowedCategories = Collections.emptyList();
    }

    /**
     * Constructor with all required fields
     * 
     * @param id Unique identifier for this mandate
     * @param requestingAgentId The ID of the agent making the request
     * @param receivingAgentId The ID of the agent receiving the payment
     * @param maxAmountPerPayment Maximum amount allowed per transaction
     * @param requiresHumanApproval Whether human approval is required
     */
    public IntentMandate(String id, String requestingAgentId, String receivingAgentId, 
                        BigDecimal maxAmountPerPayment, boolean requiresHumanApproval) {
        this.id = id;
        this.requestingAgentId = requestingAgentId;
        this.receivingAgentId = receivingAgentId;
        this.maxAmountPerPayment = maxAmountPerPayment;
        this.requiresHumanApproval = requiresHumanApproval;
        this.createdAt = Instant.now();
        this.expiresAt = Instant.now().plusSeconds(3600 * 24 * 30); // 30 days by default
        this.allowedCategories = Collections.emptyList();
    }

    // Getters and setters
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestingAgentId() {
        return requestingAgentId;
    }

    public void setRequestingAgentId(String requestingAgentId) {
        this.requestingAgentId = requestingAgentId;
    }

    public String getReceivingAgentId() {
        return receivingAgentId;
    }

    public void setReceivingAgentId(String receivingAgentId) {
        this.receivingAgentId = receivingAgentId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean requiresHumanApproval() {
        return requiresHumanApproval;
    }

    public void setRequiresHumanApproval(boolean requiresHumanApproval) {
        this.requiresHumanApproval = requiresHumanApproval;
    }

    public BigDecimal getMaxAmountPerPayment() {
        return maxAmountPerPayment;
    }

    public void setMaxAmountPerPayment(BigDecimal maxAmountPerPayment) {
        this.maxAmountPerPayment = maxAmountPerPayment;
    }
    
    public List<String> getAllowedCategories() {
        return allowedCategories;
    }
    
    public void setAllowedCategories(List<String> allowedCategories) {
        this.allowedCategories = allowedCategories != null ? allowedCategories : Collections.emptyList();
    }
    
    public VerifiableCredential getCredential() {
        return credential;
    }
    
    public void setCredential(VerifiableCredential credential) {
        this.credential = credential;
    }
    
    /**
     * Checks if this mandate is valid for the current time.
     * 
     * @return true if the mandate is valid, false otherwise
     */
    public boolean isValid() {
        Instant now = Instant.now();
        return now.isAfter(createdAt) && now.isBefore(expiresAt);
    }
    
    /**
     * Checks if this mandate is expired.
     * 
     * @return true if the mandate is expired, false otherwise
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
    
    /**
     * Checks if this mandate permits a payment with the given parameters.
     * 
     * @param merchantId the merchant ID for the payment
     * @param amount the amount of the payment
     * @param category the payment category
     * @return true if the payment is permitted, false otherwise
     */
    public boolean permits(String merchantId, BigDecimal amount, String category) {
        // Check if the merchant is allowed
        if (receivingAgentId != null && !receivingAgentId.equals(merchantId)) {
            return false;
        }
        
        // Check if the amount is within limits
        if (maxAmountPerPayment != null && amount.compareTo(maxAmountPerPayment) > 0) {
            return false;
        }
        
        // Check if the category is allowed
        if (category != null && !allowedCategories.isEmpty() && !allowedCategories.contains(category)) {
            return false;
        }
        
        return true;
    }
}