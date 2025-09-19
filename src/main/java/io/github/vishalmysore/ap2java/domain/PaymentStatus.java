package io.github.vishalmysore.ap2java.domain;

/**
 * Enum representing the possible states of a payment.
 */
public enum PaymentStatus {
    /**
     * Payment has been created but not yet processed.
     */
    CREATED,
    
    /**
     * Payment is being processed.
     */
    PROCESSING,
    
    /**
     * Payment requires authorization from the user.
     */
    REQUIRES_AUTH,
    
    /**
     * Payment is authorized but not yet captured.
     */
    AUTHORIZED,
    
    /**
     * Payment has been successfully completed.
     */
    COMPLETED,
    
    /**
     * Payment has failed.
     */
    FAILED,
    
    /**
     * Payment has been canceled.
     */
    CANCELED,
    
    /**
     * Payment has been refunded.
     */
    REFUNDED,
    
    /**
     * Payment has encountered an error.
     */
    ERROR
}