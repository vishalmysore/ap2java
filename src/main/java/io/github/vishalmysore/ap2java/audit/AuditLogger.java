package io.github.vishalmysore.ap2java.audit;

import java.time.Instant;
import java.util.Map;

/**
 * Interface for audit logging in the AP2 protocol.
 * Implementations should provide secure, tamper-evident logging
 * for all payment-related operations.
 */
public interface AuditLogger {
    
    /**
     * Logs an event with the specified details.
     * 
     * @param eventType The type of event being logged
     * @param description A description of the event
     * @param data Additional data associated with the event
     * @return The ID of the log entry
     */
    String logEvent(String eventType, String description, Map<String, Object> data);
    
    /**
     * Logs an event with signature information.
     * 
     * @param eventType The type of event being logged
     * @param description A description of the event
     * @param data Additional data associated with the event
     * @param signerId The ID of the entity that signed the event
     * @param signature The cryptographic signature of the event data
     * @return The ID of the log entry
     */
    String logSignedEvent(String eventType, String description, 
                         Map<String, Object> data, String signerId, String signature);
    
    /**
     * Retrieves the audit trail for a specific entity.
     * 
     * @param entityId The ID of the entity to retrieve logs for
     * @param startTime The start of the time range to retrieve logs from
     * @param endTime The end of the time range to retrieve logs from
     * @return A list of log entries
     */
    Iterable<AuditLogEntry> getAuditTrail(String entityId, Instant startTime, Instant endTime);
    
    /**
     * Represents an entry in the audit log.
     */
    interface AuditLogEntry {
        /**
         * Gets the ID of the log entry.
         * 
         * @return The log entry ID
         */
        String getId();
        
        /**
         * Gets the timestamp of when the log entry was created.
         * 
         * @return The timestamp
         */
        Instant getTimestamp();
        
        /**
         * Gets the type of event logged.
         * 
         * @return The event type
         */
        String getEventType();
        
        /**
         * Gets the description of the event.
         * 
         * @return The event description
         */
        String getDescription();
        
        /**
         * Gets the additional data associated with the event.
         * 
         * @return The event data
         */
        Map<String, Object> getData();
        
        /**
         * Gets the ID of the entity that signed the event, if any.
         * 
         * @return The signer ID, or null if the event is not signed
         */
        String getSignerId();
        
        /**
         * Gets the cryptographic signature of the event data, if any.
         * 
         * @return The signature, or null if the event is not signed
         */
        String getSignature();
        
        /**
         * Checks if the event has a valid signature.
         * 
         * @return true if the event has a valid signature, false otherwise
         */
        boolean hasValidSignature();
    }
}