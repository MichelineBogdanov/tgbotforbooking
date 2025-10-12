package ru.bogdanov.tgbotforbooking.services.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bogdanov.tgbotforbooking.entities.Notification;
import ru.bogdanov.tgbotforbooking.entities.User;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.services.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.services.telegram.BookingTelegramBot;
import ru.bogdanov.tgbotforbooking.services.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.services.telegram.utils.MessagesText;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final BookingTelegramBot telegramBot;
    public final UserVisitBotService userVisitBotService;

    public NotificationService(BookingTelegramBot telegramBot,
                               UserVisitBotService userVisitBotService) {
        this.telegramBot = telegramBot;
        this.userVisitBotService = userVisitBotService;
    }

    @Scheduled(cron = "0 0/30 14-20 * * ?")
    public void taskAtHalfHour() {
        sendMessageByNotification();
    }

    private void sendMessageByNotification() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusMinutes(10);
        LocalDateTime to = now.plusMinutes(10);
        Optional<Notification> notificationOptional = userVisitBotService.getNotificationByDateTimeBetween(from, to);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            log.info("Notification was found: {}", notification);
            Visit visit = notification.getVisit();
            User user = visit.getUser();
            if (user.getNotificationsOn()) {
                String text = String.format(MessagesText.NOTIFICATION_MESSAGE_TEXT, DateTimeUtils.fromLocalDateTimeToDateTimeString(visit.getVisitDateTime()));
                SendMessage message = new SendMessage(user.getChatId().toString(), text);
                sendMessage(message);
            }
        }
    }

    private void sendMessage(SendMessage sendMessage) {
        try {
            telegramBot.execute(sendMessage);
            log.info("Notification was send to user (chatId: {}, text: {})", sendMessage.getChatId(), sendMessage.getText());
        } catch (TelegramApiException e) {
            log.error("Error while sending notification", e);
            if (e.getMessage().contains("chat not found")
                    || e.getMessage().contains("bot was blocked by the user")) {
                userVisitBotService.deactivateUserByChatId(sendMessage.getChatId());
            }
        }
    }

}
