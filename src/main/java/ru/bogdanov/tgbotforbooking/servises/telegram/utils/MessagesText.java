package ru.bogdanov.tgbotforbooking.servises.telegram.utils;

public interface MessagesText {

    String START_COMMAND_TEXT = """
            Добро пожаловать, %s!
            Это бот для записи на прием!
            Доступные команды:
            /start - Начать работу с ботом
            /generalinfo - Получение информации о номерках/месту приема
            /visitdeals - Запись на прием/отмена записи""";

    String CHOOSE_DATE = "Выберите дату:";

    String CHOOSE_TIME = "Выберите время:";

    String SUCCESS_BOOKING = "Поздравляем, %s! Вы записаны на: %s %s";

}
