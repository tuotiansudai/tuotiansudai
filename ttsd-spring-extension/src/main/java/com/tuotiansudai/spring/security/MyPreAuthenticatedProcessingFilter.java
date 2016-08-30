package com.tuotiansudai.spring.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

public class MyPreAuthenticatedProcessingFilter extends GenericFilterBean implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher = null;

    private OkHttpClient okHttpClient = new OkHttpClient();

    private ObjectMapper objectMapper = new ObjectMapper();

    public MyPreAuthenticatedProcessingFilter() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

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
                String sessionId = cookieOptional.get().getValue();
                Request.Builder preAuthenticatedRequest = new Request.Builder()
                        .url(MessageFormat.format("http://localhost:5000/session/{0}", sessionId))
                        .get();
                Response execute = okHttpClient.newCall(preAuthenticatedRequest.build()).execute();
                LoginResult loginResult = objectMapper.readValue(execute.body().string(), LoginResult.class);
                if (!loginResult.isResult()) {
                    this.preAuthenticatedProcess(httpServletRequest);
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher anApplicationEventPublisher) {
        this.eventPublisher = anApplicationEventPublisher;
    }

    private void preAuthenticatedProcess(HttpServletRequest httpServletRequest) {
        SecurityContextHolder.clearContext();

        HttpSession session = httpServletRequest.getSession(false);

        if (session != null) {
            logger.debug("Invalidating existing session");
            session.invalidate();
            httpServletRequest.getSession();
        }
    }
}
