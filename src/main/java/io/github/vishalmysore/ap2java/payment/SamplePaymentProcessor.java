package io.github.vishalmysore.ap2java.payment;

import io.github.vishalmysore.ap2java.domain.Payment;
import io.github.vishalmysore.ap2java.domain.PaymentRequest;
import io.github.vishalmysore.ap2java.domain.PaymentResponse;
import io.github.vishalmysore.ap2java.domain.PaymentStatus;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Sample implementation of the PaymentProcessor interface.
 */
@Slf4j
public class SamplePaymentProcessor implements PaymentProcessor {
    
    private final Map<String, Payment> paymentStore = new HashMap<>();
    
    @Override
    public CompletableFuture<PaymentResponse> createPayment(PaymentRequest paymentRequest) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Creating payment for request: {}", paymentRequest);
            
            // Create a new payment
            Payment payment = Payment.builder()
                    .id(UUID.randomUUID().toString())
                    .amount(paymentRequest.getAmount())
                    .currencyCode(paymentRequest.getCurrencyCode())
                    .requestingAgentId(paymentRequest.getRequestingAgentId())
                    .receivingAgentId(paymentRequest.getReceivingAgentId())
                    .description(paymentRequest.getDescription())
                    .metadata(paymentRequest.getMetadata())
                    .externalReference(paymentRequest.getExternalReference())
                    .status(PaymentStatus.CREATED)
                    .createdAt(Instant.now())
                    .build();
            
            // Store the payment
            paymentStore.put(payment.getId(), payment);
            
            // Return the response
            return PaymentResponse.builder()
                    .paymentId(payment.getId())
                    .status(payment.getStatus())
                    .createdAt(payment.getCreatedAt())
                    .authUrl("https://example.com/auth?paymentId=" + payment.getId())
                    .build();
        });
    }
    
    @Override
    public CompletableFuture<PaymentResponse> authorizePayment(String paymentId, String authToken) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Authorizing payment: {}", paymentId);
            
            Payment payment = paymentStore.get(paymentId);
            if (payment == null) {
                return PaymentResponse.builder()
                        .paymentId(paymentId)
                        .status(PaymentStatus.FAILED)
                        .errorCode("PAYMENT_NOT_FOUND")
                        .errorMessage("Payment not found")
                        .build();
            }
            
            // Simulate authorization
            payment.setStatus(PaymentStatus.AUTHORIZED);
            payment.setUpdatedAt(Instant.now());
            paymentStore.put(paymentId, payment);
            
            return PaymentResponse.builder()
                    .paymentId(payment.getId())
                    .status(payment.getStatus())
                    .createdAt(payment.getCreatedAt())
                    .updatedAt(payment.getUpdatedAt())
                    .build();
        });
    }
    
    @Override
    public CompletableFuture<PaymentResponse> capturePayment(String paymentId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Capturing payment: {}", paymentId);
            
            Payment payment = paymentStore.get(paymentId);
            if (payment == null) {
                return PaymentResponse.builder()
                        .paymentId(paymentId)
                        .status(PaymentStatus.FAILED)
                        .errorCode("PAYMENT_NOT_FOUND")
                        .errorMessage("Payment not found")
                        .build();
            }
            
            if (payment.getStatus() != PaymentStatus.AUTHORIZED) {
                return PaymentResponse.builder()
                        .paymentId(paymentId)
                        .status(PaymentStatus.FAILED)
                        .errorCode("PAYMENT_NOT_AUTHORIZED")
                        .errorMessage("Payment must be authorized before capture")
                        .build();
            }
            
            // Simulate capture
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setUpdatedAt(Instant.now());
            paymentStore.put(paymentId, payment);
            
            return PaymentResponse.builder()
                    .paymentId(payment.getId())
                    .status(payment.getStatus())
                    .createdAt(payment.getCreatedAt())
                    .updatedAt(payment.getUpdatedAt())
                    .receiptUrl("https://example.com/receipt?paymentId=" + payment.getId())
                    .build();
        });
    }
    
    @Override
    public CompletableFuture<PaymentResponse> cancelPayment(String paymentId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Cancelling payment: {}", paymentId);
            
            Payment payment = paymentStore.get(paymentId);
            if (payment == null) {
                return PaymentResponse.builder()
                        .paymentId(paymentId)
                        .status(PaymentStatus.FAILED)
                        .errorCode("PAYMENT_NOT_FOUND")
                        .errorMessage("Payment not found")
                        .build();
            }
            
            if (payment.getStatus() == PaymentStatus.COMPLETED) {
                return PaymentResponse.builder()
                        .paymentId(paymentId)
                        .status(PaymentStatus.FAILED)
                        .errorCode("PAYMENT_ALREADY_COMPLETED")
                        .errorMessage("Cannot cancel a completed payment")
                        .build();
            }
            
            // Simulate cancellation
            payment.setStatus(PaymentStatus.CANCELED);
            payment.setUpdatedAt(Instant.now());
            paymentStore.put(paymentId, payment);
            
            return PaymentResponse.builder()
                    .paymentId(payment.getId())
                    .status(payment.getStatus())
                    .createdAt(payment.getCreatedAt())
                    .updatedAt(payment.getUpdatedAt())
                    .build();
        });
    }
    
    @Override
    public CompletableFuture<PaymentResponse> refundPayment(String paymentId, String reason) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Refunding payment: {}", paymentId);
            
            Payment payment = paymentStore.get(paymentId);
            if (payment == null) {
                return PaymentResponse.builder()
                        .paymentId(paymentId)
                        .status(PaymentStatus.FAILED)
                        .errorCode("PAYMENT_NOT_FOUND")
                        .errorMessage("Payment not found")
                        .build();
            }
            
            if (payment.getStatus() != PaymentStatus.COMPLETED) {
                return PaymentResponse.builder()
                        .paymentId(paymentId)
                        .status(PaymentStatus.FAILED)
                        .errorCode("PAYMENT_NOT_COMPLETED")
                        .errorMessage("Only completed payments can be refunded")
                        .build();
            }
            
            // Simulate refund
            payment.setStatus(PaymentStatus.REFUNDED);
            payment.setUpdatedAt(Instant.now());
            
            // Add refund reason to metadata
            if (payment.getMetadata() == null) {
                payment.setMetadata(new HashMap<>());
            }
            payment.getMetadata().put("refundReason", reason);
            paymentStore.put(paymentId, payment);
            
            return PaymentResponse.builder()
                    .paymentId(payment.getId())
                    .status(payment.getStatus())
                    .createdAt(payment.getCreatedAt())
                    .updatedAt(payment.getUpdatedAt())
                    .build();
        });
    }
    
    @Override
    public CompletableFuture<Optional<Payment>> getPayment(String paymentId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Getting payment: {}", paymentId);
            return Optional.ofNullable(paymentStore.get(paymentId));
        });
    }
    
    @Override
    public CompletableFuture<PaymentResponse> checkPaymentStatus(String paymentId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Checking payment status: {}", paymentId);
            
            Payment payment = paymentStore.get(paymentId);
            if (payment == null) {
                return PaymentResponse.builder()
                        .paymentId(paymentId)
                        .status(PaymentStatus.FAILED)
                        .errorCode("PAYMENT_NOT_FOUND")
                        .errorMessage("Payment not found")
                        .build();
            }
            
            return PaymentResponse.builder()
                    .paymentId(payment.getId())
                    .status(payment.getStatus())
                    .createdAt(payment.getCreatedAt())
                    .updatedAt(payment.getUpdatedAt())
                    .build();
        });
    }
}