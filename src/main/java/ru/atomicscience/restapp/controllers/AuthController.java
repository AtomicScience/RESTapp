package ru.atomicscience.restapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.atomicscience.restapp.dao.UsersRepositorySearcher;
import ru.atomicscience.restapp.models.User;
import ru.atomicscience.restapp.security.jwt.JwtProvider;
import ru.atomicscience.restapp.security.jwt.TokenInvalidationService;

import java.util.Optional;

@CrossOrigin(origins = "https://app.swaggerhub.com")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsersRepositorySearcher searcher;
    private final JwtProvider jwtProvider;
    private final TokenInvalidationService invalidationService;

    public AuthController(UsersRepositorySearcher searcher,
                          JwtProvider jwtProvider,
                          TokenInvalidationService invalidationService) {
        this.searcher = searcher;
        this.jwtProvider = jwtProvider;
        this.invalidationService = invalidationService;
    }

    @PostMapping
    public ResponseEntity<String> auth(@RequestParam String login, @RequestParam String password) {
        Optional<User> optionalUser = searcher.searchUsersByLoginAndPassword(login, password);

        if(optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

        User user = optionalUser.get();
        String token = jwtProvider.generateToken(user.getLogin());

        invalidationService.assignValidTokenForUser(user, token);

        return ResponseEntity.ok(token);
    }
}
