package io.github.djordjije11.reeled.postmetrics.domain;

import java.time.LocalDate;

/**
 * @author Djordjije Radovic
 */
public record PostDailyPerformanceUpserted(Long postId, LocalDate date) {

}
