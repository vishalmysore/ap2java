package io.github.vishalmysore.ap2java.mandate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MandateViolationException class.
 */
public class MandateViolationExceptionTest {
    
    @Test
    void testConstructorWithThreeArgs() {
        // Setup
        String message = "Test violation message";
        String mandateId = "mandate-123";
        String violationType = "AMOUNT_EXCEEDED";
        
        // Execute
        MandateViolationException exception = new MandateViolationException(message, mandateId, violationType);
        
        // Verify
        assertEquals(message, exception.getMessage());
        assertEquals(mandateId, exception.getMandateId());
        assertEquals(violationType, exception.getViolationType());
    }
    
    @Test
    void testConstructorWithFourArgs() {
        // Setup
        String message = "Test violation message";
        String mandateId = "mandate-123";
        String violationType = "EXPIRED";
        Throwable cause = new RuntimeException("Underlying cause");
        
        // Execute
        MandateViolationException exception = new MandateViolationException(message, mandateId, violationType, cause);
        
        // Verify
        assertEquals(message, exception.getMessage());
        assertEquals(mandateId, exception.getMandateId());
        assertEquals(violationType, exception.getViolationType());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    void testConstructorWithTwoArgs() {
        // Setup
        String message = "Test violation message";
        String mandateId = "mandate-123";
        
        // Execute
        MandateViolationException exception = new MandateViolationException(message, mandateId);
        
        // Verify
        assertEquals(message, exception.getMessage());
        assertEquals(mandateId, exception.getMandateId());
        assertEquals("UNKNOWN", exception.getViolationType());
    }
    
    @Test
    void testGetters() {
        // Setup
        MandateViolationException exception = new MandateViolationException(
            "Mandate violation", "mandate-456", "PAYMENT_METHOD_MISMATCH"
        );
        
        // Execute and Verify
        assertEquals("mandate-456", exception.getMandateId());
        assertEquals("PAYMENT_METHOD_MISMATCH", exception.getViolationType());
    }
}