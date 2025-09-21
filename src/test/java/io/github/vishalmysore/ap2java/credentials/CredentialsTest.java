package io.github.vishalmysore.ap2java.credentials;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class CredentialsTest {

    @Test
    void testCredentialProof() {
        // Test creating a credential proof
        String type = "Ed25519Signature2020";
        Instant created = Instant.now();
        String verificationMethod = "did:example:123#key-1";
        String proofPurpose = "authentication";
        String signature = "z4oey5q23M...";

        CredentialProof proof = new CredentialProof(
            type,
            created,
            verificationMethod,
            proofPurpose,
            signature
        );

        // Test getters
        assertEquals(type, proof.getType());
        assertEquals(created, proof.getCreated());
        assertEquals(verificationMethod, proof.getVerificationMethod());
        assertEquals(proofPurpose, proof.getProofPurpose());
        assertEquals(signature, proof.getSignature());

        // Test additional attributes
        Map<String, Object> additionalAttrs = proof.getAdditionalAttributes();
        assertNotNull(additionalAttrs);

        // Test adding an attribute
        proof.addAttribute("domain", "example.com");
        assertEquals("example.com", proof.getAdditionalAttributes().get("domain"));
    }

    @Test
    void testCredentialSubject() {
        // Test creating a credential subject
        String id = "did:example:subject123";
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", "John Doe");
        claims.put("age", 30);

        CredentialSubject subject = new CredentialSubject(id, claims);

        // Test getters
        assertEquals(id, subject.getId());
        assertEquals(claims, subject.getClaims());
        assertEquals("John Doe", subject.getClaim("name"));
        assertEquals(30, subject.getClaim("age"));

        // Test adding a claim
        subject.addClaim("country", "USA");
        assertEquals("USA", subject.getClaim("country"));
    }

    @Test
    void testVerifiableCredential() {
        // Create credential components
        String id = "http://example.com/credentials/123";
        String issuer = "did:example:issuer456";
        String type = "VerifiableCredential";
        Instant issuanceDate = Instant.now();
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", "John Doe");
        claims.put("age", 30);
        CredentialSubject subject = new CredentialSubject("did:example:subject123", claims);

        CredentialProof proof = new CredentialProof(
            "Ed25519Signature2020",
            Instant.now(),
            "did:example:issuer456#key-1",
            "assertionMethod",
            "z4oey5q23M..."
        );

        // Create credential using a concrete implementation of the abstract class
        VerifiableCredential credential = new TestVerifiableCredential(
            id,
            issuer,
            type,
            issuanceDate,
            subject,
            proof
        );

        // Test getters
        assertEquals(id, credential.getId());
        assertEquals(issuer, credential.getIssuer());
        assertEquals(type, credential.getType());
        assertEquals(issuanceDate, credential.getIssuanceDate());
        assertEquals(subject, credential.getSubject());
        assertEquals(proof, credential.getProof());

        // Test hasProof
        assertTrue(credential.hasProof());

        // Test setting a new proof
        CredentialProof newProof = new CredentialProof(
            "Ed25519Signature2020",
            Instant.now(),
            "did:example:issuer456#key-2",
            "assertionMethod",
            "newSignature123"
        );
        
        credential.setProof(newProof);
        assertEquals(newProof, credential.getProof());
    }
    
    // Concrete implementation of VerifiableCredential for testing
    private static class TestVerifiableCredential extends VerifiableCredential {
        public TestVerifiableCredential(String id, String issuer, String type,
                                      Instant issuanceDate, CredentialSubject subject,
                                      CredentialProof proof) {
            super(id, issuer, type, issuanceDate, subject, proof);
        }
    }
}