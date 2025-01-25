package ru.bogdanov.tgbotforbooking.servises.bot_services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bogdanov.tgbotforbooking.enteties.User;
import ru.bogdanov.tgbotforbooking.enteties.Visit;
import ru.bogdanov.tgbotforbooking.repositories.UserRepository;
import ru.bogdanov.tgbotforbooking.repositories.VisitRepository;

@Service
public class UserVisitBotService {

    private final UserRepository userRepository;
    private final VisitRepository visitRepository;

    public UserVisitBotService(UserRepository userRepository, VisitRepository visitRepository) {
        this.userRepository = userRepository;
        this.visitRepository = visitRepository;
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
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    public void createVisit(Visit visit) {
        visitRepository.save(visit);
    }

    public Visit getVisitByUserName(String tgAccount) {
        return visitRepository.findVisitByUserTgAccount(tgAccount);
    }

    public void deleteVisit(Visit visit) {
        visitRepository.delete(visit);
    }
}
