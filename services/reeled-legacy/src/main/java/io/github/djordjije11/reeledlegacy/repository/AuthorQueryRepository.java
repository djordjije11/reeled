package io.github.djordjije11.reeledlegacy.repository;

import io.github.djordjije11.reeledlegacy.model.Author;
import io.github.djordjije11.reeledlegacy.model.AuthorProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface AuthorQueryRepository extends Repository<Author, Long> {

    @Query("""
            SELECT new io.github.djordjije11.reeledlegacy.model.AuthorProjection(name, type.name, bio, imageUrl)
            FROM Author
            WHERE id = :id""")
    Optional<AuthorProjection> findById(Long id);

    @Query("""
            SELECT a
            FROM Author a LEFT JOIN FETCH a.analyticsEmailRecipients
            WHERE a.id = :id""")
    Optional<Author> findAnalyticsEmailRecipientsById(Long id);
}
