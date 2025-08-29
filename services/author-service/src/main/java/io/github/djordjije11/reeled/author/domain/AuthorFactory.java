package io.github.djordjije11.reeled.author.domain;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import io.github.djordjije11.reeled.integration.internal.service.legacyconnector.rest.AuthorCreateDto;
import io.github.djordjije11.reeled.integration.internal.service.legacyconnector.rest.LegacyConnectorServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class AuthorFactory {

    private final LegacyConnectorServiceClient legacyConnectorServiceClient;

    public Author create(Long id, String name, AuthorType type, String bio, String imageUrl, boolean legacy) {
        if (id == null) {
            final Long legacyAuthorId = legacyConnectorServiceClient.createAuthor(new AuthorCreateDto(name, type, bio, imageUrl));
            return new Author(legacyAuthorId, name, type, bio, imageUrl, legacy);
        } else {
            return new Author(id, name, type, bio, imageUrl, legacy);
        }
    }
}
