package io.github.djordjije11.reeled.postmetrics.domain;

import io.github.djordjije11.reeled.commons.exception.NotFoundException;
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
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "key", callSuper = false)
@ToString
@Entity
@Table(name = "pm_post_daily_performance_aggregation")
public class PostDailyPerformanceAggregation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PostDailyPerformanceAggregationKey key;

    private PostDailyPerformanceAggregationPerformance performance;

    private PostDailyPerformanceAggregationPost post;

    @Version
    private Long version;

    public PostDailyPerformanceAggregation(PostDailyPerformanceAggregationKey key,
                                           PostDailyPerformanceSupportRepository postDailyPerformanceSupportRepository,
                                           PostSupportRepository postSupportRepository) {
        Assert.notNull(key, "key must not be null");
        Assert.notNull(postDailyPerformanceSupportRepository, "postDailyPerformanceSupportRepository must not be null");
        Assert.notNull(postSupportRepository, "postSupportRepository must not be null");

        this.key = key;
        this.performance = getPerformance(postDailyPerformanceSupportRepository);
        this.post = getPost(postSupportRepository);
    }

    public void update(PostDailyPerformanceSupportRepository postDailyPerformanceSupportRepository) {
        Assert.notNull(postDailyPerformanceSupportRepository, "postDailyPerformanceSupportRepository must not be null");

        final PostDailyPerformanceAggregationPerformance updatedPerformance = getPerformance(postDailyPerformanceSupportRepository);
        if (performance.equals(updatedPerformance)) {
            return;
        }

        performance = updatedPerformance;
    }

    public void update(PostSupportRepository postSupportRepository) {
        Assert.notNull(postSupportRepository, "postSupportRepository must not be null");

        final PostDailyPerformanceAggregationPost updatedPost = getPost(postSupportRepository);
        if (Objects.equals(post, updatedPost)) {
            return;
        }

        post = updatedPost;
    }

    private PostDailyPerformanceAggregationPerformance getPerformance(PostDailyPerformanceSupportRepository postDailyPerformanceSupportRepository) {
        return postDailyPerformanceSupportRepository.findByKey(new PostDailyPerformanceKey(key.getPostId(), key.getDate()))
                .map(p -> new PostDailyPerformanceAggregationPerformance(p.searchAppearances(), p.views()))
                .orElseThrow(() -> new NotFoundException("Post daily performance does not exist (key: %s)".formatted(key)));
    }

    private PostDailyPerformanceAggregationPost getPost(PostSupportRepository postSupportRepository) {
        return postSupportRepository.findById(key.getPostId())
                .map(p -> new PostDailyPerformanceAggregationPost(p.authorId(), p.categoryKey(), p.duration(), p.monetized()))
                .orElse(null);
    }
}
