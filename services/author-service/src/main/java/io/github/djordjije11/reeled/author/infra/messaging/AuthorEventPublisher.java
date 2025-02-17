package io.github.djordjije11.reeled.author.infra.messaging;

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
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

import static io.github.djordjije11.reeled.commons.infra.messaging.ReeledMessagingHeaders.EVENT_ID;
import static io.github.djordjije11.reeled.commons.infra.messaging.ReeledMessagingHeaders.EVENT_TIMESTAMP;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class AuthorEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(AuthorEventPublisher.class);

    private static final String AUTHOR_EVENT_TOPIC_BINDING = "authorAuthorEventSource-out-0";

    private final StreamBridge streamBridge;

    @Async
    @TransactionalEventListener
    public void onAuthorUpserted(AuthorUpserted authorUpserted) {
        publishAuthorUpsertedEvent(authorUpserted);
    }

    private void publishAuthorUpsertedEvent(AuthorUpserted authorUpserted) {
        final String eventType = authorUpserted.getClass().getName();
        final String eventId = UUID.randomUUID().toString();
        final long eventTimestamp = System.currentTimeMillis();
        final AuthorUpsertedKey key = AuthorUpsertedKey.newBuilder().setId(authorUpserted.getId()).build();

        logger.info("Publishing author upserted event (eventType: {}, eventId: {}, eventTimestamp: {}, key: {})...", eventType, eventId, eventTimestamp, key);
        logger.debug("Event: {}", authorUpserted);

        final Message<AuthorUpserted> message = MessageBuilder.withPayload(authorUpserted)
                .setHeader(EVENT_ID, eventId)
                .setHeader(EVENT_TIMESTAMP, eventTimestamp)
                .setHeader(KafkaHeaders.KEY, key)
                .build();
        try {
            streamBridge.send(AUTHOR_EVENT_TOPIC_BINDING, message);

            logger.info("Author upserted event successfully published (eventType: {}, eventId: {})", eventType, eventId);
        } catch (RuntimeException e) {
            logger.error("Error while publishing author upserted event (eventType: {}, eventId: {})", eventType, eventId, e);
        }
    }

    @Async
    @TransactionalEventListener
    public void onAuthorDeleted(AuthorDeleted authorDeleted) {
        publishAuthorDeletedEvent(authorDeleted);
    }

    private void publishAuthorDeletedEvent(AuthorDeleted authorDeleted) {
        final String eventType = authorDeleted.getClass().getName();
        final String eventId = UUID.randomUUID().toString();
        final long eventTimestamp = System.currentTimeMillis();
        final AuthorDeletedKey key = AuthorDeletedKey.newBuilder().setId(authorDeleted.getId()).build();

        logger.info("Publishing author deleted event (eventType: {}, eventId: {}, eventTimestamp: {}, key: {})...", eventType, eventId, eventTimestamp, key);
        logger.debug("Event: {}", authorDeleted);

        final Message<AuthorDeleted> message = MessageBuilder.withPayload(authorDeleted)
                .setHeader(EVENT_ID, eventId)
                .setHeader(EVENT_TIMESTAMP, eventTimestamp)
                .setHeader(KafkaHeaders.KEY, key)
                .build();
        try {
            streamBridge.send(AUTHOR_EVENT_TOPIC_BINDING, message);

            logger.info("Author deleted event successfully published (eventType: {}, eventId: {})", eventType, eventId);
        } catch (RuntimeException e) {
            logger.error("Error while publishing author deleted event (eventType: {}, eventId: {})", eventType, eventId, e);
        }
    }
}
