package ru.bogdanov.tgbotforbooking.servises.telegram.utils;

public interface MessagesText {

    String START_COMMAND_TEXT = """
            Здравствуйте, %s!💌
            Это чат-бот помощник для записи к косметологу Татьяне Гиль✨
                        
            Чтобы выбрать дату и время для записи на процедуру, перейдите в меню💌
            Бот предложит для Вас свободные слоты🕐
            Отменить визит можно также с помощью бота❌
                        
            В разделе "Личный кабинет" можно увидеть информацию о предстоящих визитах,
            посмотреть историю посещений, а также управлять отправкой Вам уведомлений✨
                        
            До встречи на приёме!💋
            """;

    String GENERAL_INFO_COMMAND_TEXT = """
            Тут вы можете получить информацию о:
            - свободных слотах🕐
            - списке услуг📝
            - месте приема🧭
            """;

    String ACCOUNT_INFO_COMMAND_TEXT = """
            Тут вы можете получить информацию о:
            - ваших записях📝
            - истории посещения📝
            - включить/отключить отправку вам уведомлений🛎️
            """;

    String VISIT_DEALS_COMMAND_TEXT = """
            Тут вы можете осуществить:
            - запись на прием📝
            - отмену записи❌
            """;

    String PLACE_INFO_TEXT = """
            📍Московский пр-т, 109к2, код домофона: 2345В, 3 этаж, офис 8/2
            https://yandex.ru/maps/2/saint-petersburg/house/moskovskiy_prospekt_109/Z0kYdQZoTkAFQFtjfXR5eHRnYg==/?ll=30.320563%2C59.890760&z=16.64
            """;

    String GET_SERVICES_INFO_TEXT = """
            Список услуг:
            %s
            """;

    String NO_SERVICES_YET_INFO_TEXT = "Пока что услуги не загружены в базу😟, проконсультируйтесь в чате🙂";

    String YOUR_FUTURE_VISITS_TEXT = """
            Даты и время ваших предстоящих визитов:📝
            %s
            """;

    String YOUR_VISITS_HISTORY_TEXT = """
            Даты и время ваших прошлых визитов:📝
            %s
            """;

    String YOUR_VISITS_FOR_CANCEL_TEXT = "Список ваших визитов:📝";

    String NO_VISITS_TEXT = "К сожалению, пока у Вас нет записей😟";

    String CHOOSE_DATE_TEXT = "Выберите дату:🕐";

    String NO_FREE_SLOTS_TEXT = """
            К сожалению на этот месяц не осталось свободных мест😟
            
            Свободные номерки появляются в начале нового месяца🙂
            """;

    String CHOOSE_TIME_TEXT = """
            Выбранная дата: %s
                        
            Теперь выберите время:🕐
            """;

    String CHOOSE_SERVICE_TEXT = """
            Выбранный слот: %s🕐
                        
            Теперь, по возможности, выберите услугу📝
            * порядковый номер услуги соответствует номеру на кнопке🙂
            * если вы не знаете на какую услугу надо записаться, выбирайте консультацию🙂
                        
            Список услуг:
            %s
            """;

    String SUCCESS_BOOKING_TEXT = """
            Поздравляем, %s!🥳
                        
            Вы записаны на: %s🕐
                        
            Выбранная услуга: %s📝
                        
            Жду Вас по адресу: 📍Московский пр-т, 109к2, код домофона: 2345В, 3 этаж, офис 8/2💋
            """;

    String DESCRIPTION_EVENT_TEXT = "%s\nУслуга: %s";

    String NO_SERVICE_CHOOSE_TEXT = "услуга не выбрана";

    String FAULT_BOOKING_TEXT = "К сожалению на данное время успел записаться кто-то другой, выберите другое время.😟";

    String MAX_COUNT_BOOKING_TEXT = "Вы записались на прием более трех раз!😟";

    String SUCCESS_CANCEL_TEXT = "Ваш визит %s удален! Жду Вас в другой раз!💋";

    String NOT_SUCCESS_CANCEL_TEXT = "Что-то пошло не так,😟 попробуйте еще раз";

    String BACK_TEXT = "Назад";

    String NOTIFICATIONS_ON_TEXT = "Включить уведомления";

    String NOTIFICATIONS_STATUS_ON_TEXT = "Уведомления включены";

    String NOTIFICATIONS_OFF_TEXT = "Отключить уведомления";

    String NOTIFICATIONS_STATUS_OFF_TEXT = "Уведомления отключены";

    String NOTIFICATION_MESSAGE_TEXT = "Напоминание: завтра в %s у вас запланирован визит к косметологу!📝";

}