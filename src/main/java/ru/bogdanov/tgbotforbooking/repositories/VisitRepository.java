package ru.bogdanov.tgbotforbooking.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.tgbotforbooking.enteties.Visit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends CrudRepository<Visit, Long> {

    List<Visit> findVisitByUserTgAccountAndVisitDateTimeGreaterThan(String user_tgAccount, LocalDateTime visitDateTime);

}
