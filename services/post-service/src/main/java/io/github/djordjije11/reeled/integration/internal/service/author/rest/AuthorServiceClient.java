package io.github.djordjije11.reeled.integration.internal.service.author.rest;

import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * @author Djordjije Radovic
 */
public class AuthorServiceClient {

    private final RestTemplate restTemplate;

    private final String getUrl;

    public AuthorServiceClient(RestTemplate restTemplate, String endpoint) {
        this.restTemplate = restTemplate;
        this.getUrl = endpoint + "/v1/authors/{authorId}";
    }

    public AuthorDto get(Long id) {
        Assert.notNull(id, "id must not be null");

        return restTemplate.exchange(getUrl, HttpMethod.GET, null, AuthorDto.class, id).getBody();
    }
}
