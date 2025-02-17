package io.github.djordjije11.reeled.author.infra.web.rest;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import io.github.djordjije11.reeled.commons.converter.StringToBaseEnumConverter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorCodesAttributeConverters {

    @Component
    public static class AuthorTypeConverter extends StringToBaseEnumConverter<AuthorType> {

        public AuthorTypeConverter() {
            super(AuthorType.class);
        }
    }
}
