package com.tuotiansudai.spring.security;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.tuotiansudai.spring.MyUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

@Component
public class MySimpleUrlLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    static Logger logger = Logger.getLogger(MySimpleUrlLogoutSuccessHandler.class);

    private OkHttpClient okHttpClient = new OkHttpClient();

    @Value("${signIn.host}")
    private String signInHost;

    @Value("${signIn.port}")
    private String signInPort;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MyUser myUser = (MyUser) authentication.getPrincipal();

        Request.Builder logoutRequest = new Request.Builder()
                .url(MessageFormat.format("http://{0}:{1}/logout/{2}", signInHost, signInPort, myUser.getToken()))
                .post(RequestBody.create(null, new byte[0]));
        okHttpClient.newCall(logoutRequest.build()).execute();

        setDefaultTargetUrl("/");
        super.onLogoutSuccess(request, response, authentication);
    }
}
