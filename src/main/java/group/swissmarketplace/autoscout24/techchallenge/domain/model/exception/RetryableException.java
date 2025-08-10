package group.swissmarketplace.autoscout24.techchallenge.domain.model.exception;

/**
 * Exception that indicates a retryable operation failure.
 * This exception can be used to signal that an operation should be retried.
 */
public class RetryableException extends RuntimeException {
    public RetryableException(Throwable cause) {
        super(cause);
    }
}
