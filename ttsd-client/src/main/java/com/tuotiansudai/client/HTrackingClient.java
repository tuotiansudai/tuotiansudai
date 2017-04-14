package com.tuotiansudai.client;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

@Component
public class HTrackingClient {

    static Logger logger = Logger.getLogger(AnxinWrapperClient.class);

    private static final String HTracking_Register = "http://api5.btmedia.cn/astore/reg.php?uid={0}&idfa={1}&appname=tuotiansudai";

    private static final String HTracking_Recharge = "http://api5.btmedia.cn/astore/chr.php?uid={0}&appname=tuotiansudai";

    private OkHttpClient okHttpClient = buildOkHttpClient();

    private OkHttpClient buildOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpLoggingInterceptor loggingInterceptor = new OkHttpLoggingInterceptor(message -> logger.info(message));
        okHttpClient.interceptors().add(loggingInterceptor);
        return okHttpClient;
    }

    public String hTrackingRegister(String mobile, String deviceId){
        return execute(HTracking_Register, mobile, deviceId);
    }

    public String hTrackingRecharge(String mobile){
        return execute(HTracking_Recharge, mobile);
    }

    protected String execute(String path, Object... param) {
        ResponseBody responseBody = newCall(path, param);
        try {
            return responseBody != null ? responseBody.string() : null;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    protected ResponseBody newCall(String hTrackingUrl, Object... param){
        String url = MessageFormat.format(hTrackingUrl, param);

        Request request = new Request.Builder()
                .url(url)
                .method("get", null)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

}
