package io.github.ap2java.mandate;

import io.github.ap2java.credentials.VerifiableCredential;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * A simplified version of the CartMandate class for the AP2 protocol.
 * This class represents the approval of a specific cart of items for purchase.
 */
public class CartMandate {
    
    /**
     * Simple cart item representation
     */
    public static class CartItem {
        private String id;
        private String description;
        private BigDecimal price;
        private int quantity;
        
        public CartItem(String id, String description, BigDecimal price, int quantity) {
            this.id = id;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
        }
        
        public String getId() {
            return id;
        }
        
        public String getDescription() {
            return description;
        }
        
        public BigDecimal getPrice() {
            return price;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public BigDecimal getTotalPrice() {
            return price.multiply(new BigDecimal(quantity));
        }
    }
    
    private String id;
    private String requestingAgentId;
    private String receivingAgentId;
    private Instant createdAt;
    private Instant expiresAt;
    private BigDecimal amount;
    private String currencyCode;
    private List<CartItem> items;
    private String parentMandateId;
    private VerifiableCredential credential;
    
    /**
     * Default constructor
     */
    public CartMandate() {
        this.items = new ArrayList<>();
    }

    /**
     * Constructor with all required fields
     * 
     * @param id Unique identifier for this mandate
     * @param requestingAgentId The ID of the agent making the request
     * @param receivingAgentId The ID of the agent receiving the payment
     * @param parentMandateId The ID of the parent Intent Mandate
     * @param items List of items in the cart
     * @param currencyCode The currency code for the transaction
     * @param expiresAt When this mandate expires
     */
    public CartMandate(String id, String requestingAgentId, String receivingAgentId,
                      String parentMandateId, List<CartItem> items, 
                      String currencyCode, Instant expiresAt) {
        this.id = id;
        this.requestingAgentId = requestingAgentId;
        this.receivingAgentId = receivingAgentId;
        this.parentMandateId = parentMandateId;
        this.items = items != null ? items : new ArrayList<>();
        this.currencyCode = currencyCode;
        this.createdAt = Instant.now();
        this.expiresAt = expiresAt;
        this.amount = calculateTotalAmount();
    }

    // Calculate total amount based on cart items
    private BigDecimal calculateTotalAmount() {
        return items.stream()
            .map(CartItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
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
    
    public List<CartItem> getItems() {
        return items;
    }
    
    public void setItems(List<CartItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        this.amount = calculateTotalAmount();
    }
    
    public String getParentMandateId() {
        return parentMandateId;
    }
    
    public void setParentMandateId(String parentMandateId) {
        this.parentMandateId = parentMandateId;
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
     * Gets the ID of the intent mandate that this cart mandate is based on.
     * 
     * @return the intent mandate ID
     */
    public String getIntentMandateId() {
        return parentMandateId;
    }
    
    /**
     * Gets the merchant ID for this cart mandate.
     * 
     * @return the merchant ID
     */
    public String getMerchantId() {
        return receivingAgentId;
    }
    
    /**
     * Gets the total amount of this cart.
     * 
     * @return the total amount
     */
    public BigDecimal getTotalAmount() {
        return amount;
    }
}