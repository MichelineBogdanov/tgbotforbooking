package ru.bogdanov.tgbotforbooking.servises.notifications;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bogdanov.tgbotforbooking.entities.Notification;
import ru.bogdanov.tgbotforbooking.entities.User;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.repositories.NotificationRepository;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.telegram.BookingTelegramBot;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final BookingTelegramBot telegramBot;

    public final UserVisitBotService userVisitBotService;

    public NotificationService(NotificationRepository visitRepository,
                               BookingTelegramBot telegramBot,
                               UserVisitBotService userVisitBotService) {
        this.notificationRepository = visitRepository;
        this.telegramBot = telegramBot;
        this.userVisitBotService = userVisitBotService;
    }

    @Scheduled(cron = "0 0 13,17,19 * * ?")
    public void taskAtFullHour() {
        sendMessageByNotification();
    }

    @Scheduled(cron = "0 30 14,17 * * ?")
    public void taskAtHalfHour() {
        sendMessageByNotification();
    }

    private void sendMessageByNotification() {
        LocalDateTime from = LocalDateTime.now().minusMinutes(10);
        LocalDateTime to = LocalDateTime.now().plusMinutes(10);
        Optional<Notification> notificationOptional = notificationRepository.findFirstByNotificationDateTimeBetween(from, to);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            Visit visit = notification.getVisit();
            User user = visit.getUser();
            if (user.getActive() && user.getNotificationsOn()) {
                String text = String.format(MessagesText.NOTIFICATION_MESSAGE_TEXT, DateTimeUtils.fromLocalDateTimeToDateTimeString(visit.getVisitDateTime()));
                SendMessage message = new SendMessage(user.getChatId().toString(), text);
                sendMessage(message);
            }
        }
    }

    public void sendMessage(SendMessage sendMessage) {
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            if (e.getMessage().contains("chat not found")
                    || e.getMessage().contains("bot was blocked by the user")) {
                userVisitBotService.deactivateUserByChatId(sendMessage.getChatId());
            }
        }
    }

}
