package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.LoginLogService;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.commons.lang.StringUtils;
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

    @Autowired
    private LoginLogService loginLogService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            addLoginLog(request);
            String username = request.getParameter("j_username");
            clearFailHistory(username);

            String token = mobileAppTokenProvider.refreshToken(authentication.getName(), null);
            BaseResponseDto dto = mobileAppTokenProvider.generateResponseDto(token);
            String jsonBody = objectMapper.writeValueAsString(dto);

            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(jsonBody);
            clearAuthenticationAttributes(request);
    }

    private void addLoginLog(HttpServletRequest request){
        String username = request.getParameter("j_username");
        String strSource = request.getParameter("j_source");
        Source source = (StringUtils.isEmpty(strSource))?Source.MOBILE:Source.valueOf(strSource.toUpperCase());
        String deviceId = request.getParameter("j_deviceId");
        loginLogService.generateLoginLog(username, source, RequestIPParser.parse(request), deviceId, true);
    }

    private void clearFailHistory(String username){
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", username);
        redisWrapperClient.del(redisKey);
    }
}
