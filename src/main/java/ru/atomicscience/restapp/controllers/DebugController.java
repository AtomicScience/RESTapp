package ru.atomicscience.restapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.atomicscience.restapp.dao.UsersCrudRepository;
import ru.atomicscience.restapp.debug.RandomUserGenerator;
import ru.atomicscience.restapp.models.User;
import ru.atomicscience.restapp.security.UserRole;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

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
        return ResponseEntity.ok().build();
    }

    @PostMapping("/populateDatabase")
    public ResponseEntity<Void> populateDatabase(@RequestParam @Min(0) @Max(1000) int count) {
        List<User> usersToAdd = userGenerator.generateTestUsers(count);

        repository.deleteAll();
        repository.saveAll(usersToAdd);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/promoteToAdmin")
    public ResponseEntity<Void> promoteToAdmin(@RequestParam String login) {
        if(!repository.existsById(login))
            return ResponseEntity.notFound().build();

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        User user = repository.findById(login).get();

        user.setRole(UserRole.ADMIN);

        repository.save(user);

        return ResponseEntity.ok().build();
    }
}
