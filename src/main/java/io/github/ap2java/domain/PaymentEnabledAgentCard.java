package io.github.ap2java.domain;

import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.domain.Capabilities;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Extends the AgentCard class from a2ajava to include payment capabilities.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentEnabledAgentCard extends AgentCard {
    
    /**
     * Payment provider information.
     */
    private PaymentProvider paymentProvider;
    
    /**
     * Supported payment methods.
     */
    private String[] supportedPaymentMethods;
    
    /**
     * Supported currencies.
     */
    private String[] supportedCurrencies;
    
    /**
     * Whether this agent can receive payments.
     */
    private boolean canReceivePayments;
    
    /**
     * Whether this agent can make payments.
     */
    private boolean canMakePayments;
    
    /**
     * Whether this agent requires authentication for payments.
     */
    private boolean requiresAuthentication;
    
    /**
     * Default constructor.
     */
    public PaymentEnabledAgentCard() {
        super();
        // Initialize the AP2 capabilities in the base AgentCard capabilities
        if (getCapabilities() != null) {
            // Convert to EnhancedCapabilities
            EnhancedCapabilities enhancedCapabilities = EnhancedCapabilities.from(getCapabilities());
            enhancedCapabilities.setCustomCapability("paymentEnabled", true);
            
            // Update the capabilities
            setCapabilities(enhancedCapabilities);
        }
    }
}