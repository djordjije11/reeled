package io.github.djordjije11.reeled.post.domain;

import io.github.djordjije11.reeled.codes.PostCodes.PostCategory;
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
import java.time.Duration;
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
@Table(name = "p_post")
public class Post extends AbstractAggregateRoot<Post> {

    @Id
    private Long id;

    private Long authorId;

    private PostCategory category;

    private String description;

    private Duration duration;

    @Column(name = "is_monetized")
    private boolean monetized;

    private String title;

    private String videoUrl;

    @Column(name = "is_deleted")
    private boolean deleted;

    private ZonedDateTime deletedDate;

    @Version
    private Long version;

    Post(Long id, Long authorId, PostCategory category, String description, Duration duration, Boolean monetized, String title, String videoUrl) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(authorId, "authorId must not be null");
        Assert.notNull(category, "category must not be null");
        Assert.notNull(duration, "duration must not be null");
        Assert.notNull(monetized, "monetized must not be null");
        Assert.hasText(title, "title must not be empty");
        Assert.hasText(videoUrl, "videoUrl must not be empty");

        this.id = id;
        this.authorId = authorId;
        this.category = category;
        this.description = description;
        this.duration = duration;
        this.monetized = monetized;
        this.title = title;
        this.videoUrl = videoUrl;

        registerPostUpsertedEvent();
    }

    public void update(PostCategory category, String description, String title) {
        Assert.notNull(category, "category must not be null");
        Assert.hasText(title, "title must not be empty");

        if (this.category.equals(category) && Objects.equals(this.description, description) && this.title.equals(title)) {
            return;
        }

        this.category = category;
        this.description = description;
        this.title = title;

        registerPostUpsertedEvent();
    }

    public void delete(Clock clock) {
        if (deleted) {
            throw new ReeledDomainException("Post already deleted (id: %d)".formatted(id));
        }

        deleted = true;
        deletedDate = ZonedDateTime.now(clock);

        registerPostDeletedEvent();
    }

    private void registerPostUpsertedEvent() {
        registerEvent(PostEventMapper.mapToPostUpserted(this));
    }

    private void registerPostDeletedEvent() {
        registerEvent(PostEventMapper.mapToPostDeleted(this));
    }
}
