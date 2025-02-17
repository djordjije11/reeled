package io.github.djordjije11.reeled.shared.infra.messaging;

import io.github.djordjije11.reeled.shared.application.StoredEventPublisher;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import java.util.UUID;

import static io.github.djordjije11.reeled.commons.infra.messaging.ReeledMessagingHeaders.EVENT_ID;
import static io.github.djordjije11.reeled.commons.infra.messaging.ReeledMessagingHeaders.EVENT_TIMESTAMP;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
public class KafkaStoredEventPublisher implements StoredEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStoredEventPublisher.class);

    private static final String PARTITIONING_KEY = "reeled_partitioningKey";

    private final StreamBridge streamBridge;

    public void publish(String binding, Long partitioningKey, byte[] key, byte[] payload) {
        Assert.hasText(binding, "binding must be provided");
        Assert.notNull(partitioningKey, "partitioningKey must not be null");
        Assert.notNull(key, "key must not be null");

        final String eventId = UUID.randomUUID().toString();
        final long eventTimestamp = System.currentTimeMillis();

        logger.info("Publishing event (eventId: {}, eventTimestamp: {}, binding: {}, partitioningKey: {})...",
                eventId,
                eventTimestamp,
                binding,
                partitioningKey);
        logger.debug("Payload: {}", payload);

        final Message<?> message = MessageBuilder.withPayload(payload == null ? KafkaNull.INSTANCE : payload)
                .setHeader(EVENT_ID, eventId)
                .setHeader(EVENT_TIMESTAMP, eventTimestamp)
                .setHeader(PARTITIONING_KEY, partitioningKey)
                .setHeader(KafkaHeaders.KEY, key)
                .build();

        streamBridge.send(binding, message);

        logger.info("Event successfully published (eventId: {}, eventTimestamp: {}, binding: {}, partitioningKey: {})...",
                eventId,
                eventTimestamp,
                binding,
                partitioningKey);
    }
}
