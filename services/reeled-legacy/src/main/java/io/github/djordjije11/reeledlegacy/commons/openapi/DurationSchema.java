package io.github.djordjije11.reeledlegacy.commons.openapi;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Djordjije Radovic
 */
@Schema(format = "duration", example = "PT3H4M2S")
public final class DurationSchema {

    private DurationSchema() {
    }
}
