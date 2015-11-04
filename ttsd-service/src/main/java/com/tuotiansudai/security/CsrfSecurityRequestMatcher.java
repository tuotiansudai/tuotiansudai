package com.tuotiansudai.security;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

public class CsrfSecurityRequestMatcher implements RequestMatcher {

    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

    private List<String> excludedUrls = Lists.newArrayList();

    @Override
    public boolean matches(HttpServletRequest request) {
        PathMatcher matcher = new AntPathMatcher();
        if (CollectionUtils.isNotEmpty(excludedUrls)) {
            String servletPath = request.getRequestURI();
            for (String url : excludedUrls) {
                if (matcher.match(url, servletPath)) {
                    return false;
                }
            }
        }
        return !allowedMethods.matcher(request.getMethod()).matches();
    }

    public void setExcludedUrls(List<String> excludedUrls) {
        this.excludedUrls = excludedUrls;
    }

}
