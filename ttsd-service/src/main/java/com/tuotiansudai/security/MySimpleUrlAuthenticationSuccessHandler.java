package com.tuotiansudai.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.repository.mapper.LoginLogMapper;
import com.tuotiansudai.repository.model.LoginLogModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.utils.RequestIPParser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

public class MySimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static String LOGIN_LOG_TABLE_TEMPLATE = "login_log_{0}{1}";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Value("${login.max.times}")
    private int times;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        this.logSuccessLogin(request);

        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", request.getParameter("username"));
        boolean isAjaxRequest = this.isAjaxRequest(request);
        if (isAjaxRequest) {
            BaseDto<LoginDto> baseDto = new BaseDto<>();
            LoginDto loginDto = new LoginDto();
            loginDto.setStatus(true);
            baseDto.setData(loginDto);
            redisWrapperClient.del(redisKey);
            String jsonBody = objectMapper.writeValueAsString(baseDto);
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.print(jsonBody);
            writer.close();
            clearAuthenticationAttributes(request);
            return;
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    @Transactional
    private void logSuccessLogin(HttpServletRequest request) {
        LoginLogModel model = new LoginLogModel(request.getParameter("username"), Source.WEB, RequestIPParser.parse(request), null, true);
        DateTime now = new DateTime();
        loginLogMapper.create(model, MessageFormat.format(LOGIN_LOG_TABLE_TEMPLATE, String.valueOf(now.getYear()), String.valueOf(now.getMonthOfYear())));
    }
}
