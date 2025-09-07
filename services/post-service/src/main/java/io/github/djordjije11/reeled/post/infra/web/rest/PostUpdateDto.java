package io.github.djordjije11.reeled.post.infra.web.rest;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Djordjije Radovic
 */
public record PostUpdateDto(@NotBlank String categoryKey, String description, @NotBlank String title) {

}
