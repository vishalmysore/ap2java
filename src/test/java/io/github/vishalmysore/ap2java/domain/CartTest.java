package io.github.vishalmysore.ap2java.domain;

import static org.junit.jupiter.api.Assertions.*;

import io.github.vishalmysore.ap2java.mandate.IntentMandate;
import org.junit.jupiter.api.Test;

public class CartTest {

    @Test
    void testCartCreation() {
        String userId = "user-123";
        Cart cart = new Cart(userId);
        
        assertNotNull(cart.getId());
        assertTrue(cart.getId().startsWith("cart-"));
        assertEquals(userId, cart.getUserId());
        assertEquals(0, cart.getItems().size());
        assertEquals(0.0, cart.getTotalAmount());
    }
    
    @Test
    void testAddItem() {
        Cart cart = new Cart("user-123");
        
        String productId = "prod-1";
        String productName = "Test Product";
        double price = 10.5;
        int quantity = 2;
        
        cart.addItem(productId, productName, price, quantity);
        
        assertEquals(1, cart.getItems().size());
        assertTrue(cart.getItems().containsKey(productId));
        assertEquals(productName, cart.getItems().get(productId).getProductName());
        assertEquals(price, cart.getItems().get(productId).getPrice());
        assertEquals(quantity, cart.getItems().get(productId).getQuantity());
        assertEquals(price * quantity, cart.getTotalAmount());
    }
    
    @Test
    void testAddMultipleItems() {
        Cart cart = new Cart("user-123");
        
        cart.addItem("prod-1", "Product 1", 10.0, 1);
        cart.addItem("prod-2", "Product 2", 20.0, 2);
        
        assertEquals(2, cart.getItems().size());
        assertEquals(10.0 + (20.0 * 2), cart.getTotalAmount());
    }
    
    @Test
    void testRemoveItem() {
        Cart cart = new Cart("user-123");
        
        cart.addItem("prod-1", "Product 1", 10.0, 1);
        cart.addItem("prod-2", "Product 2", 20.0, 2);
        
        assertEquals(2, cart.getItems().size());
        assertEquals(10.0 + (20.0 * 2), cart.getTotalAmount());
        
        cart.removeItem("prod-1");
        
        assertEquals(1, cart.getItems().size());
        assertFalse(cart.getItems().containsKey("prod-1"));
        assertTrue(cart.getItems().containsKey("prod-2"));
        assertEquals(20.0 * 2, cart.getTotalAmount());
    }
    
    @Test
    void testSetAndGetIntent() {
        Cart cart = new Cart("user-123");
        IntentMandate intent = new IntentMandate("user-123", "retail", 500.0);
        intent.setUserId("user-123");
        
        cart.setIntent(intent);
        
        assertSame(intent, cart.getIntent());
        assertEquals("user-123", cart.getIntent().getUserId());
    }
    
    @Test
    void testToString() {
        Cart cart = new Cart("user-123");
        cart.addItem("prod-1", "Product 1", 10.0, 1);
        
        String toString = cart.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("Cart{id="));
        assertTrue(toString.contains("totalAmount=10.00"));
        assertTrue(toString.contains("Product 1"));
        assertTrue(toString.contains("prod-1"));
    }
    
    @Test
    void testCartItem() {
        String productId = "prod-1";
        String productName = "Test Product";
        double price = 10.5;
        int quantity = 2;
        
        Cart.CartItem item = new Cart.CartItem(productId, productName, price, quantity);
        
        assertEquals(productId, item.getProductId());
        assertEquals(productName, item.getProductName());
        assertEquals(price, item.getPrice());
        assertEquals(quantity, item.getQuantity());
    }
}