package io.github.djordjije11.reeled.postmetrics.infra.web.rest;

import io.github.djordjije11.reeled.codes.PostCodes.PostCategory;
import io.github.djordjije11.reeled.commons.converter.StringToBaseEnumConverter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMetricsCodesAttributeConverters {

    @Component
    public static class PostCategoryConverter extends StringToBaseEnumConverter<PostCategory> {

        public PostCategoryConverter() {
            super(PostCategory.class);
        }
    }
}
