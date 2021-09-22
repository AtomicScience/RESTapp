package ru.atomicscience.restapp.security.accessDecision;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UsersDecisionVoter extends ServletRequestAccessDecisionVoter {
    public UsersDecisionVoter() {
        super(new AntPathRequestMatcher("/users"));
    }

    @Override
    public int authorizeRequest(Authentication authentication,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        AuthenticationWrapper wrapper = new AuthenticationWrapper(authentication);

        if(request.getMethod().equals("GET"))
            return wrapper.grantedIfHasRole("USER");

        if(request.getMethod().equals("POST"))
            return wrapper.grantedIfHasRole("ADMIN");

        return ACCESS_ABSTAIN;
    }
}