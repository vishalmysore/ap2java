package io.github.vishalmysore.ap2java.domain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentEnabledAgentCardTest {
    
    private PaymentEnabledAgentCard agentCard;
    private PaymentProvider paymentProvider;
    
    @BeforeEach
    void setUp() {
        // Setup payment provider
        paymentProvider = new PaymentProvider();
        paymentProvider.setId("provider-123");
        paymentProvider.setName("Test Payment Provider");
        paymentProvider.setDescription("A test payment provider for unit tests");
        paymentProvider.setUrl("https://test-provider.com");
        paymentProvider.setApiEndpoint("https://api.test-provider.com");
        paymentProvider.setType("CREDIT_CARD");
        paymentProvider.setLogoUrl("https://test-provider.com/logo.png");
        
        // Setup agent card
        agentCard = new PaymentEnabledAgentCard();
        agentCard.setPaymentProvider(paymentProvider);
        agentCard.setSupportedPaymentMethods(new String[]{"credit-card", "debit-card"});
        agentCard.setSupportedCurrencies(new String[]{"USD", "EUR", "GBP"});
        agentCard.setCanReceivePayments(true);
        agentCard.setCanMakePayments(true);
        agentCard.setRequiresAuthentication(false);
    }
    
    @Test
    void testPaymentProviderGetterSetter() {
        assertEquals(paymentProvider, agentCard.getPaymentProvider());
        
        PaymentProvider newProvider = new PaymentProvider();
        newProvider.setId("new-provider-456");
        newProvider.setName("New Provider");
        
        agentCard.setPaymentProvider(newProvider);
        assertEquals(newProvider, agentCard.getPaymentProvider());
    }
    
    @Test
    void testSupportedPaymentMethodsGetterSetter() {
        String[] expected = {"credit-card", "debit-card"};
        assertArrayEquals(expected, agentCard.getSupportedPaymentMethods());
        
        String[] newMethods = {"paypal", "apple-pay", "google-pay"};
        agentCard.setSupportedPaymentMethods(newMethods);
        assertArrayEquals(newMethods, agentCard.getSupportedPaymentMethods());
    }
    
    @Test
    void testSupportedCurrenciesGetterSetter() {
        String[] expected = {"USD", "EUR", "GBP"};
        assertArrayEquals(expected, agentCard.getSupportedCurrencies());
        
        String[] newCurrencies = {"JPY", "CAD", "AUD"};
        agentCard.setSupportedCurrencies(newCurrencies);
        assertArrayEquals(newCurrencies, agentCard.getSupportedCurrencies());
    }
    
    @Test
    void testCanReceivePaymentsGetterSetter() {
        assertTrue(agentCard.isCanReceivePayments());
        
        agentCard.setCanReceivePayments(false);
        assertFalse(agentCard.isCanReceivePayments());
    }
    
    @Test
    void testCanMakePaymentsGetterSetter() {
        assertTrue(agentCard.isCanMakePayments());
        
        agentCard.setCanMakePayments(false);
        assertFalse(agentCard.isCanMakePayments());
    }
    
    @Test
    void testRequiresAuthenticationGetterSetter() {
        assertFalse(agentCard.isRequiresAuthentication());
        
        agentCard.setRequiresAuthentication(true);
        assertTrue(agentCard.isRequiresAuthentication());
    }
}