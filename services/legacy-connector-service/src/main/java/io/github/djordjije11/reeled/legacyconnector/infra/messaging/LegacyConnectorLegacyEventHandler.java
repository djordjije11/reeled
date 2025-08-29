package io.github.djordjije11.reeled.legacyconnector.infra.messaging;

import io.github.djordjije11.reeled.legacyconnector.application.AuthorLegacyAuthorSyncEntryService;
import io.github.djordjije11.reeled.legacyconnector.domain.LegacyAuthorData;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.github.djordjije11.reeled.commons.lang.MappingUtils.mapToString;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class LegacyConnectorLegacyEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(LegacyConnectorLegacyEventHandler.class);

    private final AuthorLegacyAuthorSyncEntryService authorLegacyAuthorSyncEntryService;

    public void handleLegacyAuthorEvent(Object event, MessageHeaders headers) {
        final String eventType = Optional.ofNullable(event).map(Object::getClass).map(Class::getName).orElse(null);
        final Object key = headers.get(KafkaHeaders.RECEIVED_KEY);

        logger.info("Handling legacy author event (eventType: {}, key: {})...", eventType, key);
        logger.debug("Event: {}", event);
        logger.debug("Headers: {}", headers);

        if (event instanceof reeledlegacy.public$.author.Envelope authorEnvelope && authorEnvelope.getAfter() != null) {
            final reeledlegacy.public$.author.Value author = authorEnvelope.getAfter();
            authorLegacyAuthorSyncEntryService.sync(author.getId(),
                    new LegacyAuthorData(mapToString(author.getName()), author.getTypeId(), mapToString(author.getBio()), mapToString(author.getImageUrl())));
        } else if (event instanceof reeledlegacy.public$.author.Envelope authorEnvelope
                && authorEnvelope.getAfter() == null
                && key instanceof reeledlegacy.public$.author.Key authorKey) {
            authorLegacyAuthorSyncEntryService.syncDeleteFromLegacy(authorKey.getId());
        } else {
            logger.debug("Ignored legacy author event (eventType: {}, key: {})", event, key);
        }

        logger.info("Legacy author event successfully handled (eventType: {}, key: {})", eventType, key);
    }
}
