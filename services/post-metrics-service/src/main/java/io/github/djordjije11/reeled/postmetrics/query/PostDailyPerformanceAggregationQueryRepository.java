package io.github.djordjije11.reeled.postmetrics.query;

import io.github.djordjije11.reeled.postmetrics.domain.PostDailyPerformanceAggregation;
import io.github.djordjije11.reeled.postmetrics.domain.PostDailyPerformanceAggregationKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author Djordjije Radovic
 */
public interface PostDailyPerformanceAggregationQueryRepository extends Repository<PostDailyPerformanceAggregation, PostDailyPerformanceAggregationKey> {

    @Query("""
            SELECT new io.github.djordjije11.reeled.postmetrics.query.PostDailyMetricsProjection(
                key.date,
                SUM(performance.searchAppearances),
                AVG(performance.searchAppearances),
                SUM(performance.views),
                AVG(performance.views))
            FROM PostDailyPerformanceAggregation
            WHERE post.authorId = :authorId
                AND key.date >= :dateFrom
                AND key.date < :dateTo
                AND ((:durationFrom) IS NULL OR post.duration >= :durationFrom)
                AND ((:durationTo) IS NULL OR post.duration < :durationTo)
                AND ((:categoryKeys) IS NULL OR post.categoryKey IN :categoryKeys)
                AND ((:monetized) IS NULL OR post.monetized = :monetized)
            GROUP BY key.date
            ORDER BY key.date""")
    List<PostDailyMetricsProjection> searchDailyMetrics(Long authorId,
                                                        LocalDate dateFrom,
                                                        LocalDate dateTo,
                                                        Duration durationFrom,
                                                        Duration durationTo,
                                                        Set<String> categoryKeys,
                                                        Boolean monetized);

    @Query("""
            SELECT new io.github.djordjije11.reeled.postmetrics.query.PostTotalMetricsProjection(
                SUM(performance.searchAppearances),
                AVG(performance.searchAppearances),
                SUM(performance.views),
                AVG(performance.views))
            FROM PostDailyPerformanceAggregation
            WHERE post.authorId = :authorId
                AND key.date >= :dateFrom
                AND key.date < :dateTo""")
    PostTotalMetricsProjection searchTotalMetrics(Long authorId, LocalDate dateFrom, LocalDate dateTo);
}
