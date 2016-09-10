package com.tuotiansudai.spring.security;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.MyUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    static Logger logger = Logger.getLogger(MyAuthenticationProvider.class);

    @Autowired
    private CaptchaHelper captchaHelper;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private SignInClient signInClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MyWebAuthenticationDetails details = (MyWebAuthenticationDetails) authentication.getDetails();

        if (!validateCaptcha(authentication)) {
            throw new CaptchaNotMatchException("验证码不正确");
        }

        SignInResult signInResult = signInClient.login(!Strings.isNullOrEmpty(details.getUsername()) ? details.getUsername() : details.getMobile(),
                authentication.getCredentials().toString(),
                !Strings.isNullOrEmpty(details.getSessionId()) ? details.getSessionId() : "none token",
                Source.valueOf(details.getSource().toUpperCase()) ,
                !Strings.isNullOrEmpty(details.getDeviceId()) ? details.getDeviceId() : "");

        if (signInResult == null) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        if (!signInResult.isResult()) {
            throw new BadCredentialsException(signInResult.getMessage());
        }

        List<GrantedAuthority> grantedAuthorities = Lists.transform(signInResult.getUserInfo().getRoles(), new Function<String, GrantedAuthority>() {
            @Override
            public GrantedAuthority apply(String role) {
                return new SimpleGrantedAuthority(role);
            }
        });

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                new MyUser(signInResult.getToken(), signInResult.getUserInfo().getLoginName(), authentication.getCredentials().toString(), true, true, true, true, grantedAuthorities, signInResult.getUserInfo().getMobile()),
                authentication.getCredentials(),
                grantedAuthorities);
        result.setDetails(authentication.getDetails());
        httpServletRequest.setAttribute("newSessionId", signInResult.getToken());

        return result;
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
