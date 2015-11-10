package com.tuotiansudai.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.repository.mapper.LoginLogMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.LoginLogModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.utils.RequestIPParser;
import org.joda.time.DateTime;
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

    private static String LOGIN_LOG_TABLE_TEMPLATE = "login_log_{0}{1}";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Value("${login.unlock.second}")
    private int second;

    @Value("${login.max.times}")
    private int loginMaxTimes;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        this.logFailedLogin(request);

        if (this.isAjaxRequest(request)) {
            BaseDto<LoginDto> baseDto = new BaseDto<>();
            LoginDto loginDto = new LoginDto();
            baseDto.setData(loginDto);
            this.updateUserStatus(request.getParameter("username"));
            loginDto.setLocked(exception instanceof DisabledException);

            String jsonBody = objectMapper.writeValueAsString(baseDto);
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(jsonBody);
            out.close();
            return;
        }

        super.onAuthenticationFailure(request, response, exception);
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    @Transactional
    private void updateUserStatus(String loginName) {
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", loginName);
        if (redisWrapperClient.exists(redisKey)) {
            int loginFailedTime = Integer.parseInt(redisWrapperClient.get(redisKey)) + 1;

            if (loginFailedTime < loginMaxTimes) {
                redisWrapperClient.set(redisKey, String.valueOf(loginFailedTime));
            }

            if (loginFailedTime >= loginMaxTimes) {
                redisWrapperClient.setex(redisKey, second, String.valueOf(loginMaxTimes));
                UserModel userModel = userMapper.findByLoginName(loginName);
                userModel.setStatus(UserStatus.INACTIVE);
                userMapper.updateUser(userModel);
            }
        } else {
            redisWrapperClient.set(redisKey, String.valueOf(1));
        }
    }

    @Transactional
    private void logFailedLogin(HttpServletRequest request) {
        LoginLogModel model = new LoginLogModel(request.getParameter("username"), Source.WEB, RequestIPParser.parse(request), null, false);
        DateTime now = new DateTime();
        loginLogMapper.create(model, MessageFormat.format(LOGIN_LOG_TABLE_TEMPLATE, String.valueOf(now.getYear()), String.valueOf(now.getMonthOfYear())));
    }
}
