package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.account;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

@Component
public class UserNotificationSwitchCallback implements CallbackHandler {

    private final UserVisitBotService userVisitBotService;

    public UserNotificationSwitchCallback(UserVisitBotService userVisitBotService) {
        this.userVisitBotService = userVisitBotService;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        String userName = update.getCallbackQuery().getFrom().getUserName();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        boolean isNotificationsOn = userVisitBotService.switchUserNotifications(userName);
        String text = isNotificationsOn
                ? MessagesText.NOTIFICATIONS_STATUS_ON_TEXT
                : MessagesText.NOTIFICATIONS_STATUS_OFF_TEXT;

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        InlineKeyboardMarkup keyboardMarkup = new KeyboardBuilder()
                .addBackButton(CommandTypes.ACCOUNT)
                .build();
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

}
