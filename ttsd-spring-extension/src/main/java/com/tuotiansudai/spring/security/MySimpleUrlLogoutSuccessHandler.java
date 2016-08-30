package com.tuotiansudai.spring.security;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.tuotiansudai.spring.MyUser;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

public class MySimpleUrlLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    static Logger logger = Logger.getLogger(MySimpleUrlLogoutSuccessHandler.class);

    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MyUser myUser = (MyUser) authentication.getPrincipal();

        Request.Builder logoutRequest = new Request.Builder()
                .url(MessageFormat.format("http://localhost:5000/logout/{0}", myUser.getToken()))
                .post(RequestBody.create(null, new byte[0]));
        okHttpClient.newCall(logoutRequest.build()).execute();

        setDefaultTargetUrl("/");
        super.onLogoutSuccess(request, response, authentication);
    }
}
