package ru.bogdanov.tgbotforbooking.servises.bot_services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bogdanov.tgbotforbooking.entities.Notification;
import ru.bogdanov.tgbotforbooking.entities.User;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.repositories.NotificationRepository;
import ru.bogdanov.tgbotforbooking.repositories.UserRepository;
import ru.bogdanov.tgbotforbooking.repositories.VisitRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class UserVisitBotService {

    private final UserRepository userRepository;
    private final VisitRepository visitRepository;
    private final NotificationRepository notificationRepository;

    public UserVisitBotService(UserRepository userRepository
            , VisitRepository visitRepository
            , NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.visitRepository = visitRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public boolean checkVisitPresent(LocalDateTime visitDateTime) {
        return visitRepository.existsByVisitDateTime(visitDateTime);
    }

    @Transactional
    public Integer checkCountOfVisitsPresent(Long userId, LocalDateTime visitDateTime) {
        return visitRepository.countByUserIdAndVisitDateTimeAfter(userId, visitDateTime);
    }

    @Transactional
    public void createVisit(Visit visit) {
        Visit savedVisit = visitRepository.save(visit);
        User user = savedVisit.getUser();
        LocalDateTime notificationDate = savedVisit.getVisitDateTime().minusDays(1);
        if (user.getNotificationsOn()
                && notificationDate.isAfter(LocalDateTime.now(ZoneId.systemDefault()))) {
            Notification notification = new Notification();
            notification.setVisit(savedVisit);
            notification.setNotificationDateTime(notificationDate);
            notificationRepository.save(notification);
        }
    }

    @Transactional
    public List<Visit> getFutureVisitsByUserName(String tgAccount) {
        return visitRepository.findVisitByUserTgAccountAndVisitDateTimeGreaterThan(tgAccount, LocalDateTime.now());
    }

    @Transactional
    public Visit deleteVisitById(Long id) {
        Visit visit = visitRepository.findById(id).get();
        visitRepository.delete(visit);
        return visit;
    }

    @Transactional
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public Optional<User> getUserByTgAccount(String tgAccount) {
        return userRepository.findByTgAccount(tgAccount);
    }

    @Transactional
    public Optional<User> getUserByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    @Transactional
    public boolean isUserExistsByTgAccount(String tgAccount) {
        return userRepository.existsByTgAccount(tgAccount);
    }

    @Transactional
    public boolean switchUserNotifications(String tgAccount) {
        Optional<User> userOptional = getUserByTgAccount(tgAccount);
        boolean notificationsOn = false;
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            notificationsOn = !user.getNotificationsOn();
            user.setNotificationsOn(notificationsOn);
            userRepository.save(user);
        }
        return notificationsOn;
    }

    @Transactional
    public void deactivateUserByChatId(String chatId) {
        Optional<User> userOptional = getUserByChatId(Long.parseLong(chatId));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(false);
            user.setNotificationsOn(false);
            userRepository.save(user);
        }
    }
}
