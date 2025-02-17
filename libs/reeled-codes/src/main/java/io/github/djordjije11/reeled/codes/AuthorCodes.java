package io.github.djordjije11.reeled.codes;

/**
 * @author Djordjije Radovic
 */
public final class AuthorCodes {

    private AuthorCodes() {
    }

    public enum AuthorType implements BaseEnum {
        PERSONAL("personal"),
        BUSINESS("business");

        AuthorType(String value) {
            this.value = value;
        }

        private final String value;

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
