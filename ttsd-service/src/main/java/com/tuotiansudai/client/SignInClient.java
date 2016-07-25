package com.tuotiansudai.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.dto.SignInDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

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

    private final static String SIGN_OUT_URL = "/logout";

    public BaseDto<LoginDto> sendSignIn(String oldSessionId, SignInDto dto) {
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder()
                .add("username", dto.getUsername())
                .add("password", dto.getPassword())
                .add("captcha", dto.getCaptcha())
                .add("source", dto.getSource());
        if (StringUtils.isNotEmpty(dto.getDeviceId())) {
            formEncodingBuilder.add("deviceId", dto.getDeviceId());
        }

        RequestBody requestBody = formEncodingBuilder.build();
        return this.send(oldSessionId, SIGN_IN_URL, requestBody);
    }

    public BaseDto<LoginDto> sendSignOut(String oldSessionId) {
        RequestBody requestBody = new FormEncodingBuilder().add("Cookie", "SESSION=" + oldSessionId).build();
        return send(oldSessionId, SIGN_OUT_URL, requestBody);
    }

    private BaseDto<LoginDto> send(String oldSessionId, String requestPath, RequestBody requestBody) {
        try {
            String responseString = this.execute(oldSessionId, requestPath, requestBody);
            if (!Strings.isNullOrEmpty(responseString)) {
                return objectMapper.readValue(responseString, new TypeReference<BaseDto<LoginDto>>() {
                });
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return new BaseDto<>(new LoginDto());
    }

    protected String execute(String oldSessionId, String path, RequestBody requestBody) {
        String url = URL_TEMPLATE.replace("{host}", this.getHost()).replace("{port}", this.getPort()).replace("{applicationContext}", getApplicationContext()).replace("{uri}", path);
        Request.Builder request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        if (StringUtils.isNotEmpty(oldSessionId)) {
            request.addHeader("Cookie", MessageFormat.format("SESSION={0}", oldSessionId));
        }
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
