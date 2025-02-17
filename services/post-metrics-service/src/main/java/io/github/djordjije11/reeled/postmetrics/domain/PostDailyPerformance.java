package io.github.djordjije11.reeled.postmetrics.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "key", callSuper = false)
@ToString
@Entity
@Table(name = "pm_post_daily_performance")
public class PostDailyPerformance extends AbstractAggregateRoot<PostDailyPerformance> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PostDailyPerformanceKey key;

    private Long searchAppearances;

    private Long views;

    @Version
    private Long version;

    public PostDailyPerformance(PostDailyPerformanceKey key, Long searchAppearances, Long views) {
        Assert.notNull(key, "key must not be null");
        Assert.notNull(searchAppearances, "searchAppearances must not be null");
        Assert.notNull(views, "views must not be null");

        this.key = key;
        this.searchAppearances = searchAppearances;
        this.views = views;

        registerPostDailyPerformanceUpsertedEvent();
    }

    public void update(Long searchAppearances, Long views) {
        Assert.notNull(searchAppearances, "searchAppearances must not be null");
        Assert.notNull(views, "views must not be null");

        if (this.searchAppearances.equals(searchAppearances) && this.views.equals(views)) {
            return;
        }

        this.searchAppearances = searchAppearances;
        this.views = views;

        registerPostDailyPerformanceUpsertedEvent();
    }

    private void registerPostDailyPerformanceUpsertedEvent() {
        registerEvent(new PostDailyPerformanceUpserted(key.getPostId(), key.getDate()));
    }
}
