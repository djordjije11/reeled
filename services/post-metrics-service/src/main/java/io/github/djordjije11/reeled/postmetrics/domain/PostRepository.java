package io.github.djordjije11.reeled.postmetrics.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface PostRepository extends Repository<Post, Long> {

    Optional<Post> findById(Long id);

    void save(Post post);

    void delete(Post post);
}
