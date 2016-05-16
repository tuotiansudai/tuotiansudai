package com.tuotiansudai.signin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MySuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler{

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
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
