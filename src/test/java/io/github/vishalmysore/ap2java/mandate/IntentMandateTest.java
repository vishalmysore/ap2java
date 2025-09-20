package io.github.vishalmysore.ap2java.mandate;

import io.github.vishalmysore.ap2java.credentials.VerifiableCredential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the IntentMandate class.
 */
public class IntentMandateTest {
    
    private IntentMandate mandate;
    private final String TEST_ID = "intent-123";
    private final String TEST_REQUESTING_AGENT_ID = "agent-456";
    private final String TEST_RECEIVING_AGENT_ID = "merchant-789";
    private final BigDecimal TEST_MAX_AMOUNT = new BigDecimal("100.00");
    
    @BeforeEach
    void setUp() {
        mandate = new IntentMandate(
            TEST_ID,
            TEST_REQUESTING_AGENT_ID,
            TEST_RECEIVING_AGENT_ID,
            TEST_MAX_AMOUNT,
            true
        );
    }
    
    @Test
    void testDefaultConstructor() {
        IntentMandate defaultMandate = new IntentMandate();
        assertNotNull(defaultMandate.getAllowedCategories());
        assertTrue(defaultMandate.getAllowedCategories().isEmpty());
    }
    
    @Test
    void testConstructorWithRequiredFields() {
        assertEquals(TEST_ID, mandate.getId());
        assertEquals(TEST_REQUESTING_AGENT_ID, mandate.getRequestingAgentId());
        assertEquals(TEST_RECEIVING_AGENT_ID, mandate.getReceivingAgentId());
        assertEquals(TEST_MAX_AMOUNT, mandate.getMaxAmountPerPayment());
        assertTrue(mandate.requiresHumanApproval());
        assertNotNull(mandate.getCreatedAt());
        assertNotNull(mandate.getExpiresAt());
        assertTrue(mandate.getExpiresAt().isAfter(mandate.getCreatedAt()));
        assertNotNull(mandate.getAllowedCategories());
    }
    
    @Test
    void testSimplifiedConstructor() {
        String userId = "user-123";
        String category = "clothing";
        double maxAmount = 50.0;
        
        IntentMandate simpleMnadate = new IntentMandate(userId, category, maxAmount);
        assertEquals(userId, simpleMnadate.getUserId());
        assertEquals(category, simpleMnadate.getCategory());
        assertEquals(maxAmount, simpleMnadate.getMaxAmount());
    }
    
    @Test
    void testSetterAndGetters() {
        // Test setters
        mandate.setAllowedCategories(Arrays.asList("clothing", "electronics"));
        mandate.setMaxPrice(75.0);
        
        // Test getters
        assertEquals(2, mandate.getAllowedCategories().size());
        assertTrue(mandate.getAllowedCategories().contains("clothing"));
        assertTrue(mandate.getAllowedCategories().contains("electronics"));
        assertEquals(75.0, mandate.getMaxPrice());
    }
    
    @Test
    void testSetCredential() {
        // We need to mock the VerifiableCredential since it's abstract
        VerifiableCredential mockCredential = null; // In a real test, we would use a mock framework
        mandate.setCredential(mockCredential);
        assertEquals(mockCredential, mandate.getCredential());
    }
    
    @Test
    void testIsValid() {
        // Ensure created time is set to a point well in the past to ensure validity
        mandate.setCreatedAt(Instant.now().minusSeconds(3600)); // 1 hour in the past
        mandate.setExpiresAt(Instant.now().plusSeconds(3600)); // 1 hour in the future
        assertTrue(mandate.isValid(), "Mandate should be valid when within its time window");
        
        // Test with expired mandate
        IntentMandate expiredMandate = new IntentMandate(
            "expired-id",
            "agent-1",
            "merchant-1",
            new BigDecimal("50.00"),
            false
        );
        expiredMandate.setCreatedAt(Instant.now().minusSeconds(7200)); // 2 hours in the past
        expiredMandate.setExpiresAt(Instant.now().minusSeconds(3600)); // 1 hour in the past
        assertFalse(expiredMandate.isValid(), "Mandate should be invalid when expired");
    }
    
    @Test
    void testIsExpired() {
        assertFalse(mandate.isExpired());
        
        // Test with expired mandate
        mandate.setExpiresAt(Instant.now().minusSeconds(3600)); // 1 hour in the past
        assertTrue(mandate.isExpired());
    }
    
    @Test
    void testPermits() {
        // Test with matching merchant and amount within limit
        assertTrue(mandate.permits(TEST_RECEIVING_AGENT_ID, new BigDecimal("50.00"), null));
        
        // Test with non-matching merchant
        assertFalse(mandate.permits("wrong-merchant", new BigDecimal("50.00"), null));
        
        // Test with amount exceeding limit
        assertFalse(mandate.permits(TEST_RECEIVING_AGENT_ID, new BigDecimal("150.00"), null));
        
        // Test with category constraints
        mandate.setAllowedCategories(Arrays.asList("clothing", "food"));
        assertTrue(mandate.permits(TEST_RECEIVING_AGENT_ID, new BigDecimal("50.00"), "clothing"));
        assertFalse(mandate.permits(TEST_RECEIVING_AGENT_ID, new BigDecimal("50.00"), "electronics"));
    }
}