package io.github.djordjije11.reeled.postmetrics.domain;

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
@Table(name = "pm_post")
public class Post extends AbstractAggregateRoot<Post> {

    @Id
    private Long id;

    private PostData data;

    @Column(name = "is_deleted")
    private boolean deleted;

    private ZonedDateTime deletedDate;

    @Version
    private Long version;

    public Post(Long id, PostData data) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(data, "data must not be null");

        this.id = id;
        this.data = data;

        registerPostUpsertedEvent();
    }

    public void update(PostData data) {
        Assert.notNull(data, "data must not be null");

        if (this.data.equals(data)) {
            return;
        }

        this.data = data;

        registerPostUpsertedEvent();
    }

    public void delete(ZonedDateTime deletedDate) {
        Assert.notNull(deletedDate, "deletedDate must not be null");

        if (deleted && Objects.equals(this.deletedDate, deletedDate)) {
            return;
        }

        deleted = true;
        this.deletedDate = deletedDate;
    }

    private void registerPostUpsertedEvent() {
        registerEvent(new PostUpserted(id));
    }
}
