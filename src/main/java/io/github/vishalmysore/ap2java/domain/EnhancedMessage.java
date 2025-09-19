package io.github.vishalmysore.ap2java.domain;

import io.github.vishalmysore.a2a.domain.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * An enhanced version of the Message class from a2ajava that adds methods required by AP2.
 */
@Getter
@Setter
public class EnhancedMessage extends Message {
    
    private String method;
    private Map<String, Object> params;
    
    /**
     * Gets the method name of the message.
     * 
     * @return the method name
     */
    public String getMethod() {
        return method;
    }
    
    /**
     * Sets the method name of the message.
     * 
     * @param method the method name
     */
    public void setMethod(String method) {
        this.method = method;
    }
    
    /**
     * Sets the parameters of the message.
     * 
     * @param params the parameters as a map
     */
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    
    /**
     * Gets the parameters of the message.
     * 
     * @return the parameters as a map
     */
    public Map<String, Object> getParams() {
        return params;
    }
}