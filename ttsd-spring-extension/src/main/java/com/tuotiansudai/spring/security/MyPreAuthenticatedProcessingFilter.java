package com.tuotiansudai.spring.security;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Component
public class MyPreAuthenticatedProcessingFilter extends GenericFilterBean implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher = null;

    @Autowired
    private SignInClient signInClient;

    @Autowired
    private MyAuthenticationUtil myAuthenticationUtil;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (logger.isDebugEnabled()) {
            logger.debug("Checking secure context token: " + SecurityContextHolder.getContext().getAuthentication());
        }

        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        if (currentUser != null && currentUser.isAuthenticated()) {
            List<Cookie> cookies = Lists.newArrayList(httpServletRequest.getCookies());

            Optional<Cookie> cookieOptional = Iterators.tryFind(cookies.iterator(), new Predicate<Cookie>() {
                @Override
                public boolean apply(Cookie input) {
                    return input.getName().equalsIgnoreCase("TSID");
                }
            });

            if (cookieOptional.isPresent()) {
                SignInResult signInResult = signInClient.verifyToken(cookieOptional.get().getValue(), Source.WEB);
                if (signInResult == null || !signInResult.isResult()) {
                    this.myAuthenticationUtil.removeAuthentication();
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher anApplicationEventPublisher) {
        this.eventPublisher = anApplicationEventPublisher;
    }
}
