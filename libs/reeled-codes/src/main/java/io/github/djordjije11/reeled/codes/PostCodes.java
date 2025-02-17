package io.github.djordjije11.reeled.codes;

/**
 * @author Djordjije Radovic
 */
public final class PostCodes {

    private PostCodes() {
    }

    public enum PostCategory implements BaseEnum {
        COMEDY("comedy"),
        EDUCATION("education"),
        ENTERTAINMENT("entertainment"),
        FILM("film"),
        GAMING("gaming"),
        MUSIC("music"),
        NEWS("news");

        PostCategory(String value) {
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
