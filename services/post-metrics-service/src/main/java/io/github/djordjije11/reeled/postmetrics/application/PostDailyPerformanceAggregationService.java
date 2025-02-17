package io.github.djordjije11.reeled.postmetrics.application;

import io.github.djordjije11.reeled.commons.exception.NotFoundException;
import io.github.djordjije11.reeled.postmetrics.domain.PostDailyPerformanceAggregation;
import io.github.djordjije11.reeled.postmetrics.domain.PostDailyPerformanceAggregationKey;
import io.github.djordjije11.reeled.postmetrics.domain.PostDailyPerformanceAggregationRepository;
import io.github.djordjije11.reeled.postmetrics.domain.PostDailyPerformanceAggregationSupportRepository;
import io.github.djordjije11.reeled.postmetrics.domain.PostDailyPerformanceSupportRepository;
import io.github.djordjije11.reeled.postmetrics.domain.PostSupportRepository;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static io.github.djordjije11.reeled.config.ResilienceConfiguration.DATA_ACCESS_RETRY_NAME;

/**
 * @author Djordjije Radovic
 */
@Service
public class PostDailyPerformanceAggregationService {

    private static final Logger logger = LoggerFactory.getLogger(PostDailyPerformanceAggregationService.class);

    private final PostDailyPerformanceAggregationRepository postDailyPerformanceAggregationRepository;

    private final PostDailyPerformanceAggregationSupportRepository postDailyPerformanceAggregationSupportRepository;

    private final PostDailyPerformanceSupportRepository postDailyPerformanceSupportRepository;

    private final PostSupportRepository postSupportRepository;

    private final io.github.resilience4j.retry.Retry dataAccessRetry;

    public PostDailyPerformanceAggregationService(PostDailyPerformanceAggregationRepository postDailyPerformanceAggregationRepository,
                                                  PostDailyPerformanceAggregationSupportRepository postDailyPerformanceAggregationSupportRepository,
                                                  PostDailyPerformanceSupportRepository postDailyPerformanceSupportRepository,
                                                  PostSupportRepository postSupportRepository,
                                                  RetryRegistry retryRegistry) {
        this.postDailyPerformanceAggregationRepository = postDailyPerformanceAggregationRepository;
        this.postDailyPerformanceAggregationSupportRepository = postDailyPerformanceAggregationSupportRepository;
        this.postDailyPerformanceSupportRepository = postDailyPerformanceSupportRepository;
        this.postSupportRepository = postSupportRepository;
        this.dataAccessRetry = retryRegistry.retry(DATA_ACCESS_RETRY_NAME);
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void aggregatePostDailyPerformance(PostDailyPerformanceAggregationKey key) {
        logger.info("Aggregating post daily performance (key: {})...", key);

        postDailyPerformanceAggregationRepository.findByKey(key).ifPresentOrElse(postDailyPerformanceAggregation -> {
                    postDailyPerformanceAggregation.update(postDailyPerformanceSupportRepository);
                    postDailyPerformanceAggregationRepository.save(postDailyPerformanceAggregation);
                },
                () -> postDailyPerformanceAggregationRepository.save(new PostDailyPerformanceAggregation(key,
                        postDailyPerformanceSupportRepository,
                        postSupportRepository)));

        logger.info("Post daily performance successfully aggregated (key: {})", key);
    }

    public void updatePost(Long id) {
        logger.info("Updating post daily performance aggregations post (id: {})...", id);

        postDailyPerformanceAggregationSupportRepository.findAllKeysByPostId(id).forEach(key -> {
            try {
                dataAccessRetry.executeRunnable(() -> updatePost(key));
            } catch (RuntimeException e) {
                logger.error("Failed to update post daily performance aggregation post (key: {})", key, e);
            }
        });

        logger.info("Post daily performance aggregations post successfully updated (id: {})", id);
    }

    private void updatePost(PostDailyPerformanceAggregationKey key) {
        logger.info("Updating post daily performance aggregation post (key: {})...", key);

        final PostDailyPerformanceAggregation postDailyPerformanceAggregation = postDailyPerformanceAggregationRepository.findByKey(key)
                .orElseThrow(() -> new NotFoundException("Post daily performance aggregation does not exist (key: %s)".formatted(key)));

        postDailyPerformanceAggregation.update(postSupportRepository);

        postDailyPerformanceAggregationRepository.save(postDailyPerformanceAggregation);

        logger.info("Post daily performance aggregation post successfully updated (key: {})", key);
    }
}
