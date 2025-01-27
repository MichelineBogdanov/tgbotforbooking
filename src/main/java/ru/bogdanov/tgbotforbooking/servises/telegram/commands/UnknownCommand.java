package ru.bogdanov.tgbotforbooking.servises.telegram.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

@Component
public class UnknownCommand implements Command {

    @Override
    public SendMessage apply(Update update) {
        Long chatId = update.getMessage().getChatId();
        return new SendMessage(chatId.toString(), MessagesText.UNKNOWN_COMMAND);
    }

}
