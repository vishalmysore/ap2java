package io.github.vishalmysore.ap2java.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class OptionsTest {

    @Test
    void testCartOptionsDefaultConstructor() {
        CartOptions options = new CartOptions();
        
        // Default values should be initialized
        assertFalse(options.isAllowModifications());
        assertNull(options.getCurrency());
        assertEquals(0, options.getMaxItems());
        assertEquals(0, options.getMinItems());
    }
    
    @Test
    void testCartOptionsParameterizedConstructor() {
        boolean allowModifications = true;
        String currency = "USD";
        int maxItems = 10;
        int minItems = 1;
        
        CartOptions options = new CartOptions(allowModifications, currency, maxItems, minItems);
        
        assertEquals(allowModifications, options.isAllowModifications());
        assertEquals(currency, options.getCurrency());
        assertEquals(maxItems, options.getMaxItems());
        assertEquals(minItems, options.getMinItems());
    }
    
    @Test
    void testCartOptionsSettersAndGetters() {
        CartOptions options = new CartOptions();
        
        options.setAllowModifications(true);
        options.setCurrency("EUR");
        options.setMaxItems(5);
        options.setMinItems(2);
        
        assertTrue(options.isAllowModifications());
        assertEquals("EUR", options.getCurrency());
        assertEquals(5, options.getMaxItems());
        assertEquals(2, options.getMinItems());
    }
    
    @Test
    void testProductOptionsDefaultConstructor() {
        ProductOptions options = new ProductOptions();
        
        // Default values should be initialized
        assertNull(options.getId());
        assertNull(options.getName());
        assertNull(options.getDescription());
    }
    
    @Test
    void testProductOptionsParameterizedConstructor() {
        String id = "prod-1";
        String name = "Test Product";
        String description = "Test Description";
        
        ProductOptions options = new ProductOptions(id, name, description);
        
        assertEquals(id, options.getId());
        assertEquals(name, options.getName());
        assertEquals(description, options.getDescription());
    }
    
    @Test
    void testProductOptionsSettersAndGetters() {
        ProductOptions options = new ProductOptions();
        
        options.setId("prod-2");
        options.setName("Another Product");
        options.setDescription("Another Description");
        
        assertEquals("prod-2", options.getId());
        assertEquals("Another Product", options.getName());
        assertEquals("Another Description", options.getDescription());
    }
}