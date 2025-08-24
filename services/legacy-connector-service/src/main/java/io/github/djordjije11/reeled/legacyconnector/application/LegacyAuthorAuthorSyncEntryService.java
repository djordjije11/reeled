package io.github.djordjije11.reeled.legacyconnector.application;

import io.github.djordjije11.reeled.integration.internal.service.author.rest.AuthorServiceClient;
import io.github.djordjije11.reeled.legacyconnector.domain.LegacyAuthorAuthorSyncEntry;
import io.github.djordjije11.reeled.legacyconnector.domain.LegacyAuthorAuthorSyncEntryRepository;
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
public class LegacyAuthorAuthorSyncEntryService {

    private static final Logger logger = LoggerFactory.getLogger(LegacyAuthorAuthorSyncEntryService.class);

    private final LegacyAuthorAuthorSyncEntryRepository legacyAuthorAuthorSyncEntryRepository;

    private final AuthorServiceClient authorServiceClient;

    public void upsert(Long id, String name, Long typeId, String bio, String imageUrl) {
        Assert.notNull(id, "id must not be null");

        logger.info("Upserting legacy author author sync entry (id: {})...", id);

        legacyAuthorAuthorSyncEntryRepository.findById(id).ifPresentOrElse(entry -> {
            entry.update(name, bio, imageUrl, authorServiceClient);
            legacyAuthorAuthorSyncEntryRepository.save(entry);
        }, () -> legacyAuthorAuthorSyncEntryRepository.save(new LegacyAuthorAuthorSyncEntry(id, name, typeId, bio, imageUrl, authorServiceClient)));

        logger.info("Legacy author author sync entry successfully upserted (id: {})", id);
    }

    public void delete(Long id) {
        Assert.notNull(id, "id must not be null");

        logger.info("Deleting legacy author author sync entry (id: {})...", id);

        legacyAuthorAuthorSyncEntryRepository.findById(id).ifPresent(entry -> {
            authorServiceClient.delete(id);
            legacyAuthorAuthorSyncEntryRepository.delete(entry);
        });

        logger.info("Legacy author author sync entry successfully deleted (id: {})", id);
    }
}
