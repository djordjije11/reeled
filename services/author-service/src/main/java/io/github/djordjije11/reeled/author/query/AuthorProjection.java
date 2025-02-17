package io.github.djordjije11.reeled.author.query;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;

/**
 * @author Djordjije Radovic
 */
public record AuthorProjection(String name, AuthorType type, String bio, String imageUrl) {

}
