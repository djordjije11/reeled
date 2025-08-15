package io.github.djordjije11.reeled.legacyconnector.infra.messaging;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class LegacyConnectorLegacyEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(LegacyConnectorLegacyEventHandler.class);


    public void handleLegacyPostEvent(Object event, MessageHeaders headers) {
        logger.info("Handling legacy connector legacy post event (event: {}, headers: {})", event, headers);

        if (event instanceof reeledlegacy.public$.post.Envelope postEnvelope) {
            logger.info("Received legacy post event: {}", postEnvelope.getAfter());
        } else {
            logger.error("Not expected legacy post event type: {}", event.getClass().getName());
        }
    }
}
