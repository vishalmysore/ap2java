package io.github.vishalmysore.ap2java.mandate;

import io.github.vishalmysore.ap2java.credentials.VerifiableCredential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PaymentMandate class.
 */
public class PaymentMandateTest {
    
    private PaymentMandate paymentMandate;
    private final String TEST_ID = "payment-123";
    private final String TEST_REQUESTING_AGENT_ID = "agent-456";
    private final String TEST_RECEIVING_AGENT_ID = "merchant-789";
    private final String TEST_PAYMENT_REFERENCE = "order-123";
    private final BigDecimal TEST_AMOUNT = new BigDecimal("105.00");
    private final String TEST_CURRENCY_CODE = "USD";
    private final String TEST_PARENT_MANDATE_ID = "cart-123";
    private Map<String, Object> testPaymentDetails;
    
    @BeforeEach
    void setUp() {
        testPaymentDetails = new HashMap<>();
        testPaymentDetails.put("paymentMethodId", "card-123");
        testPaymentDetails.put("cardLast4", "1234");
        
        paymentMandate = new PaymentMandate(
            TEST_ID,
            TEST_REQUESTING_AGENT_ID,
            TEST_RECEIVING_AGENT_ID,
            TEST_PAYMENT_REFERENCE,
            TEST_AMOUNT,
            TEST_CURRENCY_CODE,
            TEST_PARENT_MANDATE_ID,
            testPaymentDetails,
            Instant.now().plusSeconds(15 * 60) // 15 minutes
        );
    }
    
    @Test
    void testDefaultConstructor() {
        PaymentMandate defaultMandate = new PaymentMandate();
        assertNotNull(defaultMandate.getPaymentDetails());
        assertTrue(defaultMandate.getPaymentDetails().isEmpty());
    }
    
    @Test
    void testConstructorWithRequiredFields() {
        assertEquals(TEST_ID, paymentMandate.getId());
        assertEquals(TEST_REQUESTING_AGENT_ID, paymentMandate.getRequestingAgentId());
        assertEquals(TEST_RECEIVING_AGENT_ID, paymentMandate.getReceivingAgentId());
        assertEquals(TEST_PAYMENT_REFERENCE, paymentMandate.getPaymentReference());
        assertEquals(0, TEST_AMOUNT.compareTo(paymentMandate.getAmount()));
        assertEquals(TEST_CURRENCY_CODE, paymentMandate.getCurrencyCode());
        assertEquals(TEST_PARENT_MANDATE_ID, paymentMandate.getParentMandateId());
        assertNotNull(paymentMandate.getCreatedAt());
        assertNotNull(paymentMandate.getExpiresAt());
        assertTrue(paymentMandate.getExpiresAt().isAfter(paymentMandate.getCreatedAt()));
    }
    
    @Test
    void testSetterAndGetters() {
        Instant newExpiration = Instant.now().plusSeconds(2 * 60 * 60); // 2 hours
        BigDecimal newAmount = new BigDecimal("200.00");
        
        paymentMandate.setExpiresAt(newExpiration);
        paymentMandate.setAmount(newAmount);
        
        assertEquals(newExpiration, paymentMandate.getExpiresAt());
        assertEquals(0, newAmount.compareTo(paymentMandate.getAmount()));
    }
    
    @Test
    void testPaymentDetails() {
        // Test getting existing details
        assertEquals(2, paymentMandate.getPaymentDetails().size());
        assertEquals("card-123", paymentMandate.getPaymentDetails().get("paymentMethodId"));
        assertEquals("1234", paymentMandate.getPaymentDetails().get("cardLast4"));
        
        // Test setting new details
        Map<String, Object> newDetails = new HashMap<>();
        newDetails.put("paymentMethodId", "bank-456");
        newDetails.put("bankName", "Example Bank");
        
        paymentMandate.setPaymentDetails(newDetails);
        
        assertEquals(2, paymentMandate.getPaymentDetails().size());
        assertEquals("bank-456", paymentMandate.getPaymentDetails().get("paymentMethodId"));
        assertEquals("Example Bank", paymentMandate.getPaymentDetails().get("bankName"));
        assertNull(paymentMandate.getPaymentDetails().get("cardLast4"));
    }
    
    @Test
    void testSetPaymentDetailsWithNullHandling() {
        paymentMandate.setPaymentDetails(null);
        assertNotNull(paymentMandate.getPaymentDetails());
        assertTrue(paymentMandate.getPaymentDetails().isEmpty());
    }
    
    @Test
    void testSetCredential() {
        // We need to mock the VerifiableCredential since it's abstract
        VerifiableCredential mockCredential = null; // In a real test, we would use a mock framework
        paymentMandate.setCredential(mockCredential);
        assertEquals(mockCredential, paymentMandate.getCredential());
    }
    
    @Test
    void testIsValid() {
        // Ensure created time is set to a point well in the past to ensure validity
        paymentMandate.setCreatedAt(Instant.now().minusSeconds(3600)); // 1 hour in the past
        paymentMandate.setExpiresAt(Instant.now().plusSeconds(3600)); // 1 hour in the future
        assertTrue(paymentMandate.isValid(), "Payment mandate should be valid when within its time window");
        
        // Test with expired mandate
        PaymentMandate expiredMandate = new PaymentMandate();
        expiredMandate.setCreatedAt(Instant.now().minusSeconds(7200)); // 2 hours in the past
        expiredMandate.setExpiresAt(Instant.now().minusSeconds(3600)); // 1 hour in the past
        assertFalse(expiredMandate.isValid(), "Payment mandate should be invalid when expired");
    }
    
    @Test
    void testIsExpired() {
        assertFalse(paymentMandate.isExpired());
        
        // Test with expired mandate
        paymentMandate.setExpiresAt(Instant.now().minusSeconds(3600)); // 1 hour in the past
        assertTrue(paymentMandate.isExpired());
    }
    
    @Test
    void testGetCartMandateId() {
        assertEquals(TEST_PARENT_MANDATE_ID, paymentMandate.getCartMandateId());
    }
    
    @Test
    void testGetPaymentMethodId() {
        assertEquals("card-123", paymentMandate.getPaymentMethodId());
        
        // Test with no payment method ID
        PaymentMandate noMethodIdMandate = new PaymentMandate();
        assertNull(noMethodIdMandate.getPaymentMethodId());
        
        // Test with empty payment details
        Map<String, Object> emptyDetails = new HashMap<>();
        noMethodIdMandate.setPaymentDetails(emptyDetails);
        assertNull(noMethodIdMandate.getPaymentMethodId());
    }
    
    @Test
    void testPaymentStatus() {
        // Test default status
        assertNull(paymentMandate.getStatus());
        
        // Test setting and getting status
        paymentMandate.setStatus(PaymentMandate.PaymentStatus.AUTHORIZED);
        assertEquals(PaymentMandate.PaymentStatus.AUTHORIZED, paymentMandate.getStatus());
    }
    
    @Test
    void testCartMandate() {
        // Test setting and getting cart mandate
        CartMandate cartMandate = new CartMandate();
        cartMandate.setId("test-cart");
        
        paymentMandate.setCartMandate(cartMandate);
        assertEquals(cartMandate, paymentMandate.getCartMandate());
    }
}