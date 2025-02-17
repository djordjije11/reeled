package io.github.djordjije11.reeled.shared.application;

/**
 * @author Djordjije Radovic
 */
public interface StoredEventPublisher {

    void publish(String binding, Long partitioningKey, byte[] key, byte[] payload);
}
