package ru.atomicscience.restapp.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.atomicscience.restapp.dao.UsersCrudRepository;
import ru.atomicscience.restapp.models.User;

import java.util.Objects;
import java.util.Optional;

/*
    This service provides a unified entry point for all operations related to token invalidation
    Instead of directly setting and checking flags and fields on User, this service must be used
 */
@Service
public class TokenInvalidationService {
    private final UsersCrudRepository repository;
    private final PasswordEncoder passwordEncoder;

    public TokenInvalidationService(UsersCrudRepository repository,
                                    PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean checkIfTokenWasInvalidated(JwtAuthenticationToken token) throws AuthenticationException{
        String username = token.getPrincipal();

        Optional<User> optionalUser = repository.findUserByLogin(username);

        if(optionalUser.isEmpty())
            return false;

        User user = optionalUser.get();
        String hashedToken = user.getLastValidToken();

        if(Objects.isNull(token.getCredentials())) return true;

        return !passwordEncoder.matches(token.getCredentials(), hashedToken);
    }

    public void invalidateTokenForUser(User user) {
        user.setLastValidToken(null);
        repository.save(user);
    }

    public void assignValidTokenForUser(User user, String token) {
        user.setLastValidToken(passwordEncoder.encode(token));
        repository.save(user);
    }
}
