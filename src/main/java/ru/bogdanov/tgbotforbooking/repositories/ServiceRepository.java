package ru.bogdanov.tgbotforbooking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.tgbotforbooking.entities.CosmetologyService;

@Repository
public interface ServiceRepository extends JpaRepository<CosmetologyService, Long> {

}
