package ru.bogdanov.tgbotforbooking.servises.google;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.TimePeriod;
import org.springframework.stereotype.Service;
import ru.bogdanov.tgbotforbooking.entities.Visit;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public interface GoogleAPI {

    List<TimePeriod> getFreePeriods(DateTime start, DateTime end);

    List<LocalDate> getFreeDays(DateTime start, DateTime end, Integer duration);

    List<LocalTime> getFreeSlots(DateTime start, DateTime end, Integer duration);

    CreateVisitResult createVisit(LocalDate date, LocalTime time, String userName, Long serviceId);

    Optional<Visit> deleteVisit(Long id);

    List<Visit> getUserVisits(String tgAccount);

}
