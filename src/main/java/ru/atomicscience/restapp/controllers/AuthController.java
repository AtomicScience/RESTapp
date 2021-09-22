package ru.atomicscience.restapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.atomicscience.restapp.dao.UsersRepositorySearcher;
import ru.atomicscience.restapp.models.User;
import ru.atomicscience.restapp.security.jwt.JwtProvider;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsersRepositorySearcher searcher;
    private final JwtProvider jwtProvider;

    public AuthController(UsersRepositorySearcher searcher, JwtProvider jwtProvider) {
        this.searcher = searcher;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping
    public ResponseEntity<String> auth(@RequestParam String login, @RequestParam String password) {
        Optional<User> optionalUser = searcher.searchUsersByLoginAndPassword(login, password);

        if(optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

        User user = optionalUser.get();
        String token = jwtProvider.generateToken(user.getLogin());

        return ResponseEntity.ok(token);
    }
}
