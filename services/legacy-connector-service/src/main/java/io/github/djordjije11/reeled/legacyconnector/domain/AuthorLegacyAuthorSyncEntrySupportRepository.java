package io.github.djordjije11.reeled.legacyconnector.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * @author Djordjije Radovic
 */
public interface AuthorLegacyAuthorSyncEntrySupportRepository extends Repository<AuthorLegacyAuthorSyncEntry, Long> {

    @Query("""
            SELECT id
            FROM AuthorLegacyAuthorSyncEntry
            WHERE syncStatus = io.github.djordjije11.reeled.legacyconnector.domain.SyncStatus.ERROR
            ORDER BY id
            LIMIT :limit""")
    List<Long> findAllIdsEligibleForReconciliation(int limit);
}
