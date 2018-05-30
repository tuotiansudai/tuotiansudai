package com.tuotiansudai.fudian.util;

import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.config.BankConfig;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Component
public class BankClient {

    private static Logger logger = LoggerFactory.getLogger(BankClient.class);

    private final BankConfig bankConfig;

    private static OkHttpClient CLIENT;

    @Autowired
    public BankClient(BankConfig bankConfig) {
        this.bankConfig = bankConfig;
        CLIENT = new OkHttpClient
                .Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public String send(String data, ApiType apiType) {
        RequestBody formBody = new FormBody.Builder()
                .add("reqData", data)
                .build();

        Request request = new Request.Builder()
                .url(MessageFormat.format("{0}/{1}", bankConfig.getBankUrl(), apiType.getPath()))
                .post(formBody)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            logger.info("[Bank Client] call bank, api type: {}, request data: {}, status: {}, response data: {}",
                    apiType, data, response.code(), response.body() == null ? null : response.body().string());

            if (!response.isSuccessful()) {
                logger.error("[Bank Client] call bank is not 200, api type: {}, request data: {}, status: {}, response data: {}",
                        apiType, data, response.code(), response.body() == null ? null : response.body().string());
                return null;
            }

            ResponseBody body = response.body();
            if (body == null) {
                logger.info("[Bank Client] call bank response is empty, api type: {}, request data: {}, status: {}, response data: {}",
                        apiType, data, response.code(), response.body() == null ? null : response.body().string());
                return null;
            }

            return body.string();
        } catch (IOException e) {
            logger.error(MessageFormat.format("[Bank Client] call bank exception, api type: {0}, data: {1}",  apiType, data), e);
        }

        return null;
    }
}
