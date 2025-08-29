package io.github.djordjije11.reeled.legacyconnector.domain;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import io.github.djordjije11.reeled.commons.converter.BaseEnumToStringConverter;
import jakarta.persistence.Converter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class LegacyConnectorCodesAttributeConverters {

    @Converter(autoApply = true)
    static class AuthorTypeConverter extends BaseEnumToStringConverter<AuthorType> {

        AuthorTypeConverter() {
            super(AuthorType.class);
        }
    }
}
