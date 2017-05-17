package com.tuotiansudai.spring.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.spring.MyUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static Logger logger = Logger.getLogger(MySimpleUrlAuthenticationSuccessHandler.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MQWrapperClient mqWrapperClient;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MyUser myUser = (MyUser) authentication.getPrincipal();
        LoginDto loginDto = new LoginDto(myUser);

        String jsonBody = objectMapper.writeValueAsString(loginDto);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.print(jsonBody);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

        if (request.getHeader("Referer").indexOf("/dragon/toLogin") > 0) {
            logger.info(MessageFormat.format("[Dragon Boat] {} invite an old user {}.", request.getParameter("referrer"), loginDto.getMobile()));
            String referrer = request.getParameter("referrer");

            logger.info(MessageFormat.format("[Dragon Boat] send share login transfer message, referrer:{}, loginName:{}.", referrer, loginDto.getLoginName()));
            mqWrapperClient.sendMessage(MessageQueue.DragonBoatShareLoginTransfer, referrer + ":" + loginDto.getLoginName());
        }

        clearAuthenticationAttributes(request);
    }
}
