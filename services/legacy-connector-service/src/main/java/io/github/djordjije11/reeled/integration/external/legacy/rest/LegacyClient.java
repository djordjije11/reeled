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

    private final RestTemplate restTemplate;

    private final String createAuthorUrl;

    private final String updateAuthorUrl;

    public LegacyClient(RestTemplate restTemplate, String endpoint) {
        this.restTemplate = restTemplate;
        this.createAuthorUrl = endpoint + "/v1/authors";
        this.updateAuthorUrl = endpoint + "/v1/authors/{authorId}";
    }

    public Long createAuthor(AuthorCreateDto authorCreateDto) {
        Assert.notNull(authorCreateDto, "authorCreateDto must not be null");

        final ResponseEntity<Void> response = restTemplate.exchange(createAuthorUrl, HttpMethod.POST, new HttpEntity<>(authorCreateDto), Void.class);

        return Optional.of(response.getHeaders())
                .map(HttpHeaders::getLocation)
                .map(URI::getPath)
                .map(path -> path.replace("/", ""))
                .map(Long::parseLong)
                .orElseThrow(() -> new RuntimeException("Author id not found in response location header (response: %s)".formatted(response)));
    }

    public void updateAuthor(Long id, AuthorUpdateDto authorUpdateDto) {
        Assert.notNull(authorUpdateDto, "authorUpdateDto must not be null");

        restTemplate.exchange(updateAuthorUrl, HttpMethod.PUT, new HttpEntity<>(authorUpdateDto), Void.class, id);
    }
}
