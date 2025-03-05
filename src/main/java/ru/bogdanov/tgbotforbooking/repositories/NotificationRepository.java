package ru.bogdanov.tgbotforbooking.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.tgbotforbooking.entities.Notification;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {

    Optional<Notification> findFirstByNotificationDateTimeBetween(LocalDateTime now, LocalDateTime oneHourLater);

}
