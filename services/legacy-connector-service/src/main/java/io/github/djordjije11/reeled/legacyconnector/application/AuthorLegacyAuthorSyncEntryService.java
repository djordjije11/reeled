package io.github.djordjije11.reeled.legacyconnector.application;

import io.github.djordjije11.reeled.commons.exception.NotFoundException;
import io.github.djordjije11.reeled.integration.external.legacy.rest.LegacyClient;
import io.github.djordjije11.reeled.integration.internal.service.author.rest.AuthorServiceClient;
import io.github.djordjije11.reeled.legacyconnector.domain.AuthorData;
import io.github.djordjije11.reeled.legacyconnector.domain.AuthorLegacyAuthorSyncEntry;
import io.github.djordjije11.reeled.legacyconnector.domain.AuthorLegacyAuthorSyncEntryRepository;
import io.github.djordjije11.reeled.legacyconnector.domain.AuthorLegacyAuthorSyncEntrySupportRepository;
import io.github.djordjije11.reeled.legacyconnector.domain.LegacyAuthorData;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static io.github.djordjije11.reeled.config.ResilienceConfiguration.DATA_ACCESS_RETRY_NAME;

/**
 * @author Djordjije Radovic
 */
@Service
public class AuthorLegacyAuthorSyncEntryService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorLegacyAuthorSyncEntryService.class);

    private static final int FETCH_SIZE = 5000;

    private final AuthorLegacyAuthorSyncEntryRepository authorLegacyAuthorSyncEntryRepository;

    private final AuthorLegacyAuthorSyncEntrySupportRepository authorLegacyAuthorSyncEntrySupportRepository;

    private final AuthorServiceClient authorServiceClient;

    private final LegacyClient legacyClient;

    private final io.github.resilience4j.retry.Retry dataAccessRetry;

    public AuthorLegacyAuthorSyncEntryService(AuthorLegacyAuthorSyncEntryRepository authorLegacyAuthorSyncEntryRepository,
                                              AuthorLegacyAuthorSyncEntrySupportRepository authorLegacyAuthorSyncEntrySupportRepository,
                                              AuthorServiceClient authorServiceClient,
                                              LegacyClient legacyClient,
                                              RetryRegistry retryRegistry) {
        this.authorLegacyAuthorSyncEntryRepository = authorLegacyAuthorSyncEntryRepository;
        this.authorLegacyAuthorSyncEntrySupportRepository = authorLegacyAuthorSyncEntrySupportRepository;
        this.authorServiceClient = authorServiceClient;
        this.legacyClient = legacyClient;
        this.dataAccessRetry = retryRegistry.retry(DATA_ACCESS_RETRY_NAME);
    }

    public Long createAuthor(AuthorData authorData) {
        logger.info("Creating author...");

        final AuthorLegacyAuthorSyncEntry syncEntry = new AuthorLegacyAuthorSyncEntry(authorData, legacyClient);
        authorLegacyAuthorSyncEntryRepository.save(syncEntry);

        logger.info("Author successfully created (id: {})", syncEntry.getId());

        return syncEntry.getId();
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void sync(Long id, LegacyAuthorData legacyAuthorData) {
        Assert.notNull(id, "id must not be null");

        logger.info("Syncing author from legacy (id: {})...", id);

        authorLegacyAuthorSyncEntryRepository.findById(id).ifPresentOrElse(syncEntry -> {
            syncEntry.sync(legacyAuthorData, authorServiceClient);
            authorLegacyAuthorSyncEntryRepository.save(syncEntry);
        }, () -> authorLegacyAuthorSyncEntryRepository.save(new AuthorLegacyAuthorSyncEntry(id, legacyAuthorData, authorServiceClient)));

        logger.info("Author from legacy successfully synced (id: {})", id);
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void sync(Long id, AuthorData authorData, boolean legacy) {
        Assert.notNull(id, "id must not be null");

        logger.info("Syncing author from new platform (id: {})...", id);

        final AuthorLegacyAuthorSyncEntry syncEntry = getAuthorLegacyAuthorSyncEntry(id);

        syncEntry.sync(authorData, legacy, legacyClient);
        authorLegacyAuthorSyncEntryRepository.save(syncEntry);

        logger.info("Author from new platform successfully synced (id: {})", id);
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void syncDeleteFromLegacy(Long id) {
        Assert.notNull(id, "id must not be null");

        logger.info("Syncing author deletion from legacy (id: {})...", id);

        authorLegacyAuthorSyncEntryRepository.findById(id).ifPresent(syncEntry -> {
            syncEntry.syncDelete(authorServiceClient);
            authorLegacyAuthorSyncEntryRepository.save(syncEntry);
        });

        logger.info("Author deletion from legacy successfully synced (id: {})", id);
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void syncDeleteFromNewPlatform(Long id) {
        Assert.notNull(id, "id must not be null");

        logger.info("Syncing author deletion from new platform (id: {})...", id);

        authorLegacyAuthorSyncEntryRepository.findById(id).ifPresent(syncEntry -> {
            syncEntry.syncDelete(legacyClient);
            authorLegacyAuthorSyncEntryRepository.save(syncEntry);
        });

        logger.info("Author deletion from new platform successfully synced (id: {})", id);
    }

    public void reconcile() {
        logger.info("Reconciling author legacy author sync entries...");

        authorLegacyAuthorSyncEntrySupportRepository.findAllIdsEligibleForReconciliation(FETCH_SIZE).forEach(id -> {
            try {
                dataAccessRetry.executeRunnable(() -> reconcile(id));
            } catch (RuntimeException e) {
                logger.error("Error occurred while reconciling author legacy author sync entry (id: {})", id, e);
            }
        });

        logger.info("Author legacy author sync entries successfully reconciled");
    }

    private void reconcile(Long id) {
        logger.info("Reconciling author legacy author sync entry (id: {})...", id);

        final AuthorLegacyAuthorSyncEntry syncEntry = getAuthorLegacyAuthorSyncEntry(id);

        syncEntry.reconcile(legacyClient, authorServiceClient);
        authorLegacyAuthorSyncEntryRepository.save(syncEntry);

        logger.info("Author legacy author sync entry successfully reconciled (id: {})", id);
    }

    private AuthorLegacyAuthorSyncEntry getAuthorLegacyAuthorSyncEntry(Long id) {
        return authorLegacyAuthorSyncEntryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author legacy author sync entry does not exist (id: %d)".formatted(id)));
    }
}
