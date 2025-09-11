package io.github.djordjije11.reeledlegacy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
public record PostCreateDto(@NotNull Long categoryId,
                            String description,
                            @NotNull Duration duration,
                            @NotNull Boolean monetized,
                            @NotBlank String title,
                            @NotBlank String videoUrl) {

}
