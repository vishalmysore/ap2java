# AP2Java - Agent Payments Protocol for Java

A Java implementation of Google's Agent Payments Protocol (AP2) built on top of a2ajava.

## Overview

AP2Java provides a complete implementation of Google's Agent Payments Protocol (AP2) in Java, allowing Java applications to participate in agent-based payment ecosystems. The project extends the a2ajava library to support payment operations between agents.

## Alignment with Google's AP2 Samples

This implementation closely follows the patterns in Google's AP2 samples repository:

1. **Domain Models**: Similar to the Python and Android samples, this Java implementation defines core payment entities like Payment, PaymentRequest, and PaymentResponse.

2. **Payment Processing Flow**: Follows the same flow as Google's samples:
   - Payment creation
   - Authorization
   - Capture/settlement
   - Refund/cancellation
   - Status checking

3. **Agent Integration**: Extends a2ajava's AgentCard to add payment capabilities through PaymentEnabledAgentCard.

4. **Payment Processor Interface**: Provides a standardized interface (PaymentProcessor) that can be implemented for different payment providers.

5. **Message Processing**: Integrates with a2ajava's messaging system to process payment-related messages between agents.

## Key Features

- Complete Java implementation of the AP2 protocol
- Seamless integration with existing a2ajava applications
- Support for all AP2 payment operations
- Mandate system with Intent, Cart, and Payment mandates
- Verifiable credentials with cryptographic signatures
- Human-present and human-not-present payment flows
- Audit logging with tamper-evident records
- Sample implementations matching Google's AP2 samples
- Comprehensive test coverage

## Project Structure

```
ap2java/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── io/
│   │   │       └── github/
│   │   │           └── ap2java/
│   │   │               ├── client/     # Client implementation
│   │   │               ├── domain/     # Domain models
│   │   │               ├── payment/    # Payment handling
│   │   │               ├── server/     # Server implementation
│   │   │               ├── examples/   # Usage examples
│   │   │               ├── mandate/    # Mandate system
│   │   │               ├── credentials/ # Verifiable credentials
│   │   │               ├── security/   # Cryptographic signatures
│   │   │               └── audit/      # Audit logging
│   │   └── resources/  # Configuration and resource files
│   └── test/
│       └── java/       # Test classes
└── pom.xml            # Maven configuration
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher
- a2ajava library

### Building the Project

```bash
mvn clean install
```

### Running the Examples

```bash
# Simple payment example
mvn exec:java -Dexec.mainClass="io.github.ap2java.examples.SimplePaymentExample"

# Integration with a2ajava example
mvn exec:java -Dexec.mainClass="io.github.ap2java.examples.AP2IntegrationExample"
```

## Usage Examples

### Creating a Payment

```java
// Create payment-enabled agent card
PaymentEnabledAgentCard agentCard = new PaymentEnabledAgentCard();
// ... configure agent card ...

// Create payment processor
PaymentProcessor paymentProcessor = new SamplePaymentProcessor();

// Create AP2 client
AP2Client ap2Client = new AP2Client(paymentProcessor, agentCard);

// Create payment request
PaymentRequest paymentRequest = PaymentRequest.builder()
        .amount(new BigDecimal("50.00"))
        .currencyCode("USD")
        .requestingAgentId("agent-123")
        .receivingAgentId("merchant-456")
        .description("Service payment")
        .build();

// Create payment
PaymentResponse response = ap2Client.createPayment(paymentRequest).get();
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Google's AP2 protocol team for the specification
- a2ajava project for the foundation