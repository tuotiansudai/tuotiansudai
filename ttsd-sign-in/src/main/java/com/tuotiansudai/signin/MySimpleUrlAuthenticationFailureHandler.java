package com.tuotiansudai.signin;

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
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

public class MySimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    static Logger logger = Logger.getLogger(MySimpleUrlAuthenticationFailureHandler.class);

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
        String strSource = request.getParameter("source");
        Source source = (StringUtils.isEmpty(strSource)) ? Source.MOBILE : Source.valueOf(strSource.toUpperCase());
        loginLogService.generateLoginLog(request.getParameter("username"), source, RequestIPParser.parse(request), request.getParameter("deviceId"), false);

        BaseDto<LoginDto> baseDto = new BaseDto<>();
        LoginDto loginDto = new LoginDto();
        baseDto.setData(loginDto);
        loginDto.setLocked(exception instanceof DisabledException);
        loginDto.setCaptchaNotMatch(exception instanceof CaptchaNotMatchException);

        if(exception instanceof BadCredentialsException){
            this.updateUserStatus(request.getParameter("username"));
        }

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
                Long leftSeconds = redisWrapperClient.ttl(redisKey);
                if(leftSeconds <= 0){
                    redisWrapperClient.setex(redisKey, second, String.valueOf(loginMaxTimes));
                    userModel.setStatus(UserStatus.INACTIVE);
                    userMapper.updateUser(userModel);
                }
            }
        } else {
            redisWrapperClient.set(redisKey, "1");
        }
    }

}
