package io.github.djordjije11.reeled.legacyconnector.domain;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import io.github.djordjije11.reeled.codes.AuthorCodesLegacyLookupMappings;
import io.github.djordjije11.reeled.integration.external.legacy.rest.AuthorCreateDto;
import io.github.djordjije11.reeled.integration.external.legacy.rest.AuthorUpdateDto;
import io.github.djordjije11.reeled.integration.external.legacy.rest.LegacyClient;
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
@Table(name = "lc_author_legacy_author_sync_entry")
public class AuthorLegacyAuthorSyncEntry {

    @Id
    private Long id;

    private String name;

    private AuthorType type;

    private String bio;

    private String imageUrl;

    @Version
    private Long version;

    public AuthorLegacyAuthorSyncEntry(String name, AuthorType type, String bio, String imageUrl, LegacyClient legacyClient) {
        Assert.hasText(name, "name must be provided");
        Assert.notNull(type, "type must not be null");

        this.id = legacyClient.createAuthor(new AuthorCreateDto(name, AuthorCodesLegacyLookupMappings.AUTHOR_TYPE_AUTHOR_TYPE_ID_MAP.get(type), bio, imageUrl));
        this.name = name;
        this.type = type;
        this.bio = bio;
        this.imageUrl = imageUrl;
    }

    public void update(String name, String bio, String imageUrl, LegacyClient legacyClient) {
        Assert.hasText(name, "name must be provided");

        if (this.name.equals(name) && Objects.equals(this.bio, bio) && Objects.equals(this.imageUrl, imageUrl)) {
            return;
        }

        legacyClient.updateAuthor(id, new AuthorUpdateDto(name, bio, imageUrl));

        this.name = name;
        this.bio = bio;
        this.imageUrl = imageUrl;
    }
}
