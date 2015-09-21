package com.tuotiansudai.utils;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.tuotiansudai.client.RedisWrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Component
public class CaptchaVerifier {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    public boolean loginCaptchaVerify(String captcha) {
        String jSessionId = this.getJSessionId(httpServletRequest);
        if (Strings.isNullOrEmpty(captcha) || Strings.isNullOrEmpty(jSessionId)) {
            return false;
        }

        Object existingCaptcha = httpServletRequest.getSession().getAttribute(jSessionId);
        return existingCaptcha != null && captcha.equalsIgnoreCase((String) existingCaptcha);
    }

    public boolean registerImageCaptchaVerify(String imageCaptcha) {
        String jSessionId = this.getJSessionId(httpServletRequest);
        return redisWrapperClient.exists(jSessionId) && redisWrapperClient.get(jSessionId).equalsIgnoreCase(imageCaptcha);
    }

    public boolean mobileRetrievePasswordImageCaptchaVerify(String imageCaptcha) {
        String jSessionId = this.getJSessionId(httpServletRequest);
        return redisWrapperClient.exists(jSessionId) && redisWrapperClient.get(jSessionId).equalsIgnoreCase(imageCaptcha);
    }

    private String getJSessionId(HttpServletRequest request) {
        List<Cookie> cookies = Arrays.asList(request.getCookies());
        Optional<Cookie> cookieOptional = Iterators.tryFind(cookies.iterator(), new Predicate<Cookie>() {
            @Override
            public boolean apply(Cookie cookie) {
                return cookie.getName().equalsIgnoreCase("jsessionid");
            }
        });

        if (cookieOptional.isPresent()) {
            return cookieOptional.get().getValue();
        }

        return null;
    }
}
