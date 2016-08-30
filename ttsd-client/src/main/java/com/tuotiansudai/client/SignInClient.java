package com.tuotiansudai.client;

import com.google.common.base.Strings;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.dto.SignInDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

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

    private final static String REQUEST_ID = "requestId";

    private final static String ANONYMOUS = "anonymous";

    private final static String USER_ID = "userId";

    public LoginDto sendSignIn(String oldSessionId, SignInDto dto) {
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder()
                .add("username", dto.getUsername())
                .add("password", dto.getPassword())
                .add("captcha", dto.getCaptcha())
                .add("source", dto.getSource());
        if (!Strings.isNullOrEmpty(dto.getDeviceId())) {
            formEncodingBuilder.add("deviceId", dto.getDeviceId());
        }

        logger.info(MessageFormat.format("[Sign In Request Data] username={0}, sessionId={1}, captcha={2}, source={3}",
                dto.getUsername(), oldSessionId, dto.getCaptcha(), dto.getSource()));

        RequestBody requestBody = formEncodingBuilder.build();
        return this.send(oldSessionId, SIGN_IN_URL, requestBody);
    }

    private LoginDto send(String oldSessionId, String requestPath, RequestBody requestBody) {
        try {
            String responseString = this.execute(oldSessionId, requestPath, requestBody);
            if (!Strings.isNullOrEmpty(responseString)) {
                return objectMapper.readValue(responseString, LoginDto.class);
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return new LoginDto();
    }

    protected String execute(String oldSessionId, String path, RequestBody requestBody) {
        String url = URL_TEMPLATE.replace("{host}", this.getHost()).replace("{port}", this.getPort()).replace("{applicationContext}", getApplicationContext()).replace("{uri}", path);
        String requestId = (MDC.get(REQUEST_ID) != null && MDC.get(REQUEST_ID) instanceof String) ? MDC.get(REQUEST_ID).toString() : UUID.randomUUID().toString().replace("-", "");
        String userId = (MDC.get(USER_ID) != null && MDC.get(USER_ID) instanceof String) ? MDC.get(USER_ID).toString() : ANONYMOUS;
        Request.Builder request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader(REQUEST_ID, requestId)
                .addHeader(USER_ID, userId);

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
