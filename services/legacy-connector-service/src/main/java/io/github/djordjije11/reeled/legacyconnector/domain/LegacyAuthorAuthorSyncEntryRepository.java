package io.github.djordjije11.reeled.legacyconnector.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface LegacyAuthorAuthorSyncEntryRepository extends Repository<LegacyAuthorAuthorSyncEntry, Long> {

    Optional<LegacyAuthorAuthorSyncEntry> findById(Long id);

    void save(LegacyAuthorAuthorSyncEntry entry);

    void delete(LegacyAuthorAuthorSyncEntry entry);
}
