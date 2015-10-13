package com.tuotiansudai.security;

import com.tuotiansudai.exception.LoginFailedMaxTimesException;
import com.tuotiansudai.utils.CaptchaVerifier;
import com.tuotiansudai.utils.LoginFailedTimesVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private CaptchaVerifier captchaVerifier;
    @Autowired
    private LoginFailedTimesVerifier loginFailedTimesVerifier;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        super.additionalAuthenticationChecks(userDetails, authentication);

        String captcha = httpServletRequest.getParameter("captcha");
        String loginName = httpServletRequest.getParameter("username");

        boolean result = this.captchaVerifier.loginCaptchaVerify(captcha);

        if (!result) {
            logger.debug("Authentication failed: captcha does not match stored value");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        boolean resultLoginFailedTimes = loginFailedTimesVerifier.loginFailedTimesVerifier(loginName);
        if (!resultLoginFailedTimes) {
            logger.debug("Authentication failed: failed times is reach the ceiling");
            throw new LoginFailedMaxTimesException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials","Bad credentials"));
        }
    }

    public void setCaptchaVerifier(CaptchaVerifier captchaVerifier) {
        this.captchaVerifier = captchaVerifier;
    }

    public void setLoginFailedTimesVerifier(LoginFailedTimesVerifier loginFailedTimesVerifier) {
        this.loginFailedTimesVerifier = loginFailedTimesVerifier;
    }
}
