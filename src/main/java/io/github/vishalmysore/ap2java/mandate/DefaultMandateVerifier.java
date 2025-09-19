package io.github.vishalmysore.ap2java.mandate;

import io.github.vishalmysore.ap2java.domain.PaymentRequest;
import io.github.vishalmysore.ap2java.security.SignatureService;
import io.github.vishalmysore.ap2java.credentials.VerifiableCredential;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Default implementation of MandateVerifier that verifies mandates
 * according to the AP2 protocol rules.
 */
public class DefaultMandateVerifier implements MandateVerifier {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMandateVerifier.class);
    
    private final SignatureService signatureService;
    
    public DefaultMandateVerifier(SignatureService signatureService) {
        this.signatureService = signatureService;
    }
    
    @Override
    public boolean verifyIntentMandate(IntentMandate mandate, PaymentRequest request) {
        logger.debug("Verifying Intent Mandate: {} for request: {}", 
                    mandate.getId(), request.getExternalReference());
        
        // Check signature
        if (!verifySignature(mandate)) {
            logger.error("Intent Mandate signature verification failed: {}", mandate.getId());
            return false;
        }
        
        // Check expiry
        if (mandate.isExpired()) {
            logger.error("Intent Mandate has expired: {}", mandate.getId());
            return false;
        }
        
        // Check if the mandate permits this payment
        String merchantId = request.getReceivingAgentId();
        BigDecimal amount = request.getAmount();
        String category = null; // In a real system, this would be looked up
        
        boolean permitted = mandate.permits(merchantId, amount, category);
        
        if (!permitted) {
            logger.error("Intent Mandate does not permit this payment: {} -> merchant: {}, amount: {}", 
                         mandate.getId(), merchantId, amount);
        }
        
        return permitted;
    }
    
    @Override
    public boolean verifyCartMandate(CartMandate cartMandate, IntentMandate intentMandate, PaymentRequest request) {
        logger.debug("Verifying Cart Mandate: {} for request: {}", 
                    cartMandate.getId(), request.getExternalReference());
        
        // Check signature
        if (!verifySignature(cartMandate)) {
            logger.error("Cart Mandate signature verification failed: {}", cartMandate.getId());
            return false;
        }
        
        // Check expiry
        if (cartMandate.isExpired()) {
            logger.error("Cart Mandate has expired: {}", cartMandate.getId());
            return false;
        }
        
        // Verify that the Cart Mandate references the correct Intent Mandate
        if (!cartMandate.getIntentMandateId().equals(intentMandate.getId())) {
            logger.error("Cart Mandate references incorrect Intent Mandate: {} vs {}", 
                         cartMandate.getIntentMandateId(), intentMandate.getId());
            return false;
        }
        
        // Verify that the merchant matches
        if (!cartMandate.getMerchantId().equals(request.getReceivingAgentId())) {
            logger.error("Cart Mandate merchant does not match request: {} vs {}", 
                         cartMandate.getMerchantId(), request.getReceivingAgentId());
            return false;
        }
        
        // Verify amount
        if (cartMandate.getTotalAmount().compareTo(request.getAmount()) != 0) {
            logger.error("Cart Mandate amount does not match request: {} vs {}", 
                         cartMandate.getTotalAmount(), request.getAmount());
            return false;
        }
        
        // Verify currency
        if (!cartMandate.getCurrencyCode().equals(request.getCurrencyCode())) {
            logger.error("Cart Mandate currency does not match request: {} vs {}", 
                         cartMandate.getCurrencyCode(), request.getCurrencyCode());
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean verifyPaymentMandate(PaymentMandate paymentMandate, CartMandate cartMandate, PaymentRequest request) {
        logger.debug("Verifying Payment Mandate: {} for request: {}", 
                    paymentMandate.getId(), request.getExternalReference());
        
        // Check signature
        if (!verifySignature(paymentMandate)) {
            logger.error("Payment Mandate signature verification failed: {}", paymentMandate.getId());
            return false;
        }
        
        // Check expiry
        if (paymentMandate.isExpired()) {
            logger.error("Payment Mandate has expired: {}", paymentMandate.getId());
            return false;
        }
        
        // Verify that the Payment Mandate references the correct Cart Mandate
        if (!paymentMandate.getCartMandateId().equals(cartMandate.getId())) {
            logger.error("Payment Mandate references incorrect Cart Mandate: {} vs {}", 
                         paymentMandate.getCartMandateId(), cartMandate.getId());
            return false;
        }
        
        // Verify amount
        if (paymentMandate.getAmount().compareTo(request.getAmount()) != 0) {
            logger.error("Payment Mandate amount does not match request: {} vs {}", 
                         paymentMandate.getAmount(), request.getAmount());
            return false;
        }
        
        // Verify currency
        if (!paymentMandate.getCurrencyCode().equals(request.getCurrencyCode())) {
            logger.error("Payment Mandate currency does not match request: {} vs {}", 
                         paymentMandate.getCurrencyCode(), request.getCurrencyCode());
            return false;
        }
        
        // Check payment method if specified in the request
        if (request.getPaymentMethod() != null && 
            !request.getPaymentMethod().equals(paymentMandate.getPaymentMethodId())) {
            logger.error("Payment Mandate method does not match request: {} vs {}", 
                         paymentMandate.getPaymentMethodId(), request.getPaymentMethod());
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean verifySignature(Object mandate) {
        if (mandate == null) {
            return false;
        }
        
        if (mandate instanceof IntentMandate ||
            mandate instanceof CartMandate ||
            mandate instanceof PaymentMandate) {
            
            return signatureService.verifyCredential((VerifiableCredential) mandate);
        }
        
        logger.error("Unknown mandate type: {}", mandate.getClass().getName());
        return false;
    }
}