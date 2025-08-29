package io.github.djordjije11.reeled.author.domain;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import io.github.djordjije11.reeled.commons.exception.ReeledDomainException;
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
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.util.Assert;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * @author Djordjije Radovic
 */
@Getter
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "a_author")
public class Author extends AbstractAggregateRoot<Author> {

    @Id
    private Long id;

    private String name;

    private AuthorType type;

    private String bio;

    private String imageUrl;

    @Column(name = "is_legacy")
    private boolean legacy;

    @Column(name = "is_deleted")
    private boolean deleted;

    private ZonedDateTime deletedDate;

    @Version
    private Long version;

    Author(Long id, String name, AuthorType type, String bio, String imageUrl, boolean legacy) {
        Assert.notNull(id, "id must not be null");
        Assert.hasText(name, "name must not be empty");
        Assert.notNull(type, "type must not be null");

        this.id = id;
        this.name = name;
        this.type = type;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.legacy = legacy;

        registerAuthorUpsertedEvent();
    }

    public void update(String name, String bio, String imageUrl, boolean legacy) {
        Assert.hasText(name, "name must not be empty");

        if (!this.legacy && legacy) {
            throw new ReeledDomainException("Author is not legacy and cannot be updated from legacy (id: %d)".formatted(id));
        }

        if (this.name.equals(name) && Objects.equals(this.bio, bio) && Objects.equals(this.imageUrl, imageUrl)) {
            return;
        }

        this.name = name;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.legacy = legacy;

        registerAuthorUpsertedEvent();
    }

    public void delete(boolean legacy, Clock clock) {
        Assert.notNull(clock, "clock must not be null");

        if (this.legacy != legacy) {
            throw new ReeledDomainException("Author legacy status does not match deletion platform (id: %d)".formatted(id));
        }

        if (deleted) {
            throw new ReeledDomainException("Author already deleted (id: %d)".formatted(id));
        }

        deleted = true;
        deletedDate = ZonedDateTime.now(clock);

        registerAuthorDeletedEvent();
    }

    private void registerAuthorUpsertedEvent() {
        registerEvent(AuthorEventMapper.mapToAuthorUpserted(this));
    }

    private void registerAuthorDeletedEvent() {
        registerEvent(AuthorEventMapper.mapToAuthorDeleted(this));
    }
}
