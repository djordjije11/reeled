package io.github.djordjije11.reeled.config;

import io.github.djordjije11.reeled.legacyconnector.infra.messaging.LegacyConnectorEventHandler;
import io.github.djordjije11.reeled.legacyconnector.infra.messaging.LegacyConnectorLegacyEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageHeaders;

import java.util.function.BiConsumer;

/**
 * @author Djordjije Radovic
 */
@Configuration
class CloudStreamConfiguration {

    @RequiredArgsConstructor
    @Configuration
    @ConditionalOnProperty(prefix = "reeled.cloud.stream.legacy-connector-event-handler", name = "enabled", havingValue = "true")
    static class PostMetricsEventHandlerConfiguration {

        @Bean
        BiConsumer<Object, MessageHeaders> legacyConnectorPostEventHandler(LegacyConnectorEventHandler legacyConnectorEventHandler) {
            return legacyConnectorEventHandler::handlePostEvent;
        }

        @Bean
        BiConsumer<Object, MessageHeaders> legacyConnectorLegacyPostEventHandler(LegacyConnectorLegacyEventHandler legacyConnectorLegacyEventHandler) {
            return legacyConnectorLegacyEventHandler::handleLegacyPostEvent;
        }
    }
}
