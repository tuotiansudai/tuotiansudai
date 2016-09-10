package com.tuotiansudai.activity.interceptors;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import com.tuotiansudai.spring.security.SignInClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppTokenInterceptor extends HandlerInterceptorAdapter {

    static Logger logger = Logger.getLogger(AppTokenInterceptor.class);

    private final static String APP_SOURCE_FLAG = "app";

    @Autowired
    private MyAuthenticationUtil myAuthenticationUtil;

    @Autowired
    private SignInClient signInClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String source = request.getParameter("source");
        if (APP_SOURCE_FLAG.equalsIgnoreCase(source)) {
            String token = request.getHeader("token");
            SignInResult signInResult = signInClient.verifyToken(token);
            if (signInResult != null && signInResult.isResult()) {
                request.getSession();
                myAuthenticationUtil.createAuthentication(signInResult.getUserInfo().getLoginName(), Source.WEB);
                return true;
            }
            myAuthenticationUtil.removeAuthentication();
        }
        return true;
    }
}
