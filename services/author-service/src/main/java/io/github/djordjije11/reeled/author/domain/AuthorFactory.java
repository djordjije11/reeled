package io.github.djordjije11.reeled.author.domain;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class AuthorFactory {

    private final AuthorRepository authorRepository;

    public Author create(String name, AuthorType type, String bio, String imageUrl) {
        return new Author(authorRepository.nextId(), name, type, bio, imageUrl);
    }
}
