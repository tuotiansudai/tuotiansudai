package com.tuotiansudai.security;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.tuotiansudai.repository.model.Role;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class MyAffirmativeBased extends AffirmativeBased {

    public MyAffirmativeBased(List<AccessDecisionVoter<? extends Object>> decisionVoters) {
        super(decisionVoters);
    }

    @Override
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {

        try {
            super.decide(authentication, object, configAttributes);
        } catch (AccessDeniedException e) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof MyUser) {
                Collection<GrantedAuthority> authorities = ((MyUser) principal).getAuthorities();
                boolean isOnlyUserRole = Iterators.all(authorities.iterator(), new Predicate<GrantedAuthority>() {
                    @Override
                    public boolean apply(GrantedAuthority grantedAuthority) {
                        return Role.USER.name().equals(grantedAuthority.getAuthority());
                    }
                });

                if (isOnlyUserRole) {
                    throw new UserRoleAccessDeniedException(messages.getMessage(
                            "AbstractAccessDecisionManager.accessDenied", "Access is denied"));
                }
            }

            throw e;
        }
    }

}
