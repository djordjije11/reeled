package io.github.djordjije11.reeledlegacy.controller;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Djordjije Radovic
 */
public record AuthorUpdateDto(@NotBlank String name, String bio, String imageUrl) {

}
