package io.github.djordjije11.reeled.commons.converter;

import io.github.djordjije11.reeled.codes.BaseEnum;

import java.util.EnumSet;

/**
 * @author Djordjije Radovic
 */
public final class BaseEnumUtils {

    private BaseEnumUtils() {
    }

    public static <T extends Enum<T> & BaseEnum> T mapToEnum(String value, Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }

        if (value == null || value.isBlank()) {
            return null;
        }

        return EnumSet.allOf(clazz).stream().filter(f -> f.getValue().equals(value)).findAny().orElse(null);
    }
}
