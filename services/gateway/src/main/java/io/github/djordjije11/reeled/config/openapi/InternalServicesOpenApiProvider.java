package io.github.djordjije11.reeled.config.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Djordjije Radovic
 */
class InternalServicesOpenApiProvider {

    private static final OpenAPIV3Parser OPEN_API_PARSER = new OpenAPIV3Parser();

    private final Set<String> internalServicesApiDocsUrls;

    private final WebClient webClient;

    InternalServicesOpenApiProvider(OpenApiInternalServicesProperties openApiInternalServicesProperties, WebClient.Builder webClientBuilder) {
        this.internalServicesApiDocsUrls = openApiInternalServicesProperties.urls();
        this.webClient = webClientBuilder.build();
    }

    List<OpenAPI> getInternalServiceOpenApis() {
        return Optional.ofNullable(Flux.merge(internalServicesApiDocsUrls.stream()
                .map(internalServiceApiDocsUrl -> webClient.get()
                        .uri(internalServiceApiDocsUrl)
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(OPEN_API_PARSER::readContents)
                        .map(SwaggerParseResult::getOpenAPI)
                        .retryWhen(Retry.fixedDelay(20, Duration.ofSeconds(2))))
                .toList()).collectList().block()).orElse(Collections.emptyList());
    }
}
