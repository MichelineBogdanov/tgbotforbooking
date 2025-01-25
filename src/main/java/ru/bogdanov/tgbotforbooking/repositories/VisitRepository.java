package ru.bogdanov.tgbotforbooking.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.tgbotforbooking.enteties.Visit;

@Repository
public interface VisitRepository extends CrudRepository<Visit, Long> {

    Visit findVisitByUserTgAccount(String tgAccount);

}
