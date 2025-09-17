package io.github.ap2java.mandate;

/**
 * Exception thrown when a payment request violates mandate constraints.
 * This exception is used to indicate that a payment cannot be processed
 * because it does not comply with the mandates that the user has authorized.
 */
public class MandateViolationException extends RuntimeException {

    private final String mandateId;
    private final String violationType;

    /**
     * Creates a new MandateViolationException.
     *
     * @param message The error message
     * @param mandateId The ID of the violated mandate
     * @param violationType The type of violation that occurred
     */
    public MandateViolationException(String message, String mandateId, String violationType) {
        super(message);
        this.mandateId = mandateId;
        this.violationType = violationType;
    }

    /**
     * Creates a new MandateViolationException.
     *
     * @param message The error message
     * @param mandateId The ID of the violated mandate
     * @param violationType The type of violation that occurred
     * @param cause The cause of this exception
     */
    public MandateViolationException(String message, String mandateId, String violationType, Throwable cause) {
        super(message, cause);
        this.mandateId = mandateId;
        this.violationType = violationType;
    }

    /**
     * Creates a new MandateViolationException with a generic violation type.
     *
     * @param message The error message
     * @param mandateId The ID of the violated mandate
     */
    public MandateViolationException(String message, String mandateId) {
        this(message, mandateId, "UNKNOWN");
    }

    /**
     * Gets the ID of the violated mandate.
     *
     * @return The mandate ID
     */
    public String getMandateId() {
        return mandateId;
    }

    /**
     * Gets the type of violation that occurred.
     *
     * @return The violation type
     */
    public String getViolationType() {
        return violationType;
    }
}