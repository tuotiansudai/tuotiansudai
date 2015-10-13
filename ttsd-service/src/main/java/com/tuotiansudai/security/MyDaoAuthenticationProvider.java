package com.tuotiansudai.security;

import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.utils.CaptchaVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private CaptchaVerifier captchaVerifier;
    @Autowired
    private UserMapper userMapper;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        super.additionalAuthenticationChecks(userDetails, authentication);

        UserModel userModel = userMapper.findByLoginNameOrMobile(httpServletRequest.getParameter("username"));
        boolean enabled = userModel.isActive();
        if (!enabled) {
            String errorMessage = MessageFormat.format("Login Error: {0} is locked!", httpServletRequest.getParameter("username"));
            throw new DisabledException(errorMessage);
        }

        String captcha = httpServletRequest.getParameter("captcha");
        boolean result = this.captchaVerifier.loginCaptchaVerify(captcha);

        if (!result) {
            logger.debug("Authentication failed: captcha does not match stored value");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }

    public void setCaptchaVerifier(CaptchaVerifier captchaVerifier) {
        this.captchaVerifier = captchaVerifier;
    }

}
