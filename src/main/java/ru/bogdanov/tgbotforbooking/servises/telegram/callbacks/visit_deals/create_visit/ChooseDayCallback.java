package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit;

import com.google.api.client.util.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.entities.CosmetologyService;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.google.CalendarAPI;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.ChooseDayCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.ChooseTimeCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
public class ChooseDayCallback implements CallbackHandler {

    private final CalendarAPI service;
    private final UserVisitBotService userVisitBotService;

    public ChooseDayCallback(@Qualifier("googleCalendarService") CalendarAPI service
            , UserVisitBotService userVisitBotService) {
        this.service = service;
        this.userVisitBotService = userVisitBotService;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        ChooseDayCallbackData currentCallback = (ChooseDayCallbackData) callback;
        Long serviceId = currentCallback.getServiceId();
        CosmetologyService cosmetologyService = null;
        if (serviceId != null) {
            cosmetologyService = userVisitBotService.getServiceById(serviceId).get();
        }

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.toLocalDate().getDayOfMonth() >= 25
                ? start.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth())
                : start.plusMonths(1).withDayOfMonth(1);
        List<LocalDate> freeDays = service.getFreeDays(
                new DateTime(DateTimeUtils.fromLocalDateTimeToDate(start)),
                new DateTime(DateTimeUtils.fromLocalDateTimeToDate(end)),
                cosmetologyService == null ? 30 : cosmetologyService.getDuration());

        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(freeDays.isEmpty()
                ? MessagesText.NO_FREE_SLOTS_TEXT
                : String.format(MessagesText.CHOOSE_DATE_TEXT,
                cosmetologyService != null
                        ? String.join(" : ", cosmetologyService.getName(), cosmetologyService.getPrice().toString() + "р.", cosmetologyService.getDuration().toString() + "мин.")
                        : MessagesText.NO_SERVICE_CHOOSE_TEXT));

        KeyboardBuilder keyboardBuilder = new KeyboardBuilder();
        for (LocalDate freeDay : freeDays) {
            ChooseTimeCallbackData callbackData = new ChooseTimeCallbackData();
            callbackData.setType(CallbackTypes.CHOOSE_TIME);
            callbackData.setServiceId(serviceId);
            callbackData.setDate(freeDay);
            String text = DateTimeUtils.fromLocalDateToDateString(freeDay);
            keyboardBuilder.addButton(text, callbackData);
        }
        keyboardBuilder.addBackButton(CommandTypes.VISIT_DEALS);
        InlineKeyboardMarkup keyboardMarkup = keyboardBuilder.build();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
