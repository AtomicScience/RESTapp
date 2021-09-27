package ru.atomicscience.restapp.security.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class ProtectedUrlsBean {
    @Bean
    public RequestMatcher getProtectedUrls() {
        return new AndRequestMatcher(
                new NegatedRequestMatcher(new AntPathRequestMatcher("/auth")),
                new NegatedRequestMatcher(new AntPathRequestMatcher("/debug/**")),
                new NegatedRequestMatcher(new AntPathRequestMatcher("/error"))
        );
    }
}
