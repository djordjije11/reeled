package io.github.djordjije11.reeled.integration.internal.service.legacyconnector.rest;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;

/**
 * @author Djordjije Radovic
 */
public record AuthorCreateDto(String name, AuthorType type, String bio, String imageUrl) {

}
