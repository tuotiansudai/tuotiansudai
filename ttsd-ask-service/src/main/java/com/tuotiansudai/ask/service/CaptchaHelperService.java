package com.tuotiansudai.ask.service;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

@Component
public class CaptchaHelperService {

    static Logger logger = Logger.getLogger(CaptchaHelperService.class);

    private final static String ASK_CAPTCHA = "ASK_CAPTCHA";

    @Autowired
    private HttpServletRequest httpServletRequest;

    public void storeCaptcha(String captcha) {
        httpServletRequest.getSession().setAttribute(ASK_CAPTCHA, captcha);
    }

    public boolean captchaVerify(String captcha) {
        String actualCaptcha = (String) httpServletRequest.getSession().getAttribute(ASK_CAPTCHA);
        httpServletRequest.getSession().removeAttribute(ASK_CAPTCHA);
        logger.info(MessageFormat.format("ask captcha verify, captcha:{0}, actualCaptcha:{1}", captcha, actualCaptcha));
        return !Strings.isNullOrEmpty(captcha) && captcha.trim().equalsIgnoreCase(actualCaptcha);
    }
}
