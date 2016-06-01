package com.tuotiansudai.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.exception.CaptchaNotMatchException;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.LoginLogService;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

public class MySimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private UserMapper userMapper;

    @Value("${web.login.lock.seconds}")
    private int second;

    @Value("${web.login.max.failed.times}")
    private int loginMaxTimes;

    // 授权失败后处理
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        loginLogService.generateLoginLog(request.getParameter("username"), Source.WEB, RequestIPParser.parse(request), null, false);

        BaseDto<LoginDto> baseDto = new BaseDto<>();
        LoginDto loginDto = new LoginDto();
        baseDto.setData(loginDto);
        this.updateUserStatus(request.getParameter("username"));
        loginDto.setLocked(exception instanceof DisabledException);
        loginDto.setCaptchaNotMatch(exception instanceof CaptchaNotMatchException);

        String jsonBody = objectMapper.writeValueAsString(baseDto);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.print(jsonBody);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    private void updateUserStatus(String loginName) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginName);
        if (userModel == null) {
            return;
        }
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", userModel.getLoginName());
        if (redisWrapperClient.exists(redisKey)) {
            int loginFailedTime = Integer.parseInt(redisWrapperClient.get(redisKey)) + 1;

            if (loginFailedTime < loginMaxTimes) {
                redisWrapperClient.set(redisKey, String.valueOf(loginFailedTime));
            }

            if (loginFailedTime >= loginMaxTimes) {
                redisWrapperClient.setex(redisKey, second, String.valueOf(loginMaxTimes));
                userModel.setStatus(UserStatus.INACTIVE);
                userMapper.updateUser(userModel);
            }
        } else {
            redisWrapperClient.set(redisKey, "1");
        }
    }
}
