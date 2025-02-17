package io.github.djordjije11.reeled.postmetrics.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Date;
import java.time.YearMonth;

/**
 * @author Djordjije Radovic
 */
@Converter(autoApply = true)
class YearMonthDateConverter implements AttributeConverter<YearMonth, Date> {

    @Override
    public Date convertToDatabaseColumn(YearMonth attribute) {
        return attribute != null ? Date.valueOf(attribute.atDay(1)) : null;
    }

    @Override
    public YearMonth convertToEntityAttribute(Date dbData) {
        return dbData != null ? YearMonth.from(dbData.toLocalDate()) : null;
    }
}
