package com.tuotiansudai.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.squareup.okhttp.*;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class BaseClient {

    static Logger logger = Logger.getLogger(BaseClient.class);

    protected final static String URL_TEMPLATE = "http://{host}:{port}{applicationContext}{uri}";

    private final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final static MediaType text = MediaType.parse("text/plain; charset=utf-8");

    private final static String REQUEST_ID = "requestId";

    private final static String ANONYMOUS = "anonymous";

    private final static String USER_ID = "userId";


    protected String host;

    protected String port;

    protected String applicationContext;

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected OkHttpClient okHttpClient;

    public BaseClient() {
        this.okHttpClient = new OkHttpClient();
        this.okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        this.okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        this.okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        OkHttpLoggingInterceptor loggingInterceptor = new OkHttpLoggingInterceptor(message -> logger.info(message));
        okHttpClient.interceptors().add(loggingInterceptor);
    }

    protected String execute(String path, String requestJson, String method) {
        ResponseBody responseBody = newCall(path, requestJson, method);
        try {
            return responseBody != null ? responseBody.string() : null;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
    }


    protected byte[] downPdf(String path, String requestJson) {
        ResponseBody responseBody = newCall(path, requestJson, "POST");
        try {
            return responseBody != null ? responseBody.bytes() : null;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    protected ResponseBody newCallForBaiDu(String url,String requestStr) {
        RequestBody requestBody = RequestBody.create(text, !Strings.isNullOrEmpty(requestStr) ? requestStr : "");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", requestBody)
                .addHeader("User-Agent", "curl/7.12.1")
                .addHeader("Host", "data.zz.baidu.com")
                .addHeader("Content-Type", "text/plain; charset=UTF-8")
                .build();

        try {
            Response response = this.okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    protected ResponseBody newCall(String path, String requestJson, String method) {
        String url = URL_TEMPLATE.replace("{host}", this.getHost()).replace("{port}", this.getPort()).replace("{applicationContext}", getApplicationContext()).replace("{uri}", path);
        RequestBody requestBody = RequestBody.create(JSON, !Strings.isNullOrEmpty(requestJson) ? requestJson : "");
        if ("GET".equalsIgnoreCase(method)) {
            requestBody = null;
        }

        String requestId = (MDC.get(REQUEST_ID) != null && MDC.get(REQUEST_ID) instanceof String) ? MDC.get(REQUEST_ID).toString() : UUID.randomUUID().toString().replace("-", "");
        String userId = (MDC.get(USER_ID) != null && MDC.get(USER_ID) instanceof String) ? MDC.get(USER_ID).toString() : ANONYMOUS;

        Request request = new Request.Builder()
                .url(url)
                .method(method, requestBody)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader(REQUEST_ID, requestId)
                .addHeader(USER_ID, userId)
                .build();

        try {
            Response response = this.okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getApplicationContext() {
        return applicationContext;
    }
}
