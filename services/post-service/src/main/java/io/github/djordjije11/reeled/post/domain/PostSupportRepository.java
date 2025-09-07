package io.github.djordjije11.reeled.post.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Djordjije Radovic
 */
public interface PostSupportRepository extends Repository<Post, Long> {

    List<Long> findAllIdsByAuthorIdAndDeletedIsFalse(Long authorId);

    @Query("SELECT id FROM Post WHERE deleted = TRUE AND deletedDate < :purgeAgeThreshold ORDER BY id LIMIT :limit")
    List<Long> findAllIdsEligibleForPurge(ZonedDateTime purgeAgeThreshold, int limit);
}
