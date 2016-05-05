package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.util.CommonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

public class MobileAppAuthenticationEntryPoint implements AuthenticationEntryPoint {
    static Logger log = Logger.getLogger(MobileAppAuthenticationEntryPoint.class);
    @Autowired
    private MobileAppTokenProvider mobileAppTokenProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(request);

        log.debug(MessageFormat.format("[Authentication Entry Point] uri: {0} body: {1}",request.getRequestURI(),bufferedRequest.getInputStreamString()));

        BaseResponseDto dto = mobileAppTokenProvider.generateResponseDto(ReturnMessage.UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter out = response.getWriter();
        String jsonString = objectMapper.writeValueAsString(dto);
        out.print(jsonString);
    }
}
