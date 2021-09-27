package ru.atomicscience.restapp.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class TokenInvalidatedException extends AuthenticationException {
    public TokenInvalidatedException() {
        super("Token was invalidated");
    }
}
