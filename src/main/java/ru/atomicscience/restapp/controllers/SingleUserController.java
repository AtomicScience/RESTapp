package ru.atomicscience.restapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.atomicscience.restapp.dao.UsersCrudRepository;
import ru.atomicscience.restapp.models.User;
import ru.atomicscience.restapp.security.jwt.TokenInvalidationService;
import ru.atomicscience.restapp.util.ObjectUtilities;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "https://app.swaggerhub.com", allowCredentials = "true")
@RestController
@RequestMapping("/users/user/{id}")
public class SingleUserController {
    private final UsersCrudRepository repository;
    private final PasswordEncoder encoder;
    private final TokenInvalidationService invalidationService;

    public SingleUserController(UsersCrudRepository repository,
                                PasswordEncoder encoder,
                                TokenInvalidationService invalidationService) {
        this.repository = repository;
        this.encoder = encoder;
        this.invalidationService = invalidationService;
    }

    @GetMapping
    public ResponseEntity<Optional<User>> getSingleUser(@PathVariable("id") UUID id) {
        if(repository.existsById(id))
            return ResponseEntity.ok(repository.findById(id));

        return ResponseEntity.notFound().build();
    }

    @PatchMapping
    public ResponseEntity<Object> changeUser(@PathVariable("id") UUID id, @RequestBody User newUser) {
        Optional<User> possibleUserToChange = repository.findById(id);

        if(possibleUserToChange.isEmpty())
            return ResponseEntity.notFound().build();

        if(repository.existsByLogin(newUser.getLogin()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Optional.of("User with specified login already exists"));

        if(newUser.getId() != null) {
            return ResponseEntity.badRequest()
                    .body(Optional.of("Cannot change the ID of the user"));
        }

        if(newUser.getRole() != null) {
            return ResponseEntity.badRequest()
                    .body(Optional.of("Cannot change the Role of the user. Use /users/promote instead"));
        }

        User userToChange = possibleUserToChange.get();

        ObjectUtilities.copyNonNullProperties(newUser, userToChange);

        if(newUser.getPassword() != null) {
            String hashedPassword = encoder.encode(newUser.getPassword());
            invalidationService.invalidateTokenForUser(userToChange);
            userToChange.setPassword(hashedPassword);
        }

        repository.save(userToChange);

        return ResponseEntity.ok(Optional.of(userToChange));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id) {
        Optional<User> possibleUserToDelete = repository.findById(id);

        if(possibleUserToDelete.isEmpty())
            return ResponseEntity.notFound().build();

        User userToDelete = possibleUserToDelete.get();

        repository.delete(userToDelete);

        return ResponseEntity.noContent().build();
    }
}