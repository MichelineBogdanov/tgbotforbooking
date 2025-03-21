package ru.bogdanov.tgbotforbooking.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.tgbotforbooking.entities.Notification;

@Repository
public interface ScheduleRepository extends CrudRepository<Notification, Long> {
}
