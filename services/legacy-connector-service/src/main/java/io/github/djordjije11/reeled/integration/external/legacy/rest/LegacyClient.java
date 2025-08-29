package io.github.djordjije11.reeled.integration.external.legacy.rest;

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
public class LegacyClient {

    private static final String X_REELED_NEW_PLATFORM_IMPORT = "X-Reeled-New-Platform-Import";

    private final RestTemplate restTemplate;

    private final String createAuthorUrl;

    private final String updateAuthorUrl;

    private final String deleteAuthorUrl;

    public LegacyClient(RestTemplate restTemplate, String endpoint) {
        this.restTemplate = restTemplate;
        this.createAuthorUrl = endpoint + "/v1/authors";
        this.updateAuthorUrl = endpoint + "/v1/authors/{authorId}";
        this.deleteAuthorUrl = endpoint + "/v1/authors/{authorId}";
    }

    public Long createAuthor(LegacyAuthorCreateDto legacyAuthorCreateDto) {
        Assert.notNull(legacyAuthorCreateDto, "authorCreateDto must not be null");

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(X_REELED_NEW_PLATFORM_IMPORT, "true");

        final ResponseEntity<Void> response = restTemplate.exchange(createAuthorUrl,
                HttpMethod.POST,
                new HttpEntity<>(legacyAuthorCreateDto, httpHeaders),
                Void.class);

        return Optional.of(response.getHeaders())
                .map(HttpHeaders::getLocation)
                .map(URI::getPath)
                .map(path -> path.replace("/", ""))
                .map(Long::parseLong)
                .orElseThrow(() -> new RuntimeException("Author id not found in response location header (response: %s)".formatted(response)));
    }

    public void updateAuthor(Long id, LegacyAuthorUpdateDto legacyAuthorUpdateDto) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(legacyAuthorUpdateDto, "authorUpdateDto must not be null");

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(X_REELED_NEW_PLATFORM_IMPORT, "true");

        restTemplate.exchange(updateAuthorUrl, HttpMethod.PUT, new HttpEntity<>(legacyAuthorUpdateDto, httpHeaders), Void.class, id);
    }

    public void deleteAuthor(Long id) {
        Assert.notNull(id, "id must not be null");

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(X_REELED_NEW_PLATFORM_IMPORT, "true");

        restTemplate.exchange(deleteAuthorUrl, HttpMethod.DELETE, new HttpEntity<>(httpHeaders), Void.class, id);
    }
}
