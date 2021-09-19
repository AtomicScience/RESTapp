package ru.atomicscience.restapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.atomicscience.restapp.dao.UsersCrudRepository;
import ru.atomicscience.restapp.models.User;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersCrudRepository repository;

    public UsersController(UsersCrudRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Optional<String>> addUser(@RequestBody User user) throws URISyntaxException {
        if(repository.existsById(user.getLogin()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Optional.of("User already exists"));

        repository.save(user);

        return ResponseEntity.created(new URI("/users/" + user.getLogin())).build();
    }

    // TODO: Pagination
    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUser() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{login}")
    public ResponseEntity<Optional<User>> getSingleUser(@PathVariable("login") String login) {
        if(repository.existsById(login))
            return ResponseEntity.ok(repository.findById(login));

        return ResponseEntity.notFound().build();
    }
}

