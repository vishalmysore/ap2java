package io.github.ap2java.credentials;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents cryptographic proof of a verifiable credential.
 * This contains the signature, verification method, and other proof attributes.
 */
public class CredentialProof {
    private String type;
    private Instant created;
    private String verificationMethod;
    private String proofPurpose;
    private String signature;
    private Map<String, Object> additionalAttributes;

    /**
     * Creates a new credential proof.
     *
     * @param type The type of the proof (e.g., "Ed25519Signature2020")
     * @param created When the proof was created
     * @param verificationMethod Reference to the public key used for verification
     * @param proofPurpose Purpose of the proof (e.g., "authentication")
     * @param signature The cryptographic signature value
     */
    public CredentialProof(String type, Instant created, String verificationMethod,
                           String proofPurpose, String signature) {
        this.type = type;
        this.created = created;
        this.verificationMethod = verificationMethod;
        this.proofPurpose = proofPurpose;
        this.signature = signature;
        this.additionalAttributes = new HashMap<>();
    }

    public String getType() {
        return type;
    }

    public Instant getCreated() {
        return created;
    }

    public String getVerificationMethod() {
        return verificationMethod;
    }

    public String getProofPurpose() {
        return proofPurpose;
    }

    public String getSignature() {
        return signature;
    }

    public Map<String, Object> getAdditionalAttributes() {
        return new HashMap<>(additionalAttributes);
    }

    public void addAttribute(String key, Object value) {
        additionalAttributes.put(key, value);
    }
}