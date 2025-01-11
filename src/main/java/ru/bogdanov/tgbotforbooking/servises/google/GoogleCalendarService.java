package ru.bogdanov.tgbotforbooking.servises.google;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import org.springframework.stereotype.Service;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info.ScheduleUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class GoogleCalendarService implements GoogleAPI {

    private final Calendar service = GoogleCalendarUtils.getCalendarService();
    private final String CALENDAR_ID = "primary";

    public List<TimePeriod> getFreePeriods(String start, String end) {
        try {
            // Задайте временные рамки
            // "2024-12-16T00:00:00+03:00"
            DateTime startTime = new DateTime(start); // Начало дня
            // "2024-12-16T23:59:59+03:00"
            DateTime endTime = new DateTime(end);   // Конец дня
            // Создайте запрос FreeBusy
            FreeBusyRequest request = new FreeBusyRequest()
                    .setTimeMin(startTime)
                    .setTimeMax(endTime)
                    .setTimeZone("+03:00")
                    .setItems(Collections.singletonList(new FreeBusyRequestItem().setId(CALENDAR_ID)));
            // Отправьте запрос
            FreeBusyResponse response = service.freebusy().query(request).execute();
            // Получите занятые интервалы
            List<TimePeriod> busyTimes = response.getCalendars().get(CALENDAR_ID).getBusy();
            // Найдите свободные интервалы
            return calculateFreeSlots(startTime, endTime, busyTimes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createVisit(LocalDate date, LocalTime time) {
        Date date1 = Date.from(LocalDateTime.of(date, time).atZone(ZoneId.systemDefault()).toInstant());
        Date date2 = Date.from(LocalDateTime.of(date, time.plusHours(1).plusMinutes(30)).atZone(ZoneId.systemDefault()).toInstant());
        Event event = new Event()
                .setSummary("TEST")
                .setDescription("TEST TEST TEST");
        EventDateTime startE = new EventDateTime()
                .setDateTime(new DateTime(date1))
                .setTimeZone("+03:00");
        event.setStart(startE);
        EventDateTime endE = new EventDateTime()
                .setDateTime(new DateTime(date2))
                .setTimeZone("+03:00");
        event.setEnd(endE);
        try {
            service.events().insert(CALENDAR_ID, event).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LocalDate> getFreeDays(String start, String end) {
        List<TimePeriod> freePeriods = getFreePeriods(start, end);
        Map<LocalDate, List<LocalTime>> freeSlots = ScheduleUtils.getFreeSlots(freePeriods);
        return freeSlots.keySet().stream().toList();
    }

    @Override
    public List<LocalTime> getFreeSlots(String start, String end) {
        List<TimePeriod> freePeriods = getFreePeriods(start, end);
        Map<LocalDate, List<LocalTime>> freeSlots = ScheduleUtils.getFreeSlots(freePeriods);
        for (LocalDate localDate : freeSlots.keySet()) {
            return freeSlots.get(localDate);
        }
        return null;
    }

    private static List<TimePeriod> calculateFreeSlots(DateTime startTime, DateTime endTime, List<TimePeriod> busyTimes) {
        List<TimePeriod> freeSlots = new ArrayList<>();
        DateTime lastEnd = startTime;
        for (TimePeriod busy : busyTimes) {
            if (lastEnd.getValue() < busy.getStart().getValue()) {
                freeSlots.add(new TimePeriod()
                        .setStart(lastEnd)
                        .setEnd(busy.getStart()));
            }
            lastEnd = busy.getEnd();
        }
        if (lastEnd.getValue() < endTime.getValue()) {
            freeSlots.add(new TimePeriod()
                    .setStart(lastEnd)
                    .setEnd(endTime));
        }
        return freeSlots;
    }

}
