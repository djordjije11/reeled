package io.github.djordjije11.reeled.codes;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Djordjije Radovic
 */
// TODO: REMOVE THIS
public class AuthorCodesLegacyLookupMappings {

    public static final Map<Long, AuthorType> AUTHOR_TYPE_ID_AUTHOR_TYPE_MAP = Map.of(1001L, AuthorType.PERSONAL, 1002L, AuthorType.BUSINESS);

    public static final Map<AuthorType, Long> AUTHOR_TYPE_AUTHOR_TYPE_ID_MAP = AUTHOR_TYPE_ID_AUTHOR_TYPE_MAP.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

    private AuthorCodesLegacyLookupMappings() {
    }
}
