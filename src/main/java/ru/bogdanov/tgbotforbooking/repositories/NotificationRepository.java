package ru.bogdanov.tgbotforbooking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.tgbotforbooking.entities.Notification;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findFirstByNotificationDateTimeBetween(LocalDateTime from, LocalDateTime to);

}
