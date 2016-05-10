package com.tuotiansudai.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.dto.SignInDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class SignInClient extends BaseClient {

    static Logger logger = Logger.getLogger(BaseClient.class);

    @Value("${signIn.host}")
    protected String host;

    @Value("${signIn.port}")
    protected String port;

    @Value("${signIn.application.context}")
    protected String applicationContext;

    @Autowired
    private OkHttpClient okHttpClient;

    @Override
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(String applicationContext) {
        this.applicationContext = applicationContext;
    }

    private final static String SIGN_IN_URL = "/loginHandler";

    static {

    }

    public BaseDto<LoginDto> sendSignIn(HttpServletRequest httpServletRequest, SignInDto dto) {
        return send(httpServletRequest, dto, SIGN_IN_URL);
    }

    private BaseDto<LoginDto> send(HttpServletRequest httpServletRequest, SignInDto dto, String requestPath) {
        try {
            String responseString = this.execute(httpServletRequest, requestPath, dto, "POST");
            if (!Strings.isNullOrEmpty(responseString)) {
                return objectMapper.readValue(responseString, new TypeReference<BaseDto<LoginDto>>() {
                });
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        BaseDto<LoginDto> resultDto = new BaseDto<>();
        LoginDto dataDto = new LoginDto();
        resultDto.setData(dataDto);
        return resultDto;
    }

    protected String execute(HttpServletRequest httpServletRequest, String path, SignInDto dto, String method) {
        String url = URL_TEMPLATE.replace("{host}", this.getHost()).replace("{port}", this.getPort()).replace("{applicationContext}", getApplicationContext()).replace("{uri}", path);
        RequestBody requestBody = new FormEncodingBuilder().add("username", dto.getUsername()).add("password", dto.getPassword()).add("captcha", dto.getCaptcha()).build();
        if ("GET".equalsIgnoreCase(method)) {
            requestBody = null;
        }
        Cookie[] cookies = httpServletRequest.getCookies();
        Request.Builder request = new Request.Builder()
                .url(url)
                .method(method, requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Cookie", "SESSION=" + httpServletRequest.getSession().getId());
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

}
