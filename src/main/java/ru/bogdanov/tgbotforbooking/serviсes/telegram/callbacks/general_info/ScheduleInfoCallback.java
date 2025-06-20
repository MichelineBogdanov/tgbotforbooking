package ru.bogdanov.tgbotforbooking.serviсes.telegram.callbacks.general_info;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.TimePeriod;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.serviсes.google.CalendarAPI;
import ru.bogdanov.tgbotforbooking.serviсes.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.serviсes.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.serviсes.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.serviсes.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.serviсes.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.serviсes.telegram.utils.MessagesText;
import ru.bogdanov.tgbotforbooking.serviсes.telegram.utils.ScheduleUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class ScheduleInfoCallback implements CallbackHandler {

    private final CalendarAPI service;

    public ScheduleInfoCallback(@Qualifier("googleCalendarService") CalendarAPI service) {
        this.service = service;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.toLocalDate().getDayOfMonth() >= 25
                ? start.toLocalDate().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).atStartOfDay()
                : start.toLocalDate().plusMonths(1).withDayOfMonth(1).atStartOfDay();
        List<TimePeriod> freePeriods = service.getFreePeriods(
                new DateTime(DateTimeUtils.fromLocalDateTimeToDate(start)),
                new DateTime(DateTimeUtils.fromLocalDateTimeToDate(end)));
        Map<LocalDate, List<LocalTime>> freeSlots = ScheduleUtils.getFreeSlots(freePeriods, 30);
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String text = getStringForMessage(freeSlots);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setParseMode(ParseMode.HTML);
        message.setText(text);
        InlineKeyboardMarkup keyboardMarkup = new KeyboardBuilder().addBackButton(CommandTypes.GENERAL_INFO).build();
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    private String getStringForMessage(Map<LocalDate, List<LocalTime>> freeSlots) {
        if (freeSlots.isEmpty()) {
            return MessagesText.NO_FREE_SLOTS_TEXT;
        }
        StringBuilder result = new StringBuilder();
        TreeMap<LocalDate, List<LocalTime>> freeSlotsTreeMap = new TreeMap<>(freeSlots);
        freeSlotsTreeMap.forEach((localDate, times) -> {
            String elements = ScheduleUtils.getSlots().stream()
                    .sorted()
                    .map(time -> ScheduleUtils.isSlotPresentIn(time, times)
                            ? time.toString()
                            : "<s>" + time.toString() + "</s>")
                    .collect(Collectors.joining("  "));
            result.append(DateTimeUtils.fromLocalDateToDayString(localDate))
                    .append("(")
                    .append(ShortDayOfWeek.values()[localDate.getDayOfWeek().getValue() - 1].getShortName())
                    .append("): ")
                    .append(elements)
                    .append("\n\n");
        });
        return result.toString();
    }

}