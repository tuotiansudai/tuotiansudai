package com.tuotiansudai.spring.security;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.spring.UserRoleAccessDeniedException;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

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
            if (principal instanceof User) {
                Collection<GrantedAuthority> authorities = ((User) principal).getAuthorities();
                boolean hasAccount = authorities.stream().anyMatch(grantedAuthority -> Role.INVESTOR.name().equals(grantedAuthority.getAuthority()));

                if (!hasAccount) {
                    throw new UserRoleAccessDeniedException(messages.getMessage(
                            "AbstractAccessDecisionManager.accessDenied", "Access is denied"));
                }
            }

            throw e;
        }
    }

}
