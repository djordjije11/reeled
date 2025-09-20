package io.github.djordjije11.reeledlegacy.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Djordjije Radovic
 */
public record PostCategoryCreateDto(@NotBlank String name) {

}
