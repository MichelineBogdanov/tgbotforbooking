package ru.bogdanov.tgbotforbooking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.tgbotforbooking.entities.Visit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findVisitsByUserTgUserIdAndVisitDateTimeGreaterThan(Long tgUserId, LocalDateTime visitDateTime);

    List<Visit> findVisitsByUserTgUserIdAndVisitDateTimeLessThan(Long tgUserId, LocalDateTime visitDateTime);

    List<Visit> findVisitsByVisitDateTimeBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByVisitDateTimeBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByEndVisitDateTimeBetween(LocalDateTime start, LocalDateTime end);

    Integer countByUserIdAndVisitDateTimeAfter(Long userId, LocalDateTime visitDateTime);

}
