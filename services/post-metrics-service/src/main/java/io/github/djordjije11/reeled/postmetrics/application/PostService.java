package io.github.djordjije11.reeled.postmetrics.application;

import io.github.djordjije11.reeled.postmetrics.domain.Post;
import io.github.djordjije11.reeled.postmetrics.domain.PostData;
import io.github.djordjije11.reeled.postmetrics.domain.PostRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.ZonedDateTime;

import static io.github.djordjije11.reeled.config.ResilienceConfiguration.DATA_ACCESS_RETRY_NAME;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void save(Long id, PostData data) {
        Assert.notNull(id, "id must not be null");

        logger.info("Saving post (id: {})...", id);

        postRepository.findById(id).ifPresentOrElse(post -> {
            post.update(data);
            postRepository.save(post);
        }, () -> postRepository.save(new Post(id, data)));

        logger.info("Post successfully saved (id: {})", id);
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void delete(Long id, ZonedDateTime deletedDate) {
        Assert.notNull(id, "id must not be null");

        logger.info("Deleting post (id: {})...", id);

        postRepository.findById(id).ifPresentOrElse(post -> {
            post.delete(deletedDate);
            postRepository.save(post);

            logger.info("Post successfully deleted (id: {})", id);
        }, () -> logger.warn("Cannot delete post, post does not exist (id: {})", id));
    }
}
