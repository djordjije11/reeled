package io.github.djordjije11.reeled.postmetrics.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;
import org.springframework.util.Assert;

import java.time.LocalDate;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode
@Embeddable
@Immutable
public class PostDailyPerformanceAggregationKey {

    private Long postId;

    private LocalDate date;

    public PostDailyPerformanceAggregationKey(Long postId, LocalDate date) {
        Assert.notNull(postId, "postId must not be null");
        Assert.notNull(date, "date must not be null");

        this.postId = postId;
        this.date = date;
    }
}
