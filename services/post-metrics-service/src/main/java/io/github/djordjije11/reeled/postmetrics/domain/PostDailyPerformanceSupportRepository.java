package io.github.djordjije11.reeled.postmetrics.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface PostDailyPerformanceSupportRepository extends Repository<PostDailyPerformance, PostDailyPerformanceKey> {

    @Query("""
            SELECT new io.github.djordjije11.reeled.postmetrics.domain.PostDailyPerformanceProjection(searchAppearances, views)
            FROM PostDailyPerformance
            WHERE key = :key""")
    Optional<PostDailyPerformanceProjection> findByKey(PostDailyPerformanceKey key);
}
