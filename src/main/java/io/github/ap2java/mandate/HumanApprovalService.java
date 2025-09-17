package io.github.ap2java.mandate;

import io.github.ap2java.domain.PaymentRequest;

/**
 * Interface for handling human approval of mandates.
 * Implementations should provide mechanisms for obtaining user approval
 * for cart contents and payment details.
 */
public interface HumanApprovalService {
    
    /**
     * Requests human approval for a cart based on an Intent Mandate.
     * 
     * @param intentMandate The Intent Mandate governing the transaction
     * @param request The payment request containing cart details
     * @return A signed Cart Mandate if approved, or null if denied
     */
    CartMandate requestCartApproval(IntentMandate intentMandate, PaymentRequest request);
    
    /**
     * Requests human approval for a payment based on a Cart Mandate.
     * 
     * @param cartMandate The Cart Mandate governing the transaction
     * @param request The payment request containing payment details
     * @return A signed Payment Mandate if approved, or null if denied
     */
    PaymentMandate requestPaymentApproval(CartMandate cartMandate, PaymentRequest request);
    
    /**
     * Checks if human approval is required for a specific payment request.
     * 
     * @param request The payment request to check
     * @return true if human approval is required, false otherwise
     */
    boolean isHumanApprovalRequired(PaymentRequest request);
    
    /**
     * Sets a callback URL for receiving human approval notifications.
     * 
     * @param callbackUrl The URL to call when human approval is granted or denied
     */
    void setApprovalCallback(String callbackUrl);
}