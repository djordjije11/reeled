package io.github.djordjije11.reeled.legacyconnector.infra.messaging;

import io.github.djordjije11.reeled.author.event.AuthorDeleted;
import io.github.djordjije11.reeled.author.event.AuthorUpserted;
import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import io.github.djordjije11.reeled.legacyconnector.application.AuthorLegacyAuthorSyncEntryService;
import io.github.djordjije11.reeled.legacyconnector.domain.AuthorData;
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

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class LegacyConnectorEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(LegacyConnectorEventHandler.class);

    private final AuthorLegacyAuthorSyncEntryService authorLegacyAuthorSyncEntryService;

    public void handleAuthorEvent(Object event, MessageHeaders headers) {
        final String eventType = getEventType(event);
        final Object eventId = headers.get(EVENT_ID);
        final Object eventTimestamp = headers.get(EVENT_TIMESTAMP);
        final Object key = headers.get(KafkaHeaders.RECEIVED_KEY);

        logger.info("Handling author event (eventType: {}, eventId: {}, eventTimestamp: {}, key: {})", eventType, eventId, eventTimestamp, key);
        logger.debug("Event: {}", event);
        logger.debug("Headers: {}", headers);

        switch (event) {
            case AuthorUpserted authorUpserted -> authorLegacyAuthorSyncEntryService.sync(authorUpserted.getId(),
                    new AuthorData(mapToString(authorUpserted.getName()),
                            mapToBaseEnum(authorUpserted.getType(), AuthorType.class),
                            mapToString(authorUpserted.getBio()),
                            mapToString(authorUpserted.getImageUrl())),
                    authorUpserted.getLegacy());
            case AuthorDeleted authorDeleted -> authorLegacyAuthorSyncEntryService.syncDeleteFromNewPlatform(authorDeleted.getId());
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
