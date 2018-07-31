package com.tuotiansudai.activity.interceptors;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.SignInClient;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class AppTokenInterceptor extends HandlerInterceptorAdapter {

    private final static Logger logger = Logger.getLogger(AppTokenInterceptor.class);

    private static Map<String, Source> USER_AGENT = Maps.newHashMap(ImmutableMap.<String, Source>builder().put("iphone", Source.IOS).put("android", Source.ANDROID).build());

    private final static String APP_SOURCE_FLAG = "app";

    private final MyAuthenticationUtil myAuthenticationUtil = MyAuthenticationUtil.getInstance();

    private final SignInClient signInClient = SignInClient.getInstance();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!APP_SOURCE_FLAG.equalsIgnoreCase(request.getParameter("source"))) {
            return true;
        }

        Source userAgentSource = USER_AGENT.getOrDefault(request.getHeader("User-Agent"), Source.WEB);

        String token = request.getHeader("token");
        token = Strings.isNullOrEmpty(token) ? request.getParameter("token") : token;
        SignInResult signInResult = signInClient.verifyToken(token, userAgentSource);
        if (signInResult != null && signInResult.isResult()) {
            myAuthenticationUtil.createAuthentication(signInResult.getUserInfo().getLoginName(), Source.WEB, request.getHeader("X-Forwarded-For"));
        } else {
            myAuthenticationUtil.removeAuthentication();
        }

        return true;
    }
}
