package ru.atomicscience.restapp.security.accessDecision;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AllowForAllDecisionVoter extends ServletRequestAccessDecisionVoter {
    public AllowForAllDecisionVoter() {
        super(new OrRequestMatcher(
                new AntPathRequestMatcher("/auth"),
                new AntPathRequestMatcher("/debug/**")
        ));
    }

    @Override
    public int authorizeRequest(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        return ACCESS_GRANTED;
    }
}
