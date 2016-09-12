package com.tuotiansudai.spring.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.repository.model.Source;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.MessageFormat;

@Component
public class SignInClient {

    static Logger logger = Logger.getLogger(MyAuthenticationProvider.class);

    private final static int RETRY_MAX_TIMES = 3;

    private OkHttpClient okHttpClient = new OkHttpClient();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${signIn.host}")
    private String signInHost;

    @Value("${signIn.port}")
    private String signInPort;

    @Autowired
    private HttpServletRequest httpServletRequest;

    public SignInClient() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public SignInResult login(String username, String password, String token, Source source, String deviceId) {
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password) || source == null) {
            logger.error("[sign in client] username or password or source is empty");
            return null;
        }

        token = Strings.isNullOrEmpty(token) ? "none" : token;
        deviceId = Strings.isNullOrEmpty(deviceId) ? "" : deviceId;

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder()
                .add("username", username)
                .add("password", password)
                .add("token", token)
                .add("source", source.name())
                .add("deviceId", deviceId);

        Request.Builder request = new Request.Builder()
                .url(MessageFormat.format("http://{0}:{1}/login/", signInHost, signInPort))
                .post(formEncodingBuilder.build());

        try {
            String response = this.execute(request);
            return objectMapper.readValue(response, SignInResult.class);
        } catch (IOException e) {
            logger.error(MessageFormat.format("[sign in client] login failed (user={0} token={1} source={2} deviceId={3})", username, token, source, deviceId), e);
        }

        return null;
    }

    public SignInResult loginNoPassword(String username, Source source) {
        if (Strings.isNullOrEmpty(username) || source == null) {
            logger.error("[sign in client] username or source is empty");
            return null;
        }

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder()
                .add("username", username)
                .add("token", "none")
                .add("source", source.name());

        RequestBody requestBody = formEncodingBuilder.build();

        Request.Builder request = new Request.Builder()
                .url(MessageFormat.format("http://{0}:{1}/login/nopassword/", signInHost, signInPort))
                .post(requestBody);

        try {
            return objectMapper.readValue(this.execute(request), SignInResult.class);
        } catch (IOException e) {
            logger.error(MessageFormat.format("[sign in client] login no password failed (user={0} source={1})", username, source.name()));
        }
        return null;
    }

    public SignInResult logout(String token) {
        if (Strings.isNullOrEmpty(token)) {
            logger.error("[sign in client] token is empty");
            return null;
        }

        Request.Builder request = new Request.Builder()
                .url(MessageFormat.format("http://{0}:{1}/logout/{2}", signInHost, signInPort, token))
                .post(RequestBody.create(null, new byte[0]));

        try {
            return objectMapper.readValue(this.execute(request), SignInResult.class);
        } catch (IOException e) {
            logger.error(MessageFormat.format("[sign in client] logout (token={0})", token));
        }
        return null;
    }

    public SignInResult refresh(String token, Source source) {
        if (Strings.isNullOrEmpty(token) || source == null) {
            logger.error("[sign in client] token or source is empty");
            return null;
        }

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder()
                .add("source", source.name());

        RequestBody requestBody = formEncodingBuilder.build();
        Request.Builder request = new Request.Builder()
                .url(MessageFormat.format("http://{0}:{1}/refresh/{2}", signInHost, signInPort, token))
                .post(requestBody);
        try {
            return objectMapper.readValue(this.execute(request), SignInResult.class);
        } catch (IOException e) {
            logger.error(MessageFormat.format("[sign in client] refresh failed (token={0} source={1})", token, source.name()));
        }

        return null;
    }

    public SignInResult verifyToken(String token, Source source) {
        if (Strings.isNullOrEmpty(token)) {
            logger.info("[sign in client] verified token is empty");
            return null;
        }

        Request.Builder request = new Request.Builder()
                .url(MessageFormat.format("http://{0}:{1}/session/{2}?source={3}", signInHost, signInPort, token, source))
                .get();
        try {
            SignInResult signInResult = objectMapper.readValue(this.execute(request), SignInResult.class);
            if (!signInResult.isResult()) {
                logger.info(MessageFormat.format("[sign in client] session({0}) is invalid", token));
            }
            return signInResult;
        } catch (IOException e) {
            logger.error("[sign in client] verify token failed", e);
        }

        return null;
    }

    public void unlockUser(String loginName, String mobile) {
        Request.Builder loginNameRequest = new Request.Builder()
                .url(MessageFormat.format("http://{0}:{1}/user/{2}/active/", signInHost, signInPort, loginName))
                .post(RequestBody.create(null, new byte[0]));

        Request.Builder mobileRequest = new Request.Builder()
                .url(MessageFormat.format("http://{0}:{1}/user/{2}/active/", signInHost, signInPort, mobile))
                .post(RequestBody.create(null, new byte[0]));
        try {
            this.execute(loginNameRequest);
            this.execute(mobileRequest);
            logger.info(MessageFormat.format("[sign in client] activate user(loginName={0} mobile={1})", loginName, mobile));
        } catch (IOException e) {
            logger.error(MessageFormat.format("[sign in client] activate user(loginName={0} mobile={1}) failed", loginName, mobile), e);
        }
    }

    private String execute(Request.Builder requestBuilder) throws IOException {
        int times = 0;
        String header = httpServletRequest.getHeader("X-Forwarded-For");
        if (!Strings.isNullOrEmpty(header)) {
            requestBuilder.addHeader("X-Forwarded-For", header);
        }
        Request request = requestBuilder.build();
        do {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() < 500) {
                return response.body().string();
            }

            logger.error(MessageFormat.format("[sign in client] 500 error (url={0})", request.httpUrl().url()));

        } while (++times < RETRY_MAX_TIMES);

        throw new IOException(MessageFormat.format("[sign in client] sign in server error (url={0})", request.httpUrl().url()));
    }
}
