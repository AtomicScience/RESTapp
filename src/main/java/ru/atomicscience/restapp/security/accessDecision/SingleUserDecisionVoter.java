package ru.atomicscience.restapp.security.accessDecision;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import ru.atomicscience.restapp.dao.UsersCrudRepository;
import ru.atomicscience.restapp.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/*
    A decision manager that enables users to edit their
    own records. By default, entry editing is enabled for
    administrators only
 */
@Component
public class SingleUserDecisionVoter extends ServletRequestAccessDecisionVoter {
    private final UsersCrudRepository repository;
    public SingleUserDecisionVoter(UsersCrudRepository repository) {
        super(new AntPathRequestMatcher("/users/user/**"));
        this.repository = repository;
    }

    @Override
    public int authorizeRequest(Authentication authentication,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        AuthenticationWrapper wrapper = new AuthenticationWrapper(authentication);

        if(request.getMethod().equals("GET"))
            return wrapper.grantedIfHasRole("USER");

        if(request.getMethod().equals("DELETE"))
            return wrapper.grantedIfHasRole("ADMIN");

        if(request.getMethod().equals("PATCH")) {
            String uuidAsString         = getLastSegmentOfPath(request.getServletPath());
            String loginOfRequestSender = (String) authentication.getPrincipal();

            Optional<User> optionalRequestSender = repository.findUserByLogin(loginOfRequestSender);
            if(optionalRequestSender.isEmpty()) return ACCESS_DENIED;

            User requestSender = optionalRequestSender.get();

            boolean uuidsMatch = requestSender.getId().toString().equals(uuidAsString);
            return wrapper.grantedIfConditionIsTrue(uuidsMatch);
        }

        return ACCESS_GRANTED;
    }

    private String getLastSegmentOfPath(String path) {
        return path.replace("/users/user/", "");
    }
}
