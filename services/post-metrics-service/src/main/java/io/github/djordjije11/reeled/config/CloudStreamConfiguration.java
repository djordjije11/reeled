package io.github.djordjije11.reeled.config;

import io.github.djordjije11.reeled.postmetrics.infra.messaging.PostMetricsEventHandler;
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
    @ConditionalOnProperty(prefix = "reeled.cloud.stream.post-metrics-event-handler", name = "enabled", havingValue = "true")
    static class PostMetricsEventHandlerConfiguration {

        @Bean
        BiConsumer<Object, MessageHeaders> postMetricsPostEventHandler(PostMetricsEventHandler postMetricsEventHandler) {
            return postMetricsEventHandler::handlePostEvent;
        }

        @Bean
        BiConsumer<Object, MessageHeaders> postMetricsAuthorEventHandler(PostMetricsEventHandler postMetricsEventHandler) {
            return postMetricsEventHandler::handleAuthorEvent;
        }
    }
}
