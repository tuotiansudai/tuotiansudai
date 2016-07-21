package com.tuotiansudai.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.MonitorDataDto;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public abstract class BaseClient {

    static Logger logger = Logger.getLogger(BaseClient.class);

    protected final static String URL_TEMPLATE = "http://{host}:{port}{applicationContext}{uri}";

    private final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final static String REQUEST_ID = "requestId";

    private final static String ANONYMOUS = "anonymous";

    private final static String USER_ID = "userId";

    protected String host;

    protected String port;

    protected String applicationContext;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private OkHttpClient okHttpClient;

    protected String execute(String path, String requestJson, String method) {
        String url = URL_TEMPLATE.replace("{host}", this.getHost()).replace("{port}", this.getPort()).replace("{applicationContext}", getApplicationContext()).replace("{uri}", path);
        RequestBody requestBody = RequestBody.create(JSON, !Strings.isNullOrEmpty(requestJson) ? requestJson : "");
        if ("GET".equalsIgnoreCase(method)) {
            requestBody = null;
        }

        String requestId = (MDC.get(REQUEST_ID) != null && MDC.get(REQUEST_ID) instanceof String) ? MDC.get(REQUEST_ID).toString() : UUIDGenerator.generate();
        String userId = (MDC.get(USER_ID) != null && MDC.get(USER_ID) instanceof String) ? MDC.get(USER_ID).toString() : ANONYMOUS;

        Request request = new Request.Builder()
                .url(url)
                .method(method, requestBody)
                .addHeader("Content-Type", "application/json; chattsd-activity/src/main/webapp/templates/activities/app-download.ftlrset=UTF-8")
                .addHeader(REQUEST_ID, requestId)
                .addHeader(USER_ID, userId)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    public BaseDto<MonitorDataDto> monitor() {
        String responseString = this.execute("/monitor", null, "GET");
        if (responseString == null) {
            BaseDto<MonitorDataDto> resultDto = new BaseDto<>();
            MonitorDataDto dataDto = new MonitorDataDto();
            resultDto.setData(dataDto);
            return resultDto;
        }

        try {
            return objectMapper.readValue(responseString, new TypeReference<BaseDto<MonitorDataDto>>() {
            });
        } catch (IOException e) {
            BaseDto<MonitorDataDto> resultDto = new BaseDto<>();
            MonitorDataDto dataDto = new MonitorDataDto();
            resultDto.setData(dataDto);
            return resultDto;
        }
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
