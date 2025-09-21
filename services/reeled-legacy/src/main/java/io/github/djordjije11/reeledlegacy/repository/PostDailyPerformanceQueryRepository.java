package io.github.djordjije11.reeledlegacy.repository;

import io.github.djordjije11.reeledlegacy.model.PostDailyMetricsProjection;
import io.github.djordjije11.reeledlegacy.model.PostDailyPerformance;
import io.github.djordjije11.reeledlegacy.model.PostDailyPerformanceKey;
import io.github.djordjije11.reeledlegacy.model.PostTotalMetricsProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author Djordjije Radovic
 */
public interface PostDailyPerformanceQueryRepository extends Repository<PostDailyPerformance, PostDailyPerformanceKey> {

    @Query("""
            SELECT new io.github.djordjije11.reeledlegacy.model.PostDailyMetricsProjection(
                pdp.key.date,
                SUM(pdp.searchAppearances),
                AVG(pdp.searchAppearances),
                SUM(pdp.views),
                AVG(pdp.views))
            FROM PostDailyPerformance pdp JOIN Post p ON pdp.key.postId = p.id
            WHERE p.author.id = :authorId
                AND pdp.key.date >= :dateFrom
                AND pdp.key.date < :dateTo
                AND ((:durationFrom) IS NULL OR p.duration >= :durationFrom)
                AND ((:durationTo) IS NULL OR p.duration < :durationTo)
                AND ((:categoryIds) IS NULL OR p.category.id IN :categoryIds)
                AND ((:monetized) IS NULL OR p.monetized = :monetized)
            GROUP BY pdp.key.date
            ORDER BY pdp.key.date""")
    List<PostDailyMetricsProjection> searchDailyMetrics(Long authorId,
                                                        LocalDate dateFrom,
                                                        LocalDate dateTo,
                                                        Duration durationFrom,
                                                        Duration durationTo,
                                                        Set<Long> categoryIds,
                                                        Boolean monetized);

    @Query("""
            SELECT new io.github.djordjije11.reeledlegacy.model.PostTotalMetricsProjection(
                SUM(pdp.searchAppearances),
                AVG(pdp.searchAppearances),
                SUM(pdp.views),
                AVG(pdp.views))
            FROM PostDailyPerformance pdp JOIN Post p ON pdp.key.postId = p.id
            WHERE p.author.id = :authorId
                AND pdp.key.date >= :dateFrom
                AND pdp.key.date < :dateTo""")
    PostTotalMetricsProjection searchTotalMetrics(Long authorId, LocalDate dateFrom, LocalDate dateTo);
}
