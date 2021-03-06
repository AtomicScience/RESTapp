package ru.atomicscience.restapp.security.accessDecision;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.UnanimousBased;

import java.util.List;

@Configuration
public class DecisionManagerProvider {
    private final AllowForAllDecisionVoter   allowForAll;
    private final AllowForUsersDecisionVoter allowForUsers;
    private final SingleUserDecisionVoter    singleUser;
    private final UsersDecisionVoter         users;
    private final PromoteDecisionVoter       promote;

    public DecisionManagerProvider(AllowForAllDecisionVoter allowForAll,
                                   AllowForUsersDecisionVoter allowForUsers,
                                   SingleUserDecisionVoter singleUser,
                                   UsersDecisionVoter users,
                                   PromoteDecisionVoter promote) {
        this.allowForAll = allowForAll;
        this.allowForUsers = allowForUsers;
        this.singleUser = singleUser;
        this.users = users;
        this.promote = promote;
    }

    @Bean
    public AccessDecisionManager getAccessDecisionManager() {
        return new UnanimousBased(List.of(
                allowForAll,
                allowForUsers,
                singleUser,
                users,
                promote
        ));
    }
}
