package io.github.djordjije11.reeled.post.domain;

import io.github.djordjije11.reeled.commons.exception.NotFoundException;
import io.github.djordjije11.reeled.integration.internal.service.reference.rest.ReferenceServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class PostCategoryService {

    private final ReferenceServiceClient referenceServiceClient;

    void checkCategoryExists(String categoryKey) {
        try {
            referenceServiceClient.getPostCategory(categoryKey);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("Post category does not exist (key: %s)".formatted(categoryKey), e);
        }
    }
}
