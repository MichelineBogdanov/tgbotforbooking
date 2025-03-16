package ru.bogdanov.tgbotforbooking.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.tgbotforbooking.entities.CosmetologyService;

@Repository
public interface ServiceRepository extends CrudRepository<CosmetologyService, Long> {

}
