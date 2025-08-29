package io.github.djordjije11.reeled.legacyconnector.infra.web.rest;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;

/**
 * @author Djordjije Radovic
 */
public record AuthorCreateDto(String name, AuthorType type, String bio, String imageUrl) {

}
