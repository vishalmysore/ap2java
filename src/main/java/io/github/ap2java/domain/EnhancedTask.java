package io.github.ap2java.domain;

import io.github.vishalmysore.a2a.domain.Task;
import lombok.Getter;
import lombok.Setter;

/**
 * An enhanced version of the Task class from a2ajava that adds methods required by AP2.
 */
@Getter
@Setter
public class EnhancedTask extends Task {
    
    private boolean completed;
    private String error;
    private PaymentResponse result;
    
    /**
     * Sets whether the task is completed.
     * 
     * @param completed true if the task is completed, false otherwise
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    /**
     * Returns whether the task is completed.
     * 
     * @return true if the task is completed, false otherwise
     */
    public boolean isCompleted() {
        return completed;
    }
    
    /**
     * Sets an error message for the task.
     * 
     * @param error the error message
     */
    public void setError(String error) {
        this.error = error;
    }
    
    /**
     * Sets the result of the task.
     * 
     * @param result the payment response result
     */
    public void setResult(PaymentResponse result) {
        this.result = result;
    }
}