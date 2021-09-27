package ru.atomicscience.restapp.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtProvider {
    @Value("$(jwt.secret)")
    private String jwtSecret; // Getting a secret code from the config

    public String generateToken(String login) {
        Date date = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public JwtValidationResult validateToken(String token) {
        if(Objects.isNull(token))
            return JwtValidationResult.NO_TOKEN_FOUND;
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return JwtValidationResult.SUCCESS;
        } catch (ExpiredJwtException e) {
            return JwtValidationResult.EXPIRED;
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return JwtValidationResult.INVALID_TOKEN_FORMAT;
        } catch (SignatureException e) {
            return JwtValidationResult.INVALID_SIGNATURE;
        } catch (Exception e) {
            return JwtValidationResult.UNKNOWN_FAILURE;
        }
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getTokenFromRequest(String authorizationHeader) {
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
