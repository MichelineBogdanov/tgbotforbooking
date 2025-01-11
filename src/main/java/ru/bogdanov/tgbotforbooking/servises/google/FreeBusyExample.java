package ru.bogdanov.tgbotforbooking.servises.google;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FreeBusyExample {

    public void getFreeSlots(String start, String end) throws GeneralSecurityException, IOException {
        Calendar service = GoogleCalendarUtils.getCalendarService();
        // Задайте временные рамки
        // "2024-12-16T00:00:00+03:00"
        DateTime startTime = new DateTime(start); // Начало дня
        // "2024-12-16T23:59:59+03:00"
        DateTime endTime = new DateTime(end);   // Конец дня
        // Укажите calendarId календаря, который вы хотите проверить
        String calendarId = "primary";
        // Создайте запрос FreeBusy
        FreeBusyRequest request = new FreeBusyRequest()
                .setTimeMin(startTime)
                .setTimeMax(endTime)
                .setTimeZone("+03:00")
                .setItems(Collections.singletonList(new FreeBusyRequestItem().setId(calendarId)));
        // Отправьте запрос
        FreeBusyResponse response = service.freebusy().query(request).execute();
        // Получите занятые интервалы
        List<TimePeriod> busyTimes = response.getCalendars().get(calendarId).getBusy();
        // Найдите свободные интервалы
        List<TimePeriod> freeSlots = calculateFreeSlots(startTime, endTime, busyTimes);
        System.out.println("Free intervals:");
        for (TimePeriod slot : freeSlots) {
            System.out.println("Start: " + slot.getStart() + ", End: " + slot.getEnd());
        }
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
