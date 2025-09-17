package io.github.ap2java.mandate;

import io.github.ap2java.credentials.VerifiableCredential;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * A simplified version of the PaymentMandate class for the AP2 protocol.
 * This class represents the final authorization for a specific payment transaction.
 */
public class PaymentMandate {
    
    private String id;
    private String requestingAgentId;
    private String receivingAgentId;
    private Instant createdAt;
    private Instant expiresAt;
    private BigDecimal amount;
    private String currencyCode;
    private String paymentReference;
    private String parentMandateId;
    private Map<String, Object> paymentDetails;
    private VerifiableCredential credential;
    
    /**
     * Default constructor
     */
    public PaymentMandate() {
        this.paymentDetails = new HashMap<>();
    }

    /**
     * Constructor with all required fields
     * 
     * @param id Unique identifier for this mandate
     * @param requestingAgentId The ID of the agent making the request
     * @param receivingAgentId The ID of the agent receiving the payment
     * @param paymentReference Reference to the payment in the external system
     * @param amount The amount to be paid
     * @param currencyCode The currency code for the transaction
     * @param parentMandateId The ID of the parent Cart Mandate
     * @param paymentDetails Additional payment details
     * @param expiresAt When this mandate expires
     */
    public PaymentMandate(String id, String requestingAgentId, String receivingAgentId,
                        String paymentReference, BigDecimal amount, String currencyCode,
                        String parentMandateId, Map<String, Object> paymentDetails,
                        Instant expiresAt) {
        this.id = id;
        this.requestingAgentId = requestingAgentId;
        this.receivingAgentId = receivingAgentId;
        this.paymentReference = paymentReference;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.parentMandateId = parentMandateId;
        this.paymentDetails = paymentDetails != null ? paymentDetails : new HashMap<>();
        this.createdAt = Instant.now();
        this.expiresAt = expiresAt;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    public String getPaymentReference() {
        return paymentReference;
    }
    
    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }
    
    public String getParentMandateId() {
        return parentMandateId;
    }
    
    public void setParentMandateId(String parentMandateId) {
        this.parentMandateId = parentMandateId;
    }
    
    public Map<String, Object> getPaymentDetails() {
        return paymentDetails;
    }
    
    public void setPaymentDetails(Map<String, Object> paymentDetails) {
        this.paymentDetails = paymentDetails != null ? paymentDetails : new HashMap<>();
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
     * Gets the ID of the cart mandate that this payment mandate is based on.
     * 
     * @return the cart mandate ID
     */
    public String getCartMandateId() {
        return parentMandateId;
    }
    
    /**
     * Gets the payment method ID for this payment mandate.
     * 
     * @return the payment method ID
     */
    public String getPaymentMethodId() {
        if (paymentDetails != null && paymentDetails.containsKey("paymentMethodId")) {
            return paymentDetails.get("paymentMethodId").toString();
        }
        return null;
    }
}