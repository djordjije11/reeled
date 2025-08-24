package io.github.djordjije11.reeledlegacy.repository;

import io.github.djordjije11.reeledlegacy.model.Author;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface AuthorRepository extends Repository<Author, Long> {

    void save(Author author);

    Optional<Author> findById(Long id);

    void delete(Author author);
}
