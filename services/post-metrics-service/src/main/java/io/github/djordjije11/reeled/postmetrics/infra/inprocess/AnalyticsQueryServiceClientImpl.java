package io.github.djordjije11.reeled.postmetrics.infra.inprocess;

import io.github.djordjije11.reeled.postmetrics.domain.AnalyticsQueryServiceClient;
import io.github.djordjije11.reeled.postmetrics.domain.PostMonthlyMetricsProjection;
import io.github.djordjije11.reeled.postmetrics.query.PostDailyPerformanceAggregationQueryRepository;
import io.github.djordjije11.reeled.postmetrics.query.PostTotalMetricsProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class AnalyticsQueryServiceClientImpl implements AnalyticsQueryServiceClient {

    private final PostDailyPerformanceAggregationQueryRepository postDailyPerformanceAggregationQueryRepository;

    @Override
    public PostMonthlyMetricsProjection getPostMonthlyMetricsByAuthor(Long authorId, YearMonth period) {
        final PostTotalMetricsProjection postTotalMetricsProjection = postDailyPerformanceAggregationQueryRepository.searchTotalMetrics(authorId,
                period.atDay(1),
                period.plusMonths(1).atDay(1));

        return new PostMonthlyMetricsProjection(postTotalMetricsProjection.searchAppearancesSum(),
                postTotalMetricsProjection.searchAppearancesAvg(),
                postTotalMetricsProjection.viewsSum(),
                postTotalMetricsProjection.viewsAvg());
    }
}
