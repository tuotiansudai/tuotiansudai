package com.tuotiansudai.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.LoginLogService;
import com.tuotiansudai.service.UserRoleService;
import com.tuotiansudai.util.RequestIPParser;
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

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserMapper userMapper;

    @Value("${web.login.max.failed.times}")
    private int times;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String loginName =  userMapper.findByLoginNameOrMobile(request.getParameter("username")).getLoginName();
        loginLogService.generateLoginLog(loginName, Source.WEB, RequestIPParser.parse(request), null, true);

        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", loginName);
        BaseDto<LoginDto> baseDto = new BaseDto<>();
        LoginDto loginDto = new LoginDto();
        loginDto.setStatus(true);
        loginDto.setRoles(userRoleService.findRoleNameByLoginName(loginName));
        loginDto.setNewSessionId(request.getSession().getId());
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

}
