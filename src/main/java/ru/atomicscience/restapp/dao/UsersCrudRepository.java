package ru.atomicscience.restapp.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.atomicscience.restapp.models.User;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface UsersCrudRepository extends CrudRepository<User, UUID> {
    Optional<User> findUserByLogin(String login);
    boolean existsByLogin(String login);
}