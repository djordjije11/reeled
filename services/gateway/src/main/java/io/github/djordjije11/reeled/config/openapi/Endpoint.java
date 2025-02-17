package io.github.djordjije11.reeled.config.openapi;

import io.swagger.v3.oas.models.PathItem;

/**
 * @author Djordjije Radovic
 */
record Endpoint(String path, PathItem.HttpMethod method) {

    static Endpoint of(String path, String method) {
        return new Endpoint(path, PathItem.HttpMethod.valueOf(method.toUpperCase()));
    }
}
