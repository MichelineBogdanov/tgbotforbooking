package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info;

import com.google.api.services.calendar.model.TimePeriod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleUtils {

    private static final LocalTime startOfDay = LocalTime.of(13, 0, 0, 1);
    private static final LocalTime endOfDay = LocalTime.of(20, 29);

    private static final List<LocalTime> slots = List.of(
            LocalTime.of(13, 0, 1)
            , LocalTime.of(14, 30, 1)
            , LocalTime.of(16, 0, 1)
            , LocalTime.of(17, 30, 1)
            , LocalTime.of(19, 0, 1));

    public ScheduleUtils() {
    }

    public static List<LocalDateTimePeriod> map(List<TimePeriod> freeIntervals) {
        return freeIntervals.stream()
                .map(timePeriod -> new LocalDateTimePeriod(
                        parseDateTimeFromRFC3339(timePeriod.getStart().toStringRfc3339()),
                        parseDateTimeFromRFC3339(timePeriod.getEnd().toStringRfc3339())))
                .toList();
    }

    public static Map<LocalDate, List<LocalTime>> getFreeSlots(List<TimePeriod> freeIntervals) {
        List<LocalDateTimePeriod> localDateTimePeriods = map(freeIntervals);
        List<LocalDateTimePeriod> dividedPeriods = new ArrayList<>();
        for (LocalDateTimePeriod localDateTimePeriod : localDateTimePeriods) {
            List<LocalDateTimePeriod> dividedByDays = divideByDays(localDateTimePeriod);
            dividedPeriods.addAll(dividedByDays);
        }
        System.out.println(dividedPeriods);
        Map<LocalDate, List<LocalTime>> collect = new HashMap<>();
        for (LocalDateTimePeriod dividedPeriod : dividedPeriods) {
            LocalDate key = dividedPeriod.getStart().toLocalDate();
            collect.putIfAbsent(key, new ArrayList<>());
            for (LocalTime slot : slots) {
                if (slot.isAfter(dividedPeriod.getStart().toLocalTime())
                        && slot.isBefore(dividedPeriod.getEnd().toLocalTime())) {
                    collect.get(key).add(slot.minusSeconds(1));
                }
            }
        }
        System.out.println(collect);
        return collect;
    }

    private static List<LocalDateTimePeriod> divideByDays(LocalDateTimePeriod period) {
        List<LocalDateTimePeriod> intervals = new ArrayList<>();
        LocalDateTime currentStart = period.getStart();
        while (!currentStart.toLocalDate().isAfter(period.getEnd().toLocalDate())) {
            LocalDateTime currentEnd = currentStart.toLocalDate().atTime(23, 59, 59);
            if (currentEnd.isAfter(period.getEnd())) {
                currentEnd = period.getEnd();
            }
            intervals.add(new LocalDateTimePeriod(currentStart, currentEnd));
            currentStart = currentStart.toLocalDate().plusDays(1).atStartOfDay();
        }
        intervals.removeIf(next -> next.getStart().equals(next.getEnd())
                || next.getStart().isAfter(LocalDateTime.of(next.getStart().toLocalDate(), endOfDay))
                || next.getEnd().isBefore(LocalDateTime.of(next.getEnd().toLocalDate(), startOfDay)));
        return intervals;
    }

    private static LocalDateTime parseDateTimeFromRFC3339(String rfc3339DateStr) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(rfc3339DateStr);
        return offsetDateTime.toLocalDateTime();
    }

    private static LocalDate parseDateFromRFC3339(String rfc3339DateStr) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(rfc3339DateStr);
        return offsetDateTime.toLocalDate();
    }

    private static LocalTime parseTimeFromRFC3339(String rfc3339DateStr) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(rfc3339DateStr);
        return offsetDateTime.toLocalTime();
    }

}
