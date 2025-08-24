package io.github.djordjije11.reeled.author.infra.web.rest;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Djordjije Radovic
 */
public record AuthorCreateDto(Long id, @NotBlank String name, @NotNull AuthorType type, String bio, String imageUrl) {

}
