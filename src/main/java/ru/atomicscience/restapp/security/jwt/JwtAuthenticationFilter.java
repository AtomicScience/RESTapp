package ru.atomicscience.restapp.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(RequestMatcher urlsToProtect, JwtProvider jwtProvider) {
        super(urlsToProtect);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String authHeader = request.getHeader("Authorization");
        JwtAuthenticationToken token = new JwtAuthenticationToken(authHeader, jwtProvider);

        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        if(failed instanceof JwtAuthenticationException) {
            response
                    .getWriter()
                    .write("Failed to process JWT token: " + failed.getMessage());
        } else if(failed instanceof UsernameNotFoundException) {
            response
                    .getWriter()
                    .write("Failed to authorize user: username in the token was not found. Try to relogin");
        } else {
            response
                    .getWriter()
                    .write("Unknown auth error: " + failed.getMessage());
        }
    }
}
