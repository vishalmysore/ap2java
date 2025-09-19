package io.github.vishalmysore.ap2java.mandate;

import io.github.vishalmysore.ap2java.domain.PaymentRequest;

/**
 * Interface for verifying different types of mandates in the AP2 protocol.
 * Implementations should provide specific verification logic for each mandate type.
 */
public interface MandateVerifier {
    
    /**
     * Verifies an Intent Mandate.
     * 
     * @param mandate The Intent Mandate to verify
     * @param request The payment request to check against the mandate
     * @return true if the mandate is valid and permits the payment, false otherwise
     */
    boolean verifyIntentMandate(IntentMandate mandate, PaymentRequest request);
    
    /**
     * Verifies a Cart Mandate.
     * 
     * @param cartMandate The Cart Mandate to verify
     * @param intentMandate The Intent Mandate referenced by the Cart Mandate
     * @param request The payment request to check against the mandate
     * @return true if the mandate is valid and permits the payment, false otherwise
     */
    boolean verifyCartMandate(CartMandate cartMandate, IntentMandate intentMandate, PaymentRequest request);
    
    /**
     * Verifies a Payment Mandate.
     * 
     * @param paymentMandate The Payment Mandate to verify
     * @param cartMandate The Cart Mandate referenced by the Payment Mandate
     * @param request The payment request to check against the mandate
     * @return true if the mandate is valid and permits the payment, false otherwise
     */
    boolean verifyPaymentMandate(PaymentMandate paymentMandate, CartMandate cartMandate, PaymentRequest request);
    
    /**
     * Verifies the cryptographic signatures on a mandate.
     * 
     * @param mandate The mandate to verify
     * @return true if the mandate has a valid signature, false otherwise
     */
    boolean verifySignature(Object mandate);
}