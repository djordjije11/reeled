package io.github.djordjije11.reeled.author.application;

import io.github.djordjije11.reeled.author.domain.AuthorPurged;

/**
 * @author Djordjije Radovic
 */
public interface AuthorPurgedEventPublisher {

    void publishAuthorPurged(AuthorPurged authorPurged);
}
