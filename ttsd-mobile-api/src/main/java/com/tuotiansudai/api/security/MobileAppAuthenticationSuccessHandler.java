package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.LoginResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.client.RedisWrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

public class MobileAppAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private MobileAppTokenProvider mobileAppTokenProvider;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(authentication.isAuthenticated()){

            String username = request.getParameter("j_username");
            clearFailHistory(username);

            String token = mobileAppTokenProvider.generateToken(authentication.getName());
            BaseResponseDto dto = mobileAppTokenProvider.generateResponseDto(token);
            String jsonBody = objectMapper.writeValueAsString(dto);

            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(jsonBody);
            clearAuthenticationAttributes(request);
        }
    }

    private void clearFailHistory(String username){
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", username);
        redisWrapperClient.del(redisKey);
    }
}
