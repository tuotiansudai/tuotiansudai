package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.LoginResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

public class MobileAppAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MobileAppTokenProvider mobileAppTokenProvider;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private UserMapper userMapper;

    @Value("${web.login.lock.seconds}")
    private int second;

    @Value("${web.login.max.failed.times}")
    private int times;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("j_username");
        logUserLoginFail(username);

        ReturnMessage errorMsg = (exception instanceof DisabledException) ?
                ReturnMessage.USER_IS_DISABLED : ReturnMessage.UNAUTHORIZED;
        BaseResponseDto dto = mobileAppTokenProvider.generateResponseDto(errorMsg);
        String jsonBody = objectMapper.writeValueAsString(dto);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonBody);
    }

    private void logUserLoginFail(String username) {
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", username);
        if (!redisWrapperClient.exists(redisKey)) {
            redisWrapperClient.set(redisKey, "1");
        } else {
            if (Integer.parseInt(redisWrapperClient.get(redisKey)) < times - 1) {
                redisWrapperClient.set(redisKey, String.valueOf(Integer.parseInt(redisWrapperClient.get(redisKey)) + 1));
            } else if (Integer.parseInt(redisWrapperClient.get(redisKey)) == times - 1) {
                redisWrapperClient.setex(redisKey, second, String.valueOf(times));
                UserModel userModel = userMapper.findByLoginName(username);
                if (userModel != null) {
                    userModel.setStatus(UserStatus.INACTIVE);
                    userMapper.updateUser(userModel);
                }
            }
        }
    }
}
