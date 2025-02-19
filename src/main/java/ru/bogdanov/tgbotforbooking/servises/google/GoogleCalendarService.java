package ru.bogdanov.tgbotforbooking.servises.google;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bogdanov.tgbotforbooking.entities.User;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.ScheduleUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
public class GoogleCalendarService implements GoogleAPI {

    public static final String TIME_ZONE = "+03:00";
    private final Calendar service = GoogleCalendarUtils.getCalendarService();

    private final UserVisitBotService userVisitBotService;

    private final String CALENDAR_ID = "primary";

    public GoogleCalendarService(UserVisitBotService userVisitBotService) {
        this.userVisitBotService = userVisitBotService;
    }

    public List<TimePeriod> getFreePeriods(DateTime start, DateTime end) {
        try {
            // Создайте запрос FreeBusy
            FreeBusyRequest request = new FreeBusyRequest()
                    .setTimeMin(start)
                    .setTimeMax(end)
                    .setTimeZone(TIME_ZONE)
                    .setItems(Collections.singletonList(new FreeBusyRequestItem().setId(CALENDAR_ID)));
            // Отправьте запрос
            FreeBusyResponse response = service.freebusy().query(request).execute();
            // Получите занятые интервалы
            List<TimePeriod> busyTimes = response.getCalendars().get(CALENDAR_ID).getBusy();
            // Найдите свободные интервалы
            return calculateFreeSlots(start, end, busyTimes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CreateVisitResult createVisit(LocalDate date, LocalTime time, String userName) {
        User user = userVisitBotService.getUserByTgAccount(userName);
        if (userVisitBotService.checkVisitPresent(LocalDateTime.of(date, time))) {
            return new CreateVisitResult(MessagesText.FAULT_BOOKING_TEXT);
        }
        if (userVisitBotService.checkCountOfVisitsPresent(user.getId(), LocalDateTime.now()) > 3) {
            return new CreateVisitResult(MessagesText.MAX_COUNT_BOOKING_TEXT);
        }
        Date start = Date.from(LocalDateTime.of(date, time).atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(LocalDateTime.of(date, time.plusHours(1).plusMinutes(30)).atZone(ZoneId.systemDefault()).toInstant());
        Event event = new Event()
                .setSummary(userName)
                .setDescription(user.getName());
        EventDateTime startEvent = new EventDateTime()
                .setDateTime(new DateTime(start))
                .setTimeZone(TIME_ZONE);
        event.setStart(startEvent);
        EventDateTime endEvent = new EventDateTime()
                .setDateTime(new DateTime(end))
                .setTimeZone(TIME_ZONE);
        event.setEnd(endEvent);
        try {
            Event execute = service.events().insert(CALENDAR_ID, event).execute();
            Visit visit = new Visit();
            visit.setVisitId(execute.getId());
            visit.setVisitDateTime(LocalDateTime.of(date, time));
            visit.setUser(user);
            userVisitBotService.createVisit(visit);
            String message = String.format(MessagesText.SUCCESS_BOOKING_TEXT
                    , userName
                    , DateTimeUtils.fromLocalDateTimeToDateTimeString(LocalDateTime.of(date, time)));
            return new CreateVisitResult(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Visit> deleteVisit(String userName) {
        List<Visit> visits = userVisitBotService.getFutureVisitsByUserName(userName);
        for (Visit visit : visits) {
            try {
                userVisitBotService.deleteVisit(visit);
                service.events().delete(CALENDAR_ID, visit.getVisitId()).execute();
            } catch (IOException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
        return visits;
    }

    @Override
    public List<LocalDate> getFreeDays(DateTime start, DateTime end) {
        List<TimePeriod> freePeriods = getFreePeriods(start, end);
        Map<LocalDate, List<LocalTime>> freeSlots = ScheduleUtils.getFreeSlots(freePeriods);
        return freeSlots.keySet().stream().sorted(LocalDate::compareTo).toList();
    }

    @Override
    public List<LocalTime> getFreeSlots(DateTime start, DateTime end) {
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
