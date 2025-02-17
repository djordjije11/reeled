package io.github.djordjije11.reeled.shared.domain;

/**
 * @author Djordjije Radovic
 */
public record StoredEventProjection(Long id, String type, Long aggregateId, byte[] key, byte[] payload) {

}
