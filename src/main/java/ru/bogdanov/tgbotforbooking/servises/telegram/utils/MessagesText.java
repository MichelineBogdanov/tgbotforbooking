package ru.bogdanov.tgbotforbooking.servises.telegram.utils;

public interface MessagesText {

    String START_COMMAND_TEXT = """
            Добро пожаловать, %s!
            Это бот для записи на прием!
            Доступные команды:
            /start - Начать работу с ботом
            /generalinfo - Получение информации о номерках/месту приема
            /visitdeals - Запись на прием/отмена записи""";

    String GENERAL_INFO_COMMAND_TEXT = """
            Тут вы можете получить информацию о:
            - ваших записях
            - свободных номерках
            - месте приема
            """;

    String PLACE_INFO_TEXT = """
            https://yandex.ru/maps/2/saint-petersburg/house/moskovskiy_prospekt_109/Z0kYdQZoTkAFQFtjfXR5eHRnYg==/?ll=30.320563%2C59.890760&z=16.64
            """;

    String YOUR_VISITS_TEXT = """
            Список ваших визитов:
            %s
            """;

    String NO_VISITS = "У вас нет записей!";

    String CHOOSE_DATE = "Выберите дату:";

    String CHOOSE_TIME = "Выберите время:";

    String SUCCESS_BOOKING = "Поздравляем, %s! Вы записаны на: %s %s";

    String SUCCESS_CANCEL = "Поздравляем! Был(и) удалены следующие визиты:";

}
