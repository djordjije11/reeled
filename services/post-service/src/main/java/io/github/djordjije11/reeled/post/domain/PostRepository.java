package io.github.djordjije11.reeled.post.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface PostRepository extends Repository<Post, Long> {

    @Query(value = "SELECT NEXTVAL('p_post_id_seq')", nativeQuery = true)
    Long nextId();

    void save(Post post);

    Optional<Post> findByIdAndAuthorIdAndDeletedIsFalse(Long id, Long authorId);

    Optional<Post> findById(Long id);

    void delete(Post post);
}
