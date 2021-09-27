package ru.atomicscience.restapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.atomicscience.restapp.dao.UsersCrudRepository;
import ru.atomicscience.restapp.models.User;
import ru.atomicscience.restapp.models.UserRole;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersCrudRepository repository;

    public UsersController(UsersCrudRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Optional<String>> addUser(@RequestBody User user) throws URISyntaxException {
        if(!user.isFull())
            return ResponseEntity.badRequest().body(Optional.of("The user is missing required fields"));

        if(repository.existsByLogin(user.getLogin()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Optional.of("User with specified login already exists"));

        repository.save(user);

        return ResponseEntity.created(new URI("/users/" + user.getId())).build();
    }

    @PutMapping("/promote")
    public ResponseEntity<String> promoteUser(@RequestParam UserRole role, @RequestParam UUID id) {
        Optional<User> possibleUser = repository.findById(id);

        if(possibleUser.isEmpty()) return ResponseEntity.notFound().build();
        if(role == null) return ResponseEntity.badRequest().body("Invalid role provide");

        User user = possibleUser.get();

        if(user.getRole() == role) return ResponseEntity.ok().build();
        user.setRole(role);

        repository.save(user);

        return ResponseEntity.ok().build();
    }

    // TODO: Pagination
    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUser() {
        return ResponseEntity.ok(repository.findAll());
    }
}

