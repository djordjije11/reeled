package io.github.djordjije11.reeled.postmetrics.domain;

import java.time.YearMonth;

/**
 * @author Djordjije Radovic
 */
public interface AnalyticsQueryServiceClient {

    PostMonthlyMetricsProjection getPostMonthlyMetricsByAuthor(Long authorId, YearMonth period);
}
