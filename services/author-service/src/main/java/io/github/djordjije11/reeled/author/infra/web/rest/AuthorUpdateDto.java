package io.github.djordjije11.reeled.author.infra.web.rest;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Djordjije Radovic
 */
public record AuthorUpdateDto(@NotBlank String name, String bio, String imageUrl) {

}
