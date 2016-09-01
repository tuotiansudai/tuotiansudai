package com.tuotiansudai.signin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.dto.LoginDto;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class MySimpleUrlLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    static Logger logger = Logger.getLogger(MySimpleUrlLogoutSuccessHandler.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter writer = response.getWriter();
        LoginDto baseDto = new LoginDto();
        LoginDto loginDto = new LoginDto();
        loginDto.setStatus(true);
        String jsonBody = objectMapper.writeValueAsString(baseDto);
        writer.print(jsonBody);
        writer.close();
    }
}
