package io.github.ap2java.examples;

import io.github.ap2java.client.AP2Client;
import io.github.ap2java.domain.EnhancedCapabilities;
import io.github.ap2java.domain.EnhancedMessage;
import io.github.ap2java.domain.EnhancedTask;
import io.github.ap2java.domain.PaymentEnabledAgentCard;
import io.github.ap2java.domain.PaymentProvider;
import io.github.ap2java.payment.SamplePaymentProcessor;
import io.github.vishalmysore.a2a.domain.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Example demonstrating integration of AP2 with the a2ajava agent system.
 */
@Slf4j
public class AP2IntegrationExample {
    
    public static void main(String[] args) {
        log.info("Starting AP2 integration example");
        
        try {
            // Create payment-enabled agent
            PaymentEnabledAgentCard agentCard = createPaymentEnabledAgentCard();
            
            // Create AP2 client
            AP2Client ap2Client = new AP2Client(new SamplePaymentProcessor(), agentCard);
            
            // Create message for payment request
            Message paymentRequestMessage = createPaymentRequestMessage();
            
            // Process the message
            Task task = ap2Client.processMessage(paymentRequestMessage);
            
            // Log task details
            log.info("Task ID: {}", task.getId());
            
            // Check if task is EnhancedTask
            if (task instanceof io.github.ap2java.domain.EnhancedTask) {
                io.github.ap2java.domain.EnhancedTask enhancedTask = (io.github.ap2java.domain.EnhancedTask) task;
                log.info("Task status: {}", enhancedTask.isCompleted() ? "Completed" : "In Progress");
            } else {
                log.info("Task status: Unknown (not an EnhancedTask)");
            }
            
            // In a real implementation, we would wait for the task to complete
            // and then process the result
            
        } catch (Exception e) {
            log.error("Error in AP2 integration example", e);
        }
        
        log.info("AP2 integration example completed");
    }
    
    /**
     * Create a payment-enabled agent card.
     *
     * @return The agent card.
     */
    private static PaymentEnabledAgentCard createPaymentEnabledAgentCard() {
        PaymentEnabledAgentCard agentCard = new PaymentEnabledAgentCard();
        
        // Set basic agent properties
        agentCard.setName("Payment Agent");
        agentCard.setDescription("Agent that processes payments");
        agentCard.setUrl("https://example.com/payment-agent");
        agentCard.setVersion("1.0.0");
        agentCard.setDocumentationUrl("https://example.com/docs/payment-agent");
        
        // Set provider
        Provider provider = new Provider();
        provider.setOrganization("Example Corp");
        provider.setUrl("https://example.com");
        agentCard.setProvider(provider);
        
        // Set capabilities
        EnhancedCapabilities capabilities = new EnhancedCapabilities();
        capabilities.setStreaming(true);
        capabilities.setPushNotifications(true);
        capabilities.setCustomCapability("paymentEnabled", true);
        agentCard.setCapabilities(capabilities);
        
        // Set authentication
        Authentication auth = new Authentication();
        auth.setBearerAuth("api-key-123");
        agentCard.setAuthentication(auth);
        
        // Set default modes
        agentCard.setDefaultInputModes(new String[]{"text"});
        agentCard.setDefaultOutputModes(new String[]{"text", "json"});
        
        // Add payment skills
        Skill createPaymentSkill = createSkill("createPayment", "Create a new payment", 
                new String[]{"payment", "create"});
        Skill authorizePaymentSkill = createSkill("authorizePayment", "Authorize a payment", 
                new String[]{"payment", "authorize"});
        Skill capturePaymentSkill = createSkill("capturePayment", "Capture an authorized payment", 
                new String[]{"payment", "capture"});
        
        agentCard.setSkills(Arrays.asList(createPaymentSkill, authorizePaymentSkill, capturePaymentSkill));
        
        // Set payment provider
        PaymentProvider paymentProvider = PaymentProvider.builder()
                .id("example-provider")
                .name("Example Payment Provider")
                .description("Payment processing services")
                .url("https://example.com/payments")
                .apiEndpoint("https://api.example.com/payments")
                .type("multi")
                .logoUrl("https://example.com/logo.png")
                .build();
        agentCard.setPaymentProvider(paymentProvider);
        
        // Set payment capabilities
        agentCard.setSupportedPaymentMethods(new String[]{"credit_card", "wallet", "bank_transfer"});
        agentCard.setSupportedCurrencies(new String[]{"USD", "EUR", "GBP"});
        agentCard.setCanReceivePayments(true);
        agentCard.setCanMakePayments(true);
        agentCard.setRequiresAuthentication(true);
        
        return agentCard;
    }
    
    /**
     * Create a skill for the agent.
     *
     * @param name        Skill name.
     * @param description Skill description.
     * @param tags        Skill tags.
     * @return The created skill.
     */
    private static Skill createSkill(String name, String description, String[] tags) {
        Skill skill = new Skill();
        skill.setName(name);
        skill.setDescription(description);
        skill.setTags(tags);
        return skill;
    }
    
    /**
     * Create a payment request message.
     *
     * @return The created message.
     */
    private static Message createPaymentRequestMessage() {
        EnhancedMessage message = new EnhancedMessage();
        message.setId(UUID.randomUUID().toString());
        message.setMethod("createPayment");
        
        // In a real implementation, the params would include the payment request details
        Map<String, Object> params = new HashMap<>();
        params.put("amount", "99.99");
        params.put("currencyCode", "USD");
        params.put("description", "Purchase of Premium Service");
        params.put("requestingAgentId", "agent-123");
        params.put("receivingAgentId", "merchant-456");
        
        message.setParams(params);
        
        return message;
    }
}