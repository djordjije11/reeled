package io.github.djordjije11.reeled.postmetrics.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * @author Djordjije Radovic
 */
public interface PostDailyPerformanceAggregationSupportRepository extends Repository<PostDailyPerformanceAggregation, PostDailyPerformanceAggregationKey> {

    @Query("SELECT key FROM PostDailyPerformanceAggregation WHERE key.postId = :postId")
    List<PostDailyPerformanceAggregationKey> findAllKeysByPostId(Long postId);
}
