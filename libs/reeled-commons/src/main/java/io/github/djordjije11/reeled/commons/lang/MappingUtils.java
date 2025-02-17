package io.github.djordjije11.reeled.commons.lang;

import io.github.djordjije11.reeled.codes.BaseEnum;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

/**
 * @author Djordjije Radovic
 */
public final class MappingUtils {

    private static final ZoneId UTC = ZoneId.of("UTC");

    private MappingUtils() {
    }

    public static <T extends Enum<T> & BaseEnum> T mapToBaseEnum(CharSequence value, Class<T> targetEnumClass) {
        return Stream.of(targetEnumClass.getEnumConstants()).filter(ec -> ec.getValue().equals(String.valueOf(value))).findFirst().orElse(null);
    }

    public static String mapToBaseEnumValue(BaseEnum baseEnum) {
        return baseEnum == null ? null : baseEnum.getValue();
    }

    public static Duration mapToDuration(Long nanos) {
        return nanos == null ? null : Duration.ofNanos(nanos);
    }

    public static Long mapToEpochMilli(ZonedDateTime date) {
        return date == null ? null : date.toInstant().toEpochMilli();
    }

    public static Long mapToNanos(Duration duration) {
        return duration == null ? null : duration.toNanos();
    }

    public static String mapToString(CharSequence value) {
        return value == null ? null : value.toString();
    }

    public static ZonedDateTime mapToZonedDateTime(Long timestampMillis) {
        return timestampMillis == null ? null : ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestampMillis), UTC);
    }
}
