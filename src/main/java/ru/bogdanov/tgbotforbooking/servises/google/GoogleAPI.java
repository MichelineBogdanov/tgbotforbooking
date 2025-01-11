package ru.bogdanov.tgbotforbooking.servises.google;

import com.google.api.services.calendar.model.TimePeriod;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public interface GoogleAPI {

    List<TimePeriod> getFreePeriods(String start, String end);

    List<LocalDate> getFreeDays(String start, String end);

    List<LocalTime> getFreeSlots(String start, String end);

    void createVisit(LocalDate date, LocalTime time);

}
