package io.github.vishalmysore.ap2java.mandate;

/**
 * Interface for accessing mandate storage.
 * Implementations should provide persistence and retrieval of mandates.
 */
public interface MandateRepository {
    
    /**
     * Stores an Intent Mandate.
     * 
     * @param mandate The Intent Mandate to store
     * @return The ID of the stored mandate
     */
    String storeIntentMandate(IntentMandate mandate);
    
    /**
     * Stores a Cart Mandate.
     * 
     * @param mandate The Cart Mandate to store
     * @return The ID of the stored mandate
     */
    String storeCartMandate(CartMandate mandate);
    
    /**
     * Stores a Payment Mandate.
     * 
     * @param mandate The Payment Mandate to store
     * @return The ID of the stored mandate
     */
    String storePaymentMandate(PaymentMandate mandate);
    
    /**
     * Finds an active Intent Mandate for the specified agent.
     * 
     * @param agentId The ID of the agent
     * @param merchantId The ID of the merchant, or null to find any merchant
     * @return The Intent Mandate, or null if none found
     */
    IntentMandate findActiveIntentMandateForAgent(String agentId, String merchantId);
    
    /**
     * Finds a Cart Mandate by ID.
     * 
     * @param mandateId The ID of the Cart Mandate
     * @return The Cart Mandate, or null if not found
     */
    CartMandate findCartMandate(String mandateId);
    
    /**
     * Finds a Payment Mandate by ID.
     * 
     * @param mandateId The ID of the Payment Mandate
     * @return The Payment Mandate, or null if not found
     */
    PaymentMandate findPaymentMandate(String mandateId);
    
    /**
     * Finds an Intent Mandate associated with a specific payment.
     * 
     * @param paymentId The ID of the payment
     * @return The Intent Mandate, or null if none found
     */
    IntentMandate findIntentMandateForPayment(String paymentId);
    
    /**
     * Finds a Cart Mandate associated with a specific payment.
     * 
     * @param paymentId The ID of the payment
     * @return The Cart Mandate, or null if none found
     */
    CartMandate findCartMandateForPayment(String paymentId);
    
    /**
     * Finds a Payment Mandate associated with a specific payment.
     * 
     * @param paymentId The ID of the payment
     * @return The Payment Mandate, or null if none found
     */
    PaymentMandate findPaymentMandateForPayment(String paymentId);
    
    /**
     * Revokes a mandate, making it invalid for future use.
     * 
     * @param mandateId The ID of the mandate to revoke
     * @param reason The reason for revocation
     * @return true if the mandate was revoked, false otherwise
     */
    boolean revokeMandate(String mandateId, String reason);
}