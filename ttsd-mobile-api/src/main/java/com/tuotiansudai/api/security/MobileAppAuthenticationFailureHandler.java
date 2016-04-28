package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.SWITCH;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.LoginResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.exception.CaptchaNotMatchException;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.LoginLogService;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.commons.lang3.StringUtils;
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
    private LoginLogService loginLogService;

    @Autowired
    private UserMapper userMapper;

    @Value("${web.login.lock.seconds}")
    private int second;

    @Value("${web.login.max.failed.times}")
    private int times;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        addLoginLog(request);
        String username = request.getParameter("j_username");
        logUserLoginFail(username);
        ReturnMessage errorMsg = ReturnMessage.LOGIN_FAILED;
        if(exception instanceof DisabledException){
            errorMsg = ReturnMessage.USER_IS_DISABLED;
        }else if(exception instanceof CaptchaNotMatchException){
            errorMsg = ReturnMessage.IMAGE_CAPTCHA_IS_WRONG;
        }
        BaseResponseDto dto = mobileAppTokenProvider.generateResponseDto(errorMsg);
        String jsonBody = objectMapper.writeValueAsString(dto);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonBody);
    }

    private void addLoginLog(HttpServletRequest request){
        String username = request.getParameter("j_username");
        String strSource = request.getParameter("j_source");
        Source source = (StringUtils.isEmpty(strSource))?Source.MOBILE:Source.valueOf(strSource.toUpperCase());
        String deviceId = request.getParameter("j_deviceId");
        loginLogService.generateLoginLog(username, source, RequestIPParser.parse(request), deviceId, false);
    }

    private void logUserLoginFail(String loginName) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginName);
        if (userModel == null) {
            return;
        }
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", userModel.getLoginName());
        if (!redisWrapperClient.exists(redisKey)) {
            redisWrapperClient.set(redisKey, "1");
        } else {
            if (Integer.parseInt(redisWrapperClient.get(redisKey)) < times - 1) {
                redisWrapperClient.set(redisKey, String.valueOf(Integer.parseInt(redisWrapperClient.get(redisKey)) + 1));
            } else if (Integer.parseInt(redisWrapperClient.get(redisKey)) == times - 1) {
                redisWrapperClient.setex(redisKey, second, String.valueOf(times));
                userModel.setStatus(UserStatus.INACTIVE);
                userMapper.updateUser(userModel);
            }
        }
    }
}
