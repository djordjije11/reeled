package io.github.djordjije11.reeledlegacy.model;

import java.time.LocalDate;

/**
 * @author Djordjije Radovic
 */
public record PostDailyMetricsProjection(LocalDate date, Long searchAppearancesSum, Double searchAppearancesAvg, Long viewsSum, Double viewsAvg) {

}
