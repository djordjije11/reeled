package io.github.djordjije11.reeled.postmetrics.application;

import io.github.djordjije11.reeled.codes.PostCodes.PostCategory;
import io.github.djordjije11.reeled.postmetrics.query.PostDailyMetricsProjection;
import io.github.djordjije11.reeled.postmetrics.query.PostDailyPerformanceAggregationQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class PostMetricsQueryService {

    private final PostDailyPerformanceAggregationQueryRepository postDailyPerformanceAggregationQueryRepository;

    public List<PostDailyMetricsProjection> search(Long authorId,
                                                   LocalDate dateFrom,
                                                   LocalDate dateTo,
                                                   Duration durationFrom,
                                                   Duration durationTo,
                                                   Set<PostCategory> categories,
                                                   Boolean monetized) {
        Assert.notNull(authorId, "authorId must not be null");
        Assert.notNull(dateFrom, "dateFrom must not be null");
        Assert.notNull(dateTo, "dateTo must not be null");

        return postDailyPerformanceAggregationQueryRepository.searchDailyMetrics(authorId, dateFrom, dateTo, durationFrom, durationTo, categories, monetized);
    }
}
