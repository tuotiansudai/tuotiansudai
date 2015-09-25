package com.tuotiansudai.security;

import com.tuotiansudai.utils.CaptchaVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private HttpServletRequest httpServletRequest;

    private CaptchaVerifier captchaVerifier;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        super.additionalAuthenticationChecks(userDetails, authentication);

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
