package com.tuotiansudai.web.config.security;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
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
                final List<String> roles = Lists.newArrayList("USER", "STAFF", "CUSTOMER_SERVICE", "ADMIN");
                boolean noAccount = Iterators.all(authorities.iterator(), new Predicate<GrantedAuthority>() {
                    @Override
                    public boolean apply(GrantedAuthority grantedAuthority) {
                        return roles.contains(grantedAuthority.getAuthority());
                    }
                });

                if (noAccount) {
                    throw new UserRoleAccessDeniedException(messages.getMessage(
                            "AbstractAccessDecisionManager.accessDenied", "Access is denied"));
                }
            }

            throw e;
        }
    }

}
