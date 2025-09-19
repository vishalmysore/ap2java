package io.github.vishalmysore.ap2java.mandate;

import io.github.vishalmysore.ap2java.domain.Payment;
import io.github.vishalmysore.ap2java.domain.PaymentRequest;
import io.github.vishalmysore.ap2java.domain.PaymentResponse;

/**
 * The MandateEnforcer is responsible for ensuring that payment requests
 * adhere to the mandates that have been authorized by the user.
 * It validates that payment requests meet the constraints defined
 * in the applicable mandates and prevents unauthorized payments.
 */
public interface MandateEnforcer {

    /**
     * Checks if a payment request is permitted by the available mandates.
     * If necessary and possible, this method will obtain additional mandates
     * (e.g., by requesting human approval).
     * 
     * @param request The payment request to check
     * @return true if the payment is permitted, false otherwise
     */
    boolean checkPaymentPermission(PaymentRequest request);
    
    /**
     * Enforces mandate compliance for a payment request.
     * If the request violates any mandate terms, this method will
     * throw an exception.
     * 
     * @param paymentRequest The payment request to enforce mandates on
     * @throws MandateViolationException if the payment request violates mandate terms
     */
    void enforce(PaymentRequest paymentRequest) throws MandateViolationException;
    
    /**
     * Validates that a completed payment was executed according to
     * the mandates that were in effect.
     * 
     * @param payment The completed payment to audit
     * @return true if the payment complied with all mandates, false otherwise
     */
    boolean auditPayment(Payment payment);
    
    /**
     * Attaches mandate information to a payment response.
     * This enriches the response with references to the mandates
     * that authorized the payment.
     * 
     * @param response The payment response to enhance
     */
    void attachMandateInfo(PaymentResponse response);
    
    /**
     * Checks if the payment request is compliant with existing mandates.
     * 
     * @param paymentRequest The payment request to validate
     * @return true if the request complies with mandates, false otherwise
     */
    boolean isCompliant(PaymentRequest paymentRequest);
}