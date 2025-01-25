package ru.bogdanov.tgbotforbooking.servises.google;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.TimePeriod;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public interface GoogleAPI {

    List<TimePeriod> getFreePeriods(DateTime start, DateTime end);

    List<LocalDate> getFreeDays(DateTime start, DateTime end);

    List<LocalTime> getFreeSlots(DateTime start, DateTime end);

    void createVisit(LocalDate date, LocalTime time, String userName);

    String deleteVisit(String userName);

}
