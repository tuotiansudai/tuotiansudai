package com.tuotiansudai.ask.utils;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class CaptchaHelper {

    static Logger logger = Logger.getLogger(CaptchaHelper.class);

    public final static String ASK_CAPTCHA = "ASK_CAPTCHA";

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
