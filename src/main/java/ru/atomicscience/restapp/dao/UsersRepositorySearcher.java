package ru.atomicscience.restapp.dao;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.atomicscience.restapp.models.User;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UsersRepositorySearcher {
    private final UsersCrudRepository repository;
    private final PasswordEncoder encoder;

    public UsersRepositorySearcher(UsersCrudRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
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

    public Optional<User> searchUsersByLoginAndPassword(String login, String password) {
        Optional<User> optionalUser = repository.findUserByLogin(login);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (encoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
