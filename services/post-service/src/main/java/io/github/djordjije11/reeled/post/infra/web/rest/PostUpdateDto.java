package io.github.djordjije11.reeled.post.infra.web.rest;

import io.github.djordjije11.reeled.codes.PostCodes.PostCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Djordjije Radovic
 */
public record PostUpdateDto(@NotNull PostCategory category, String description, @NotBlank String title) {

}
