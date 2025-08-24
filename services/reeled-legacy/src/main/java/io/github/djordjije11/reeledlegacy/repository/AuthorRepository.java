package io.github.djordjije11.reeledlegacy.repository;

import io.github.djordjije11.reeledlegacy.model.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface AuthorRepository extends Repository<Author, Long> {

    void save(Author author);

    Optional<Author> findByIdAndDeletedIsFalse(Long id);

    Optional<Author> findById(Long id);

    void delete(Author author);

    @Query("SELECT id FROM Author WHERE deleted = TRUE AND deletedDate < :purgeAgeThreshold ORDER BY id LIMIT :limit")
    List<Long> findAllIdsEligibleForPurge(ZonedDateTime purgeAgeThreshold, int limit);
}
