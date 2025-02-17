package io.github.djordjije11.reeled.post.domain;

import io.github.djordjije11.reeled.shared.domain.PurgedEvent;

/**
 * @author Djordjije Radovic
 */
public record PostPurged(Long id) implements PurgedEvent {

    @Override
    public Long getId() {
        return id;
    }
}
