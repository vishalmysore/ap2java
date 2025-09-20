package io.github.vishalmysore.ap2java.mandate;

import io.github.vishalmysore.ap2java.credentials.VerifiableCredential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CartMandate class.
 */
public class CartMandateTest {
    
    private CartMandate cartMandate;
    private final String TEST_ID = "cart-123";
    private final String TEST_REQUESTING_AGENT_ID = "agent-456";
    private final String TEST_RECEIVING_AGENT_ID = "merchant-789";
    private final String TEST_PARENT_MANDATE_ID = "intent-123";
    private final String TEST_CURRENCY_CODE = "USD";
    private List<CartMandate.CartItem> testItems;
    
    @BeforeEach
    void setUp() {
        testItems = new ArrayList<>();
        testItems.add(new CartMandate.CartItem(
            "item-1", 
            "Nike Running Shoes", 
            new BigDecimal("95.00"), 
            1
        ));
        testItems.add(new CartMandate.CartItem(
            "item-2", 
            "Socks", 
            new BigDecimal("5.00"), 
            2
        ));
        
        cartMandate = new CartMandate(
            TEST_ID,
            TEST_REQUESTING_AGENT_ID,
            TEST_RECEIVING_AGENT_ID,
            TEST_PARENT_MANDATE_ID,
            testItems,
            TEST_CURRENCY_CODE,
            Instant.now().plusSeconds(30 * 60) // 30 minutes
        );
    }
    
    @Test
    void testDefaultConstructor() {
        CartMandate defaultMandate = new CartMandate();
        assertNotNull(defaultMandate.getItems());
        assertTrue(defaultMandate.getItems().isEmpty());
    }
    
    @Test
    void testConstructorWithRequiredFields() {
        assertEquals(TEST_ID, cartMandate.getId());
        assertEquals(TEST_REQUESTING_AGENT_ID, cartMandate.getRequestingAgentId());
        assertEquals(TEST_RECEIVING_AGENT_ID, cartMandate.getReceivingAgentId());
        assertEquals(TEST_PARENT_MANDATE_ID, cartMandate.getIntentMandateId());
        assertEquals(TEST_CURRENCY_CODE, cartMandate.getCurrencyCode());
        assertEquals(2, cartMandate.getItems().size());
        assertNotNull(cartMandate.getCreatedAt());
        assertNotNull(cartMandate.getExpiresAt());
        assertTrue(cartMandate.getExpiresAt().isAfter(cartMandate.getCreatedAt()));
        
        // Test calculated total amount
        BigDecimal expectedTotal = new BigDecimal("105.00"); // 95 + (5 * 2)
        assertEquals(0, expectedTotal.compareTo(cartMandate.getAmount()));
    }
    
    @Test
    void testCartItem() {
        CartMandate.CartItem item = cartMandate.getItems().get(0);
        assertEquals("item-1", item.getId());
        assertEquals("Nike Running Shoes", item.getDescription());
        assertEquals(0, new BigDecimal("95.00").compareTo(item.getPrice()));
        assertEquals(1, item.getQuantity());
        assertEquals(0, new BigDecimal("95.00").compareTo(item.getTotalPrice()));
        
        // Test item with quantity > 1
        CartMandate.CartItem item2 = cartMandate.getItems().get(1);
        assertEquals(2, item2.getQuantity());
        assertEquals(0, new BigDecimal("10.00").compareTo(item2.getTotalPrice()));
    }
    
    @Test
    void testSetterAndGetters() {
        Instant newExpiration = Instant.now().plusSeconds(2 * 60 * 60); // 2 hours
        BigDecimal newAmount = new BigDecimal("200.00");
        
        cartMandate.setExpiresAt(newExpiration);
        cartMandate.setAmount(newAmount);
        
        assertEquals(newExpiration, cartMandate.getExpiresAt());
        assertEquals(0, newAmount.compareTo(cartMandate.getAmount()));
    }
    
    @Test
    void testSetItems() {
        List<CartMandate.CartItem> newItems = new ArrayList<>();
        newItems.add(new CartMandate.CartItem(
            "item-3", 
            "Headphones", 
            new BigDecimal("150.00"), 
            1
        ));
        
        cartMandate.setItems(newItems);
        
        assertEquals(1, cartMandate.getItems().size());
        assertEquals("item-3", cartMandate.getItems().get(0).getId());
        assertEquals(0, new BigDecimal("150.00").compareTo(cartMandate.getAmount()));
    }
    
    @Test
    void testSetItemsWithNullHandling() {
        cartMandate.setItems(null);
        assertNotNull(cartMandate.getItems());
        assertTrue(cartMandate.getItems().isEmpty());
        assertEquals(0, BigDecimal.ZERO.compareTo(cartMandate.getAmount()));
    }
    
    @Test
    void testSetCredential() {
        // We need to mock the VerifiableCredential since it's abstract
        VerifiableCredential mockCredential = null; // In a real test, we would use a mock framework
        cartMandate.setCredential(mockCredential);
        assertEquals(mockCredential, cartMandate.getCredential());
    }
    
    @Test
    void testIsValid() {
        // Ensure created time is set to a point well in the past to ensure validity
        cartMandate.setCreatedAt(Instant.now().minusSeconds(3600)); // 1 hour in the past
        cartMandate.setExpiresAt(Instant.now().plusSeconds(3600)); // 1 hour in the future
        assertTrue(cartMandate.isValid(), "Cart mandate should be valid when within its time window");
        
        // Test with expired mandate
        CartMandate expiredMandate = new CartMandate();
        expiredMandate.setCreatedAt(Instant.now().minusSeconds(7200)); // 2 hours in the past
        expiredMandate.setExpiresAt(Instant.now().minusSeconds(3600)); // 1 hour in the past
        assertFalse(expiredMandate.isValid(), "Cart mandate should be invalid when expired");
    }
    
    @Test
    void testIsExpired() {
        assertFalse(cartMandate.isExpired());
        
        // Test with expired mandate
        cartMandate.setExpiresAt(Instant.now().minusSeconds(3600)); // 1 hour in the past
        assertTrue(cartMandate.isExpired());
    }
    
    @Test
    void testGetIntentMandateId() {
        assertEquals(TEST_PARENT_MANDATE_ID, cartMandate.getIntentMandateId());
    }
    
    @Test
    void testGetMerchantId() {
        assertEquals(TEST_RECEIVING_AGENT_ID, cartMandate.getMerchantId());
    }
    
    @Test
    void testGetTotalAmount() {
        BigDecimal expectedTotal = new BigDecimal("105.00"); // 95 + (5 * 2)
        assertEquals(0, expectedTotal.compareTo(cartMandate.getTotalAmount()));
    }
}