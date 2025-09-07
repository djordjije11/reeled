package io.github.djordjije11.reeled.post.query;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
public record PostProjection(String categoryKey, String description, Duration duration, String title, String videoUrl) {

}
