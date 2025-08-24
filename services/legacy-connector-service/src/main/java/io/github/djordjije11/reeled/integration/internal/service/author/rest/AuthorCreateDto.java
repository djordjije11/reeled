package io.github.djordjije11.reeled.integration.internal.service.author.rest;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;

/**
 * @author Djordjije Radovic
 */
public record AuthorCreateDto(Long id, String name, AuthorType type, String bio, String imageUrl) {

}
