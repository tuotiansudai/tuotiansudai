package com.tuotiansudai.spring.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.spring.MyUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MySimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MyUser myUser = (MyUser) authentication.getPrincipal();
        LoginDto loginDto = new LoginDto(myUser);

        String jsonBody = objectMapper.writeValueAsString(loginDto);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.print(jsonBody);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

        clearAuthenticationAttributes(request);
    }
}
