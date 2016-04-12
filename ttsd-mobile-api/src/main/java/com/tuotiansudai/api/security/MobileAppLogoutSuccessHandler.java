package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MobileAppLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MobileAppTokenProvider mobileAppTokenProvider;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getParameter("token");
        if (!Strings.isNullOrEmpty(token)) {
            mobileAppTokenProvider.deleteToken(token);
        }

        BaseResponseDto dto = new BaseResponseDto();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        String jsonBody = objectMapper.writeValueAsString(dto);

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonBody);
    }
}
