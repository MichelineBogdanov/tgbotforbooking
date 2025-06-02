package ru.bogdanov.tgbotforbooking.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.tgbotforbooking.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByTgUserId(Long tgUserId);

    Optional<User> findByChatId(Long chatId);

    boolean existsByTgUserId(Long tgUserId);

}
