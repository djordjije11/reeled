package io.github.djordjije11.reeled.shared.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface StoredEventRepository extends Repository<StoredEvent, Long> {

    Optional<StoredEvent> findById(Long id);

    void save(StoredEvent storedEvent);

    void delete(StoredEvent storedEvent);
}
