package com.tuotiansudai.util;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class CaptchaHelper {

    public final static String LOGIN_CAPTCHA = "LOGIN_CAPTCHA";

    public final static String REGISTER_CAPTCHA = "REGISTER_CAPTCHA";

    public final static String RETRIEVE_PASSWORD_CAPTCHA = "RETRIEVE_PASSWORD_CAPTCHA";

    @Autowired
    private HttpServletRequest httpServletRequest;

    public void storeCaptcha(String attributeKey, String captcha) {
        httpServletRequest.getSession().setAttribute(attributeKey, captcha);
    }

    public boolean captchaVerify(String attributeKey, String captcha) {
        String actualCaptcha = (String) httpServletRequest.getSession().getAttribute(attributeKey);
        httpServletRequest.getSession().removeAttribute(attributeKey);
        return !Strings.isNullOrEmpty(captcha) && captcha.trim().equalsIgnoreCase(actualCaptcha);
    }
}
