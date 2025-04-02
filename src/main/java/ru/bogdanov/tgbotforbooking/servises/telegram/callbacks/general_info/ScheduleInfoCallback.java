package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.TimePeriod;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.servises.google.GoogleAPI;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.ScheduleUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class ScheduleInfoCallback implements CallbackHandler {

    private final GoogleAPI service;

    public ScheduleInfoCallback(@Qualifier("googleCalendarService") GoogleAPI service) {
        this.service = service;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        List<TimePeriod> freePeriods = service.getFreePeriods(
                new DateTime(DateTimeUtils.fromLocalDateTimeToDate(LocalDateTime.now())),
                new DateTime(DateTimeUtils.fromLocalDateTimeToDate(LocalDate.now().plusMonths(1).withDayOfMonth(1).atStartOfDay())));
        Map<LocalDate, List<LocalTime>> freeSlots = ScheduleUtils.getFreeSlots(freePeriods);
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        String text = getStringForMessage(freeSlots);
        message.setText(text);
        message.setParseMode(ParseMode.MARKDOWNV2);
        InlineKeyboardMarkup keyboardMarkup = new KeyboardBuilder().addBackButton(CommandTypes.GENERAL_INFO).build();
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    private static String getStringForMessage(Map<LocalDate, List<LocalTime>> freeSlots) {
        if (freeSlots.isEmpty()) {
            return MessagesText.NO_FREE_SLOTS_TEXT;
        }
        StringBuilder result = new StringBuilder();
        TreeMap<LocalDate, List<LocalTime>> freeSlotsTreeMap = new TreeMap<>(freeSlots);
        freeSlotsTreeMap.forEach((localDate, times) -> {
            List<String> elements = ScheduleUtils.getSlots().stream()
                    .sorted()
                    .map(time -> ScheduleUtils.isSlotPresentIn(time, times)
                            ? MessagesText.escapeMarkdownV2(time.toString())
                            : "~~" + MessagesText.escapeMarkdownV2(time.toString()) + "~~")
                    .toList();
            StringBuilder formatBuilder = new StringBuilder();
            for (String element : elements) {
                formatBuilder.append("%-").append(element.length() + 3).append("s ");
            }
            result.append(MessagesText.escapeMarkdownV2(DateTimeUtils.fromLocalDateToDayString(localDate)))
                    .append(MessagesText.escapeMarkdownV2("("))
                    .append(MessagesText.escapeMarkdownV2(ShortDayOfWeek.values()[localDate.getDayOfWeek().getValue() - 1].getShortName()))
                    .append(MessagesText.escapeMarkdownV2("): "))
                    .append(String.format(formatBuilder.toString(), elements.toArray()))
                    .append("\n")
                    .append("\n");
        });
        return result.toString();
    }

}
