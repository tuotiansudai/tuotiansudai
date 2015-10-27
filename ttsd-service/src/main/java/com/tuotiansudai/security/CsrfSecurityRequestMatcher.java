package com.tuotiansudai.security;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/9/1.
 */
public class CsrfSecurityRequestMatcher implements RequestMatcher {

    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

    private List<String> execludeUrls;

    public List<String> getExecludeUrls() {
        return execludeUrls;
    }

    public void setExecludeUrls(List<String> execludeUrls) {
        this.execludeUrls = execludeUrls;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        PathMatcher matcher = new AntPathMatcher();
        if (execludeUrls != null && execludeUrls.size() > 0) {
            String servletPath = request.getRequestURI();
            for (String url : execludeUrls) {
                if (matcher.match(url, servletPath)) {
                    return false;
                }
            }
        }
        return !allowedMethods.matcher(request.getMethod()).matches();
    }

}
