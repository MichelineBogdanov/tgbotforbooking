package ru.bogdanov.tgbotforbooking.servises.telegram.utils;

public interface MessagesText {

    String START_COMMAND_TEXT = """
            Добро пожаловать, %s!
            Это бот для записи на прием!
            Доступные команды:
            /start - Начать работу с ботом
            /generalinfo - Получение информации о номерках/месте приема
            /visitdeals - Запись на прием/отмена записи
            """;

    String GENERAL_INFO_COMMAND_TEXT = """
            Тут вы можете получить информацию о:
            - ваших записях
            - свободных номерках
            - месте приема
            """;

    String VISIT_DEALS_COMMAND_TEXT = """
            Тут вы можете осуществить:
            - запись на прием
            - отмену записи
            """;

    String PLACE_INFO_TEXT = """
            https://yandex.ru/maps/2/saint-petersburg/house/moskovskiy_prospekt_109/Z0kYdQZoTkAFQFtjfXR5eHRnYg==/?ll=30.320563%2C59.890760&z=16.64
            """;

    String YOUR_VISITS_TEXT = """
            Список ваших визитов:
            %s
            """;

    String YOUR_VISITS_FOR_CANCEL_TEXT = "Список ваших визитов:";

    String NO_VISITS_TEXT = "У вас нет записей!";

    String CHOOSE_DATE_TEXT = "Выберите дату:";

    String CHOOSE_TIME_TEXT = """
            Выбранная дата: %s
            Теперь выберите время:
            """;

    String SUCCESS_BOOKING_TEXT = "Поздравляем, %s! Вы записаны на: %s";

    String FAULT_BOOKING_TEXT = "К сожалению на данное время успел записаться кто-то другой, выберите другое время.";

    String MAX_COUNT_BOOKING_TEXT = "Вы записались на прием более трех раз!";

    String SUCCESS_CANCEL_TEXT = """
            Поздравляем! Был удален следующий визит:
            %s
            """;

    String UNKNOWN_COMMAND_TEXT = "Данная команда не может быть обработана!";

    String BACK_TEXT = "Назад";

}