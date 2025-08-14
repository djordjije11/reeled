package io.github.djordjije11.reeledlegacy.commons.exception;

/**
 * @author Djordjije Radovic
 */
public class ReeledDomainException extends ReeledException {

    public ReeledDomainException(String message) {
        super(message);
    }

    public ReeledDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
