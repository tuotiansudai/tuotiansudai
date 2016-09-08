package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.LoginResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.MyUser;
import com.tuotiansudai.spring.security.LoginResult;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.text.MessageFormat;
import java.util.List;

@Component
public class MobileAppAuthenticationTokenProcessingFilter extends GenericFilterBean {

    private String refreshTokenUrl = "/refresh-token";

    private OkHttpClient okHttpClient = new OkHttpClient();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${signIn.host}")
    private String signInHost;

    @Value("${signIn.port}")
    private String signInPort;

    @Autowired
    private MyAuthenticationUtil myAuthenticationUtil;

    public MobileAppAuthenticationTokenProcessingFilter() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String token = getToken(httpServletRequest);
        if (!Strings.isNullOrEmpty(token)) {
            Request.Builder preAuthenticatedRequest = new Request.Builder()
                    .url(MessageFormat.format("http://{0}:{1}/session/{2}", signInHost, signInPort, token))
                    .get();
            Response execute = okHttpClient.newCall(preAuthenticatedRequest.build()).execute();
            LoginResult loginResult = objectMapper.readValue(execute.body().string(), LoginResult.class);
            if (loginResult.isResult()) {
                List<GrantedAuthority> grantedAuthorities = Lists.transform(loginResult.getUserInfo().getRoles(), new Function<String, GrantedAuthority>() {
                    @Override
                    public GrantedAuthority apply(String role) {
                        return new SimpleGrantedAuthority(role);
                    }
                });

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        new MyUser(loginResult.getToken(), loginResult.getUserInfo().getLoginName(), "", true, true, true, true, grantedAuthorities, loginResult.getUserInfo().getMobile()),
                        "",
                        grantedAuthorities);
                authenticationToken.setDetails(authenticationToken.getDetails());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                if (httpServletRequest.getRequestURI().equalsIgnoreCase(refreshTokenUrl)) {
                    String newToken = myAuthenticationUtil.refreshTokenProcess(token, getSource(httpServletRequest));
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
            } else {
                logger.error(MessageFormat.format("token {0} is invalid", token));
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
            return objectMapper.readValue(bufferedRequest.getInputStream(), BaseParamDto.class);
        } catch (IOException ignored) {
        }
        return null;
    }
}