package io.github.djordjije11.reeledlegacy.repository;

import io.github.djordjije11.reeledlegacy.model.PostCategory;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface PostCategoryRepository extends Repository<PostCategory, Long> {

    Optional<PostCategory> findById(long id);
}
