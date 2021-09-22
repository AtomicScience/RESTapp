package ru.atomicscience.restapp.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(JwtValidationResult cause) {
        super(cause.getMessage());

        if(cause == JwtValidationResult.SUCCESS)
            throw new IllegalArgumentException("SUCCESS cannot be a cause of this exception");
    }
}
