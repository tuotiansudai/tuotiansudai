package com.tuotiansudai.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

public class MySuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler{

    private static String SPRING_SESSION_TEMPLATE = "spring:session:sessions:{0}";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String session = "";
            for (Cookie cookie : cookies) {
                if ("SESSION".equalsIgnoreCase(cookie.getName())) {
                    session = cookie.getValue();
                }
            }
            String redisSessionKey = MessageFormat.format(SPRING_SESSION_TEMPLATE, session);
            if (redisWrapperClient.exists(redisSessionKey)) {
                redisWrapperClient.del(redisSessionKey);
            }
        }
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        BaseDto<LoginDto> baseDto = new BaseDto<>();
        LoginDto loginDto = new LoginDto();
        loginDto.setStatus(true);
        baseDto.setData(loginDto);
        String jsonBody = objectMapper.writeValueAsString(baseDto);
        writer.print(jsonBody);
        writer.close();
        return;
    }

}
