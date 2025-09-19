package io.github.vishalmysore.ap2java.credentials;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the subject of a verifiable credential.
 * The subject contains the identifier and claims about the entity.
 */
public class CredentialSubject {
    private String id;
    private Map<String, Object> claims;

    /**
     * Creates a new credential subject.
     *
     * @param id The identifier of the subject
     * @param claims Claims about the subject
     */
    public CredentialSubject(String id, Map<String, Object> claims) {
        this.id = id;
        this.claims = claims != null ? new HashMap<>(claims) : new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getClaims() {
        return new HashMap<>(claims);
    }

    public Object getClaim(String key) {
        return claims.get(key);
    }

    public void addClaim(String key, Object value) {
        claims.put(key, value);
    }
}