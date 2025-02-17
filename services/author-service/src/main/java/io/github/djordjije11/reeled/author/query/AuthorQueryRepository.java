package io.github.djordjije11.reeled.author.query;

import io.github.djordjije11.reeled.author.domain.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface AuthorQueryRepository extends Repository<Author, Long> {

    @Query("""
            SELECT new io.github.djordjije11.reeled.author.query.AuthorProjection(name, type, bio, imageUrl)
            FROM Author
            WHERE id = :id AND deleted = FALSE""")
    Optional<AuthorProjection> findByIdAndDeletedIsFalse(Long id);
}
