package io.github.djordjije11.reeled.post.domain;

import io.github.djordjije11.reeled.commons.exception.NotFoundException;
import io.github.djordjije11.reeled.integration.internal.service.author.rest.AuthorServiceClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class PostFactory {

    private static final Logger logger = LoggerFactory.getLogger(PostFactory.class);

    private final PostRepository postRepository;

    private final AuthorServiceClient authorServiceClient;

    private final PostCategoryService postCategoryService;

    public Post create(Long authorId, String categoryKey, String description, Duration duration, Boolean monetized, String title, String videoUrl) {
        checkAuthorExists(authorId);
        postCategoryService.checkCategoryExists(categoryKey);

        return new Post(postRepository.nextId(), authorId, categoryKey, description, duration, monetized, title, videoUrl);
    }

    private void checkAuthorExists(Long authorId) {
        try {
            authorServiceClient.get(authorId);
        } catch (HttpClientErrorException.NotFound e) {
            logger.error("Received NOT FOUND while fetching the author (id: {})", authorId);

            throw new NotFoundException("Author does not exist (id: %d)".formatted(authorId), e);
        }
    }
}
