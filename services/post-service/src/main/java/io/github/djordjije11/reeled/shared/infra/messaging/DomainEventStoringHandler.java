package io.github.djordjije11.reeled.shared.infra.messaging;

import io.github.djordjije11.reeled.shared.application.EventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class DomainEventStoringHandler {

    private final EventStore eventStore;

    @EventListener
    public void handleEvent(Object event) {
        eventStore.append(event);
    }
}
