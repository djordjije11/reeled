package io.github.djordjije11.reeledlegacy.repository;

import io.github.djordjije11.reeledlegacy.model.PostDailyPerformance;
import io.github.djordjije11.reeledlegacy.model.PostDailyPerformanceKey;
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
