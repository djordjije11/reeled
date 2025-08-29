package io.github.djordjije11.reeled.config;

import io.github.djordjije11.reeled.integration.internal.service.legacyconnector.rest.LegacyConnectorServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
@Configuration
class ClientConfiguration {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.connectTimeout(Duration.ofMillis(3000)).readTimeout(Duration.ofMillis(10000)).build();
    }

    @Bean
    LegacyConnectorServiceClient legacyConnectorServiceClient(RestTemplate restTemplate,
                                                              @Value("${reeled.integration.internal.service.legacy-connector.rest.endpoint}") String endpoint) {
        return new LegacyConnectorServiceClient(restTemplate, endpoint);
    }
}
