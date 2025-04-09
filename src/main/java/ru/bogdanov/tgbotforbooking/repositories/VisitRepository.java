package ru.bogdanov.tgbotforbooking.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.tgbotforbooking.entities.Visit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends CrudRepository<Visit, Long> {

    List<Visit> findVisitsByUserTgAccountAndVisitDateTimeGreaterThan(String tgAccount, LocalDateTime visitDateTime);

    List<Visit> findVisitsByUserTgAccountAndVisitDateTimeLessThan(String tgAccount, LocalDateTime visitDateTime);

    List<Visit> findVisitsByVisitDateTimeBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByVisitDateTimeBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByEndVisitDateTimeBetween(LocalDateTime start, LocalDateTime end);

    Integer countByUserIdAndVisitDateTimeAfter(Long userId, LocalDateTime visitDateTime);

}
