package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.client.SignInClient;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.repository.model.Source;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyMobileSimpleUrlLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private static Logger logger = Logger.getLogger(MyMobileSimpleUrlLogoutSuccessHandler.class);

    private final SignInClient signInClient = SignInClient.getInstance();

    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());

        String jsonBody = objectMapper.writeValueAsString(baseResponseDto);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try(PrintWriter writer = response.getWriter()) {
            writer.print(jsonBody);
        }

        super.onLogoutSuccess(request, response, authentication);

        String token = request.getParameter("token");
        SignInResult loginOutResult = signInClient.logout(token);
        if (null == loginOutResult) {
            return;
        }

        signInClient.verifyToken(token, Source.MOBILE);
    }
}
