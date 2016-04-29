package com.tuotiansudai.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.tuotiansudai.dto.AjaxAccessDeniedDto;
import com.tuotiansudai.dto.BaseDto;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;


public class MyLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    static Logger logger = Logger.getLogger(MyLoginUrlAuthenticationEntryPoint.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public MyLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);

    }

    // 匿名用户访问受限资源时，跳转到授权
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // ajax 请求访问受限资源时
        if (isAjaxRequest(request)) {
            this.generateAjaxAccessDeniedResponse(request, response);
            return;
        }

        // 非ajax 请求访问受限资源时
        super.commence(request, response, authException);
    }

    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        String requestURI = request.getRequestURI();
        String loginFormUrl = super.determineUrlToUseForThisRequest(request, response, exception);
        return MessageFormat.format("{0}?redirect={1}", loginFormUrl, requestURI);
    }

    private void generateAjaxAccessDeniedResponse(HttpServletRequest request, HttpServletResponse response) {
        BaseDto<AjaxAccessDeniedDto> baseDto = new BaseDto<>();
        AjaxAccessDeniedDto dataDto = new AjaxAccessDeniedDto();
        baseDto.setData(dataDto);

        String referer = request.getHeader(HttpHeaders.REFERER);

        if (!Strings.isNullOrEmpty(referer)) {
            dataDto.setRefererUrl(referer);
        }
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.print(objectMapper.writeValueAsString(baseDto));
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }
}