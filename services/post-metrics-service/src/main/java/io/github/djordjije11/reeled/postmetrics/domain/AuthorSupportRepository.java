package io.github.djordjije11.reeled.postmetrics.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.YearMonth;
import java.util.List;

/**
 * @author Djordjije Radovic
 */
public interface AuthorSupportRepository extends Repository<Author, Long> {

    @Query("""
            SELECT id
            FROM Author
            WHERE type = io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType.BUSINESS
                AND (analyticsMonthlyReportLastProcessedPeriod IS NULL OR analyticsMonthlyReportLastProcessedPeriod < :period)
            ORDER BY id
            LIMIT :limit""")
    List<Long> findAllIdsEligibleForAnalyticsMonthlyReport(YearMonth period, int limit);
}
