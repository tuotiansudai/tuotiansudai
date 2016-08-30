package com.tuotiansudai.signin;

import com.tuotiansudai.exception.CaptchaNotMatchException;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.spring.security.CaptchaHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider {

    static Logger logger = Logger.getLogger(MyDaoAuthenticationProvider.class);

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private CaptchaHelper captchaHelper;

    @Autowired
    private UserMapper userMapper;

    private boolean enableCaptchaVerify = true;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        super.additionalAuthenticationChecks(userDetails, authentication);
        String loginName = userDetails.getUsername();
        UserModel userModel = userMapper.findByLoginName(loginName);
        boolean enabled = userModel.getStatus() == UserStatus.ACTIVE;
        if (!enabled) {
            String errorMessage = MessageFormat.format("Login Error: {0} is locked!", loginName);
            throw new DisabledException(errorMessage);
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (enableCaptchaVerify) {
            String captcha = httpServletRequest.getParameter("captcha");
            String deviceId = StringUtils.isEmpty(httpServletRequest.getParameter("j_deviceId")) ? httpServletRequest.getParameter("deviceId") : httpServletRequest.getParameter("j_deviceId");
            if (StringUtils.isNotEmpty(captcha)) {
                boolean result;
                if (StringUtils.isNotEmpty(deviceId)) {
                    result = this.captchaHelper.captchaVerify(captcha, deviceId, null);
                } else {
                    result = this.captchaHelper.captchaVerify(captcha, deviceId, null);
                }
                if (!result) {
                    logger.debug("Authentication failed: captcha does not match actual value");
                    throw new CaptchaNotMatchException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.captchaNotMatch", "Captcha Not Match"));
                }
            }
        }
        return super.authenticate(authentication);
    }

    public void setEnableCaptchaVerify(boolean enableCaptchaVerify) {
        this.enableCaptchaVerify = enableCaptchaVerify;
    }
}
