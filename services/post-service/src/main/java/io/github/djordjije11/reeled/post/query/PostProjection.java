package io.github.djordjije11.reeled.post.query;

import io.github.djordjije11.reeled.codes.PostCodes.PostCategory;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
public record PostProjection(PostCategory category, String description, Duration duration, String title, String videoUrl) {

}
