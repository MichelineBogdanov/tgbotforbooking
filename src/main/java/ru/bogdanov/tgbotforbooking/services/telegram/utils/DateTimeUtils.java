package ru.bogdanov.tgbotforbooking.services.telegram.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {

    private static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm";
    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private static final String DAY_PATTERN = "dd.MM";

    public static Date fromLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String fromLocalDateTimeToDateTimeString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    public static String fromLocalDateToDateString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static String fromLocalDateToDayString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DAY_PATTERN));
    }

    public static LocalDateTime parseDateTimeFromRFC3339(String rfc3339DateStr) {
        return OffsetDateTime.parse(rfc3339DateStr).toLocalDateTime();
    }

}
