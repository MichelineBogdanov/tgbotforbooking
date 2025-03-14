package ru.bogdanov.tgbotforbooking.servises.telegram.utils;

public interface MessagesText {

    String START_COMMAND_TEXT = """
            Здравствуйте, %s!💌
            Это чат-бот помощник для записи к косметологу Татьяне Гиль✨
            
            Чтобы выбрать дату и время для записи на процедуру, перейдите в меню💌
            Бот предложит для Вас свободные слоты🕐
            Отменить визит можно также с помощью бота
            
            До встречи на приёме!💋
            """;

    String GENERAL_INFO_COMMAND_TEXT = """
            Тут вы можете получить информацию о:
            - ваших записях
            - свободных слотах
            - месте приема
            - включить/отключить отправку вам уведомлений
            """;

    String VISIT_DEALS_COMMAND_TEXT = """
            Тут вы можете осуществить:
            - запись на прием
            - отмену записи
            """;

    String PLACE_INFO_TEXT = """
            Московский пр-т, 109к2, код домофона: 2345В, 3 этаж, офис 8/2
            https://yandex.ru/maps/2/saint-petersburg/house/moskovskiy_prospekt_109/Z0kYdQZoTkAFQFtjfXR5eHRnYg==/?ll=30.320563%2C59.890760&z=16.64
            """;

    String YOUR_VISITS_TEXT = """
            Даты и время ваших предстоящих визитов:
            %s
            """;

    String YOUR_VISITS_FOR_CANCEL_TEXT = "Список ваших визитов:";

    String NO_VISITS_TEXT = "К сожалению, пока у Вас нет записей😟";

    String CHOOSE_DATE_TEXT = "Выберите дату:";

    String CHOOSE_TIME_TEXT = """
            Выбранная дата: %s
            Теперь выберите время:
            """;

    String CHOOSE_SERVICE_TEXT = """
            Выбранный слот: %s
            Теперь, по возможности, выберите услугу
            PS. Если вы не знаете на какую услугу надо записаться, выбирайте консультацию)
            """;

    String SUCCESS_BOOKING_TEXT = """
            Поздравляем, %s! Вы записаны на: %s
            Жду Вас по адресу: Московский пр-т, 109к2, код домофона: 2345В, 3 этаж, офис 8/2
            """;

    String FAULT_BOOKING_TEXT = "К сожалению на данное время успел записаться кто-то другой, выберите другое время.";

    String MAX_COUNT_BOOKING_TEXT = "Вы записались на прием более трех раз!";

    String SUCCESS_CANCEL_TEXT = """
            Ваш визит %s удален! Жду Вас в другой раз!💋
            """;

    String BACK_TEXT = "Назад";

    String NOTIFICATIONS_ON_TEXT = "Включить уведомления";

    String NOTIFICATIONS_STATUS_ON_TEXT = "Уведомления включены";

    String NOTIFICATIONS_OFF_TEXT = "Отключить уведомления";

    String NOTIFICATIONS_STATUS_OFF_TEXT = "Уведомления отключены";

    String NOTIFICATION_MESSAGE_TEXT = "Напоминание: завтра в %s у вас запланирован визит к косметологу!";

}