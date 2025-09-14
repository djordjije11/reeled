package io.github.djordjije11.reeled.integration.internal.service.legacyconnector.rest;

import io.github.djordjije11.reeled.commons.exception.ReeledException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

/**
 * @author Djordjije Radovic
 */
public class LegacyConnectorServiceClient {

    private final RestTemplate restTemplate;

    private final String createAuthorUrl;

    public LegacyConnectorServiceClient(RestTemplate restTemplate, String endpoint) {
        this.restTemplate = restTemplate;
        this.createAuthorUrl = endpoint + "/v1/legacy-authors";
    }

    public Long createAuthor(AuthorCreateDto authorCreateDto) {
        Assert.notNull(authorCreateDto, "authorCreateDto must not be null");

        final ResponseEntity<Void> response = restTemplate.exchange(createAuthorUrl, HttpMethod.POST, new HttpEntity<>(authorCreateDto), Void.class);

        return Optional.of(response.getHeaders())
                .map(HttpHeaders::getLocation)
                .map(URI::getPath)
                .map(path -> path.replace("/", ""))
                .map(Long::parseLong)
                .orElseThrow(() -> new ReeledException("Author id not found in response location header (response: %s)".formatted(response)));
    }
}
