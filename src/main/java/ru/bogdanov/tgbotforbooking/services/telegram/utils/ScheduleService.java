package ru.bogdanov.tgbotforbooking.services.telegram.utils;

import com.google.api.services.calendar.model.TimePeriod;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleService {

    @Value("${schedule.start-day}")
    private LocalTime startOfDay;
    @Value("${schedule.end-day}")
    private LocalTime endOfDay;
    @Value("${schedule.slot-duration}")
    private Duration slotDuration;

    private static final List<LocalTime> slots = new ArrayList<>();

    @PostConstruct
    public void init() {
        LocalTime current = startOfDay;
        while (current.isBefore(endOfDay)) {
            slots.add(current);
            current = current.plus(slotDuration);
        }
    }

    public Map<LocalDate, List<LocalTime>> getFreeSlots(List<TimePeriod> freeIntervals, Integer duration) {
        List<LocalDateTimePeriod> localDateTimePeriods = map(freeIntervals);
        List<LocalDateTimePeriod> dividedPeriods = new ArrayList<>();
        for (LocalDateTimePeriod localDateTimePeriod : localDateTimePeriods) {
            List<LocalDateTimePeriod> dividedByDays = divideByDays(localDateTimePeriod);
            dividedPeriods.addAll(dividedByDays);
        }
        Map<LocalDate, List<LocalTime>> collect = new HashMap<>();
        for (LocalDateTimePeriod dividedPeriod : dividedPeriods) {
            LocalDate key = dividedPeriod.start().toLocalDate();
            collect.putIfAbsent(key, new ArrayList<>());
            for (LocalTime slot : slots) {
                if (!slot.isBefore(dividedPeriod.start().toLocalTime())
                        && !slot.plusMinutes(duration).isAfter(dividedPeriod.end().toLocalTime())) {
                    collect.get(key).add(slot);
                }
            }
            if (collect.get(key).isEmpty()) {
                collect.remove(key);
            }
        }
        return collect;
    }

    public boolean isSlotPresentIn(LocalTime slot, List<LocalTime> times) {
        return times.stream().anyMatch(currentSlot -> currentSlot.equals(slot));
    }

    public List<LocalTime> getSlots() {
        return slots;
    }

    private List<LocalDateTimePeriod> map(List<TimePeriod> freeIntervals) {
        return freeIntervals.stream()
                .map(timePeriod -> new LocalDateTimePeriod(
                        DateTimeUtils.parseDateTimeFromRFC3339(timePeriod.getStart().toStringRfc3339()),
                        DateTimeUtils.parseDateTimeFromRFC3339(timePeriod.getEnd().toStringRfc3339())))
                .toList();
    }

    private List<LocalDateTimePeriod> divideByDays(LocalDateTimePeriod period) {
        List<LocalDateTimePeriod> intervals = new ArrayList<>();
        LocalDateTime currentStart = period.start().isAfter(period.start().toLocalDate().atTime(startOfDay))
                ? period.start()
                : period.start().toLocalDate().atTime(startOfDay);
        while (!currentStart.toLocalDate().isAfter(period.end().toLocalDate())) {
            LocalDateTime currentEnd = currentStart.toLocalDate().atTime(endOfDay);
            if (currentEnd.isAfter(period.end())) {
                currentEnd = period.end();
            }
            intervals.add(new LocalDateTimePeriod(currentStart, currentEnd));
            currentStart = currentStart.toLocalDate().plusDays(1).atTime(startOfDay);
        }
        intervals.removeIf(next -> next.start().equals(next.end())
                || next.start().isAfter(LocalDateTime.of(next.start().toLocalDate(), endOfDay))
                || next.end().isBefore(LocalDateTime.of(next.end().toLocalDate(), startOfDay)));
        return intervals;
    }

}
