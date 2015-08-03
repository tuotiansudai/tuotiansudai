package com.ttsd.api.security;

import com.esoft.archer.user.service.impl.MyJdbcUserDetailsManager;
import com.esoft.core.annotations.Logger;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.ttsd.api.dto.ReturnMessage;
import com.ttsd.redis.RedisClient;
import org.apache.commons.logging.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import java.util.List;

public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    @Logger
    static Log log;

    @Autowired
    private MyJdbcUserDetailsManager myJdbcUserDetailsManager;

    @Autowired
    private RedisClient redisClient;

    private String loginUrl;
    private String appRequestUrlPrefix;
    private List<String> excludedUrls = Lists.newArrayList();

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        final String uri = httpServletRequest.getRequestURI();

        Optional<String> tryFindExcludedUrl = Iterators.tryFind(excludedUrls.iterator(), new Predicate<String>() {
            @Override
            public boolean apply(String excludedUrl) {
                return uri.equalsIgnoreCase(excludedUrl);
            }
        });
        if (tryFindExcludedUrl.isPresent()) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        if (uri.startsWith(appRequestUrlPrefix) && httpServletRequest.getMethod().equalsIgnoreCase("post")) {
            BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpServletRequest);
            String token = this.getToken(bufferedRequest);
            String loginName = this.getTokenLoginName(token);
            if (Strings.isNullOrEmpty(loginName)) {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Authentication token was either missing or invalid.");
                return;
            }
            this.authenticateToken(loginName);
            chain.doFilter(bufferedRequest, response);
            return;
        }

        if (Strings.isNullOrEmpty(httpServletRequest.getParameter("source")) && loginUrl.equalsIgnoreCase(uri) && httpServletRequest.getMethod().equalsIgnoreCase("post")) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            this.generateLoginResponse(httpServletResponse);
            return;
        }

        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void generateLoginResponse(HttpServletResponse httpServletResponse) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        httpServletResponse.setContentType("application/json; charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        try (PrintWriter out = httpServletResponse.getWriter()) {
            JSONObject root = new JSONObject();
            if (authentication != null && authentication.isAuthenticated()) {
                String token = this.generateToken(authentication);
                root.put("code", ReturnMessage.SUCCESS.getCode());
                root.put("message", ReturnMessage.SUCCESS.getMsg());
                JSONObject data = new JSONObject();
                data.put("token", token);
                root.put("data", data);
            } else {
                root.put("code", ReturnMessage.LOGIN_FAILED.getCode());
                root.put("message", ReturnMessage.LOGIN_FAILED.getMsg());
            }
            out.print(root);
        } catch (JSONException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    private String getTokenLoginName(String token) {
        if (Strings.isNullOrEmpty(token)) {
            return null;
        }
        String loginName = redisClient.get(token);
        if (Strings.isNullOrEmpty(loginName)) {
            return null;
        }

        return loginName;
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
            String json = new String(outStream.toByteArray(), "UTF-8");
            JSONObject root = new JSONObject(json);
            JSONObject baseParam = (JSONObject) root.get("baseParam");
            return (String) baseParam.get("token");
        } catch (IOException | JSONException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private String generateToken(Authentication authentication) {
        //TODO: generate token
        String token = "test";
        int expiredSeconds = 300;
        this.redisClient.setex(token, authentication.getName(), expiredSeconds);
        return token;
    }

    public void setExcludedUrls(List<String> excludedUrls) {
        this.excludedUrls = excludedUrls;
    }

    public void setAppRequestUrlPrefix(String appRequestUrlPrefix) {
        this.appRequestUrlPrefix = appRequestUrlPrefix;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
