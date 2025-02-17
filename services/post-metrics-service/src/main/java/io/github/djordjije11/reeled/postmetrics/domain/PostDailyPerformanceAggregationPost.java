package io.github.djordjije11.reeled.postmetrics.domain;

import io.github.djordjije11.reeled.codes.PostCodes.PostCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;
import org.springframework.util.Assert;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode
@Embeddable
@Immutable
public class PostDailyPerformanceAggregationPost {

    private Long authorId;

    private PostCategory category;

    private Duration duration;

    @Column(name = "is_monetized")
    private boolean monetized;

    public PostDailyPerformanceAggregationPost(Long authorId, PostCategory category, Duration duration, Boolean monetized) {
        Assert.notNull(authorId, "authorId must not be null");
        Assert.notNull(category, "category must not be null");
        Assert.notNull(duration, "duration must not be null");
        Assert.notNull(monetized, "monetized must not be null");

        this.authorId = authorId;
        this.category = category;
        this.duration = duration;
        this.monetized = monetized;
    }
}
