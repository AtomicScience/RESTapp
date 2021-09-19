package ru.atomicscience.restapp.dao;

import org.springframework.stereotype.Component;
import ru.atomicscience.restapp.models.User;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UsersRepositorySearcher {
    private final UsersCrudRepository repository;

    public UsersRepositorySearcher(UsersCrudRepository repository) {
        this.repository = repository;
    }

    public Iterable<User> searchUsersByFirstName(String firstNamePart) {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(user -> user.getFirstName().contains(firstNamePart))
                .collect(Collectors.toList());
    }

    public Iterable<User> searchUsersByLastName(String lastNamePart) {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(user -> user.getLastName().contains(lastNamePart))
                .collect(Collectors.toList());
    }

    public Iterable<User> searchUsersByFirstAndLastName(String firstNamePart, String lastNamePart) {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(user ->
                        user.getFirstName().contains(firstNamePart) && user.getLastName().contains(lastNamePart))
                .collect(Collectors.toList());
    }
}
