package io.github.djordjije11.reeled.legacyconnector.application;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import io.github.djordjije11.reeled.integration.external.legacy.rest.LegacyClient;
import io.github.djordjije11.reeled.legacyconnector.domain.AuthorLegacyAuthorSyncEntry;
import io.github.djordjije11.reeled.legacyconnector.domain.AuthorLegacyAuthorSyncEntryRepository;
import io.github.djordjije11.reeled.legacyconnector.domain.LegacyAuthorAuthorSyncEntry;
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

    private final LegacyClient legacyClient;

    public Long create(String name, AuthorType type, String bio, String imageUrl) {
        logger.info("Creating author legacy author sync entry...");

        final AuthorLegacyAuthorSyncEntry syncEntry = new AuthorLegacyAuthorSyncEntry(name, type, bio, imageUrl, legacyClient);
        authorLegacyAuthorSyncEntryRepository.save(syncEntry);

        logger.info("Author legacy author sync entry successfully created (id: {})", syncEntry.getId());
    }

    public void upsert(Long id, String name, Long typeId, String bio, String imageUrl) {
        Assert.notNull(id, "id must not be null");

        logger.info("Upserting author legacy author sync entry (id: {})...", id);

        authorLegacyAuthorSyncEntryRepository.findById(id).ifPresentOrElse(syncEntry -> {
            entry.update(name, bio, imageUrl, authorServiceClient);
            authorLegacyAuthorSyncEntryRepository.save(syncEntry);
        }, () -> authorLegacyAuthorSyncEntryRepository.save(new LegacyAuthorAuthorSyncEntry(id, name, typeId, bio, imageUrl, authorServiceClient)));

        logger.info("Legacy author author sync entry successfully upserted (id: {})", id);
    }

    public void delete(Long id) {
        Assert.notNull(id, "id must not be null");

        logger.info("Deleting legacy author author sync entry (id: {})...", id);

        authorLegacyAuthorSyncEntryRepository.findById(id).ifPresent(entry -> {
            authorServiceClient.delete(id);
            authorLegacyAuthorSyncEntryRepository.delete(entry);
        });

        logger.info("Legacy author author sync entry successfully deleted (id: {})", id);
    }
}
