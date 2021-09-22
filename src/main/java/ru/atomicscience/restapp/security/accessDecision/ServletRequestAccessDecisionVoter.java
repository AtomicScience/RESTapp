package ru.atomicscience.restapp.security.accessDecision;

import lombok.Getter;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/*
    A class for more convenient implementations of AccessDecisionFilters for HTTP
    requests. Enables to engage the filter only when the request matches the
    RequestMatcher, provided at the instantiation.
 */
public abstract class ServletRequestAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {
    @Getter private final RequestMatcher matcher;

    public ServletRequestAccessDecisionVoter(RequestMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {
        HttpServletRequest  request  = object.getRequest();
        HttpServletResponse response = object.getResponse();

        if(!matcher.matches(request)) return ACCESS_ABSTAIN;

        return authorizeRequest(authentication, request, response);
    }

    // This method will be fired only when the request matches the provided RequestMatcher
    public abstract int authorizeRequest(Authentication authentication,
                                         HttpServletRequest request,
                                         HttpServletResponse response);

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true; // This class' subclasses are not intended to utilize config attributes
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.equals(clazz);
    }
}
