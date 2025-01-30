package ru.bogdanov.tgbotforbooking.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.tgbotforbooking.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByTgAccount(String tgAccount);

    boolean existsUserByTgAccount(String tgAccount);

}
