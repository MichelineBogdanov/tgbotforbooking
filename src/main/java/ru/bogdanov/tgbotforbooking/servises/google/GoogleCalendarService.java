package ru.bogdanov.tgbotforbooking.servises.google;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.bogdanov.tgbotforbooking.entities.CosmetologyService;
import ru.bogdanov.tgbotforbooking.entities.User;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.exceptions.CreateVisitException;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GoogleCalendarService implements GoogleAPI {

    private static final Logger log = LoggerFactory.getLogger(GoogleCalendarService.class);

    private final UserVisitBotService userVisitBotService;

    public static final String TIME_ZONE = "+03:00";

    private final Calendar calendarService;

    private final String CALENDAR_ID = "primary";

    public GoogleCalendarService(UserVisitBotService userVisitBotService
            , Calendar calendarService) {
        this.userVisitBotService = userVisitBotService;
        this.calendarService = calendarService;
    }

    public List<TimePeriod> getFreePeriods(DateTime start, DateTime end) {
        try {
            FreeBusyRequest request = new FreeBusyRequest()
                    .setTimeMin(start)
                    .setTimeMax(end)
                    .setTimeZone(TIME_ZONE)
                    .setItems(Collections.singletonList(new FreeBusyRequestItem().setId(CALENDAR_ID)));
            FreeBusyResponse response = calendarService.freebusy().query(request).execute();
            List<TimePeriod> busyTimes = response.getCalendars().get(CALENDAR_ID).getBusy();
            return calculateFreeSlots(start, end, busyTimes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CreateVisitResult createVisit(LocalDate date, LocalTime time, Long tgUserId, Long serviceId) {
        Optional<User> userOptional = userVisitBotService.getUserByTgUserId(tgUserId);
        User user = userOptional.get();
        CosmetologyService service = null;
        Integer duration = 60;
        if (serviceId != null) {
            Optional<CosmetologyService> optionalCosmetologyService = userVisitBotService.getServiceById(serviceId);
            if (optionalCosmetologyService.isPresent()) {
                service = optionalCosmetologyService.get();
                duration = service.getDuration();
            }
        }
        if (userVisitBotService.checkVisitPresent(LocalDateTime.of(date, time), LocalDateTime.of(date, time).plusMinutes(duration))) {
            throw new CreateVisitException(MessagesText.FAULT_BOOKING_TEXT);
        }
        if (userVisitBotService.checkCountOfVisitsPresent(user.getId(), LocalDateTime.now()) > 2) {
            throw new CreateVisitException(MessagesText.MAX_COUNT_BOOKING_TEXT);
        }
        Date start = Date.from(LocalDateTime.of(date, time).atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(LocalDateTime.of(date, time.plusMinutes(duration)).atZone(ZoneId.systemDefault()).toInstant());
        String description = String.format(MessagesText.DESCRIPTION_EVENT_TEXT,
                Stream.of(user.getFirstName(), user.getLastName())
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(" ")),
                service != null ? service.getName() : MessagesText.NO_SERVICE_CHOOSE_TEXT
        );

        Event event = new Event()
                .setSummary(user.getTgAccount())
                .setDescription(description);
        EventDateTime startEvent = new EventDateTime()
                .setDateTime(new DateTime(start))
                .setTimeZone(TIME_ZONE);
        event.setStart(startEvent);
        EventDateTime endEvent = new EventDateTime()
                .setDateTime(new DateTime(end))
                .setTimeZone(TIME_ZONE);
        event.setEnd(endEvent);
        try {
            Event execute = calendarService.events().insert(CALENDAR_ID, event).execute();
            Visit visit = new Visit();
            visit.setGoogleEventId(execute.getId());
            visit.setVisitDateTime(LocalDateTime.of(date, time));
            visit.setEndVisitDateTime(LocalDateTime.of(date, time).plusMinutes(duration));
            visit.setUser(user);
            visit.setCosmetologyService(service);
            userVisitBotService.createVisit(visit);
            String message = String.format(MessagesText.SUCCESS_BOOKING_TEXT
                    , user.getTgAccount()
                    , DateTimeUtils.fromLocalDateTimeToDateTimeString(LocalDateTime.of(date, time))
                    , service == null
                            ? MessagesText.NO_SERVICE_CHOOSE_TEXT
                            : String.join(" : ", service.getName(), service.getPrice().toString()));
            return new CreateVisitResult(message);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new CreateVisitException(MessagesText.ERROR_BOOKING_TEXT);
        }
    }

    public Optional<Visit> deleteVisit(Long id) {
        Optional<Visit> optionalVisit = userVisitBotService.deleteVisitById(id);
        try {
            if (optionalVisit.isPresent()) {
                calendarService.events().delete(CALENDAR_ID, optionalVisit.get().getGoogleEventId()).execute();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return optionalVisit;
    }

    public List<Visit> getUserVisits(Long tgUserId) {
        return userVisitBotService.getFutureVisitsByTgUserIdName(tgUserId);
    }

    @Override
    public List<LocalDate> getFreeDays(DateTime start, DateTime end, Integer duration) {
        List<TimePeriod> freePeriods = getFreePeriods(start, end);
        Map<LocalDate, List<LocalTime>> freeSlots = ScheduleUtils.getFreeSlots(freePeriods, duration);
        return freeSlots.keySet().stream().sorted(LocalDate::compareTo).toList();
    }

    @Override
    public List<LocalTime> getFreeSlots(DateTime start, DateTime end, Integer duration) {
        List<TimePeriod> freePeriods = getFreePeriods(start, end);
        Map<LocalDate, List<LocalTime>> freeSlots = ScheduleUtils.getFreeSlots(freePeriods, duration);
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
