package io.github.ap2java.security;

import io.github.ap2java.credentials.VerifiableCredential;
import io.github.ap2java.credentials.CredentialProof;

import java.time.Instant;
import java.security.KeyPair;

/**
 * Service for handling cryptographic signatures and verification
 * in the AP2 protocol.
 */
public interface SignatureService {
    
    /**
     * Signs a verifiable credential using the specified key.
     * 
     * @param credential The credential to sign
     * @param keyId The identifier for the key to use for signing
     * @return The signed credential
     */
    VerifiableCredential signCredential(VerifiableCredential credential, String keyId);
    
    /**
     * Verifies the signature on a credential.
     * 
     * @param credential The credential to verify
     * @return true if the signature is valid, false otherwise
     */
    boolean verifyCredential(VerifiableCredential credential);
    
    /**
     * Generates a new key pair for signing and verification.
     * 
     * @param keyId The identifier for the new key pair
     * @return The generated key pair
     */
    KeyPair generateKeyPair(String keyId);
    
    /**
     * Creates a new proof for a credential.
     * 
     * @param keyId The identifier for the key to use
     * @param method The verification method to use
     * @param purpose The purpose of the proof
     * @return A new credential proof
     */
    CredentialProof createProof(String keyId, String method, String purpose);
}