package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private MyUserDetailsService myUserDetailsService;

    final private ObjectMapper objectMapper = new ObjectMapper();

    private List<String> ignoreUrls = Lists.newArrayList("/login");

    private String refreshTokenUrl = "/refresh-token";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpServletRequest);

        String loginName = mobileAppTokenProvider.getLoginName(bufferedRequest);

        if (Strings.isNullOrEmpty(loginName) || shouldNotFilter(httpServletRequest)) {
            authenticateAnonymousToken();
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        if (refreshTokenUrl.equalsIgnoreCase(httpServletRequest.getRequestURI())) {
            this.processGenerateTokenRequest(httpServletResponse, loginName);
            return;
        }

        authenticateToken(loginName);
        chain.doFilter(httpServletRequest, response);
    }

    private boolean shouldNotFilter(HttpServletRequest request) {
        final String uri = request.getRequestURI();
        boolean isIgnoreUri = Iterators.any(ignoreUrls.iterator(), new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.equalsIgnoreCase(uri);
            }
        });

        return request.getMethod().equalsIgnoreCase(HttpMethod.GET.name()) || isIgnoreUri;
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

    private void authenticateToken(String loginName) {
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(loginName);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void authenticateAnonymousToken() {
        AnonymousAuthenticationToken authentication = new AnonymousAuthenticationToken("anonymousUser",
                "anonymousUser",
                Lists.newArrayList(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setIgnoreUrls(List<String> ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    public void setRefreshTokenUrl(String refreshTokenUrl) {
        this.refreshTokenUrl = refreshTokenUrl;
    }
}
