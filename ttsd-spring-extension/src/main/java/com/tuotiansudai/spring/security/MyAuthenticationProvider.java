package com.tuotiansudai.spring.security;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.WeChatService;
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

import java.util.List;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    static Logger logger = Logger.getLogger(MyAuthenticationProvider.class);

    @Autowired
    private CaptchaHelper captchaHelper;

    @Autowired
    private SignInClient signInClient;

    @Autowired
    private WeChatService weChatService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MyWebAuthenticationDetails details = (MyWebAuthenticationDetails) authentication.getDetails();

        if (!validateCaptcha(authentication)) {
            throw new CaptchaNotMatchException("验证码不正确");
        }

        SignInResult signInResult = signInClient.login(Strings.isNullOrEmpty(details.getUsername()) ? details.getMobile() : details.getUsername(),
                authentication.getCredentials().toString(),
                details.getSessionId(),
                Source.valueOf(details.getSource().toUpperCase()),
                details.getDeviceId());

        if (signInResult == null || !signInResult.isResult()) {
            throw new BadCredentialsException(signInResult != null ? signInResult.getMessage() : "登录异常");
        }

        this.weChatService.bind(details.getMobile(), details.getOpenid()); //登录成功绑定微信号

        List<GrantedAuthority> grantedAuthorities = Lists.transform(signInResult.getUserInfo().getRoles(), SimpleGrantedAuthority::new);

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                new MyUser(signInResult.getToken(), signInResult.getUserInfo().getLoginName(), authentication.getCredentials().toString(), true, true, true, true, grantedAuthorities, signInResult.getUserInfo().getMobile()),
                authentication.getCredentials(),
                grantedAuthorities);
        result.setDetails(authentication.getDetails());
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
