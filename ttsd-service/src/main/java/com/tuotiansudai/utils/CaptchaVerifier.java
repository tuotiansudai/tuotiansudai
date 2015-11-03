package com.tuotiansudai.utils;

import com.tuotiansudai.client.RedisWrapperClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class CaptchaVerifier {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    public boolean loginCaptchaVerify(String captcha) {
        Object existingCaptcha = httpServletRequest.getSession().getAttribute("loginCaptcha");
        return existingCaptcha != null && captcha.equalsIgnoreCase((String) existingCaptcha);
    }

    public boolean registerImageCaptchaVerify(String imageCaptcha) {
        String sessionId = httpServletRequest.getSession().getId();
        return redisWrapperClient.exists(sessionId) && redisWrapperClient.get(sessionId).equalsIgnoreCase(imageCaptcha);
    }

    public boolean mobileRetrievePasswordImageCaptchaVerify(String imageCaptcha) {
        String sessionId = httpServletRequest.getSession().getId();
        return redisWrapperClient.exists(sessionId) && redisWrapperClient.get(sessionId).equalsIgnoreCase(imageCaptcha);
    }
}
