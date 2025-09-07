package io.github.djordjije11.reeled.reference.infra.web.rest;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Djordjije Radovic
 */
public record PostCategoryCreateDto(@NotBlank String key) {

}
