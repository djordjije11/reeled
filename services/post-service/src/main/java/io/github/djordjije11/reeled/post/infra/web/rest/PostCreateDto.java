package io.github.djordjije11.reeled.post.infra.web.rest;

import io.github.djordjije11.reeled.codes.PostCodes.PostCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
public record PostCreateDto(@NotNull PostCategory category,
                            String description,
                            @NotNull Duration duration,
                            @NotNull Boolean monetized,
                            @NotBlank String title,
                            @NotBlank String videoUrl) {

}
