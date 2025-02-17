package io.github.djordjije11.reeled.author.domain;

import io.github.djordjije11.reeled.author.event.AuthorDeleted;
import io.github.djordjije11.reeled.author.event.AuthorUpserted;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static io.github.djordjije11.reeled.commons.lang.MappingUtils.mapToBaseEnumValue;
import static io.github.djordjije11.reeled.commons.lang.MappingUtils.mapToEpochMilli;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AuthorEventMapper {

    static AuthorUpserted mapToAuthorUpserted(Author author) {
        return AuthorUpserted.newBuilder()
                .setBio(author.getBio())
                .setId(author.getId())
                .setImageUrl(author.getImageUrl())
                .setName(author.getName())
                .setType(mapToBaseEnumValue(author.getType()))
                .build();
    }

    static AuthorDeleted mapToAuthorDeleted(Author author) {
        return AuthorDeleted.newBuilder().setId(author.getId()).setDeletedDate(mapToEpochMilli(author.getDeletedDate())).build();
    }
}
