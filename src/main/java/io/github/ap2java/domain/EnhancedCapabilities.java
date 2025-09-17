package io.github.ap2java.domain;

import io.github.vishalmysore.a2a.domain.Capabilities;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * An enhanced version of the Capabilities class from a2ajava that adds methods required by AP2.
 */
@Getter
@Setter
public class EnhancedCapabilities extends Capabilities {
    
    private Map<String, Object> customCapabilities = new HashMap<>();
    
    /**
     * Creates an enhanced capabilities object from a base capabilities object.
     * 
     * @param capabilities the base capabilities
     * @return the enhanced capabilities
     */
    public static EnhancedCapabilities from(Capabilities capabilities) {
        EnhancedCapabilities enhanced = new EnhancedCapabilities();
        enhanced.setStreaming(capabilities.isStreaming());
        enhanced.setPushNotifications(capabilities.isPushNotifications());
        enhanced.setStateTransitionHistory(capabilities.isStateTransitionHistory());
        return enhanced;
    }
    
    /**
     * Sets a custom capability.
     * 
     * @param key the capability name
     * @param value the capability value
     */
    public void setCustomCapability(String key, Object value) {
        if (customCapabilities == null) {
            customCapabilities = new HashMap<>();
        }
        customCapabilities.put(key, value);
    }
    
    /**
     * Gets a custom capability.
     * 
     * @param key the capability name
     * @return the capability value
     */
    public Object getCustomCapability(String key) {
        return customCapabilities != null ? customCapabilities.get(key) : null;
    }
}