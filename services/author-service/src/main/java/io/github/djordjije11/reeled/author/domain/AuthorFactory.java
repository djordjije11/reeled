package io.github.djordjije11.reeled.author.domain;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import io.github.djordjije11.reeled.codes.AuthorCodesLegacyLookupMappings;
import io.github.djordjije11.reeled.integration.external.legacy.rest.AuthorCreateDto;
import io.github.djordjije11.reeled.integration.external.legacy.rest.LegacyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class AuthorFactory {

    private final AuthorRepository authorRepository;

    private final LegacyClient legacyClient;

    public Author create(Long id, String name, AuthorType type, String bio, String imageUrl) {
        if (id == null) {
            final Long legacyAuthorId = legacyClient.createAuthor(new AuthorCreateDto(name,
                    AuthorCodesLegacyLookupMappings.AUTHOR_TYPE_AUTHOR_TYPE_ID_MAP.get(type),
                    bio,
                    imageUrl));
            return new Author(legacyAuthorId, name, type, bio, imageUrl);
        } else {
            return new Author(id, name, type, bio, imageUrl);
        }
    }
}
