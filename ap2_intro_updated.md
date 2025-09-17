# AP2 Protocol: Agent Payments Protocol

## Overview

The Agent Payments Protocol (AP2) addresses a fundamental challenge in AI agent interactions: enabling secure, authorized financial transactions on behalf of users. This document explains how AP2 works in the context of our Java implementation.

## Core Challenges Addressed by AP2

AP2 solves several critical problems in agent-mediated transactions:

1. **Authorization**: Ensuring the user properly authorized an agent to make purchases.
2. **Authenticity**: Verifying that transactions reflect what the user actually intended.
3. **Accountability**: Creating an audit trail to trace responsibility if something goes wrong.

## AP2 Mandate Flow

The AP2 protocol implements a chain of authorization through three types of mandates:

### 1. Intent Mandate

When a user instructs an agent (e.g., "Buy running shoes under $100"), the agent creates an Intent Mandate, which is a verifiable credential (VC) that captures:

- The user's intention (purchase running shoes)
- Constraints (price under $100)
- Authorization scope (e.g., one-time vs. ongoing)

```java
// Example from our IntentMandate.java implementation
IntentMandate mandate = new IntentMandate(
    "intent-123",                 // Unique ID
    "user-agent-456",             // Requesting agent
    "merchant-789",               // Receiving agent/merchant
    new BigDecimal("100.00"),     // Maximum allowed amount
    true                          // Requires human approval
);
mandate.setAllowedCategories(Collections.singletonList("shoes"));
```

### 2. Cart Mandate

After finding options, the agent builds a cart and seeks user approval. The Cart Mandate is another VC that:

- References the original Intent Mandate
- Contains specific items and prices
- Is cryptographically signed by the user

```java
// Example from our CartMandate.java implementation
List<CartMandate.CartItem> items = new ArrayList<>();
items.add(new CartMandate.CartItem(
    "item-123",                   // Item ID
    "Nike Air Zoom Pegasus",      // Description
    new BigDecimal("95.00"),      // Price
    1                             // Quantity
));

CartMandate cartMandate = new CartMandate(
    "cart-456",                   // Unique ID 
    "user-agent-456",             // Requesting agent
    "merchant-789",               // Receiving agent/merchant
    "intent-123",                 // Parent Intent Mandate ID
    items,                        // List of items
    "USD",                        // Currency
    Instant.now().plusMinutes(30) // Expiration
);
```

### 3. Payment Mandate

With the cart approved, the agent executes payment. The Payment Mandate:

- References the Cart Mandate
- Includes payment details and methods
- Provides context to payment processors
- Completes the authorization chain

```java
// Example from our PaymentMandate.java implementation
Map<String, Object> paymentDetails = new HashMap<>();
paymentDetails.put("paymentMethodId", "card-123");

PaymentMandate paymentMandate = new PaymentMandate(
    "payment-789",                // Unique ID
    "user-agent-456",             // Requesting agent
    "merchant-789",               // Receiving agent/merchant
    "order-321",                  // Payment reference
    new BigDecimal("95.00"),      // Amount
    "USD",                        // Currency
    "cart-456",                   // Parent Cart Mandate ID
    paymentDetails,               // Payment details
    Instant.now().plusMinutes(15) // Expiration
);
```

## Mandate Verification

Our implementation includes a `DefaultMandateVerifier` that ensures:

1. All mandates are properly signed
2. No mandate has expired
3. Each mandate references its parent correctly
4. Transaction details match across mandates
5. Payment constraints are respected

```java
// Example verification from DefaultMandateVerifier.java
boolean isValid = mandateVerifier.verifyIntentMandate(intentMandate, paymentRequest);
if (isValid) {
    isValid = mandateVerifier.verifyCartMandate(cartMandate, intentMandate, paymentRequest);
    if (isValid) {
        isValid = mandateVerifier.verifyPaymentMandate(paymentMandate, cartMandate, paymentRequest);
    }
}
```

## Implementation Details

Our Java implementation follows these key principles:

- **Verifiable Credentials**: All mandates implement the VerifiableCredential interface
- **Cryptographic Security**: Signatures ensure mandate integrity and authenticity
- **Expiration Controls**: All mandates have built-in expiration for security
- **Validation Logic**: Comprehensive checks for mandate validity and consistency
- **Payment Processing**: Clean integration with various payment methods

## Usage Examples

The `SimplePaymentExample` and `AP2IntegrationExample` classes demonstrate how to:

1. Create payment-enabled agents
2. Generate and verify mandates
3. Process payment requests through the AP2 protocol
4. Handle authorization and capture flows
5. Integrate with the A2A (Agent-to-Agent) protocol

## Technical Architecture

The AP2Java implementation is built on:

- JSON for data representation
- Cryptographic signing for verifiable credentials
- Simple interfaces for payment processor integration
- Clean separation of mandate, payment, and verification concerns

## Security Considerations

Our implementation addresses:

- Expiration of credentials
- Proper chain of authority
- Payment amount verification
- Merchant identity verification
- User-approved constraints