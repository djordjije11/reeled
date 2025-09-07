package io.github.djordjije11.reeled.postmetrics.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
@Embeddable
public class PostData {

    private Long authorId;

    private String categoryKey;

    private Duration duration;

    @Column(name = "is_monetized")
    private boolean monetized;

    private String title;

    private String videoUrl;

    public PostData(Long authorId, String categoryKey, Duration duration, Boolean monetized, String title, String videoUrl) {
        Assert.notNull(authorId, "authorId must not be null");
        Assert.hasText(categoryKey, "categoryKey must be provided");
        Assert.notNull(duration, "duration must not be null");
        Assert.notNull(monetized, "monetized must not be null");
        Assert.hasText(title, "title must not be empty");
        Assert.hasText(videoUrl, "videoUrl must not be empty");

        this.authorId = authorId;
        this.categoryKey = categoryKey;
        this.duration = duration;
        this.monetized = monetized;
        this.title = title;
        this.videoUrl = videoUrl;
    }
}
