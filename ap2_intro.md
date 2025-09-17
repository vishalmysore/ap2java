# AP2 Protocol: Agent Payments Protocol# AP2 Protocol: Agent Payments Protocol



## Overview## Overview



The Agent Payments Protocol (AP2) addresses a fundamental challenge in AI agent interactions: enabling secure, authorized financial transactions on behalf of users. This document explains how AP2 works in the context of our Java implementation.The Age### 2. Cart Mandate



## Core Challenges Addressed by AP2After finding options, the agent builds a cart and seek### 3. Payment Mandate



Traditional e-commerce assumes a human is directly making the purchase (clicking "buy" etc.). Agentic AI changes that assumption: agents might act on user behalf, possibly even with minimal human involvement. That raises new challenges:With the cart approved, the agent executes payment. The Payment Mandate:



1. **Authorization**: Ensuring the user properly authorized an agent to make purchases.- References the Cart Mandate

2. **Authenticity**: Verifying that transactions reflect what the user actually intended.- Includes payment details and methods

3. **Accountability**: Creating an audit trail to trace responsibility if something goes wrong.- Provides context to payment processors

- Completes the authorization chain

AP2 is meant to provide a standard, interoperable foundation so that multiple parties (agents, merchants, payments providers, regulators etc.) can rely on shared assumptions and tools.

```java

## AP2 Mandate Flow// Example from our PaymentMandate.java implementation

Map<String, Object> paymentDetails = new HashMap<>();

The AP2 protocol implements a chain of authorization through three types of mandates:paymentDetails.put("paymentMethodId", "card-123");



### 1. Intent MandatePaymentMandate paymentMandate = new PaymentMandate(

    "payment-789",                // Unique ID

When a user instructs an agent (e.g., "Buy running shoes under $100"), the agent creates an Intent Mandate, which is a verifiable credential (VC) that captures:    "user-agent-456",             // Requesting agent

    "merchant-789",               // Receiving agent/merchant

- The user's intention (purchase running shoes)    "order-321",                  // Payment reference

- Constraints (price under $100)    new BigDecimal("95.00"),      // Amount

- Authorization scope (e.g., one-time vs. ongoing)    "USD",                        // Currency

    "cart-456",                   // Parent Cart Mandate ID

```java    paymentDetails,               // Payment details

// Example from our IntentMandate.java implementation    Instant.now().plusMinutes(15) // Expiration

IntentMandate mandate = new IntentMandate();

    "intent-123",                 // Unique ID```

    "user-agent-456",             // Requesting agent

    "merchant-789",               // Receiving agent/merchant## Mandate Verification

    new BigDecimal("100.00"),     // Maximum allowed amount

    true                          // Requires human approvalOur implementation includes a `DefaultMandateVerifier` that ensures:

);

mandate.setAllowedCategories(Collections.singletonList("shoes"));1. All mandates are properly signed

```2. No mandate has expired

3. Each mandate references its parent correctly

### 2. Cart Mandate4. Transaction details match across mandates

5. Payment constraints are respected

After finding options, the agent builds a cart and seeks user approval. The Cart Mandate is another VC that:

```java

- References the original Intent Mandate// Example verification from DefaultMandateVerifier.java

- Contains specific items and pricesboolean isValid = mandateVerifier.verifyIntentMandate(intentMandate, paymentRequest);

- Is cryptographically signed by the userif (isValid) {

    isValid = mandateVerifier.verifyCartMandate(cartMandate, intentMandate, paymentRequest);

```java    if (isValid) {

// Example from our CartMandate.java implementation        isValid = mandateVerifier.verifyPaymentMandate(paymentMandate, cartMandate, paymentRequest);

List<CartMandate.CartItem> items = new ArrayList<>();    }

items.add(new CartMandate.CartItem(}

    "item-123",                   // Item ID```al. The Cart Mandate is another VC that:

    "Nike Air Zoom Pegasus",      // Description

    new BigDecimal("95.00"),      // Price- References the original Intent Mandate

    1                             // Quantity- Contains specific items and prices

));- Is cryptographically signed by the user



CartMandate cartMandate = new CartMandate(```java

    "cart-456",                   // Unique ID // Example from our CartMandate.java implementation

    "user-agent-456",             // Requesting agentList<CartMandate.CartItem> items = new ArrayList<>();

    "merchant-789",               // Receiving agent/merchantitems.add(new CartMandate.CartItem(

    "intent-123",                 // Parent Intent Mandate ID    "item-123",                   // Item ID

    items,                        // List of items    "Nike Air Zoom Pegasus",      // Description

    "USD",                        // Currency    new BigDecimal("95.00"),      // Price

    Instant.now().plusMinutes(30) // Expiration    1                             // Quantity

);));

```

CartMandate cartMandate = new CartMandate(

### 3. Payment Mandate    "cart-456",                   // Unique ID 

    "user-agent-456",             // Requesting agent

With the cart approved, the agent executes payment. The Payment Mandate:    "merchant-789",               // Receiving agent/merchant

    "intent-123",                 // Parent Intent Mandate ID

- References the Cart Mandate    items,                        // List of items

- Includes payment details and methods    "USD",                        // Currency

- Provides context to payment processors    Instant.now().plusMinutes(30) // Expiration

- Completes the authorization chain);

```AP2 Mandate Flow

```java

// Example from our PaymentMandate.java implementationThe AP2 protocol implements a chain of authorization through three types of mandates:

Map<String, Object> paymentDetails = new HashMap<>();

paymentDetails.put("paymentMethodId", "card-123");### 1. Intent Mandate



PaymentMandate paymentMandate = new PaymentMandate(When a user instructs an agent (e.g., "Buy running shoes under $100"), the agent creates an Intent Mandate, which is a verifiable credential (VC) that captures:

    "payment-789",                // Unique ID

    "user-agent-456",             // Requesting agent- The user's intention (purchase running shoes)

    "merchant-789",               // Receiving agent/merchant- Constraints (price under $100)

    "order-321",                  // Payment reference- Authorization scope (e.g., one-time vs. ongoing)

    new BigDecimal("95.00"),      // Amount

    "USD",                        // Currency```java

    "cart-456",                   // Parent Cart Mandate ID// Example from our IntentMandate.java implementation

    paymentDetails,               // Payment detailsIntentMandate mandate = new IntentMandate(

    Instant.now().plusMinutes(15) // Expiration    "intent-123",                 // Unique ID

);    "user-agent-456",             // Requesting agent

```    "merchant-789",               // Receiving agent/merchant

    new BigDecimal("100.00"),     // Maximum allowed amount

## Mandate Verification    true                          // Requires human approval

);

Our implementation includes a `DefaultMandateVerifier` that ensures:mandate.setAllowedCategories(Collections.singletonList("shoes"));

```ol (AP2) addresses a fundamental challenge in AI agent interactions: enabling secure, authorized financial transactions on behalf of users. This document explains how AP2 works in the context of our Java implementation.

1. All mandates are properly signed

2. No mandate has expired## Core Challenges Addressed by AP2

3. Each mandate references its parent correctly

4. Transaction details match across mandatesTraditional e-commerce assumes a human is directly making the purchase (clicking "buy" etc.). Agentic AI changes that assumption: agents might act on user behalf, possibly even with minimal human involvement. That raises new challenges:

5. Payment constraints are respected

1. **Authorization**: Ensuring the user properly authorized an agent to make purchases.

```java2. **Authenticity**: Verifying that transactions reflect what the user actually intended.

// Example verification from DefaultMandateVerifier.java3. **Accountability**: Creating an audit trail to trace responsibility if something goes wrong.

boolean isValid = mandateVerifier.verifyIntentMandate(intentMandate, paymentRequest);

if (isValid) {AP2 is meant to provide a standard, interoperable foundation so that multiple parties (agents, merchants, payments providers, regulators etc.) can rely on shared assumptions and tools.xample Walk-Through

    isValid = mandateVerifier.verifyCartMandate(cartMandate, intentMandate, paymentRequest);

    if (isValid) {Traditional e-commerce assumes a human is directly making the purchase (clicking “buy” etc.). Agentic AI changes that assumption: agents might act on user behalf, possibly even with minimal human involvement. That raises new challenges:

        isValid = mandateVerifier.verifyPaymentMandate(paymentMandate, cartMandate, paymentRequest);

    }Authorization: Was the user’s permission properly obtained for that purchase? Did they explicitly authorize the agent for that action? 

}Google Cloud

```+1



## Implementation DetailsAuthenticity: Does the request truly reflect what the user intended? I.e. did the agent misinterpret or go off-script in some way? 

Google Cloud

Our Java implementation follows these key principles:+1



- **Verifiable Credentials**: All mandates implement the VerifiableCredential interfaceAccountability: If something goes wrong (fraud, wrong product, incorrect amount etc.), how can it be traced, who is responsible, how can disputes be resolved? 

- **Cryptographic Security**: Signatures ensure mandate integrity and authenticityGoogle Cloud

- **Expiration Controls**: All mandates have built-in expiration for security+1

- **Validation Logic**: Comprehensive checks for mandate validity and consistency

- **Payment Processing**: Clean integration with various payment methodsAP2 is meant to provide a standard, interoperable foundation so that multiple parties (agents, merchants, payments providers, regulators etc.) can rely on shared assumptions and tools.



## Usage ExamplesFrom the Google / AP2 spec and press coverage:



The `SimplePaymentExample` and `AP2IntegrationExample` classes demonstrate how to:User sets an intent

Suppose you tell your agent: “Buy a pair of running shoes, size 10, under $100.” That creates an Intent Mandate: a verifiable credential (VC) capturing what you’ve authorized the agent to do, under what constraints. 

1. Create payment-enabled agentsVenturebeat

2. Generate and verify mandates+3

3. Process payment requests through the AP2 protocolapidog

4. Handle authorization and capture flows+3

5. Integrate with the A2A (Agent-to-Agent) protocolap2-protocol.org

+3

## Technical Architecture

Agent finds options and builds a cart

AP2 is designed to sit on top of A2A (Agent-to-Agent) which itself is JSON-RPC-based (with optional gRPC/HTTP transports).The agent then queries merchants, perhaps over APIs, to find shoes meeting the size & price constraints + shipping/tax. It composes a “cart” (items + price + any extra info). 

apidog

That means AP2 flows (Intent Mandate, Cart Mandate, Payment Mandate, etc.) are exchanged as JSON-RPC messages carrying verifiable credentials (VCs) as payloads. The VCs are JSON-LD documents (signed with cryptographic proofs), and the JSON-RPC envelope carries them between agents, merchants, and payment processors.+1



🔹 Example: Intent Mandate Request (JSON-RPC)Cart Mandate

```jsonWhen the user is present (or via an interactive flow), the agent asks you to approve the cart. You sign off via a Cart Mandate, which is also a VC. 

{ap2-protocol.org

  "jsonrpc": "2.0",+1

  "id": "req-123",

  "method": "ap2.createIntentMandate",Payment Execution

  "params": {With the cart mandated, the agent goes through payment with the merchant/payment network. A Payment Mandate may also be involved to tell the payment processor that this transaction is being done by an agent with user’s authorization and provide context (human present or not). 

    "userId": "user-456",ap2-protocol.org

    "intent": {

      "@context": "https://www.w3.org/2018/credentials/v1",Audit & Traceability

      "type": ["VerifiableCredential", "IntentMandate"],Throughout, the flow is cryptographically signed, and each mandate is part of a chain so one can verify: “Did the agent have authority? Did the user approve this cart? Are the items & prices as agreed?” This helps with fraud prevention, dispute resolution, accountability. 

      "issuer": "did:example:user-456",Google Cloud

      "credentialSubject": {+2

        "action": "purchase",ap2-protocol.org

        "item": "running shoes",+2

        "constraints": {

          "size": "10",Support for different payment types / rails

          "maxPrice": 100,AP2 supports credit/debit cards, stablecoins/crypto, real-time bank transfers, etc. It also has “x402” extension for crypto rails among others. 

          "currency": "USD"Venturebeat

        }+2

      },ap2-protocol.org

      "issuanceDate": "2025-09-17T14:30:00Z",+2

      "proof": {

        "type": "Ed25519Signature2020",How AP2 works — Example Walk-Through

        "created": "2025-09-17T14:30:00Z",

        "verificationMethod": "did:example:user-456#keys-1",From the Google / AP2 spec and press coverage:

        "jws": "eyJ..."

      }User sets an intent

    }Suppose you tell your agent: “Buy a pair of running shoes, size 10, under $100.” That creates an Intent Mandate: a verifiable credential (VC) capturing what you’ve authorized the agent to do, under what constraints. 

  }Venturebeat

}+3

```apidog

+3

🔹 Example: Cart Mandate Approval (JSON-RPC)ap2-protocol.org

```json+3

{

  "jsonrpc": "2.0",Agent finds options and builds a cart

  "id": "req-124",The agent then queries merchants, perhaps over APIs, to find shoes meeting the size & price constraints + shipping/tax. It composes a “cart” (items + price + any extra info). 

  "method": "ap2.signCartMandate",apidog

  "params": {+1

    "cart": {

      "items": [Cart Mandate

        {When the user is present (or via an interactive flow), the agent asks you to approve the cart. You sign off via a Cart Mandate, which is also a VC. 

          "name": "Nike Air Zoom Pegasus",ap2-protocol.org

          "size": "10",+1

          "price": 95,

          "currency": "USD"Payment Execution

        }With the cart mandated, the agent goes through payment with the merchant/payment network. A Payment Mandate may also be involved to tell the payment processor that this transaction is being done by an agent with user’s authorization and provide context (human present or not). 

      ],ap2-protocol.org

      "total": 95,

      "merchant": "did:example:merchant-789"Audit & Traceability

    },Throughout, the flow is cryptographically signed, and each mandate is part of a chain so one can verify: “Did the agent have authority? Did the user approve this cart? Are the items & prices as agreed?” This helps with fraud prevention, dispute resolution, accountability. 

    "mandate": {Google Cloud

      "@context": "https://www.w3.org/2018/credentials/v1",+2

      "type": ["VerifiableCredential", "CartMandate"],ap2-protocol.org

      "issuer": "did:example:user-456",+2

      "credentialSubject": {

        "cartId": "cart-321",Support for different payment types / rails

        "approved": trueAP2 supports credit/debit cards, stablecoins/crypto, real-time bank transfers, etc. It also has “x402” extension for crypto rails among others.

      },

      "proof": {## Implementation Details

        "type": "Ed25519Signature2020",

        "created": "2025-09-17T14:35:00Z",Our Java implementation follows these key principles:

        "verificationMethod": "did:example:user-456#keys-1",

        "jws": "eyJ..."- **Verifiable Credentials**: All mandates implement the VerifiableCredential interface

      }- **Cryptographic Security**: Signatures ensure mandate integrity and authenticity

    }- **Expiration Controls**: All mandates have built-in expiration for security

  }- **Validation Logic**: Comprehensive checks for mandate validity and consistency

}- **Payment Processing**: Clean integration with various payment methods

```

## Usage Examples

🔹 Example: Payment Execution Request (JSON-RPC)

```jsonThe `SimplePaymentExample` and `AP2IntegrationExample` classes demonstrate how to:

{

  "jsonrpc": "2.0",1. Create payment-enabled agents

  "id": "req-125",2. Generate and verify mandates

  "method": "ap2.executePayment",3. Process payment requests through the AP2 protocol

  "params": {4. Handle authorization and capture flows

    "paymentMandate": {5. Integrate with the A2A (Agent-to-Agent) protocol

      "@context": "https://www.w3.org/2018/credentials/v1",

      "type": ["VerifiableCredential", "PaymentMandate"],## Technical Architecture

      "issuer": "did:example:user-456",

      "credentialSubject": {AP2 is designed to sit on top of A2A (Agent-to-Agent) which itself is JSON-RPC-based (with optional gRPC/HTTP transports).

        "amount": 95,

        "currency": "USD",That means AP2 flows (Intent Mandate, Cart Mandate, Payment Mandate, etc.) are exchanged as JSON-RPC messages carrying verifiable credentials (VCs) as payloads. The VCs are JSON-LD documents (signed with cryptographic proofs), and the JSON-RPC envelope carries them between agents, merchants, and payment processors.

        "merchant": "did:example:merchant-789",

        "paymentMethod": "credit_card"🔹 Example: Intent Mandate Request (JSON-RPC)

      },{

      "proof": {  "jsonrpc": "2.0",

        "type": "Ed25519Signature2020",  "id": "req-123",

        "created": "2025-09-17T14:40:00Z",  "method": "ap2.createIntentMandate",

        "verificationMethod": "did:example:user-456#keys-1",  "params": {

        "jws": "eyJ..."    "userId": "user-456",

      }    "intent": {

    }      "@context": "https://www.w3.org/2018/credentials/v1",

  }      "type": ["VerifiableCredential", "IntentMandate"],

}      "issuer": "did:example:user-456",

```      "credentialSubject": {

        "action": "purchase",

🔹 Notes        "item": "running shoes",

        "constraints": {

- method field = AP2-specific extension methods (on top of A2A JSON-RPC).          "size": "10",

- VCs (Verifiable Credentials) = JSON-LD docs with cryptographic signatures.          "maxPrice": 100,

- Proofs ensure integrity and traceability.          "currency": "USD"

- Transport = JSON-RPC (can be over HTTP, WebSockets, or gRPC)        }

      },

## Security Considerations      "issuanceDate": "2025-09-17T14:30:00Z",

      "proof": {

Our implementation addresses:        "type": "Ed25519Signature2020",

        "created": "2025-09-17T14:30:00Z",

- Expiration of credentials        "verificationMethod": "did:example:user-456#keys-1",

- Proper chain of authority        "jws": "eyJ..."

- Payment amount verification      }

- Merchant identity verification    }

- User-approved constraints  }
}

🔹 Example: Cart Mandate Approval (JSON-RPC)
{
  "jsonrpc": "2.0",
  "id": "req-124",
  "method": "ap2.signCartMandate",
  "params": {
    "cart": {
      "items": [
        {
          "name": "Nike Air Zoom Pegasus",
          "size": "10",
          "price": 95,
          "currency": "USD"
        }
      ],
      "total": 95,
      "merchant": "did:example:merchant-789"
    },
    "mandate": {
      "@context": "https://www.w3.org/2018/credentials/v1",
      "type": ["VerifiableCredential", "CartMandate"],
      "issuer": "did:example:user-456",
      "credentialSubject": {
        "cartId": "cart-321",
        "approved": true
      },
      "proof": {
        "type": "Ed25519Signature2020",
        "created": "2025-09-17T14:35:00Z",
        "verificationMethod": "did:example:user-456#keys-1",
        "jws": "eyJ..."
      }
    }
  }
}

🔹 Example: Payment Execution Request (JSON-RPC)
{
  "jsonrpc": "2.0",
  "id": "req-125",
  "method": "ap2.executePayment",
  "params": {
    "paymentMandate": {
      "@context": "https://www.w3.org/2018/credentials/v1",
      "type": ["VerifiableCredential", "PaymentMandate"],
      "issuer": "did:example:user-456",
      "credentialSubject": {
        "amount": 95,
        "currency": "USD",
        "merchant": "did:example:merchant-789",
        "paymentMethod": "credit_card"
      },
      "proof": {
        "type": "Ed25519Signature2020",
        "created": "2025-09-17T14:40:00Z",
        "verificationMethod": "did:example:user-456#keys-1",
        "jws": "eyJ..."
      }
    }
  }
}

🔹 Notes

- method field = AP2-specific extension methods (on top of A2A JSON-RPC).
- VCs (Verifiable Credentials) = JSON-LD docs with cryptographic signatures.
- Proofs ensure integrity and traceability.
- Transport = JSON-RPC (can be over HTTP, WebSockets, or gRPC)

## Security Considerations

Our implementation addresses:

- Expiration of credentials
- Proper chain of authority
- Payment amount verification
- Merchant identity verification
- User-approved constraints