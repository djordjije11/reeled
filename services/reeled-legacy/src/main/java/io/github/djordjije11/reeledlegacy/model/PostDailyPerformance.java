package io.github.djordjije11.reeledlegacy.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Djordjije Radovic
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "key", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "post_daily_performance")
public class PostDailyPerformance {

    @EmbeddedId
    private PostDailyPerformanceKey key;

    private Long searchAppearances;

    private Long views;

    @Version
    private Long version;
}
