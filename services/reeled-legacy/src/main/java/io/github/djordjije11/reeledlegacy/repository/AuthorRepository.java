package io.github.djordjije11.reeledlegacy.repository;

import io.github.djordjije11.reeledlegacy.model.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface AuthorRepository extends Repository<Author, Long> {

    void save(Author author);

    Optional<Author> findById(Long id);

    void delete(Author author);

    @Query("""
            SELECT id
            FROM Author
            WHERE type.name = 'business'
                AND (analyticsMonthlyReportLastProcessedPeriod IS NULL OR analyticsMonthlyReportLastProcessedPeriod < :period)
            ORDER BY id
            LIMIT :limit""")
    List<Long> findAllIdsEligibleForAnalyticsMonthlyReport(YearMonth period, int limit);
}
