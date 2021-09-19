package ru.atomicscience.restapp.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.atomicscience.restapp.dao.UsersCrudRepository;
import ru.atomicscience.restapp.models.User;

import java.util.Optional;

@Component
public class ApplicationUserDetailsService implements UserDetailsService {
    private final UsersCrudRepository repository;

    public ApplicationUserDetailsService(UsersCrudRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> loadedUser = repository.findUserByLogin(username);
        if(loadedUser.isEmpty()) throw new UsernameNotFoundException("Username not found");

        User user = loadedUser.get();
        return new SpringSecurityUser(
                user.getLogin(),
                user.getPassword(),
                user.getAuthorities());
    }
}
