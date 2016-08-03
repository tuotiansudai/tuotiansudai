package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.spring.MyAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MobileAppAuthenticationTokenProcessingFilter extends GenericFilterBean {

    @Autowired
    private MobileAppTokenProvider mobileAppTokenProvider;

    @Autowired
    private MyAuthenticationManager myAuthenticationManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final PathMatcher matcher = new AntPathMatcher();

    private List<String> ignoreUrls = Lists.newArrayList("/login");

    private String refreshTokenUrl = "/refresh-token";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (shouldNotAuthenticated(httpServletRequest)) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String loginName = mobileAppTokenProvider.getLoginName(httpServletRequest);
        if (Strings.isNullOrEmpty(loginName)) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        if (refreshTokenUrl.equalsIgnoreCase(httpServletRequest.getRequestURI())) {
            this.processGenerateTokenRequest(httpServletResponse, loginName);
            return;
        }

        myAuthenticationManager.createAuthentication(loginName);
        chain.doFilter(httpServletRequest, response);
    }

    private boolean shouldNotAuthenticated(HttpServletRequest request) {
        final String uri = request.getRequestURI();

        return Iterators.any(ignoreUrls.iterator(), new Predicate<String>() {
            @Override
            public boolean apply(String ignoreUrl) {
                return matcher.match(ignoreUrl, uri);
            }
        });
    }

    private void processGenerateTokenRequest(HttpServletResponse httpServletResponse, String loginName) throws IOException {
        String newToken = mobileAppTokenProvider.refreshToken(loginName);
        BaseResponseDto dto = mobileAppTokenProvider.generateResponseDto(newToken);
        httpServletResponse.setContentType("application/json; charset=UTF-8");
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());

        PrintWriter out = null;
        try {
            out = httpServletResponse.getWriter();
            out.print(objectMapper.writeValueAsString(dto));
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void setIgnoreUrls(List<String> ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    public void setRefreshTokenUrl(String refreshTokenUrl) {
        this.refreshTokenUrl = refreshTokenUrl;
    }
}
