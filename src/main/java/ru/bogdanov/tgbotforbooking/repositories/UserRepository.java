package ru.bogdanov.tgbotforbooking.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.tgbotforbooking.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByTgUserId(Long tgUserId);

    Optional<User> findByChatId(Long chatId);

    boolean existsByTgUserId(Long tgUserId);

}
