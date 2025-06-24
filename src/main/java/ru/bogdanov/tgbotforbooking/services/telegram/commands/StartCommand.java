package ru.bogdanov.tgbotforbooking.services.telegram.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.services.telegram.utils.MessagesText;

@Component
public class StartCommand implements Command {

    @Override
    public SendMessage apply(Update update) {
        String userName = update.getMessage().getFrom().getUserName();
        String messageText = String.format(MessagesText.START_COMMAND_TEXT, userName);
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(messageText);
        return message;
    }

}
