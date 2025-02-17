package io.github.djordjije11.reeled.postmetrics.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface AuthorRepository extends Repository<Author, Long> {

    Optional<Author> findById(Long id);

    void save(Author author);

    void delete(Author author);
}
