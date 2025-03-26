package ru.bogdanov.tgbotforbooking.servises.bot_services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bogdanov.tgbotforbooking.entities.CosmetologyService;
import ru.bogdanov.tgbotforbooking.entities.Notification;
import ru.bogdanov.tgbotforbooking.entities.User;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.repositories.NotificationRepository;
import ru.bogdanov.tgbotforbooking.repositories.ServiceRepository;
import ru.bogdanov.tgbotforbooking.repositories.UserRepository;
import ru.bogdanov.tgbotforbooking.repositories.VisitRepository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserVisitBotService {

    private final UserRepository userRepository;
    private final VisitRepository visitRepository;
    private final ServiceRepository serviceRepository;
    private final NotificationRepository notificationRepository;

    public UserVisitBotService(UserRepository userRepository
            , VisitRepository visitRepository
            , ServiceRepository serviceRepository
            , NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.visitRepository = visitRepository;
        this.serviceRepository = serviceRepository;
        this.notificationRepository = notificationRepository;
    }

    public boolean checkVisitPresent(LocalDateTime visitDateTime) {
        return visitRepository.existsByVisitDateTime(visitDateTime);
    }

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

    public List<Visit> getFutureVisitsByUserName(String tgAccount) {
        return visitRepository.findVisitsByUserTgAccountAndVisitDateTimeGreaterThan(tgAccount, LocalDateTime.now());
    }

    public List<Visit> getVisitsHistoryByUserName(String tgAccount) {
        return visitRepository.findVisitsByUserTgAccountAndVisitDateTimeLessThan(tgAccount, LocalDateTime.now());
    }

    public List<Visit> getAllVisits() {
        ArrayList<Visit> visits = new ArrayList<>();
        visitRepository.findAll().forEach(visits::add);
        return visits;
    }

    @Transactional
    public Optional<Visit> deleteVisitById(Long id) {
        Optional<Visit> optionalVisit = visitRepository.findById(id);
        if (optionalVisit.isPresent()) {
            Visit visit = optionalVisit.get();
            visitRepository.delete(visit);
            Notification notification = visit.getNotification();
            notificationRepository.delete(notification);
        }
        return optionalVisit;
    }

    @Transactional
    public void createUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByTgAccount(String tgAccount) {
        return userRepository.findByTgAccount(tgAccount);
    }

    public Optional<User> getUserByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    public boolean isUserExistsByTgAccount(String tgAccount) {
        return userRepository.existsByTgAccount(tgAccount);
    }

    public List<User> findAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public List<CosmetologyService> findAllServices() {
        ArrayList<CosmetologyService> services = new ArrayList<>();
        serviceRepository.findAll().forEach(services::add);
        services.sort(Comparator.comparing(CosmetologyService::getId));
        return services;
    }

    public Optional<CosmetologyService> getServiceById(Long serviceId) {
        return serviceRepository.findById(serviceId);
    }

    @Transactional
    public CosmetologyService updateService(CosmetologyService service) {
        return serviceRepository.save(service);
    }

    @Transactional
    public void deleteService(Long serviceId) {
        serviceRepository.deleteById(serviceId);
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
            user.setNotificationsOn(false);
            userRepository.save(user);
        }
    }

    @Transactional
    public List<Long> getIncomeForMonthByDays(YearMonth month) {
        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime end = month.atDay(1).plusMonths(1).atStartOfDay();
        List<Visit> visits = visitRepository.findVisitsByVisitDateTimeBetween(start, end);
        List<Long> dailyIncomeList = new ArrayList<>(Collections.nCopies(month.lengthOfMonth(), 0L));
        visits.stream()
                .filter(visit -> visit.getCosmetologyService() != null)
                .forEach(visit -> {
                    int dayOfMonth = visit.getVisitDateTime().getDayOfMonth();
                    long price = visit.getCosmetologyService().getPrice();
                    dailyIncomeList.set(dayOfMonth - 1, dailyIncomeList.get(dayOfMonth - 1) + price);
                });
        return dailyIncomeList;
    }
}
