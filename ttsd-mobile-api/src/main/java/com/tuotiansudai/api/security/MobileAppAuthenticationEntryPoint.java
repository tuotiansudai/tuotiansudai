package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        if (!StringUtils.isEmpty(bufferedRequest.getInputStreamString())) {
            BaseParamDto baseParamDto;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                baseParamDto = objectMapper.readValue(request.getInputStream(), BaseParamDto.class);
            } catch (Exception e) {
                baseParamDto = null;
                log.debug(e);
            }
            if (null != baseParamDto && null != baseParamDto.getBaseParam() && !StringUtils.isEmpty(baseParamDto.getBaseParam().getToken())) {
                String requestToken = baseParamDto.getBaseParam().getToken();
                String redisLoginName = mobileAppTokenProvider.getUserNameByToken(requestToken);
                log.debug(MessageFormat.format("[Authentication Entry Point] uri: {0} body: {1} searchToken: {2} redisTokenLoginName:{3}", request.getRequestURI(), bufferedRequest.getInputStreamString(), requestToken, redisLoginName));
            } else {
                log.debug(MessageFormat.format("[Authentication Entry Point] uri: {0} body: {1}", request.getRequestURI(), bufferedRequest.getInputStreamString()));
            }
        }

        BaseResponseDto dto = mobileAppTokenProvider.generateResponseDto(ReturnMessage.UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter out = response.getWriter();
        String jsonString = objectMapper.writeValueAsString(dto);
        out.print(jsonString);
    }
}