package io.github.djordjije11.reeled.postmetrics.domain;

import jakarta.persistence.Embeddable;
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
@Embeddable
public class PostDailyPerformanceAggregationPerformance {

    private Long searchAppearances;

    private Long views;

    public PostDailyPerformanceAggregationPerformance(Long searchAppearances, Long views) {
        Assert.notNull(searchAppearances, "searchAppearances must not be null");
        Assert.notNull(views, "views must not be null");

        this.searchAppearances = searchAppearances;
        this.views = views;
    }
}
