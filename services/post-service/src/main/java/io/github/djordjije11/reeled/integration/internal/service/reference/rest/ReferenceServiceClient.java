package io.github.djordjije11.reeled.integration.internal.service.reference.rest;

import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * @author Djordjije Radovic
 */
public class ReferenceServiceClient {

    private final RestTemplate restTemplate;

    private final String getPostCategoryUrl;

    public ReferenceServiceClient(RestTemplate restTemplate, String endpoint) {
        this.restTemplate = restTemplate;
        this.getPostCategoryUrl = endpoint + "/v1/post-categories/{postCategoryKey}";
    }

    public PostCategoryDto getPostCategory(String key) {
        Assert.hasText(key, "key must be provided");

        return restTemplate.exchange(getPostCategoryUrl, HttpMethod.GET, null, PostCategoryDto.class, key).getBody();
    }
}
