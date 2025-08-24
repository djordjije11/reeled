package io.github.djordjije11.reeled.legacyconnector.domain;

import io.github.djordjije11.reeled.codes.AuthorCodesLegacyLookupMappings;
import io.github.djordjije11.reeled.integration.internal.service.author.rest.AuthorCreateDto;
import io.github.djordjije11.reeled.integration.internal.service.author.rest.AuthorServiceClient;
import io.github.djordjije11.reeled.integration.internal.service.author.rest.AuthorUpdateDto;
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

import java.util.Objects;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "lc_legacy_author_author_sync_entry")
public class LegacyAuthorAuthorSyncEntry {

    @Id
    private Long id;

    private String name;

    private Long typeId;

    private String bio;

    private String imageUrl;

    @Version
    private Long version;

    public LegacyAuthorAuthorSyncEntry(Long id, String name, Long typeId, String bio, String imageUrl, AuthorServiceClient authorServiceClient) {
        Assert.notNull(id, "id must not be null");
        Assert.hasText(name, "name must be provided");
        Assert.notNull(typeId, "typeId must not be null");

        authorServiceClient.create(new AuthorCreateDto(id, name, AuthorCodesLegacyLookupMappings.AUTHOR_TYPE_ID_AUTHOR_TYPE_MAP.get(typeId), bio, imageUrl));

        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.bio = bio;
        this.imageUrl = imageUrl;
    }

    public void update(String name, String bio, String imageUrl, AuthorServiceClient authorServiceClient) {
        Assert.hasText(name, "name must be provided");

        if (this.name.equals(name) && Objects.equals(this.bio, bio) && Objects.equals(this.imageUrl, imageUrl)) {
            return;
        }

        authorServiceClient.update(id, new AuthorUpdateDto(name, bio, imageUrl));

        this.name = name;
        this.bio = bio;
        this.imageUrl = imageUrl;
    }
}
