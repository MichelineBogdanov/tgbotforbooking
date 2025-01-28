package ru.bogdanov.tgbotforbooking.servises.telegram.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {

    public static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm";
    public static final String DATE_PATTERN = "dd-MM-yyyy";

    public static Date fromLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String fromLocalDateTimeToDateTimeString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(DateTimeUtils.DATE_TIME_PATTERN));
    }

    public static String fromLocalDateToDateString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DateTimeUtils.DATE_PATTERN));
    }

}
