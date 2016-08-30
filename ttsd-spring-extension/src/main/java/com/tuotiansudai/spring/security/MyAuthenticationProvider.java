package com.tuotiansudai.spring.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.squareup.okhttp.*;
import com.tuotiansudai.spring.MyUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class MyAuthenticationProvider implements AuthenticationProvider {

    static Logger logger = Logger.getLogger(MyAuthenticationProvider.class);

    @Autowired
    private CaptchaHelper captchaHelper;

    @Autowired
    private HttpServletRequest httpServletRequest;

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private OkHttpClient okHttpClient = new OkHttpClient();

    private ObjectMapper objectMapper = new ObjectMapper();

    public MyAuthenticationProvider() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MyWebAuthenticationDetails details = (MyWebAuthenticationDetails) authentication.getDetails();

        if (!validateCaptcha(authentication)) {
            throw new CaptchaNotMatchException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.captchaNotMatch", "验证码不正确"));
        }

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder()
                .add("username", !Strings.isNullOrEmpty(details.getUsername()) ? details.getUsername() : details.getMobile())
                .add("password", authentication.getCredentials().toString())
                .add("token", !Strings.isNullOrEmpty(details.getSessionId()) ? details.getSessionId() : "none token")
                .add("source", details.getSource())
                .add("deviceId", !Strings.isNullOrEmpty(details.getDeviceId()) ?  details.getDeviceId() : "");

        RequestBody requestBody = formEncodingBuilder.build();
        Request.Builder request = new Request.Builder()
                .url("http://localhost:5000/login/")
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

                UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                        new MyUser(loginResult.getToken(), loginResult.getUserInfo().getLoginName(), authentication.getCredentials().toString(), true, true, true, true, grantedAuthorities, loginResult.getUserInfo().getMobile()),
                        authentication.getCredentials(),
                        grantedAuthorities);
                result.setDetails(authentication.getDetails());
                httpServletRequest.setAttribute("newSessionId", loginResult.getToken());

                return result;
            }
            throw new BadCredentialsException(loginResult.getMessage());
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }

    private boolean validateCaptcha(Authentication authentication) {
        MyWebAuthenticationDetails details = (MyWebAuthenticationDetails) authentication.getDetails();
        String sessionId = details.getSessionId();
        String deviceId = details.getDeviceId();
        return captchaHelper.captchaVerify(details.getCaptcha(), Strings.isNullOrEmpty(deviceId) ? sessionId : deviceId, details.getRemoteAddress());
    }
}
