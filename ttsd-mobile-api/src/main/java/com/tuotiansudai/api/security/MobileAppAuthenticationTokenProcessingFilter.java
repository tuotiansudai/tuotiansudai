package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.LoginResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.MyUser;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.spring.security.SignInClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class MobileAppAuthenticationTokenProcessingFilter extends GenericFilterBean {

    private String refreshTokenUrl = "/refresh-token";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SignInClient signInClient;

    public MobileAppAuthenticationTokenProcessingFilter() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String token = getToken(httpServletRequest);

        SignInResult verifyTokenResult = signInClient.verifyToken(token);

        if (verifyTokenResult != null && verifyTokenResult.isResult()) {
            List<GrantedAuthority> grantedAuthorities = Lists.transform(verifyTokenResult.getUserInfo().getRoles(), new Function<String, GrantedAuthority>() {
                @Override
                public GrantedAuthority apply(String role) {
                    return new SimpleGrantedAuthority(role);
                }
            });

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    new MyUser(verifyTokenResult.getToken(), verifyTokenResult.getUserInfo().getLoginName(), "", true, true, true, true, grantedAuthorities, verifyTokenResult.getUserInfo().getMobile()),
                    "",
                    grantedAuthorities);
            authenticationToken.setDetails(authenticationToken.getDetails());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            if (httpServletRequest.getRequestURI().equalsIgnoreCase(refreshTokenUrl)) {
                SignInResult refreshResult = signInClient.refresh(token, getSource(httpServletRequest));
                String newToken = refreshResult != null && refreshResult.isResult() ? refreshResult.getToken() : null;

                BaseResponseDto<LoginResponseDataDto> dto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
                LoginResponseDataDto loginResponseDataDto = new LoginResponseDataDto();
                loginResponseDataDto.setToken(newToken);
                dto.setData(loginResponseDataDto);

                PrintWriter writer = null;
                try {
                    writer = response.getWriter();
                    writer.print(objectMapper.writeValueAsString(dto));
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
        }
        chain.doFilter(httpServletRequest, response);
    }


    private String getToken(HttpServletRequest httpServletRequest) {
        // 访问H5页面时token在header里
        if (!Strings.isNullOrEmpty(httpServletRequest.getHeader("token"))) {
            return httpServletRequest.getHeader("token");
        }
        // logout时token在query里
        if (!Strings.isNullOrEmpty(httpServletRequest.getParameter("token"))) {
            return httpServletRequest.getParameter("token");
        }
        // API token在baseParam里
        BaseParamDto baseParamDto = this.getBaseParamDto(httpServletRequest);
        return baseParamDto != null ? baseParamDto.getBaseParam().getToken() : null;
    }

    private Source getSource(HttpServletRequest httpServletRequest) {
        BaseParamDto baseParamDto = this.getBaseParamDto(httpServletRequest);
        return baseParamDto != null && baseParamDto.getBaseParam() != null && Strings.isNullOrEmpty(baseParamDto.getBaseParam().getPlatform())
                ? Source.valueOf(baseParamDto.getBaseParam().getPlatform()) : null;
    }

    private BaseParamDto getBaseParamDto(HttpServletRequest httpServletRequest) {
        try {
            BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpServletRequest);
            return objectMapper.readValue(bufferedRequest.getInputStreamString(), BaseParamDto.class);
        } catch (IOException ignored) {
        }
        return null;
    }
}