package io.github.djordjije11.reeled.postmetrics.domain;

import io.github.djordjije11.reeled.codes.PostCodes.PostCategory;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
public record PostProjection(Long authorId, PostCategory category, Duration duration, boolean monetized) {

}
