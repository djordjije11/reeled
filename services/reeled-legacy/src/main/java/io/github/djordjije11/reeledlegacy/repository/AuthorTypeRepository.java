package io.github.djordjije11.reeledlegacy.repository;

import io.github.djordjije11.reeledlegacy.model.AuthorType;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface AuthorTypeRepository extends Repository<AuthorType, Long> {

    Optional<AuthorType> findById(Long id);
}
