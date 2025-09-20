package io.github.djordjije11.reeledlegacy.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Djordjije Radovic
 */
public record PostUpdateDto(@NotBlank Long categoryId, String description, @NotBlank String title) {

}
