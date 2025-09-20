package io.github.djordjije11.reeledlegacy.repository;

import io.github.djordjije11.reeledlegacy.model.Post;
import io.github.djordjije11.reeledlegacy.model.PostProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface PostQueryRepository extends Repository<Post, Long> {

    @Query("""
            SELECT new io.github.djordjije11.reeledlegacy.model.PostProjection(
                        category.name,
                        description,
                        duration,
                        title,
                        videoUrl)
            FROM Post
            WHERE id = :id AND author.id = :authorId""")
    Optional<PostProjection> findByIdAndAuthorId(Long id, Long authorId);
}
