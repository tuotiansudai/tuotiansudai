package com.tuotiansudai.spring.security;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.squareup.okhttp.*;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.MyUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Component
public class MyAuthenticationUtil {

    static Logger logger = Logger.getLogger(MyAuthenticationUtil.class);

    private OkHttpClient okHttpClient = new OkHttpClient();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Value("${signIn.host}")
    private String signInHost;

    @Value("${signIn.port}")
    private String signInPort;

    public MyAuthenticationUtil() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String createAuthentication(String username, Source source) {
        HttpSession session = httpServletRequest.getSession(false);
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder()
                .add("username", username)
                .add("token", session != null ? session.getId() : "token")
                .add("source", source.name());

        RequestBody requestBody = formEncodingBuilder.build();
        Request.Builder request = new Request.Builder()
                .url(MessageFormat.format("http://{0}:{1}/login/nopassword/", signInHost, signInPort))
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            LoginResult loginResult = objectMapper.readValue(response.body().string(), LoginResult.class);

            if (loginResult.isResult()) {
                List<GrantedAuthority> grantedAuthorities = Lists.transform(loginResult.getUserInfo().getRoles(), new Function<String, GrantedAuthority>() {
                    @Override
                    public GrantedAuthority apply(String role) {
                        return new SimpleGrantedAuthority(role);
                    }
                });

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        new MyUser(loginResult.getToken(), loginResult.getUserInfo().getLoginName(), "", true, true, true, true, grantedAuthorities, loginResult.getUserInfo().getMobile()),
                        "",
                        grantedAuthorities);
                authenticationToken.setDetails(authenticationToken.getDetails());
                if (Source.WEB == source) {
                    httpServletRequest.setAttribute("newSessionId", loginResult.getToken());
                    httpServletRequest.changeSessionId();
                }
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                return loginResult.getToken();
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return "";
    }

    public String refreshTokenProcess(String token, Source source) {
        if (Strings.isNullOrEmpty(token) || source == null) {
            return null;
        }

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder()
                .add("source", source.name());

        RequestBody requestBody = formEncodingBuilder.build();
        Request.Builder request = new Request.Builder()
                .url(MessageFormat.format("http://{0}:{1}/refresh/{2}", signInHost, signInPort, token))
                .post(requestBody);
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            if (response.isSuccessful()) {
                LoginResult loginResult = objectMapper.readValue(response.body().string(), LoginResult.class);
                return loginResult.getToken();
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public void removeAuthentication() {
        SecurityContextHolder.clearContext();

        HttpSession session = httpServletRequest.getSession(false);

        if (session != null) {
            logger.debug("Invalidating existing session");
            session.invalidate();
            httpServletRequest.getSession();
        }
    }
}
