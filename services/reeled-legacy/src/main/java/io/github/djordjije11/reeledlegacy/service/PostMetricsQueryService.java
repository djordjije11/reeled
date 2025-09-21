package io.github.djordjije11.reeledlegacy.service;

import io.github.djordjije11.reeledlegacy.model.PostDailyMetricsProjection;
import io.github.djordjije11.reeledlegacy.repository.PostDailyPerformanceQueryRepository;
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

    private final PostDailyPerformanceQueryRepository postDailyPerformanceQueryRepository;

    public List<PostDailyMetricsProjection> search(Long authorId,
                                                   LocalDate dateFrom,
                                                   LocalDate dateTo,
                                                   Duration durationFrom,
                                                   Duration durationTo,
                                                   Set<Long> categoryIds,
                                                   Boolean monetized) {
        Assert.notNull(authorId, "authorId must not be null");
        Assert.notNull(dateFrom, "dateFrom must not be null");
        Assert.notNull(dateTo, "dateTo must not be null");

        return postDailyPerformanceQueryRepository.searchDailyMetrics(authorId, dateFrom, dateTo, durationFrom, durationTo, categoryIds, monetized);
    }
}
