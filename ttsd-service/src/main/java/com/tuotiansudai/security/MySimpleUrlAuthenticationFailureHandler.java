package com.tuotiansudai.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

@Component
public class MySimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Value("${login.unlock.second}")
    private int second;

    @Value("${login.max.times}")
    private int times;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes",request.getParameter("username"));
        if (this.isAjaxRequest(request)) {
            BaseDto<BaseDataDto> baseDto = new BaseDto<>();
            BaseDataDto dataDto = new BaseDataDto();
            dataDto.setStatus(false);
            if (redisWrapperClient.exists(redisKey) && Integer.parseInt(redisWrapperClient.get(redisKey)) < times-1) {
                redisWrapperClient.set(redisKey, String.valueOf(Integer.parseInt(redisWrapperClient.get(redisKey))+1));
            } else if (redisWrapperClient.exists(redisKey) && Integer.parseInt(redisWrapperClient.get(redisKey)) == times-1){
                redisWrapperClient.setex(redisKey, second, String.valueOf(times));
            } else if (!redisWrapperClient.exists(redisKey)){
                redisWrapperClient.set(redisKey, "1");
            }
            baseDto.setData(dataDto);
            String jsonBody = objectMapper.writeValueAsString(baseDto);
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(jsonBody);
            return;
        }

        super.onAuthenticationFailure(request, response, exception);
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }
}
