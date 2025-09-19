package io.github.vishalmysore.ap2java.mandate;

import io.github.vishalmysore.ap2java.credentials.VerifiableCredential;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
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
    @Setter
    @Getter
    private String intentMandateId;
    private VerifiableCredential credential;

    @Getter
    @Setter
    private  String cartId;
    @Getter
    @Setter
    private  String userId;

    @Getter
    @Setter
    private IntentMandate intentMandate;

    public  CartMandate(String intentMandateId, String cartId, String userId,BigDecimal totalAmount) {
        this.intentMandateId = intentMandateId;
        this.cartId = cartId;
        this.userId = userId;
        this.amount = totalAmount;
        this.items = new ArrayList<>();

   }
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
     * @param intentMandateId The ID of the parent Intent Mandate
     * @param items List of items in the cart
     * @param currencyCode The currency code for the transaction
     * @param expiresAt When this mandate expires
     */
    public CartMandate(String id, String requestingAgentId, String receivingAgentId,
                      String intentMandateId, List<CartItem> items,
                      String currencyCode, Instant expiresAt) {
        this.id = id;
        this.requestingAgentId = requestingAgentId;
        this.receivingAgentId = receivingAgentId;
        this.intentMandateId = intentMandateId;
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
    
    public String getIntentMandateId() {
        return intentMandateId ;
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