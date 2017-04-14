package com.tuotiansudai.client;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

public class HTrackingClient {

    static Logger logger = Logger.getLogger(AnxinWrapperClient.class);

    private static final String HTracking_Register = "http://api5.btmedia.cn/astore/reg.php?uid={0}&idfa={1}&appname=tuotiansudai";

    private static final String HTracking_Recharge = "http://api5.btmedia.cn/astore/chr.php?uid={0}&idfa={1}&appname=tuotiansudai";

    private final static String REQUEST_ID = "requestId";

    private final static String ANONYMOUS = "anonymous";

    private final static String USER_ID = "userId";

    private OkHttpClient okHttpClient = buildOkHttpClient();

    private OkHttpClient buildOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpLoggingInterceptor loggingInterceptor = new OkHttpLoggingInterceptor(message -> logger.info(message));
        okHttpClient.interceptors().add(loggingInterceptor);
        return okHttpClient;
    }

    public String hTrakingRegister(String mobile, String deviceId){
        return execute(HTracking_Register, mobile, deviceId);
    }

    public String hTrakingRechange(String mobile, String deviceId){
        return execute(HTracking_Recharge, mobile, deviceId);
    }

    protected String execute(String path, String... param) {
        ResponseBody responseBody = newCall(path, param);
        try {
            return responseBody != null ? responseBody.string() : null;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    protected ResponseBody newCall(String hTrackingUrl, String... param){
        String url = MessageFormat.format(hTrackingUrl, param);
        String requestId = (MDC.get(REQUEST_ID) != null && MDC.get(REQUEST_ID) instanceof String) ? MDC.get(REQUEST_ID).toString() : UUID.randomUUID().toString().replace("-", "");
        String userId = (MDC.get(USER_ID) != null && MDC.get(USER_ID) instanceof String) ? MDC.get(USER_ID).toString() : ANONYMOUS;

        Request request = new Request.Builder()
                .url(url)
                .method("get", null)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader(REQUEST_ID, requestId)
                .addHeader(USER_ID, userId)
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
