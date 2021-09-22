package ru.atomicscience.restapp.security.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/*
    Represents authentication request with a JWT token
    and also user, who has authenticated with one
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    @Getter private final String principal;      // AKA User login
    @Getter private final String credentials;    // Prove the principal is correct.

    @Getter private final JwtValidationResult validationResult;

    /*
        This constructor is used right after receiving any request that requires
        authorization with JWT token
     */
    public JwtAuthenticationToken(String authorizationHeader, JwtProvider provider) {
        super(null);

        String jwtToken = provider.getTokenFromRequest(authorizationHeader);
        validationResult = provider.validateToken(jwtToken);

        if(validationResult == JwtValidationResult.SUCCESS) {
            principal   = provider.getLoginFromToken(jwtToken);
            credentials = jwtToken;
        } else {
            principal   = "";
            credentials = "";
        }
    }

    /*
        This constructor is used when the authentication succeeded
     */
    public JwtAuthenticationToken(String username, Collection<? extends GrantedAuthority> authorities, String jwtToken) {
        super(authorities);
        principal = username;
        credentials = jwtToken;
        validationResult = JwtValidationResult.SUCCESS;
        setAuthenticated(true);
    }

    public boolean isSuccessful() {
        return validationResult == JwtValidationResult.SUCCESS;
    }
}
