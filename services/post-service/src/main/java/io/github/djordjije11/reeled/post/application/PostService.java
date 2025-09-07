package io.github.djordjije11.reeled.post.application;

import io.github.djordjije11.reeled.commons.exception.NotFoundException;
import io.github.djordjije11.reeled.post.domain.Post;
import io.github.djordjije11.reeled.post.domain.PostCategoryService;
import io.github.djordjije11.reeled.post.domain.PostFactory;
import io.github.djordjije11.reeled.post.domain.PostPurged;
import io.github.djordjije11.reeled.post.domain.PostRepository;
import io.github.djordjije11.reeled.post.domain.PostSupportRepository;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.ClockProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Clock;
import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.List;

import static io.github.djordjije11.reeled.config.ResilienceConfiguration.DATA_ACCESS_RETRY_NAME;

/**
 * @author Djordjije Radovic
 */
@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private static final Period RETENTION_PERIOD = Period.ofDays(7);

    private static final int FETCH_SIZE = 5000;

    private final PostFactory postFactory;

    private final PostRepository postRepository;

    private final PostSupportRepository postSupportRepository;

    private final PostCategoryService postCategoryService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final io.github.resilience4j.retry.Retry dataAccessRetry;

    private final Clock clock;

    public PostService(PostFactory postFactory,
                       PostRepository postRepository,
                       PostSupportRepository postSupportRepository,
                       PostCategoryService postCategoryService,
                       ApplicationEventPublisher applicationEventPublisher,
                       RetryRegistry retryRegistry,
                       ClockProvider clockProvider) {
        this.postFactory = postFactory;
        this.postRepository = postRepository;
        this.postSupportRepository = postSupportRepository;
        this.postCategoryService = postCategoryService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.dataAccessRetry = retryRegistry.retry(DATA_ACCESS_RETRY_NAME);
        this.clock = clockProvider.getClock();
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public Long create(Long authorId, String categoryKey, String description, Duration duration, Boolean monetized, String title, String videoUrl) {
        logger.info("Creating a post (authorId: {}, title: {})...", authorId, title);

        final Post post = postFactory.create(authorId, categoryKey, description, duration, monetized, title, videoUrl);

        postRepository.save(post);

        logger.info("Post successfully created (id: {}, authorId: {}, title: {})", post.getId(), authorId, title);

        return post.getId();
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void update(Long id, Long authorId, String categoryKey, String description, String title) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(authorId, "authorId must not be null");

        logger.info("Updating a post (id: {}, authorId: {})...", id, authorId);

        final Post post = getPost(id, authorId);
        post.update(categoryKey, description, title, postCategoryService);

        postRepository.save(post);

        logger.info("Post successfully updated (id: {}, authorId: {})", id, authorId);
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void delete(Long id, Long authorId) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(authorId, "authorId must not be null");

        logger.info("Deleting a post (id: {}, authorId: {})...", id, authorId);

        final Post post = getPost(id, authorId);
        post.delete(clock);

        postRepository.save(post);

        logger.info("Post successfully deleted (id: {}, authorId: {})", id, authorId);
    }

    public void purge() {
        logger.info("Purging posts...");

        final ZonedDateTime purgeAgeThreshold = ZonedDateTime.now(clock).minus(RETENTION_PERIOD);

        List<Long> ids;
        do {
            ids = postSupportRepository.findAllIdsEligibleForPurge(purgeAgeThreshold, FETCH_SIZE);
            ids.forEach(id -> {
                try {
                    dataAccessRetry.executeRunnable(() -> purge(id));
                } catch (RuntimeException e) {
                    logger.error("Error occurred while purging a post (id: {})", id, e);
                }
            });
        } while (ids.size() == FETCH_SIZE);

        logger.info("Posts successfully purged");
    }

    private void purge(Long id) {
        logger.info("Purging a post (id: {})...", id);

        final Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post does not exist (id: %d)".formatted(id)));

        applicationEventPublisher.publishEvent(new PostPurged(id));

        postRepository.delete(post);

        logger.info("Post successfully purged (id: {})", id);
    }

    private Post getPost(Long id, Long authorId) {
        return postRepository.findByIdAndAuthorIdAndDeletedIsFalse(id, authorId)
                .orElseThrow(() -> new NotFoundException("Post does not exist (id: %d)".formatted(id)));
    }
}
