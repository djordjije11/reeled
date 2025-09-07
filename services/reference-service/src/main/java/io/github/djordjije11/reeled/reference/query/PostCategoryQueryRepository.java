package io.github.djordjije11.reeled.reference.query;

import io.github.djordjije11.reeled.reference.domain.PostCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface PostCategoryQueryRepository extends Repository<PostCategory, Long> {

    @Query("SELECT new io.github.djordjije11.reeled.reference.query.PostCategoryProjection(key) FROM PostCategory")
    List<PostCategoryProjection> findAll();

    @Query("SELECT new io.github.djordjije11.reeled.reference.query.PostCategoryProjection(key) FROM PostCategory WHERE key = :key")
    Optional<PostCategoryProjection> findByKey(String key);
}
