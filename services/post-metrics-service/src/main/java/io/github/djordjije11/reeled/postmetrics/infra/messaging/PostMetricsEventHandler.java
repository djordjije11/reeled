package io.github.djordjije11.reeled.postmetrics.infra.messaging;

import io.github.djordjije11.reeled.author.event.AuthorDeleted;
import io.github.djordjije11.reeled.author.event.AuthorUpserted;
import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import io.github.djordjije11.reeled.post.event.PostDeleted;
import io.github.djordjije11.reeled.post.event.PostUpserted;
import io.github.djordjije11.reeled.postmetrics.application.AuthorService;
import io.github.djordjije11.reeled.postmetrics.application.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.github.djordjije11.reeled.commons.infra.messaging.ReeledMessagingHeaders.EVENT_ID;
import static io.github.djordjije11.reeled.commons.infra.messaging.ReeledMessagingHeaders.EVENT_TIMESTAMP;
import static io.github.djordjije11.reeled.commons.lang.MappingUtils.mapToBaseEnum;
import static io.github.djordjije11.reeled.commons.lang.MappingUtils.mapToString;
import static io.github.djordjije11.reeled.commons.lang.MappingUtils.mapToZonedDateTime;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class PostMetricsEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(PostMetricsEventHandler.class);

    private final PostService postService;

    private final AuthorService authorService;

    public void handlePostEvent(Object event, MessageHeaders headers) {
        final String eventType = getEventType(event);
        final Object eventId = headers.get(EVENT_ID);
        final Object eventTimestamp = headers.get(EVENT_TIMESTAMP);
        final Object key = headers.get(KafkaHeaders.RECEIVED_KEY);

        logger.info("Handling post event (eventType: {}, eventId: {}, eventTimestamp: {}, key: {})", eventType, eventId, eventTimestamp, key);
        logger.debug("Event: {}", event);
        logger.debug("Headers: {}", headers);

        switch (event) {
            case PostUpserted postUpserted -> postService.save(postUpserted.getId(), PostMetricsEventMapper.mapToPostData(postUpserted));
            case PostDeleted postDeleted -> postService.delete(postDeleted.getId(), mapToZonedDateTime(postDeleted.getDeletedDate()));
            case KafkaNull ignored -> logger.debug("Ignored post event type (eventType: {}, eventId: {})", eventType, eventId);
            case null -> logger.debug("Ignored post event type (eventType: {}, eventId: {})", eventType, eventId);
            default -> logger.warn("Unsupported post event type (eventType: {}, eventId: {})", eventType, eventId);
        }

        logger.info("Post event successfully handled (eventType: {}, eventId: {})", eventType, eventId);
    }

    public void handleAuthorEvent(Object event, MessageHeaders headers) {
        final String eventType = getEventType(event);
        final Object eventId = headers.get(EVENT_ID);
        final Object eventTimestamp = headers.get(EVENT_TIMESTAMP);
        final Object key = headers.get(KafkaHeaders.RECEIVED_KEY);

        logger.info("Handling author event (eventType: {}, eventId: {}, eventTimestamp: {}, key: {})", eventType, eventId, eventTimestamp, key);
        logger.debug("Event: {}", event);
        logger.debug("Headers: {}", headers);

        switch (event) {
            case AuthorUpserted authorUpserted -> authorService.save(authorUpserted.getId(),
                    mapToString(authorUpserted.getName()),
                    mapToBaseEnum(authorUpserted.getType(), AuthorType.class));
            case AuthorDeleted authorDeleted -> authorService.delete(authorDeleted.getId());
            case KafkaNull ignored -> logger.debug("Ignored author event type (eventType: {}, eventId: {})", eventType, eventId);
            case null -> logger.debug("Ignored author event type (eventType: {}, eventId: {})", eventType, eventId);
            default -> logger.warn("Unsupported author event type (eventType: {}, eventId: {})", eventType, eventId);
        }

        logger.info("Author event successfully handled (eventType: {}, eventId: {})", eventType, eventId);
    }

    private static String getEventType(Object event) {
        return Optional.ofNullable(event).map(Object::getClass).map(Class::getName).orElse(null);
    }
}
