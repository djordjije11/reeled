package io.github.djordjije11.reeled.postmetrics.infra.messaging;

import io.github.djordjije11.reeled.postmetrics.application.PostDailyPerformanceAggregationService;
import io.github.djordjije11.reeled.postmetrics.domain.PostDailyPerformanceAggregationKey;
import io.github.djordjije11.reeled.postmetrics.domain.PostDailyPerformanceUpserted;
import io.github.djordjije11.reeled.postmetrics.domain.PostUpserted;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class PostMetricsInternalEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(PostMetricsInternalEventHandler.class);

    private final PostDailyPerformanceAggregationService postDailyPerformanceAggregationService;

    @Async
    @TransactionalEventListener
    public void onPostDailyPerformanceUpserted(PostDailyPerformanceUpserted event) {
        logger.info("Handling internal post daily performance upserted event (postId: {}, date: {})...", event.postId(), event.date());

        postDailyPerformanceAggregationService.aggregatePostDailyPerformance(new PostDailyPerformanceAggregationKey(event.postId(), event.date()));

        logger.info("Internal post daily performance upserted event successfully handled (postId: {}, date: {})", event.postId(), event.date());
    }

    @Async
    @TransactionalEventListener
    public void onPostUpserted(PostUpserted event) {
        logger.info("Handling internal post upserted event (id: {})...", event.id());

        postDailyPerformanceAggregationService.updatePost(event.id());

        logger.info("Internal post upserted event successfully handled (id: {})", event.id());
    }
}
