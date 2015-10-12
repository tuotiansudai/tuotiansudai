package com.tuotiansudai.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

@Component
public class MySimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Value("${login.max.times}")
    private int times;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", request.getParameter("username"));
        boolean isAjaxRequest = this.isAjaxRequest(request);
        if (isAjaxRequest) {
            BaseDto<BaseDataDto> baseDto = new BaseDto<>();
            BaseDataDto dataDto = new BaseDataDto();
            if (!redisWrapperClient.exists(redisKey) || Integer.parseInt(redisWrapperClient.get(redisKey)) < times) {
                dataDto.setStatus(true);
                redisWrapperClient.del(redisKey);
            } else {
                dataDto.setStatus(false);
            }
            baseDto.setData(dataDto);
            String jsonBody = objectMapper.writeValueAsString(baseDto);
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(jsonBody);
            clearAuthenticationAttributes(request);
            return;
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }


}
