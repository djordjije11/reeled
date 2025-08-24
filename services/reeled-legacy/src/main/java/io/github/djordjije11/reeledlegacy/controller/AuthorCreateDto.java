package io.github.djordjije11.reeledlegacy.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Djordjije Radovic
 */
public record AuthorCreateDto(@NotBlank String name, @NotNull Long typeId, String bio, String imageUrl) {

}
