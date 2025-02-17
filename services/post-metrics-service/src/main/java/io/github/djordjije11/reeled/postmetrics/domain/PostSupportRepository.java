package io.github.djordjije11.reeled.postmetrics.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface PostSupportRepository extends Repository<Post, Long> {

    @Query("""
            SELECT new io.github.djordjije11.reeled.postmetrics.domain.PostProjection(
                        data.authorId,
                        data.category,
                        data.duration,
                        data.monetized)
            FROM Post
            WHERE id = :id""")
    Optional<PostProjection> findById(Long id);
}
