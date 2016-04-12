package com.ttsd.api.security;

import com.esoft.archer.user.service.impl.MyJdbcUserDetailsManager;
import com.esoft.core.annotations.Logger;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ttsd.api.dto.ReturnMessage;
import com.ttsd.redis.RedisClient;
import org.apache.commons.logging.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    @Logger
    static Log log;

    @Autowired
    private MyJdbcUserDetailsManager myJdbcUserDetailsManager;

    @Autowired
    private RedisClient redisClient;

    private Map<String, String> appHeaders = Maps.newHashMap();

    private String loginUrl = "/login";

    private List<String> uriPrefixes = Lists.newArrayList("/v1.0");

    private String refreshTokenUrl = "/refresh-token";

    private int tokenExpiredSeconds = 3600;

    private String tokenName = "token";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        final String uri = httpServletRequest.getRequestURI();

        if (!isHandleCurrentRequest(httpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        if (!this.isContainsAppHeader(httpServletRequest)) {
            try {
                JSONObject root = new JSONObject();
                root.put("code", ReturnMessage.BAD_REQUEST.getCode());
                root.put("message", ReturnMessage.BAD_REQUEST.getMsg());
                this.generateJsonResponse(httpServletResponse, root);
            } catch (JSONException e) {
                log.error(e.getLocalizedMessage(), e);
            }
            return;
        }

        if (loginUrl.equalsIgnoreCase(uri)) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            JSONObject root = new JSONObject();
            try {
                if (authentication != null && authentication.isAuthenticated()) {
                    String token = this.generateToken(authentication.getName());
                    JSONObject data = new JSONObject();
                    root.put("code", ReturnMessage.SUCCESS.getCode());
                    root.put("message", ReturnMessage.SUCCESS.getMsg());
                    data.put(tokenName, token);
                    root.put("data", data);
                } else {
                    AuthenticationException springSecurityLastException = (AuthenticationException) httpServletRequest.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
                    if (springSecurityLastException instanceof DisabledException) {
                        root.put("code", ReturnMessage.USER_IS_DISABLED.getCode());
                        root.put("message", ReturnMessage.USER_IS_DISABLED.getMsg());
                    } else {
                        root.put("code", ReturnMessage.LOGIN_FAILED.getCode());
                        root.put("message", ReturnMessage.LOGIN_FAILED.getMsg());
                    }
                }
                this.generateJsonResponse(httpServletResponse, root);
            } catch (JSONException e) {
                log.error(e.getLocalizedMessage(), e);
            }
            return;
        }

        BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpServletRequest);
        String token = this.getToken(bufferedRequest);
        String loginName = this.getTokenLoginName(token);
        if (Strings.isNullOrEmpty(loginName)) {
            try {
                JSONObject root = new JSONObject();
                root.put("code", ReturnMessage.UNAUTHORIZED.getCode());
                root.put("message", ReturnMessage.UNAUTHORIZED.getMsg());
                this.generateJsonResponse(httpServletResponse, root);
            } catch (JSONException e) {
                log.error(e.getLocalizedMessage(), e);
            }
            return;
        }

        if (refreshTokenUrl.equalsIgnoreCase(uri)) {
            String newToken = this.generateToken(loginName);
            try {
                JSONObject root = new JSONObject();
                JSONObject data = new JSONObject();
                root.put("code", ReturnMessage.SUCCESS.getCode());
                root.put("message", ReturnMessage.SUCCESS.getMsg());
                data.put(tokenName, newToken);
                root.put("data", data);
                this.generateJsonResponse(httpServletResponse, root);
                return;
            } catch (JSONException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        this.authenticateToken(loginName);
        chain.doFilter(bufferedRequest, response);
    }

    private boolean isHandleCurrentRequest(HttpServletRequest httpServletRequest) {
        final String uri = httpServletRequest.getRequestURI();
        return (loginUrl.equalsIgnoreCase(uri) && this.isContainsAppHeader(httpServletRequest)) || refreshTokenUrl.equalsIgnoreCase(uri) ||
                    Iterators.any(uriPrefixes.iterator(), new Predicate<String>() {
                        @Override
                        public boolean apply(String uriPrefix) {
                            return uri.startsWith(uriPrefix);
                        }
                    });
    }


    private void generateJsonResponse(HttpServletResponse httpServletResponse, JSONObject jsonObject) throws IOException {
        httpServletResponse.setContentType("application/json; charset=UTF-8");
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter out = httpServletResponse.getWriter()) {
            out.print(jsonObject);
        }
    }

    private String getTokenLoginName(String token) {
        if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(redisClient.get(token))) {
            return null;
        }
        return redisClient.get(token);
    }

    private void authenticateToken(String loginName) {
        UserDetails userDetails = myJdbcUserDetailsManager.loadUserByUsername(loginName);
        List<GrantedAuthority> grantedAuthorities = myJdbcUserDetailsManager.getUserAuthorities(loginName);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), grantedAuthorities); //this.authenticationProvider.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getToken(BufferedRequestWrapper bufferedRequestWrapper) {
        int bufferSize = 4096;
        ServletInputStream inputStream = bufferedRequestWrapper.getInputStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[bufferSize];
        int count;
        try {
            while ((count = inputStream.read(data, 0, bufferSize)) != -1) {
                outStream.write(data, 0, count);
            }
            String json = new String(outStream.toByteArray(), StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(json);
            JSONObject baseParam = (JSONObject) root.get("baseParam");
            return (String) baseParam.get(tokenName);
        } catch (IOException | JSONException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private String generateToken(String loginName) {
        String tokenTemplate = "app-token:{0}:{1}";
        String token = MessageFormat.format(tokenTemplate, loginName, UUID.randomUUID().toString());
        this.redisClient.setex(token, loginName, this.tokenExpiredSeconds);
        return token;
    }

    private boolean isContainsAppHeader(final HttpServletRequest httpServletRequest) {
        Optional<Map.Entry<String, String>> optional = Iterators.tryFind(appHeaders.entrySet().iterator(), new Predicate<Map.Entry<String, String>>() {
            @Override
            public boolean apply(Map.Entry<String, String> entry) {
                return entry.getValue().equals(httpServletRequest.getHeader(entry.getKey()));
            }
        });

        return httpServletRequest.getMethod().equalsIgnoreCase("post") && optional.isPresent();
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public void setRefreshTokenUrl(String refreshTokenUrl) {
        this.refreshTokenUrl = refreshTokenUrl;
    }

    public void setAppHeaders(Map<String, String> appHeaders) {
        this.appHeaders = appHeaders;
    }

    public void setTokenExpiredSeconds(int tokenExpiredSeconds) {
        this.tokenExpiredSeconds = tokenExpiredSeconds;
    }

    public void setUriPrefixes(List<String> uriPrefixes) {
        this.uriPrefixes = uriPrefixes;
    }
}
