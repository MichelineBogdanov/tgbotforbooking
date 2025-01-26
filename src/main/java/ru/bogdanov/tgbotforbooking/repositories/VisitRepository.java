package ru.bogdanov.tgbotforbooking.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.tgbotforbooking.enteties.Visit;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends CrudRepository<Visit, Long> {

    List<Visit> findVisitByUserTgAccount(String tgAccount);

}
