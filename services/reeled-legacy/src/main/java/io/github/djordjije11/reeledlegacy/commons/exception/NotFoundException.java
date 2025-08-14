package io.github.djordjije11.reeledlegacy.commons.exception;

/**
 * @author Djordjije Radovic
 */
public class NotFoundException extends ReeledDomainException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
