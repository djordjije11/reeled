package io.github.djordjije11.reeled.commons.converter;

import io.github.djordjije11.reeled.codes.BaseEnum;
import org.springframework.core.convert.converter.Converter;

import java.util.EnumSet;

/**
 * @author Djordjije Radovic
 */
public class StringToBaseEnumConverter<T extends Enum<T> & BaseEnum> implements Converter<String, T> {

    private final Class<T> clazz;

    public StringToBaseEnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T convert(String value) {
        return (EnumSet.allOf(this.clazz)
                .stream()
                .filter(x -> x.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unable to convert value of %s to enum %s", value, this.clazz.getName()))));
    }
}
