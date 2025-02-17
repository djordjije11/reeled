package io.github.djordjije11.reeled.post.query;

import io.github.djordjije11.reeled.post.domain.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface PostQueryRepository extends Repository<Post, Long> {

    @Query("""
            SELECT new io.github.djordjije11.reeled.post.query.PostProjection(
                        category,
                        description,
                        duration,
                        title,
                        videoUrl)
            FROM Post
            WHERE id = :id AND authorId = :authorId AND deleted = FALSE""")
    Optional<PostProjection> findByIdAndAuthorIdAndDeletedIsFalse(Long id, Long authorId);
}
