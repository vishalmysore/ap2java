package io.github.vishalmysore.ap2java.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PaymentProviderTest {

    @Test
    void testPaymentProviderBuilder() {
        String id = "pp-123";
        String name = "Test Provider";
        String description = "Test Description";
        String url = "https://test-provider.com";
        String apiEndpoint = "https://api.test-provider.com";
        String type = "credit_card";
        String logoUrl = "https://test-provider.com/logo.png";
        
        PaymentProvider provider = PaymentProvider.builder()
            .id(id)
            .name(name)
            .description(description)
            .url(url)
            .apiEndpoint(apiEndpoint)
            .type(type)
            .logoUrl(logoUrl)
            .build();
            
        assertEquals(id, provider.getId());
        assertEquals(name, provider.getName());
        assertEquals(description, provider.getDescription());
        assertEquals(url, provider.getUrl());
        assertEquals(apiEndpoint, provider.getApiEndpoint());
        assertEquals(type, provider.getType());
        assertEquals(logoUrl, provider.getLogoUrl());
    }
    
    @Test
    void testPaymentProviderDefaultConstructor() {
        PaymentProvider provider = new PaymentProvider();
        
        assertNull(provider.getId());
        assertNull(provider.getName());
        assertNull(provider.getDescription());
        assertNull(provider.getUrl());
        assertNull(provider.getApiEndpoint());
        assertNull(provider.getType());
        assertNull(provider.getLogoUrl());
    }
    
    @Test
    void testPaymentProviderAllArgsConstructor() {
        String id = "pp-456";
        String name = "Another Provider";
        String description = "Another Description";
        String url = "https://another-provider.com";
        String apiEndpoint = "https://api.another-provider.com";
        String type = "wallet";
        String logoUrl = "https://another-provider.com/logo.png";
        
        PaymentProvider provider = new PaymentProvider(
            id, name, description, url, apiEndpoint, type, logoUrl);
            
        assertEquals(id, provider.getId());
        assertEquals(name, provider.getName());
        assertEquals(description, provider.getDescription());
        assertEquals(url, provider.getUrl());
        assertEquals(apiEndpoint, provider.getApiEndpoint());
        assertEquals(type, provider.getType());
        assertEquals(logoUrl, provider.getLogoUrl());
    }
    
    @Test
    void testPaymentProviderSettersAndGetters() {
        PaymentProvider provider = new PaymentProvider();
        
        String id = "pp-789";
        String name = "Updated Provider";
        String description = "Updated Description";
        String url = "https://updated-provider.com";
        String apiEndpoint = "https://api.updated-provider.com";
        String type = "bank";
        String logoUrl = "https://updated-provider.com/logo.png";
        
        provider.setId(id);
        provider.setName(name);
        provider.setDescription(description);
        provider.setUrl(url);
        provider.setApiEndpoint(apiEndpoint);
        provider.setType(type);
        provider.setLogoUrl(logoUrl);
        
        assertEquals(id, provider.getId());
        assertEquals(name, provider.getName());
        assertEquals(description, provider.getDescription());
        assertEquals(url, provider.getUrl());
        assertEquals(apiEndpoint, provider.getApiEndpoint());
        assertEquals(type, provider.getType());
        assertEquals(logoUrl, provider.getLogoUrl());
    }
    
    @Test
    void testPaymentProviderEqualsAndHashCode() {
        PaymentProvider provider1 = PaymentProvider.builder()
            .id("pp-123")
            .name("Test Provider")
            .build();
            
        PaymentProvider provider2 = PaymentProvider.builder()
            .id("pp-123")
            .name("Test Provider")
            .build();
            
        PaymentProvider provider3 = PaymentProvider.builder()
            .id("pp-456")
            .name("Another Provider")
            .build();
            
        assertEquals(provider1, provider2);
        assertEquals(provider1.hashCode(), provider2.hashCode());
        
        assertNotEquals(provider1, provider3);
        assertNotEquals(provider1.hashCode(), provider3.hashCode());
    }
    
    @Test
    void testPaymentProviderToString() {
        PaymentProvider provider = PaymentProvider.builder()
            .id("pp-123")
            .name("Test Provider")
            .type("credit_card")
            .build();
            
        String toString = provider.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("id=pp-123"));
        assertTrue(toString.contains("name=Test Provider"));
        assertTrue(toString.contains("type=credit_card"));
    }
}