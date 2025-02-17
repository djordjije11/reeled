package io.github.djordjije11.reeled.codes;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Djordjije Radovic
 */
public interface BaseEnum {

    @JsonValue
    String getValue();
}
