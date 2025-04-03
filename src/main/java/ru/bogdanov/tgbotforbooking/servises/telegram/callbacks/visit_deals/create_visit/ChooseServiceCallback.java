package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.entities.CosmetologyService;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.ChooseServiceCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.CreateVisitCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.SpringContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class ChooseServiceCallback implements CallbackHandler {

    private final UserVisitBotService userVisitBotService;

    public ChooseServiceCallback(UserVisitBotService userVisitBotService) {
        this.userVisitBotService = userVisitBotService;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        ChooseServiceCallbackData currentCallback = (ChooseServiceCallbackData) callback;
        List<CosmetologyService> allServices = userVisitBotService.findAllServices();
        if (allServices.isEmpty()) {
            CreateVisitCallbackData emptyServiceCreateVisitCallbackData = new CreateVisitCallbackData();
            emptyServiceCreateVisitCallbackData.setType(currentCallback.getType());
            emptyServiceCreateVisitCallbackData.setDate(currentCallback.getDate());
            emptyServiceCreateVisitCallbackData.setTime(currentCallback.getTime());
            return SpringContextHolder.getBean(CreateVisitCallback.class).apply(emptyServiceCreateVisitCallbackData, update);
        }
        LocalDate date = currentCallback.getDate();
        LocalTime time = currentCallback.getTime();

        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        StringBuilder text = new StringBuilder();
        KeyboardBuilder keyboardBuilder = new KeyboardBuilder(5);
        for (int i = 0; i < allServices.size(); i++) {
            CosmetologyService service = allServices.get(i);
            CreateVisitCallbackData callbackData = new CreateVisitCallbackData();
            callbackData.setType(CallbackTypes.CREATE_VISIT);
            callbackData.setTime(time);
            callbackData.setDate(date);
            callbackData.setServiceId(service.getId());
            keyboardBuilder.addButton(String.valueOf(i + 1), callbackData);
            text.append("<b>")
                    .append(i + 1)
                    .append(" - ")
                    .append(service.getName())
                    .append(" - цена : ")
                    .append(service.getPrice())
                    .append("р.</b> ")
                    .append("<i>")
                    .append(service.getDescription())
                    .append("</i>")
                    .append("\n");
        }
        keyboardBuilder.addBackButton(CommandTypes.VISIT_DEALS);
        InlineKeyboardMarkup keyboardMarkup = keyboardBuilder.build();
        message.setReplyMarkup(keyboardMarkup);
        message.setText(String.format(MessagesText.CHOOSE_SERVICE_TEXT,
                DateTimeUtils.fromLocalDateTimeToDateTimeString(LocalDateTime.of(date, time)),
                text));
        message.setParseMode(ParseMode.HTML);

        return message;
    }

}
