package io.github.ap2java.credentials;

import java.time.Instant;

/**
 * Base class for verifiable credentials in the AP2 protocol.
 * 
 * A verifiable credential is a tamper-evident credential with authorship that can be 
 * cryptographically verified. Verifiable credentials can be used to build trust in 
 * agent payment interactions.
 */
public abstract class VerifiableCredential {
    private String id;
    private String issuer;
    private String type;
    private Instant issuanceDate;
    private CredentialSubject subject;
    private CredentialProof proof;

    /**
     * Creates a new verifiable credential.
     *
     * @param id Unique identifier for the credential
     * @param issuer The entity that issued the credential
     * @param type The type of credential
     * @param issuanceDate When the credential was issued
     * @param subject The subject of the credential
     * @param proof Cryptographic proof for the credential
     */
    public VerifiableCredential(String id, String issuer, String type,
                               Instant issuanceDate, CredentialSubject subject,
                               CredentialProof proof) {
        this.id = id;
        this.issuer = issuer;
        this.type = type;
        this.issuanceDate = issuanceDate;
        this.subject = subject;
        this.proof = proof;
    }

    public String getId() {
        return id;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getType() {
        return type;
    }

    public Instant getIssuanceDate() {
        return issuanceDate;
    }

    public CredentialSubject getSubject() {
        return subject;
    }

    public CredentialProof getProof() {
        return proof;
    }

    public void setProof(CredentialProof proof) {
        this.proof = proof;
    }

    /**
     * Checks if this credential has a valid proof.
     * 
     * @return true if the credential has a proof, false otherwise
     */
    public boolean hasProof() {
        return proof != null;
    }
}