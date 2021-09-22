package ru.atomicscience.restapp.security.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService service;

    public JwtAuthenticationProvider(UserDetailsService service) {
        this.service = service;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(!supports(authentication.getClass())) return null;
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;

        if(!jwtToken.isSuccessful())
            throw new JwtAuthenticationException(jwtToken.getValidationResult());

        UserDetails user = service.loadUserByUsername(jwtToken.getPrincipal());

        return new JwtAuthenticationToken(
                user.getUsername(),
                user.getAuthorities(),
                jwtToken.getCredentials()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }
}
