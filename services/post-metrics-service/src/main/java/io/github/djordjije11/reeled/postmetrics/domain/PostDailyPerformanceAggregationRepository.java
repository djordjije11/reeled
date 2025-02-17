package io.github.djordjije11.reeled.postmetrics.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface PostDailyPerformanceAggregationRepository extends Repository<PostDailyPerformanceAggregation, PostDailyPerformanceAggregationKey> {

    Optional<PostDailyPerformanceAggregation> findByKey(PostDailyPerformanceAggregationKey key);

    void save(PostDailyPerformanceAggregation postDailyPerformanceAggregation);
}
