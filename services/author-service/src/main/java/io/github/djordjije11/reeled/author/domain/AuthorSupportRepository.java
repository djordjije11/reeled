package io.github.djordjije11.reeled.author.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Djordjije Radovic
 */
public interface AuthorSupportRepository extends Repository<Author, Long> {

    @Query("SELECT id FROM Author WHERE deleted = TRUE AND deletedDate < :purgeAgeThreshold ORDER BY id LIMIT :limit")
    List<Long> findAllIdsEligibleForPurge(ZonedDateTime purgeAgeThreshold, int limit);
}
