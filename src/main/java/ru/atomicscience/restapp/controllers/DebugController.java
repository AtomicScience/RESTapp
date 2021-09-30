package ru.atomicscience.restapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.atomicscience.restapp.dao.UsersCrudRepository;
import ru.atomicscience.restapp.debug.RandomUserGenerator;
import ru.atomicscience.restapp.models.User;
import ru.atomicscience.restapp.models.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "https://app.swaggerhub.com", allowCredentials = "true")
@RestController
@RequestMapping("/debug")
public class DebugController {
    private final UsersCrudRepository repository;
    private final RandomUserGenerator userGenerator;

    public DebugController(UsersCrudRepository repository, RandomUserGenerator userGenerator) {
        this.repository = repository;
        this.userGenerator = userGenerator;
    }

    @PostMapping("/resetStorage")
    public ResponseEntity<Void> resetStorage() {
        repository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/populateDatabase")
    public ResponseEntity<Object> populateDatabase(@RequestParam int count) {
        if(count < 0 || count > 100) return ResponseEntity.badRequest().body("Count can be between 0 and 100");

        List<User> usersToAdd = userGenerator.generateTestUsers(count);

        repository.deleteAll();
        repository.saveAll(usersToAdd);

        return ResponseEntity.ok(usersToAdd);
    }

    @PostMapping("/promoteToAdmin")
    public ResponseEntity<Void> promoteToAdmin(@RequestParam UUID id) {
        Optional<User> possibleUser = repository.findById(id);

        if(possibleUser.isEmpty())
            return ResponseEntity.notFound().build();

        User user = possibleUser.get();

        user.setRole(UserRole.ADMIN);
        repository.save(user);

        return ResponseEntity.ok().build();
    }
}
