package io.github.djordjije11.reeled.postmetrics.query;

import io.github.djordjije11.reeled.postmetrics.domain.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface AuthorQueryRepository extends Repository<Author, Long> {

    @Query("""
            SELECT new io.github.djordjije11.reeled.postmetrics.query.AuthorAnalyticsEmailRecipientsProjection(analyticsEmailRecipients)
            FROM Author
            WHERE id = :id""")
    Optional<AuthorAnalyticsEmailRecipientsProjection> findAnalyticsEmailRecipientsById(Long id);
}
