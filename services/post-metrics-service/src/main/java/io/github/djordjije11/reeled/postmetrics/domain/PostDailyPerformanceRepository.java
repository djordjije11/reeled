package io.github.djordjije11.reeled.postmetrics.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface PostDailyPerformanceRepository extends Repository<PostDailyPerformance, PostDailyPerformanceKey> {

    Optional<PostDailyPerformance> findByKey(PostDailyPerformanceKey key);

    void save(PostDailyPerformance postDailyPerformance);

    void delete(PostDailyPerformance postDailyPerformance);
}
