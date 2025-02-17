package io.github.djordjije11.reeled.author.infra.messaging;

import io.github.djordjije11.reeled.author.application.AuthorPurgedEventPublisher;
import io.github.djordjije11.reeled.author.domain.AuthorPurged;
import io.github.djordjije11.reeled.author.event.AuthorDeleted;
import io.github.djordjije11.reeled.author.event.AuthorDeletedKey;
import io.github.djordjije11.reeled.author.event.AuthorUpserted;
import io.github.djordjije11.reeled.author.event.AuthorUpsertedKey;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static io.github.djordjije11.reeled.commons.infra.messaging.ReeledMessagingHeaders.EVENT_ID;
import static io.github.djordjije11.reeled.commons.infra.messaging.ReeledMessagingHeaders.EVENT_TIMESTAMP;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class AuthorKafkaPurgedEventPublisher implements AuthorPurgedEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(AuthorKafkaPurgedEventPublisher.class);

    private static final String AUTHOR_EVENT_TOPIC_BINDING = "authorAuthorEventSource-out-0";

    private final StreamBridge streamBridge;

    @Override
    public void publishAuthorPurged(AuthorPurged authorPurged) {
        publishAuthorUpsertedTombstoneEvent(authorPurged.id());
        publishAuthorDeletedTombstoneEvent(authorPurged.id());
    }

    private void publishAuthorUpsertedTombstoneEvent(Long id) {
        final KafkaNull event = KafkaNull.INSTANCE;
        final String eventType = AuthorUpserted.class.getName();
        final String eventId = UUID.randomUUID().toString();
        final long eventTimestamp = System.currentTimeMillis();
        final AuthorUpsertedKey key = AuthorUpsertedKey.newBuilder().setId(id).build();

        logger.info("Publishing author upserted tombstone event (eventType: {}, eventId: {}, eventTimestamp: {}, key: {})...",
                eventType,
                eventId,
                eventTimestamp,
                key);
        logger.debug("Event: {}", event);

        final Message<KafkaNull> message = MessageBuilder.withPayload(event)
                .setHeader(EVENT_ID, eventId)
                .setHeader(EVENT_TIMESTAMP, eventTimestamp)
                .setHeader(KafkaHeaders.KEY, key)
                .build();
        try {
            streamBridge.send(AUTHOR_EVENT_TOPIC_BINDING, message);

            logger.info("Author upserted tombstone event successfully published (eventType: {}, eventId: {})", eventType, eventId);
        } catch (RuntimeException e) {
            logger.error("Error while publishing author upserted tombstone event (eventType: {}, eventId: {})", eventType, eventId, e);
        }
    }

    private void publishAuthorDeletedTombstoneEvent(Long id) {
        final KafkaNull event = KafkaNull.INSTANCE;
        final String eventType = AuthorDeleted.class.getName();
        final String eventId = UUID.randomUUID().toString();
        final long eventTimestamp = System.currentTimeMillis();
        final AuthorDeletedKey key = AuthorDeletedKey.newBuilder().setId(id).build();

        logger.info("Publishing author deleted tombstone event (eventType: {}, eventId: {}, eventTimestamp: {}, key: {})...",
                eventType,
                eventId,
                eventTimestamp,
                key);
        logger.debug("Event: {}", event);

        final Message<KafkaNull> message = MessageBuilder.withPayload(event)
                .setHeader(EVENT_ID, eventId)
                .setHeader(EVENT_TIMESTAMP, eventTimestamp)
                .setHeader(KafkaHeaders.KEY, key)
                .build();
        try {
            streamBridge.send(AUTHOR_EVENT_TOPIC_BINDING, message);

            logger.info("Author deleted tombstone event successfully published (eventType: {}, eventId: {})", eventType, eventId);
        } catch (RuntimeException e) {
            logger.error("Error while publishing author deleted tombstone event (eventType: {}, eventId: {})", eventType, eventId, e);
        }
    }
}
