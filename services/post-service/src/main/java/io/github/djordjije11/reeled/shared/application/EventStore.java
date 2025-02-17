package io.github.djordjije11.reeled.shared.application;

import io.github.djordjije11.reeled.commons.exception.NotFoundException;
import io.github.djordjije11.reeled.shared.domain.EventStoreProperties;
import io.github.djordjije11.reeled.shared.domain.PurgedEvent;
import io.github.djordjije11.reeled.shared.domain.StoredEvent;
import io.github.djordjije11.reeled.shared.domain.StoredEventRepository;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author Djordjije Radovic
 */
@Component
public class EventStore {

    private final EventStoreProperties eventStoreProperties;

    private final BindingServiceProperties bindingServiceProperties;

    private final StoredEventRepository storedEventRepository;

    private final Serializer<Object> keySerializer;

    private final Serializer<Object> valueSerializer;

    public EventStore(EventStoreProperties eventStoreProperties,
                      BindingServiceProperties bindingServiceProperties,
                      StoredEventRepository storedEventRepository,
                      @Qualifier("kafkaKeySerializer") Serializer<Object> keySerializer,
                      @Qualifier("kafkaValueSerializer") Serializer<Object> valueSerializer) {
        this.eventStoreProperties = eventStoreProperties;
        this.bindingServiceProperties = bindingServiceProperties;
        this.storedEventRepository = storedEventRepository;
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
    }

    public void append(Object event) {
        Assert.notNull(event, "event must not be null");

        //noinspection unchecked
        eventStoreProperties.findByClass(event.getClass())
                .stream()
                .map(configuration -> (EventStoreProperties.DomainEventConfiguration<Object>) configuration)
                .forEach(configuration -> {
                    final Long aggregateId = configuration.getAggregateId(event);
                    final String topic = bindingServiceProperties.getBindingDestination(configuration.binding());

                    storedEventRepository.save(new StoredEvent(configuration.name(),
                            aggregateId,
                            keySerializer.serialize(topic, configuration.toKey(aggregateId)),
                            event instanceof PurgedEvent ? null : valueSerializer.serialize(topic, event)));
                });
    }

    public void markAsPublished(Long id) {
        Assert.notNull(id, "id must not be null");

        final StoredEvent storedEvent = storedEventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stored event does not exist (id: %d)".formatted(id)));

        storedEvent.markAsPublished();

        storedEventRepository.save(storedEvent);
    }
}
