package com.tuotiansudai.web.util;

import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.security.MyAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Component
public class AppTokenParser {

    @Autowired
    private MyAuthenticationManager myAuthenticationManager;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    public String getLoginName(HttpServletRequest httpServletRequest) {
        if (RequestMethod.GET.name().equalsIgnoreCase(httpServletRequest.getMethod())) {
            return LoginUserInfo.getLoginName();
        }

        String token = httpServletRequest.getParameter("token");
        String loginName = redisWrapperClient.get(token);

        if (Strings.isNullOrEmpty(loginName)) {
            myAuthenticationManager.removeAuthentication();
        } else {
            myAuthenticationManager.createAuthentication(loginName);
        }

        return loginName;
    }
}
