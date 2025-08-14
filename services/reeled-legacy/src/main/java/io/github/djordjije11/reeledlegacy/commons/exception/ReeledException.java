package io.github.djordjije11.reeledlegacy.commons.exception;

public class ReeledException extends RuntimeException {

    public ReeledException(String message) {
        super(message);
    }

    public ReeledException(String message, Throwable cause) {
        super(message, cause);
    }
}
