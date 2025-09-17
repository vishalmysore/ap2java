package io.github.ap2java.payment;

import io.github.ap2java.domain.Payment;
import io.github.ap2java.domain.PaymentRequest;
import io.github.ap2java.domain.PaymentResponse;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for processing payments in the AP2 protocol.
 */
public interface PaymentProcessor {
    
    /**
     * Create a new payment based on the payment request.
     *
     * @param paymentRequest The payment request details.
     * @return A future containing the payment response.
     */
    CompletableFuture<PaymentResponse> createPayment(PaymentRequest paymentRequest);
    
    /**
     * Authorize a payment.
     *
     * @param paymentId The ID of the payment to authorize.
     * @param authToken Authorization token provided by the user.
     * @return A future containing the payment response.
     */
    CompletableFuture<PaymentResponse> authorizePayment(String paymentId, String authToken);
    
    /**
     * Capture a previously authorized payment.
     *
     * @param paymentId The ID of the payment to capture.
     * @return A future containing the payment response.
     */
    CompletableFuture<PaymentResponse> capturePayment(String paymentId);
    
    /**
     * Cancel a payment.
     *
     * @param paymentId The ID of the payment to cancel.
     * @return A future containing the payment response.
     */
    CompletableFuture<PaymentResponse> cancelPayment(String paymentId);
    
    /**
     * Refund a payment.
     *
     * @param paymentId The ID of the payment to refund.
     * @param reason    The reason for the refund.
     * @return A future containing the payment response.
     */
    CompletableFuture<PaymentResponse> refundPayment(String paymentId, String reason);
    
    /**
     * Get the details of a payment.
     *
     * @param paymentId The ID of the payment to retrieve.
     * @return A future containing an optional payment.
     */
    CompletableFuture<Optional<Payment>> getPayment(String paymentId);
    
    /**
     * Check the status of a payment.
     *
     * @param paymentId The ID of the payment to check.
     * @return A future containing the payment response.
     */
    CompletableFuture<PaymentResponse> checkPaymentStatus(String paymentId);
}