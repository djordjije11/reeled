package io.github.djordjije11.reeled.post.domain;

import io.github.djordjije11.reeled.codes.PostCodes.PostCategory;
import io.github.djordjije11.reeled.commons.converter.BaseEnumToStringConverter;
import jakarta.persistence.Converter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class PostCodesAttributeConverters {

    @Converter(autoApply = true)
    static class PostCategoryConverter extends BaseEnumToStringConverter<PostCategory> {

        PostCategoryConverter() {
            super(PostCategory.class);
        }
    }
}
