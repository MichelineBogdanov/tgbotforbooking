package ru.bogdanov.tgbotforbooking.servises.google;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import org.springframework.stereotype.Service;
import ru.bogdanov.tgbotforbooking.enteties.User;
import ru.bogdanov.tgbotforbooking.enteties.Visit;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.ScheduleUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;

@Service
public class GoogleCalendarService implements GoogleAPI {

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
                    .setTimeZone("+03:00")
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

    public void createVisit(LocalDate date, LocalTime time, String userName) {
        Date start = Date.from(LocalDateTime.of(date, time).atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(LocalDateTime.of(date, time.plusHours(1).plusMinutes(30)).atZone(ZoneId.systemDefault()).toInstant());
        User user = userVisitBotService.getUserByTgAccount(userName);
        Event event = new Event()
                .setSummary(userName)
                .setDescription(user.getName());
        EventDateTime startE = new EventDateTime()
                .setDateTime(new DateTime(start))
                .setTimeZone("+03:00");
        event.setStart(startE);
        EventDateTime endE = new EventDateTime()
                .setDateTime(new DateTime(end))
                .setTimeZone("+03:00");
        event.setEnd(endE);
        try {
            Event execute = service.events().insert(CALENDAR_ID, event).execute();
            Visit entity = new Visit();
            entity.setVisitId(execute.getId());
            entity.setVisitDateTime(LocalDateTime.of(date, time));
            entity.setUser(user);
            userVisitBotService.createVisit(entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String deleteVisit(String userName) {
        List<Visit> visits = userVisitBotService.getVisitByUserName(userName);
        try {
            for (Visit visit : visits) {
                service.events().delete(CALENDAR_ID, visit.getVisitId()).execute();
                userVisitBotService.deleteVisit(visit);
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
        StringJoiner sj = new StringJoiner("\n");
        sj.add(MessagesText.SUCCESS_CANCEL);
        visits.forEach(visit -> sj.add(visit.getVisitDateTime().toString()));
        return sj.toString();
    }

    @Override
    public List<LocalDate> getFreeDays(DateTime start, DateTime end) {
        List<TimePeriod> freePeriods = getFreePeriods(start, end);
        Map<LocalDate, List<LocalTime>> freeSlots = ScheduleUtils.getFreeSlots(freePeriods);
        return freeSlots.keySet().stream().toList();
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
