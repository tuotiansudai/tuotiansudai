package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.security.MyUser;
import com.tuotiansudai.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
import java.util.ArrayList;
import java.util.List;

public class MobileAppAuthenticationTokenProcessingFilter extends GenericFilterBean {

    @Autowired
    private MobileAppTokenProvider mobileAppTokenProvider;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String ignoreUrls = "/login";

    private String[] ignoreUrlArray;

    private String refreshTokenUrl = "/refresh-token";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (shouldNotFilter(httpServletRequest)) {
            authenticateAnonymousToken();
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpServletRequest);
        String token = mobileAppTokenProvider.getToken(bufferedRequest.getInputStream());
        String loginName = null;
        if (!Strings.isNullOrEmpty(token)) {
            loginName = mobileAppTokenProvider.getUserNameByToken(token);
        }
        if (!Strings.isNullOrEmpty(loginName)) {
            if (isRefreshTokenRequest(httpServletRequest)) {
                processGenerateTokenRequest(httpServletResponse, loginName, token);
                return;
            }
            bufferedRequest.setAttribute("currentLoginName", loginName);
            authenticateToken(loginName);
        } else {
            authenticateAnonymousToken();
        }
        chain.doFilter(bufferedRequest, response);
    }

    private boolean isRefreshTokenRequest(HttpServletRequest request) {
        return refreshTokenUrl.equalsIgnoreCase(request.getRequestURI());
    }

    private boolean shouldNotFilter(HttpServletRequest request) {
        if (request.getMethod().equalsIgnoreCase("GET")) {
            return true;
        }

        String uri = request.getRequestURI();
        for (String ignoreUrl : ignoreUrlArray) {
            if (ignoreUrl.equalsIgnoreCase(uri)) {
                return true;
            }
        }
        return false;
    }

    private void processGenerateTokenRequest(HttpServletResponse httpServletResponse, String loginName, String token) throws IOException {
        String newToken = mobileAppTokenProvider.refreshToken(loginName, token);
        BaseResponseDto dto = mobileAppTokenProvider.generateResponseDto(newToken);
        responseObject(httpServletResponse, dto);
    }

    private void responseObject(HttpServletResponse httpServletResponse, BaseResponseDto responseDto) throws IOException {
        httpServletResponse.setContentType("application/json; charset=UTF-8");
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter out = httpServletResponse.getWriter()) {
            String jsonString = objectMapper.writeValueAsString(responseDto);
            out.print(jsonString);
        }
    }

    private void authenticateToken(String loginName) {
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(loginName);

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(grantedAuthority);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void authenticateAnonymousToken() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
        UserDetails userDetails = new MyUser("anonymousUser", "anonymousUser", true, true, true, true, grantedAuthorities, "", "");

        AnonymousAuthenticationToken authentication = new AnonymousAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword(), grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected void initFilterBean() throws ServletException {
        if (!Strings.isNullOrEmpty(ignoreUrls)) {
            ignoreUrlArray = ignoreUrls.split(",");
        } else {
            ignoreUrlArray = new String[]{};
        }
    }

    public void setIgnoreUrls(String ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    public void setRefreshTokenUrl(String refreshTokenUrl) {
        this.refreshTokenUrl = refreshTokenUrl;
    }
}
