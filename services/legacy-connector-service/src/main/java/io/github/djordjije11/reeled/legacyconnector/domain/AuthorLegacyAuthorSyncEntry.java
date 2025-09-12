package io.github.djordjije11.reeled.legacyconnector.domain;

import io.github.djordjije11.reeled.commons.exception.ReeledDomainException;
import io.github.djordjije11.reeled.integration.external.legacy.rest.LegacyAuthorCreateDto;
import io.github.djordjije11.reeled.integration.external.legacy.rest.LegacyAuthorUpdateDto;
import io.github.djordjije11.reeled.integration.external.legacy.rest.LegacyClient;
import io.github.djordjije11.reeled.integration.internal.service.author.rest.AuthorCreateDto;
import io.github.djordjije11.reeled.integration.internal.service.author.rest.AuthorServiceClient;
import io.github.djordjije11.reeled.integration.internal.service.author.rest.AuthorUpdateDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "lc_author_legacy_author_sync_entry")
public class AuthorLegacyAuthorSyncEntry {

    private static final Logger logger = LoggerFactory.getLogger(AuthorLegacyAuthorSyncEntry.class);

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private SyncStatus syncStatus;

    @Enumerated(EnumType.STRING)
    private SyncAction syncAction;

    private AuthorData authorData;

    private LegacyAuthorData legacyAuthorData;

    @Column(name = "is_legacy")
    private boolean legacy;

    @Column(name = "is_deleted")
    private boolean deleted;

    @Version
    private Long version;

    public AuthorLegacyAuthorSyncEntry(Long id, LegacyAuthorData legacyAuthorData, AuthorServiceClient authorServiceClient) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(legacyAuthorData, "legacyAuthorData must not be null");
        Assert.notNull(authorServiceClient, "authorServiceClient must not be null");

        this.id = id;
        this.legacyAuthorData = legacyAuthorData;

        syncCreatedOnLegacy(authorServiceClient);
    }

    public AuthorLegacyAuthorSyncEntry(AuthorData authorData, LegacyClient legacyClient) {
        Assert.notNull(authorData, "authorData must not be null");
        Assert.notNull(legacyClient, "legacyClient must not be null");

        this.id = legacyClient.createAuthor(new LegacyAuthorCreateDto(authorData.getName(),
                AuthorCodesLegacyLookupMappings.AUTHOR_TYPE_AUTHOR_TYPE_ID_MAP.get(authorData.getType()),
                authorData.getBio(),
                authorData.getImageUrl()));
        this.authorData = authorData;
        this.legacy = false;

        this.syncAction = SyncAction.CREATED_ON_NEW_PLATFORM;
        this.syncStatus = SyncStatus.SUCCESS;
    }

    public void sync(LegacyAuthorData legacyAuthorData, AuthorServiceClient authorServiceClient) {
        Assert.notNull(legacyAuthorData, "legacyAuthorData must not be null");
        Assert.notNull(authorServiceClient, "authorServiceClient must not be null");

        if (legacyAuthorData.equals(this.legacyAuthorData)) {
            return;
        }

        this.legacyAuthorData = legacyAuthorData;

        syncUpdatedOnLegacy(authorServiceClient);
    }

    public void sync(AuthorData authorData, boolean legacy, LegacyClient legacyClient) {
        Assert.notNull(authorData, "authorData must not be null");
        Assert.notNull(legacyClient, "legacyClient must not be null");

        if (legacy) {
            return;
        }

        if (authorData.equals(this.authorData)) {
            return;
        }

        this.authorData = authorData;
        this.legacy = false;

        syncUpdatedOnNewPlatform(legacyClient);
    }

    public void syncDelete(AuthorServiceClient authorServiceClient) {
        Assert.notNull(authorServiceClient, "authorServiceClient must not be null");

        if (!legacy) {
            return;
        }

        deleted = true;

        syncDeletedOnLegacy(authorServiceClient);
    }

    public void syncDelete(LegacyClient legacyClient) {
        Assert.notNull(legacyClient, "legacyClient must not be null");

        if (legacy) {
            return;
        }

        deleted = true;

        syncDeletedOnNewPlatform(legacyClient);
    }

    public void reconcile(LegacyClient legacyClient, AuthorServiceClient authorServiceClient) {
        Assert.notNull(legacyClient, "legacyClient must not be null");
        Assert.notNull(authorServiceClient, "authorServiceClient must not be null");

        if (syncStatus != SyncStatus.ERROR) {
            throw new ReeledDomainException("Cannot reconcile author legacy author sync entry, status is not error (id: %d)".formatted(id));
        }

        switch (syncAction) {
            case CREATED_ON_LEGACY -> syncCreatedOnLegacy(authorServiceClient);
            case UPDATED_ON_LEGACY -> syncUpdatedOnLegacy(authorServiceClient);
            case UPDATED_ON_NEW_PLATFORM -> syncUpdatedOnNewPlatform(legacyClient);
            case DELETED_ON_LEGACY -> syncDeletedOnLegacy(authorServiceClient);
            case DELETED_ON_NEW_PLATFORM -> syncDeletedOnNewPlatform(legacyClient);
            default -> throw new ReeledDomainException("Sync action not supported for reconciliation (id: %d, syncAction: %s)".formatted(id, syncAction));
        }
    }

    private void syncCreatedOnLegacy(AuthorServiceClient authorServiceClient) {
        syncAction = SyncAction.CREATED_ON_LEGACY;
        try {
            authorServiceClient.create(id,
                    new AuthorCreateDto(legacyAuthorData.getName(),
                            AuthorCodesLegacyLookupMappings.AUTHOR_TYPE_ID_AUTHOR_TYPE_MAP.get(legacyAuthorData.getTypeId()),
                            legacyAuthorData.getBio(),
                            legacyAuthorData.getImageUrl()));
            syncStatus = SyncStatus.SUCCESS;
        } catch (RuntimeException e) {
            logger.error("Error occurred while syncing author created on legacy to new platform (id: {})", id, e);
            syncStatus = SyncStatus.ERROR;
        }
    }

    private void syncUpdatedOnLegacy(AuthorServiceClient authorServiceClient) {
        syncAction = SyncAction.UPDATED_ON_LEGACY;
        try {
            authorServiceClient.update(id, new AuthorUpdateDto(legacyAuthorData.getName(), legacyAuthorData.getBio(), legacyAuthorData.getImageUrl()));
            syncStatus = SyncStatus.SUCCESS;
        } catch (RuntimeException e) {
            logger.error("Error occurred while syncing author updated on legacy to new platform (id: {})", id, e);
            syncStatus = SyncStatus.ERROR;
        }
    }

    private void syncUpdatedOnNewPlatform(LegacyClient legacyClient) {
        syncAction = SyncAction.UPDATED_ON_NEW_PLATFORM;
        try {
            legacyClient.updateAuthor(id, new LegacyAuthorUpdateDto(authorData.getName(), authorData.getBio(), authorData.getImageUrl()));
            syncStatus = SyncStatus.SUCCESS;
        } catch (RuntimeException e) {
            logger.error("Error occurred while syncing author updated on new platform to legacy (id: {})", id, e);
            syncStatus = SyncStatus.ERROR;
        }
    }

    private void syncDeletedOnLegacy(AuthorServiceClient authorServiceClient) {
        syncAction = SyncAction.DELETED_ON_LEGACY;
        try {
            authorServiceClient.delete(id);
            syncStatus = SyncStatus.SUCCESS;
        } catch (RuntimeException e) {
            logger.error("Error occurred while syncing author deleted on legacy to new platform (id: {})", id, e);
            syncStatus = SyncStatus.ERROR;
        }
    }

    private void syncDeletedOnNewPlatform(LegacyClient legacyClient) {
        syncAction = SyncAction.DELETED_ON_NEW_PLATFORM;
        try {
            legacyClient.deleteAuthor(id);
            syncStatus = SyncStatus.SUCCESS;
        } catch (RuntimeException e) {
            logger.error("Error occurred while syncing author deleted on new platform to legacy (id: {})", id, e);
            syncStatus = SyncStatus.ERROR;
        }
    }

    enum SyncAction {
        CREATED_ON_LEGACY,
        CREATED_ON_NEW_PLATFORM,
        UPDATED_ON_LEGACY,
        UPDATED_ON_NEW_PLATFORM,
        DELETED_ON_LEGACY,
        DELETED_ON_NEW_PLATFORM;
    }
}
