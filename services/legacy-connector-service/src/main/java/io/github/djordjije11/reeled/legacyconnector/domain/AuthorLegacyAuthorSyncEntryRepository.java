package io.github.djordjije11.reeled.legacyconnector.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public interface AuthorLegacyAuthorSyncEntryRepository extends Repository<AuthorLegacyAuthorSyncEntry, Long> {

    Optional<AuthorLegacyAuthorSyncEntry> findById(Long id);

    void save(AuthorLegacyAuthorSyncEntry syncEntry);
}
