package io.github.djordjije11.reeled.legacyconnector.domain;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AuthorCodesLegacyLookupMappings {

    static final Map<Long, AuthorType> AUTHOR_TYPE_ID_AUTHOR_TYPE_MAP = Map.of(1001L, AuthorType.PERSONAL, 1002L, AuthorType.BUSINESS);

    static final Map<AuthorType, Long> AUTHOR_TYPE_AUTHOR_TYPE_ID_MAP = AUTHOR_TYPE_ID_AUTHOR_TYPE_MAP.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
}
