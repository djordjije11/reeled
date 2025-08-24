package io.github.djordjije11.reeled.integration.internal.service.author.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * @author Djordjije Radovic
 */
public class AuthorServiceClient {

    private final RestTemplate restTemplate;

    private final String createUrl;

    private final String updateUrl;

    private final String deleteUrl;

    public AuthorServiceClient(RestTemplate restTemplate, String endpoint) {
        this.restTemplate = restTemplate;
        this.createUrl = endpoint + "/v1/authors";
        this.updateUrl = endpoint + "/v1/authors/{authorId}";
        this.deleteUrl = endpoint + "/v1/authors/{authorId}";
    }

    public void create(AuthorCreateDto authorCreateDto) {
        Assert.notNull(authorCreateDto, "authorCreateDto must not be null");

        restTemplate.exchange(createUrl, HttpMethod.POST, new HttpEntity<>(authorCreateDto), Void.class);
    }

    public void update(Long id, AuthorUpdateDto authorUpdateDto) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(authorUpdateDto, "authorUpdateDto must not be null");

        restTemplate.exchange(updateUrl, HttpMethod.PUT, new HttpEntity<>(authorUpdateDto), Void.class, id);
    }

    public void delete(Long id) {
        Assert.notNull(id, "id must not be null");

        // TODO: CHECK IF IT WORKS
        restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, Void.class, id);
    }
}
