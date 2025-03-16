package ru.bogdanov.tgbotforbooking.servises.telegram.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.entities.User;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

@Component
public class GeneralInfoCommand implements Command {

    private final UserVisitBotService userVisitBotService;

    public GeneralInfoCommand(UserVisitBotService userVisitBotService) {
        this.userVisitBotService = userVisitBotService;
    }

    @Override
    public SendMessage apply(Update update) {
        String messageText = String.format(MessagesText.GENERAL_INFO_COMMAND_TEXT);
        Long chatId = update.getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);

        User user = userVisitBotService.getUserByChatId(chatId).get();
        String notificationSwitchText = user.getNotificationsOn()
                ? MessagesText.NOTIFICATIONS_OFF_TEXT
                : MessagesText.NOTIFICATIONS_ON_TEXT;

        InlineKeyboardMarkup keyboardMarkup = new KeyboardBuilder()
                .addButton(CallbackTypes.GET_VISITS.getDescription(), new BaseCallbackData(CallbackTypes.GET_VISITS))
                .goToNewLine()
                .addButton(CallbackTypes.GET_SCHEDULE.getDescription(), new BaseCallbackData(CallbackTypes.GET_SCHEDULE))
                .goToNewLine()
                .addButton(CallbackTypes.GET_SERVICES_LIST_INFO.getDescription(), new BaseCallbackData(CallbackTypes.GET_SERVICES_LIST_INFO))
                .goToNewLine()
                .addButton(CallbackTypes.GET_PLACE_INFO.getDescription(), new BaseCallbackData(CallbackTypes.GET_PLACE_INFO))
                .goToNewLine()
                .addButton(notificationSwitchText, new BaseCallbackData(CallbackTypes.NOTIFICATIONS_SWITCH))
                .build();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
