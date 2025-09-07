package io.github.djordjije11.reeled.post.infra.web.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
public record PostCreateDto(@NotBlank String categoryKey,
                            String description,
                            @NotNull Duration duration,
                            @NotNull Boolean monetized,
                            @NotBlank String title,
                            @NotBlank String videoUrl) {

}
