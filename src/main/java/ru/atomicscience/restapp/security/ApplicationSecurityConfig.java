package ru.atomicscience.restapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.atomicscience.restapp.security.jwt.JwtAuthenticationFilter;
import ru.atomicscience.restapp.security.jwt.JwtProvider;
import ru.atomicscience.restapp.security.jwt.TokenInvalidationService;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService detailsService;
    private final AuthenticationProvider authenticationProvider;
    private final RequestMatcher urlsToProtect;
    private final JwtProvider jwtProvider;
    private final AccessDecisionManager accessDecisionManager;
    private final TokenInvalidationService invalidationService;

    public ApplicationSecurityConfig(UserDetailsService detailsService,
                                     AuthenticationProvider authenticationProvider,
                                     RequestMatcher urlsToProtect,
                                     JwtProvider jwtProvider,
                                     AccessDecisionManager accessDecisionManager,
                                     TokenInvalidationService invalidationService) {
        this.detailsService = detailsService;
        this.authenticationProvider = authenticationProvider;
        this.urlsToProtect = urlsToProtect;
        this.jwtProvider = jwtProvider;
        this.accessDecisionManager = accessDecisionManager;
        this.invalidationService = invalidationService;
    }

    @Autowired
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()      // We don't need to take any anti-CSRF actions since there are no sessions
                .formLogin().disable() // To login, users must generate JWT tokens at /auth
                .httpBasic().disable() // We use JWT tokens for authentication
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Disabling any session behavior
                .and().authorizeRequests()
                        // Since we require advanced authorization logic, custom authorization voters are used
                        .accessDecisionManager(accessDecisionManager)
                        .anyRequest().permitAll() // Line affects nothing, since custom manager is used
                .and()
                    .addFilterBefore(authFilter(), AnonymousAuthenticationFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder
                    .userDetailsService(detailsService)
                .and()
                    .authenticationProvider(authenticationProvider);
    }

    // TODO: Token invalidation
    public JwtAuthenticationFilter authFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(urlsToProtect, jwtProvider, invalidationService);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }
}
