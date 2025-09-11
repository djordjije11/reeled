package io.github.djordjije11.reeledlegacy.repository;

import io.github.djordjije11.reeledlegacy.model.Post;
import org.springframework.data.repository.Repository;

/**
 * @author Djordjije Radovic
 */
public interface PostRepository extends Repository<Post, Long> {

    void save(Post post);
}
