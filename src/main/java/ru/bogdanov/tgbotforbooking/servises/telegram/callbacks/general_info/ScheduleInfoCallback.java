package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.TimePeriod;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.servises.google.GoogleAPI;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.ScheduleUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class ScheduleInfoCallback implements CallbackHandler {

    private final GoogleAPI service;

    public ScheduleInfoCallback(@Qualifier("googleCalendarService") GoogleAPI service) {
        this.service = service;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        List<TimePeriod> freePeriods = service.getFreePeriods(
                new DateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())),
                new DateTime(Date.from(LocalDate.now().plusMonths(1).withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
        Map<LocalDate, List<LocalTime>> freeSlots = ScheduleUtils.getFreeSlots(freePeriods);
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        String text = getStringForMessage(freeSlots);
        message.setText(text);
        return message;
    }

    private String getStringForMessage(Map<LocalDate, List<LocalTime>> freeSlots) {
        StringBuilder result = new StringBuilder();
        int index = 1;
        for (Map.Entry<LocalDate, List<LocalTime>> entry : freeSlots.entrySet()) {
            result.append(index).append(") ").append(entry.getKey().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(": ");
            String values = String.join(", ", entry.getValue().toString());
            result.append(values).append("\n");
            index++;
        }
        return result.toString();
    }

}
