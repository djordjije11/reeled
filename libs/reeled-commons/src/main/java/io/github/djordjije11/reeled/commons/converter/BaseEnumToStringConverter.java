package io.github.djordjije11.reeled.commons.converter;

import io.github.djordjije11.reeled.codes.BaseEnum;
import jakarta.persistence.AttributeConverter;

/**
 * @author Djordjije Radovic
 */
public class BaseEnumToStringConverter<T extends Enum<T> & BaseEnum> implements AttributeConverter<T, String> {

    private final Class<T> clazz;

    public BaseEnumToStringConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        return BaseEnumUtils.mapToEnum(dbData, clazz);
    }
}
