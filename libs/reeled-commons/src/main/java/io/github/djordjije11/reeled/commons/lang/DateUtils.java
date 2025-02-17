package io.github.djordjije11.reeled.commons.lang;

import org.springframework.util.Assert;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Djordjije Radovic
 */
public final class DateUtils {

    private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

    private DateUtils() {
    }

    public static ZonedDateTime fromDateToSystemDefaultZone(Date inputDate) {
        return fromDateToZone(inputDate, ZoneId.systemDefault());
    }

    public static ZonedDateTime fromDateToUTC(Date inputDate) {
        return fromDateToZone(inputDate, UTC_ZONE_ID);
    }

    public static ZonedDateTime fromDateToZone(Date inputDate, ZoneId zoneId) {
        Assert.notNull(inputDate, "inputDate must be specified");
        Assert.notNull(zoneId, "zoneId must be specified");

        return inputDate.toInstant().atZone(zoneId);
    }

    public static boolean isEqual(ZonedDateTime first, ZonedDateTime second) {
        return (first == null && second == null) || (first != null && second != null && (first == second || first.isEqual(second)));
    }

    public static ZonedDateTime nowInUTC() {
        return ZonedDateTime.now(UTC_ZONE_ID);
    }
}
