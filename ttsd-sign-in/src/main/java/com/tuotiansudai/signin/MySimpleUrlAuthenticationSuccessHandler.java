package com.tuotiansudai.signin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.UserRoleService;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

public class MySimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static Logger logger = Logger.getLogger(MySimpleUrlAuthenticationSuccessHandler.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private UserMapper userMapper;

    @Value("${web.login.max.failed.times}")
    private int times;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String loginName = userMapper.findByLoginNameOrMobile(request.getParameter("username").trim()).getLoginName();
        String strSource = request.getParameter("source");
        Source source = (StringUtils.isEmpty(strSource)) ? Source.MOBILE : Source.valueOf(strSource.toUpperCase());
        loginLogService.generateLoginLog(loginName, source, RequestIPParser.parse(request), request.getParameter("deviceId"), true);

        LoginDto loginDto = new LoginDto();
        loginDto.setStatus(true);
        loginDto.setNewSessionId(request.getSession().getId());

        clearFailHistory(loginName);
        String jsonBody = objectMapper.writeValueAsString(loginDto);
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
        clearAuthenticationAttributes(request);
    }

    private void clearFailHistory(String username) {
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", username);
        redisWrapperClient.del(redisKey);
    }

}
