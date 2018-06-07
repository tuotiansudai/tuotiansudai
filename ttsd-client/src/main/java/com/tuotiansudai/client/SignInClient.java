package com.tuotiansudai.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.repository.model.Source;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class SignInClient {

    private final static Logger logger = Logger.getLogger(SignInClient.class);

    private final static SignInClient self = new SignInClient();

    private final static String SIGN_IN_SERVICE = ETCDConfigReader.getReader().getValue("user.rest.server");

    private final static int RETRY_MAX_TIMES = 3;

    private static OkHttpClient okHttpClient;

    private final static ObjectMapper objectMapper;

    static {
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(5, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(5, TimeUnit.SECONDS);
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private SignInClient() {
    }

    public static SignInClient getInstance() {
        return self;
    }

    public SignInResult login(String username, String password, String token, Source source, String deviceId, String xForwardedForHeader) {
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password) || source == null) {
            logger.error("[sign in client] username or password or source is empty");
            return null;
        }
        if (length(username) < 5 || length(username) > 25) {
            logger.error("[sign in client] Input the username too short or too long, please enter again! ");
            SignInResult signInResult = new SignInResult();
            signInResult.setResult(false);
            signInResult.setMessage("用户名长度不符合规则");
            return signInResult;
        }

        token = Strings.isNullOrEmpty(token) ? "none" : token;
        deviceId = Strings.isNullOrEmpty(deviceId) ? "" : deviceId;

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder()
                .add("username", username)
                .add("password", password)
                .add("token", token)
                .add("source", source.name())
                .add("device_id", deviceId);

        Request.Builder request = new Request.Builder()
                .url(MessageFormat.format("{0}/login/", SIGN_IN_SERVICE))
                .post(formEncodingBuilder.build());

        if (!Strings.isNullOrEmpty(xForwardedForHeader)) {
            request.addHeader("X-Forwarded-For", xForwardedForHeader);
        }

        try {
            Response response = execute(request);
            return objectMapper.readValue(response.body().string(), SignInResult.class);
        } catch (IllegalArgumentException e) {
            logger.warn(MessageFormat.format("[sign in client] login failed, response is 4xx (user={0} token={1} source={2} deviceId={3})", username, token, source, deviceId));
            SignInResult signInResult = new SignInResult();
            signInResult.setResult(false);
            signInResult.setMessage("用户名或密码错误");
            return signInResult;
        } catch (Exception e) {
            logger.error(MessageFormat.format("[sign in client] login failed (user={0} token={1} source={2} deviceId={3})", username, token, source, deviceId), e);
        }

        return null;
    }

    public SignInResult loginNoPassword(String username, Source source, String xForwardedForHeader) {
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
                .url(MessageFormat.format("{0}/login/nopassword/", SIGN_IN_SERVICE))
                .post(requestBody);

        if (!Strings.isNullOrEmpty(xForwardedForHeader)) {
            request.addHeader("X-Forwarded-For", xForwardedForHeader);
        }

        try {
            return objectMapper.readValue(execute(request).body().string(), SignInResult.class);
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
                .url(MessageFormat.format("{0}/logout/{1}", SIGN_IN_SERVICE, token))
                .post(RequestBody.create(null, new byte[0]));

        try {
            return objectMapper.readValue(execute(request).body().string(), SignInResult.class);
        } catch (IOException e) {
            logger.error(MessageFormat.format("[sign in client] logout (token={0})", token));
        }
        return null;
    }

    public SignInResult refreshToken(String token, Source source) {
        if (Strings.isNullOrEmpty(token) || source == null) {
            logger.error("[sign in client] token or source is empty");
            return null;
        }

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder()
                .add("source", source.name());

        RequestBody requestBody = formEncodingBuilder.build();
        Request.Builder request = new Request.Builder()
                .url(MessageFormat.format("{0}/refreshToken/{1}", SIGN_IN_SERVICE, token))
                .post(requestBody);
        try {
            return objectMapper.readValue(this.execute(request).body().string(), SignInResult.class);
        } catch (IOException e) {
            logger.error(MessageFormat.format("[sign in client] refreshToken failed (token={0} source={1})", token, source.name()));
        }

        return null;
    }

    public SignInResult refreshData(String token, Source source) {
        if (Strings.isNullOrEmpty(token) || source == null) {
            logger.error("[sign in client] token or source is empty");
            return null;
        }

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder()
                .add("source", source.name());

        RequestBody requestBody = formEncodingBuilder.build();
        Request.Builder request = new Request.Builder()
                .url(MessageFormat.format("{0}/session/{1}", SIGN_IN_SERVICE, token))
                .put(requestBody);
        try {
            return objectMapper.readValue(execute(request).body().string(), SignInResult.class);
        } catch (IOException e) {
            logger.error(MessageFormat.format("[sign in client] refresh data failed (token={0} source={1})", token, source.name()));
        }

        return null;
    }

    public SignInResult verifyToken(String token, Source source) {
        if (Strings.isNullOrEmpty(token)) {
            return null;
        }

        Request.Builder request = new Request.Builder()
                .url(MessageFormat.format("{0}/session/{1}?source={2}", SIGN_IN_SERVICE, token, source))
                .get();
        try {
            return objectMapper.readValue(execute(request).body().string(), SignInResult.class);
        } catch (IOException e) {
            logger.warn("[sign in client] verify token failed", e);
        }

        return null;
    }

    public void unlockUser(String loginName, String mobile) {
        Request.Builder loginNameRequest = new Request.Builder()
                .url(MessageFormat.format("{0}/user/{1}/active/", SIGN_IN_SERVICE, loginName))
                .post(RequestBody.create(null, new byte[0]));

        Request.Builder mobileRequest = new Request.Builder()
                .url(MessageFormat.format("{0}/user/{1}/active/", SIGN_IN_SERVICE, mobile))
                .post(RequestBody.create(null, new byte[0]));
        try {
            execute(loginNameRequest);
            execute(mobileRequest);
            logger.info(MessageFormat.format("[sign in client] activate user(loginName={0} mobile={1})", loginName, mobile));
        } catch (IOException e) {
            logger.error(MessageFormat.format("[sign in client] activate user(loginName={0} mobile={1}) failed", loginName, mobile), e);
        }
    }

    private Response execute(Request.Builder requestBuilder) throws IOException {
        int tryTimes = 0;

        do {
            Request request = requestBuilder.build();
            Response response = okHttpClient.newCall(request).execute();

            //用户名或密码错误
            if (response.code() == 401) {
                return response;
            }

            //用户名或密码参数格式错误
            if (HttpStatus.valueOf(response.code()).is4xxClientError()) {
                throw new IllegalArgumentException();
            }

            if (!HttpStatus.valueOf(response.code()).is5xxServerError()) {
                return response;
            }

            logger.error(MessageFormat.format("[sign in client] sign in server response is 5xx error (url={0}), try times is {1}",
                    request.httpUrl().url(), String.valueOf(tryTimes)));

        } while (++tryTimes < RETRY_MAX_TIMES);

        throw new IOException();
    }

    /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
    private static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";

        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            valueLength += 1;
            if (temp.matches(chinese)) {
                valueLength += 1;
            }
        }
        return valueLength;
    }
}
