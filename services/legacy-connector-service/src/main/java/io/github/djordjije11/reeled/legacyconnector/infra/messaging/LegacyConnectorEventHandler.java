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
public class LegacyConnectorEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(LegacyConnectorEventHandler.class);

    public void handlePostEvent(Object event, MessageHeaders headers) {
        logger.info("Handling legacy connector post event (event: {}, headers: {})", event, headers);
    }
}
