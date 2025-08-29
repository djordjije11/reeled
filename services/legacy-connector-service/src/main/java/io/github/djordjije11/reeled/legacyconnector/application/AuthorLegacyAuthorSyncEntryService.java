package io.github.djordjije11.reeled.legacyconnector.application;

import io.github.djordjije11.reeled.commons.exception.NotFoundException;
import io.github.djordjije11.reeled.integration.external.legacy.rest.LegacyClient;
import io.github.djordjije11.reeled.integration.internal.service.author.rest.AuthorServiceClient;
import io.github.djordjije11.reeled.legacyconnector.domain.AuthorData;
import io.github.djordjije11.reeled.legacyconnector.domain.AuthorLegacyAuthorSyncEntry;
import io.github.djordjije11.reeled.legacyconnector.domain.AuthorLegacyAuthorSyncEntryRepository;
import io.github.djordjije11.reeled.legacyconnector.domain.LegacyAuthorData;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class AuthorLegacyAuthorSyncEntryService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorLegacyAuthorSyncEntryService.class);

    private final AuthorLegacyAuthorSyncEntryRepository authorLegacyAuthorSyncEntryRepository;

    private final AuthorServiceClient authorServiceClient;

    private final LegacyClient legacyClient;

    public Long createAuthor(AuthorData authorData) {
        logger.info("Creating author...");

        final AuthorLegacyAuthorSyncEntry syncEntry = new AuthorLegacyAuthorSyncEntry(authorData, legacyClient);
        authorLegacyAuthorSyncEntryRepository.save(syncEntry);

        logger.info("Author successfully created (id: {})", syncEntry.getId());

        return syncEntry.getId();
    }

    public void sync(Long id, LegacyAuthorData legacyAuthorData) {
        Assert.notNull(id, "id must not be null");

        logger.info("Syncing author from legacy (id: {})...", id);

        authorLegacyAuthorSyncEntryRepository.findById(id).ifPresentOrElse(syncEntry -> {
            syncEntry.sync(legacyAuthorData, authorServiceClient);
            authorLegacyAuthorSyncEntryRepository.save(syncEntry);
        }, () -> authorLegacyAuthorSyncEntryRepository.save(new AuthorLegacyAuthorSyncEntry(id, legacyAuthorData, authorServiceClient)));

        logger.info("Author from legacy successfully synced (id: {})", id);
    }

    public void sync(Long id, AuthorData authorData, boolean legacy) {
        Assert.notNull(id, "id must not be null");

        logger.info("Syncing author from new platform (id: {})...", id);

        final AuthorLegacyAuthorSyncEntry syncEntry = authorLegacyAuthorSyncEntryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author legacy author sync entry does not exist (id: %d)".formatted(id)));

        syncEntry.sync(authorData, legacy, legacyClient);
        authorLegacyAuthorSyncEntryRepository.save(syncEntry);

        logger.info("Author from new platform successfully synced (id: {})", id);
    }

    public void syncDeleteFromLegacy(Long id) {
        Assert.notNull(id, "id must not be null");

        logger.info("Syncing author deletion from legacy (id: {})...", id);

        authorLegacyAuthorSyncEntryRepository.findById(id).ifPresent(syncEntry -> {
            syncEntry.syncDelete(authorServiceClient);
            authorLegacyAuthorSyncEntryRepository.delete(syncEntry);
        });

        logger.info("Author deletion from legacy successfully synced (id: {})", id);
    }

    public void syncDeleteFromNewPlatform(Long id) {
        Assert.notNull(id, "id must not be null");

        logger.info("Syncing author deletion from new platform (id: {})...", id);

        authorLegacyAuthorSyncEntryRepository.findById(id).ifPresent(syncEntry -> {
            syncEntry.syncDelete(legacyClient);
            authorLegacyAuthorSyncEntryRepository.delete(syncEntry);
        });

        logger.info("Author deletion from new platform successfully synced (id: {})", id);
    }
}
