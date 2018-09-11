package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.LoginResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.client.SignInClient;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MobileAppAuthenticationTokenProcessingFilter extends GenericFilterBean {

    private final static String refreshTokenUrl = "/refresh-token";

    private final SignInClient signInClient = SignInClient.getInstance();

    private final MyAuthenticationUtil myAuthenticationUtil = MyAuthenticationUtil.getInstance();

    private ObjectMapper objectMapper = new ObjectMapper();

    public MobileAppAuthenticationTokenProcessingFilter() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        BufferedRequestWrapper bufferedRequestWrapper = new BufferedRequestWrapper((HttpServletRequest) request);

        BaseParamDto baseParamDto = this.getBaseParamDto(bufferedRequestWrapper);

        String token = baseParamDto != null && baseParamDto.getBaseParam() != null ? baseParamDto.getBaseParam().getToken() : null;
        token = Strings.isNullOrEmpty(token) ? bufferedRequestWrapper.getHeader("token") : token;

        Source source = baseParamDto != null && baseParamDto.getBaseParam() != null && !Strings.isNullOrEmpty(baseParamDto.getBaseParam().getPlatform())
                ? Source.valueOf(baseParamDto.getBaseParam().getPlatform().toUpperCase()) : null;

        SignInResult verifyTokenResult = signInClient.verifyToken(token, source);

        if (verifyTokenResult != null && verifyTokenResult.isResult()) {
            myAuthenticationUtil.refreshGrantedAuthority(verifyTokenResult);

            if (bufferedRequestWrapper.getRequestURI().equalsIgnoreCase(refreshTokenUrl)) {
                SignInResult refreshResult = signInClient.refreshToken(token, source);
                String newToken = refreshResult != null && refreshResult.isResult() ? refreshResult.getToken() : null;

                BaseResponseDto<LoginResponseDataDto> dto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
                LoginResponseDataDto loginResponseDataDto = new LoginResponseDataDto();
                loginResponseDataDto.setToken(newToken);
                dto.setData(loginResponseDataDto);

                response.setContentType("application/json; charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                writer.print(objectMapper.writeValueAsString(dto));
                return;
            }
        }
        chain.doFilter(bufferedRequestWrapper, response);
    }

    private BaseParamDto getBaseParamDto(BufferedRequestWrapper bufferedRequestWrapper) {
        try {
            return objectMapper.readValue(bufferedRequestWrapper.getInputStreamString(), BaseParamDto.class);
        } catch (IOException ignored) {
        }
        return null;
    }
}