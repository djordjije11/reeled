package io.github.djordjije11.reeledlegacy.model;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
public record PostProjection(String category, String description, Duration duration, String title, String videoUrl) {

}
