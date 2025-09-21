package io.github.djordjije11.reeledlegacy.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

/**
 * @author Djordjije Radovic
 */
public record PostMetricsSearchDto(@Valid @NotNull Query query) {

    public record Query(@NotNull LocalDate dateFrom,
                        @NotNull LocalDate dateTo,
                        Duration durationFrom,
                        Duration durationTo,
                        Set<@NotNull Long> categoryIds,
                        Boolean monetized) {

    }
}
