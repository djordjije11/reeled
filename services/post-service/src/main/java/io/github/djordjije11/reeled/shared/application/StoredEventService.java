package io.github.djordjije11.reeled.shared.application;

import io.github.djordjije11.reeled.commons.exception.ReeledException;
import io.github.djordjije11.reeled.shared.domain.EventStoreProperties;
import io.github.djordjije11.reeled.shared.domain.StoredEventProjection;
import io.github.djordjije11.reeled.shared.domain.StoredEventStatus;
import io.github.djordjije11.reeled.shared.domain.StoredEventSupportRepository;
import org.springframework.stereotype.Component;

/**
 * @author Djordjije Radovic
 */
@Component
public class StoredEventService {

    private static final int FETCH_SIZE = 500;

    private final EventStoreProperties eventStoreProperties;

    private final StoredEventSupportRepository storedEventSupportRepository;

    private final StoredEventPublisher storedEventPublisher;

    private final EventStore eventStore;

    public StoredEventService(EventStoreProperties eventStoreProperties,
                              StoredEventSupportRepository storedEventSupportRepository,
                              StoredEventPublisher storedEventPublisher,
                              EventStore eventStore) {
        this.eventStoreProperties = eventStoreProperties;
        this.storedEventSupportRepository = storedEventSupportRepository;
        this.storedEventPublisher = storedEventPublisher;
        this.eventStore = eventStore;
    }

    public void publishPending() {
        storedEventSupportRepository.findAllByStatus(StoredEventStatus.PENDING, FETCH_SIZE).forEach(this::publish);
    }

    private void publish(StoredEventProjection storedEvent) {
        eventStoreProperties.findBindingByType(storedEvent.type()).ifPresentOrElse(binding -> {
            try {
                storedEventPublisher.publish(binding, storedEvent.aggregateId(), storedEvent.key(), storedEvent.payload());
            } catch (RuntimeException e) {
                throw new ReeledException("Error occurred while publishing event (event: %s)".formatted(storedEvent), e);
            }

            eventStore.markAsPublished(storedEvent.id());
        }, () -> {
            throw new ReeledException("Publishing unsupported event, configuration doesn't exist. Processing skipped (event: %s)".formatted(storedEvent));
        });
    }
}
