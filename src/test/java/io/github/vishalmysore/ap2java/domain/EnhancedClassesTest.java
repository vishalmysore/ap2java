package io.github.vishalmysore.ap2java.domain;

import static org.junit.jupiter.api.Assertions.*;

import io.github.vishalmysore.a2a.domain.Capabilities;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnhancedClassesTest {

    @Test
    void testEnhancedCapabilitiesFromMethod() {
        Capabilities originalCapabilities = new Capabilities();
        originalCapabilities.setStreaming(true);
        originalCapabilities.setPushNotifications(true);
        originalCapabilities.setStateTransitionHistory(false);
        
        EnhancedCapabilities enhancedCapabilities = EnhancedCapabilities.from(originalCapabilities);
        
        assertTrue(enhancedCapabilities.isStreaming());
        assertTrue(enhancedCapabilities.isPushNotifications());
        assertFalse(enhancedCapabilities.isStateTransitionHistory());
        assertNotNull(enhancedCapabilities.getCustomCapabilities());
        assertTrue(enhancedCapabilities.getCustomCapabilities().isEmpty());
    }
    
    @Test
    void testEnhancedCapabilitiesCustomCapabilities() {
        EnhancedCapabilities enhancedCapabilities = new EnhancedCapabilities();
        
        String key = "testKey";
        Object value = "testValue";
        
        enhancedCapabilities.setCustomCapability(key, value);
        
        assertEquals(1, enhancedCapabilities.getCustomCapabilities().size());
        assertEquals(value, enhancedCapabilities.getCustomCapability(key));
    }
    
    @Test
    void testEnhancedCapabilitiesNullCustomCapabilities() {
        EnhancedCapabilities enhancedCapabilities = new EnhancedCapabilities();
        enhancedCapabilities.setCustomCapabilities(null);
        
        String key = "testKey";
        Object value = "testValue";
        
        enhancedCapabilities.setCustomCapability(key, value);
        
        assertEquals(1, enhancedCapabilities.getCustomCapabilities().size());
        assertEquals(value, enhancedCapabilities.getCustomCapability(key));
    }
    
    @Test
    void testEnhancedCapabilitiesGetCustomCapabilityWithNullMap() {
        EnhancedCapabilities enhancedCapabilities = new EnhancedCapabilities();
        enhancedCapabilities.setCustomCapabilities(null);
        
        assertNull(enhancedCapabilities.getCustomCapability("nonExistentKey"));
    }
    
    @Test
    void testEnhancedMessage() {
        EnhancedMessage message = new EnhancedMessage();
        
        String method = "testMethod";
        Map<String, Object> params = new HashMap<>();
        params.put("key1", "value1");
        
        message.setMethod(method);
        message.setParams(params);
        
        assertEquals(method, message.getMethod());
        assertSame(params, message.getParams());
        assertEquals("value1", message.getParams().get("key1"));
    }
    
    @Test
    void testEnhancedTask() {
        EnhancedTask task = new EnhancedTask();
        
        // Test default values
        assertFalse(task.isCompleted());
        assertNull(task.getError());
        assertNull(task.getResult());
        
        // Test setters and getters
        task.setCompleted(true);
        assertTrue(task.isCompleted());
        
        String errorMessage = "Test error message";
        task.setError(errorMessage);
        assertEquals(errorMessage, task.getError());
        
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .paymentId(UUID.randomUUID().toString())
                .status(PaymentStatus.COMPLETED)
                .build();
        task.setResult(paymentResponse);
        
        assertSame(paymentResponse, task.getResult());
        assertEquals(PaymentStatus.COMPLETED, task.getResult().getStatus());
    }
}