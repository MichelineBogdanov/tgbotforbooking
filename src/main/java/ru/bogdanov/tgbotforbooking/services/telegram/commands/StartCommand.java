package ru.bogdanov.tgbotforbooking.services.telegram.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.bogdanov.tgbotforbooking.services.telegram.utils.MessagesText;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartCommand implements Command {

    @Override
    public SendMessage apply(Update update) {
        String userName = update.getMessage().getFrom().getUserName();
        String messageText = String.format(MessagesText.START_COMMAND_TEXT, userName);
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(messageText);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Общая информация"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Личный кабинет"));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("Управление записями"));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
