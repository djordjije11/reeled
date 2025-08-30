package io.github.djordjije11.reeled.integration.internal.service.author.rest;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;

/**
 * @author Djordjije Radovic
 */
public record AuthorDto(String name, AuthorType type, String bio, String imageUrl) {

}
