package ru.bogdanov.tgbotforbooking.services.telegram.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {

    private static final String DATE_TIME_STRING_PATTERN = "dd-MM-yyyy HH:mm";
    private static final String DATE_STRING_PATTERN = "dd-MM-yyyy";
    private static final String DAY_STRING_PATTERN = "dd.MM";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_STRING_PATTERN);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_STRING_PATTERN);
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern(DAY_STRING_PATTERN);

    public static Date fromLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String fromLocalDateTimeToDateTimeString(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TIME_FORMATTER);
    }

    public static String fromLocalDateToDateString(LocalDate localDate) {
        return localDate.format(DATE_FORMATTER);
    }

    public static String fromLocalDateToDayString(LocalDate localDate) {
        return localDate.format(DAY_FORMATTER);
    }

    public static LocalDateTime parseDateTimeFromRFC3339(String rfc3339DateStr) {
        return OffsetDateTime.parse(rfc3339DateStr).toLocalDateTime();
    }

}
