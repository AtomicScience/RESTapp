package ru.atomicscience.restapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.atomicscience.restapp.dao.UsersCrudRepository;
import ru.atomicscience.restapp.models.User;
import ru.atomicscience.restapp.models.UserRole;
import ru.atomicscience.restapp.security.jwt.TokenInvalidationService;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersCrudRepository repository;
    private final TokenInvalidationService invalidationService;

    public UsersController(UsersCrudRepository repository,
                           TokenInvalidationService invalidationService) {
        this.repository = repository;
        this.invalidationService = invalidationService;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        if(!user.isFull())
            return ResponseEntity.badRequest().body("The user is missing required fields");

        if(repository.existsByLogin(user.getLogin()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with specified login already exists");

        if(Objects.nonNull(user.getId()))
            return ResponseEntity.badRequest().body("Creation of users with specific IDs is forbidden");

        user.setRole(UserRole.USER); // For security reasons, users cannot be created as Administrators
        repository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(user.getId().toString());
    }

    @PutMapping("/promote")
    public ResponseEntity<String> promoteUser(@RequestParam UserRole role, @RequestParam UUID id) {
        Optional<User> possibleUser = repository.findById(id);

        if(possibleUser.isEmpty()) return ResponseEntity.notFound().build();
        if(role == null) return ResponseEntity.badRequest().body("Invalid role provided");

        User user = possibleUser.get();

        if(user.getRole() == role) return ResponseEntity.ok().build();
        user.setRole(role);

        repository.save(user);

        invalidationService.invalidateTokenForUser(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUser() {
        return ResponseEntity.ok(repository.findAll());
    }
}

