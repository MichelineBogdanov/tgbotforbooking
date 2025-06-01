package ru.bogdanov.tgbotforbooking.servises.telegram.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeUtilsTest {

    @Test
    void fromLocalDateTimeToDate() {
        LocalDateTime localDateTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
        Date date = DateTimeUtils.fromLocalDateTimeToDate(localDateTime);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date checkDate = calendar.getTime();

        assertEquals(checkDate, date);
    }

    @Test
    void fromLocalDateTimeToDateTimeString() {
        LocalDateTime localDateTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
        String dateTimeString = DateTimeUtils.fromLocalDateTimeToDateTimeString(localDateTime);

        String checkDateTimeString = "01-01-2000 00:00";

        assertEquals(checkDateTimeString, dateTimeString);
    }

    @Test
    void fromLocalDateToDateString() {
        LocalDate localDate = LocalDate.of(2000, 1, 1);
        String dateString = DateTimeUtils.fromLocalDateToDateString(localDate);

        String checkDateString = "01-01-2000";

        assertEquals(checkDateString, dateString);
    }

    @Test
    void fromLocalDateToDayString() {
        LocalDate localDate = LocalDate.of(2000, 1, 1);
        String dateString = DateTimeUtils.fromLocalDateToDayString(localDate);

        String checkDateString = "01.01";

        assertEquals(checkDateString, dateString);
    }

    @Test
    void parseDateTimeFromRFC3339() {
        String localDate = "2000-01-01T00:00:00+03:00";
        LocalDateTime localDateTime = DateTimeUtils.parseDateTimeFromRFC3339(localDate);

        LocalDateTime checkLocalDateTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);

        assertEquals(checkLocalDateTime, localDateTime);
    }
}