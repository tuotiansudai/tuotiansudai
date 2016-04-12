package com.ttsd.api.security;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class MySavedRequestAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private Map<String, String> appHeaders = Maps.newHashMap();

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = this.determineTargetUrl(request, response);
        if(response.isCommitted()) {
            this.logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
        } else if (!this.isContainsAppHeader(request)) {
            this.getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }

    private boolean isContainsAppHeader(final HttpServletRequest httpServletRequest) {
        Optional<Map.Entry<String, String>> optional = Iterators.tryFind(appHeaders.entrySet().iterator(), new Predicate<Map.Entry<String, String>>() {
            @Override
            public boolean apply(Map.Entry<String, String> entry) {
                return entry.getValue().equals(httpServletRequest.getHeader(entry.getKey()));
            }
        });

        return optional.isPresent();
    }

    public void setAppHeaders(Map<String, String> appHeaders) {
        this.appHeaders = appHeaders;
    }
}
