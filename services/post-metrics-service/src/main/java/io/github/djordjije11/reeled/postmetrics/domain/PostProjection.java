package io.github.djordjije11.reeled.postmetrics.domain;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
public record PostProjection(Long authorId, String categoryKey, Duration duration, boolean monetized) {

}
