package ru.bogdanov.tgbotforbooking.servises.telegram.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand implements Command {

    @Override
    public SendMessage apply(Update update) {
        String userName = update.getMessage().getFrom().getUserName();
        String messageText = String.format("Hi, %s nice to meet you!\n Это бот для записи на прием!", userName);
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(messageText);
        return message;
    }

}
