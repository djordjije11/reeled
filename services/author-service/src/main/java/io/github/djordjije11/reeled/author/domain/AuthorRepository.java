package io.github.djordjije11.reeled.author.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface AuthorRepository extends Repository<Author, Long> {

    void save(Author author);

    Optional<Author> findByIdAndDeletedIsFalse(Long id);

    Optional<Author> findById(Long id);

    void delete(Author author);
}
