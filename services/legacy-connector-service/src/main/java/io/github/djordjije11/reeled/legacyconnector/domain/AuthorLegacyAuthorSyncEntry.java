package io.github.djordjije11.reeled.legacyconnector.domain;

import io.github.djordjije11.reeled.codes.AuthorCodesLegacyLookupMappings;
import io.github.djordjije11.reeled.integration.external.legacy.rest.LegacyAuthorCreateDto;
import io.github.djordjije11.reeled.integration.external.legacy.rest.LegacyAuthorUpdateDto;
import io.github.djordjije11.reeled.integration.external.legacy.rest.LegacyClient;
import io.github.djordjije11.reeled.integration.internal.service.author.rest.AuthorCreateDto;
import io.github.djordjije11.reeled.integration.internal.service.author.rest.AuthorServiceClient;
import io.github.djordjije11.reeled.integration.internal.service.author.rest.AuthorUpdateDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

    @Id
    private Long id;

    private AuthorData authorData;

    private LegacyAuthorData legacyAuthorData;

    @Column(name = "is_legacy")
    private boolean legacy;

    @Version
    private Long version;

    public AuthorLegacyAuthorSyncEntry(AuthorData authorData, LegacyClient legacyClient) {
        Assert.notNull(authorData, "authorData must not be null");
        Assert.notNull(legacyClient, "legacyClient must not be null");

        this.id = legacyClient.createAuthor(new LegacyAuthorCreateDto(authorData.getName(),
                AuthorCodesLegacyLookupMappings.AUTHOR_TYPE_AUTHOR_TYPE_ID_MAP.get(authorData.getType()),
                authorData.getBio(),
                authorData.getImageUrl()));
        this.authorData = authorData;
        this.legacy = false;
    }

    public AuthorLegacyAuthorSyncEntry(Long id, LegacyAuthorData legacyAuthorData, AuthorServiceClient authorServiceClient) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(legacyAuthorData, "legacyAuthorData must not be null");
        Assert.notNull(authorServiceClient, "authorServiceClient must not be null");

        authorServiceClient.create(id,
                new AuthorCreateDto(legacyAuthorData.getName(),
                        AuthorCodesLegacyLookupMappings.AUTHOR_TYPE_ID_AUTHOR_TYPE_MAP.get(legacyAuthorData.getTypeId()),
                        legacyAuthorData.getBio(),
                        legacyAuthorData.getImageUrl()));

        this.id = id;
        this.legacyAuthorData = legacyAuthorData;
        this.legacy = true;
    }

    public void sync(AuthorData authorData, boolean legacy, LegacyClient legacyClient) {
        Assert.notNull(authorData, "authorData must not be null");
        Assert.notNull(legacyClient, "legacyClient must not be null");

        if (legacy) {
            return;
        }

        if (authorData.equals(this.authorData) && this.legacy == legacy) {
            return;
        }

        legacyClient.updateAuthor(id, new LegacyAuthorUpdateDto(authorData.getName(), authorData.getBio(), authorData.getImageUrl()));

        this.authorData = authorData;
        this.legacy = legacy;
    }

    public void sync(LegacyAuthorData legacyAuthorData, AuthorServiceClient authorServiceClient) {
        Assert.notNull(legacyAuthorData, "legacyAuthorData must not be null");
        Assert.notNull(authorServiceClient, "authorServiceClient must not be null");

        if (!legacy) {
            return;
        }

        if (legacyAuthorData.equals(this.legacyAuthorData)) {
            return;
        }

        authorServiceClient.update(id, new AuthorUpdateDto(legacyAuthorData.getName(), legacyAuthorData.getBio(), legacyAuthorData.getImageUrl()));

        this.legacyAuthorData = legacyAuthorData;
    }

    public void syncDelete(AuthorServiceClient authorServiceClient) {
        Assert.notNull(authorServiceClient, "authorServiceClient must not be null");

        if (!legacy) {
            return;
        }

        authorServiceClient.delete(id);
    }

    public void syncDelete(LegacyClient legacyClient) {
        Assert.notNull(legacyClient, "legacyClient must not be null");

        if (legacy) {
            return;
        }

        legacyClient.deleteAuthor(id);
    }
}
