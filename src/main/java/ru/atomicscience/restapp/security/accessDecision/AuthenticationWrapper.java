package ru.atomicscience.restapp.security.accessDecision;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * A simple utility class to simplify some checks performed by AccessDecisionVoters on
 * Authentication objects, avoiding boilerplate code, such as:
 * <p>
 *  {@code authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ? ACCESS_GRANTED : ACCESS_DENIED;}
 * <p>
 * Instead, using the wrapper:
 * <p>
 *  {@code wrapper.grantedIfHasRole("ADMIN");}
 */
public class AuthenticationWrapper {
    private final Authentication authentication;

    public AuthenticationWrapper(Authentication authentication) {
        this.authentication = authentication;
    }

    /**
     * Checks if authentication has the provided role, and returns
     * appropriate authorization result
     * @param role a role, without the "ROLE_" prefix
     * @return ACCESS_GRANTED if user has the rule, and ACCESS_DENIED otherwise
     */
    public int grantedIfHasRole(String role) {
        return grantedIfConditionIsTrue(hasRole(role));
    }

    /**
     * Returns the authorization result based on the boolean provided
     * @param condition a boolean to check
     * @return ACCESS_GRANTED if condition is true, and ACCESS_DENIED if false
     */
    public int grantedIfConditionIsTrue(boolean condition) {
        return condition ? AccessDecisionVoter.ACCESS_GRANTED : AccessDecisionVoter.ACCESS_DENIED;
    }

    /**
     * Checks if authentication has the provided role
     * @param role a role, without the "ROLE_" prefix
     * @return true if authentication has the role
     */
    public boolean hasRole(String role) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role));
    }
}
