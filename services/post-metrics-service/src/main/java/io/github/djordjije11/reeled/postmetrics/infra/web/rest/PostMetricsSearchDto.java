package io.github.djordjije11.reeled.postmetrics.infra.web.rest;

import io.github.djordjije11.reeled.codes.PostCodes.PostCategory;
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
                        Set<@NotNull PostCategory> categories,
                        Boolean monetized) {

    }
}
