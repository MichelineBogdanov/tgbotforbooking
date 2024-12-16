package ru.bogdanov.tgbotforbooking.servises.google;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FreeBusyExample {

    public static void main(String[] args) throws Exception {
        Calendar service = GoogleCalendarUtils.getCalendarService();
        // Задайте временные рамки
        DateTime startTime = new DateTime("2024-12-16T00:00:00Z"); // Начало дня
        DateTime endTime = new DateTime("2024-12-16T23:59:59Z");   // Конец дня
        // Укажите email календаря, который вы хотите проверить
        String calendarId = "primary";
        // Создайте запрос FreeBusy
        FreeBusyRequest request = new FreeBusyRequest()
                .setTimeMin(startTime)
                .setTimeMax(endTime)
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
