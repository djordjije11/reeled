package io.github.djordjije11.reeledlegacy.repository;

import io.github.djordjije11.reeledlegacy.model.Post;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface PostRepository extends Repository<Post, Long> {

    Optional<Post> findByIdAndAuthorId(Long id, Long authorId);

    void save(Post post);

    void delete(Post post);
}
