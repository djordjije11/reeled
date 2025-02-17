package io.github.djordjije11.reeled.postmetrics.domain;

/**
 * @author Djordjije Radovic
 */
public record PostMonthlyMetricsProjection(Long searchAppearancesSum, Double searchAppearancesAvg, Long viewsSum, Double viewsAvg) {

}
