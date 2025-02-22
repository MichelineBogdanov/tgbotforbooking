package ru.bogdanov.tgbotforbooking.servises.bot_services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bogdanov.tgbotforbooking.entities.User;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.repositories.UserRepository;
import ru.bogdanov.tgbotforbooking.repositories.VisitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserVisitBotService {

    private final UserRepository userRepository;
    private final VisitRepository visitRepository;

    public UserVisitBotService(UserRepository userRepository, VisitRepository visitRepository) {
        this.userRepository = userRepository;
        this.visitRepository = visitRepository;
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
        visitRepository.save(visit);
    }

    @Transactional
    public List<Visit> getFutureVisitsByUserName(String tgAccount) {
        return visitRepository.findVisitByUserTgAccountAndVisitDateTimeGreaterThan(tgAccount, LocalDateTime.now());
    }

    @Transactional
    public Visit deleteVisitById(String id) {
        long parseLong = Long.parseLong(id);
        Visit visit = visitRepository.findById(parseLong).get();
        visitRepository.delete(visit);
        return visit;
    }

    @Transactional
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public User getUserByTgAccount(String tgAccount) {
        return userRepository.findByTgAccount(tgAccount);
    }

    @Transactional
    public boolean isUserExistsByTgAccount(String tgAccount) {
        return userRepository.existsByTgAccount(tgAccount);
    }
}
