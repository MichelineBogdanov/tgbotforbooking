package ru.bogdanov.tgbotforbooking.servises.telegram.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bogdanov.tgbotforbooking.servises.telegram.JsonHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BackCallBackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;

import java.util.ArrayList;
import java.util.List;

public class KeyboardBuilder {

    private final List<List<InlineKeyboardButton>> keyboard;
    private final List<InlineKeyboardButton> currentRow;

    public KeyboardBuilder() {
        this.keyboard = new ArrayList<>();
        this.currentRow = new ArrayList<>();
    }

    public KeyboardBuilder addButton(String text, BaseCallbackData callbackData) {
        // Создаем кнопку и добавляем её в текущую строку
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        String toJson = JsonHandler.toJson(callbackData);
        button.setCallbackData(toJson);
        if (currentRow.size() < 3) {
            currentRow.add(button);
        } else {
            keyboard.add(new ArrayList<>(currentRow));
            currentRow.clear();
        }
        return this;
    }

    public KeyboardBuilder addBackButton(CommandTypes toCommand) {
        // Если текущая строка не пуста, добавляем её в клавиатуру
        if (!currentRow.isEmpty()) {
            keyboard.add(new ArrayList<>(currentRow));
            currentRow.clear();
        }
        // Добавляем кнопку "Назад" отдельной строкой
        InlineKeyboardButton backButton = new InlineKeyboardButton(MessagesText.BACK_TEXT);

        BackCallBackData callbackData = new BackCallBackData();
        callbackData.setType(CallbackTypes.BACK);
        callbackData.setCommand(toCommand);
        String json = JsonHandler.toJson(callbackData);
        backButton.setCallbackData(json);

        List<InlineKeyboardButton> backButtonRow = new ArrayList<>();
        backButtonRow.add(backButton);
        keyboard.add(backButtonRow);
        return this;
    }

    public KeyboardBuilder goToNewLine() {
        keyboard.add(new ArrayList<>(currentRow));
        currentRow.clear();
        return this;
    }

    public InlineKeyboardMarkup build() {
        // Если текущая строка не пуста, добавляем её в клавиатуру перед возвратом
        if (!currentRow.isEmpty()) {
            keyboard.add(new ArrayList<>(currentRow));
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }
}
