package io.github.djordjije11.reeled.reference.domain;

import org.springframework.data.repository.Repository;

/**
 * @author Djordjije Radovic
 */
public interface PostCategoryRepository extends Repository<PostCategory, Long> {

    boolean existsByKey(String key);

    void save(PostCategory postCategory);
}
