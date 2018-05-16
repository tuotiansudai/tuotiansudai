package com.tuotiansudai.smswrapper.provider;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.squareup.okhttp.*;
import com.tuotiansudai.client.OkHttpLoggingInterceptor;
import com.tuotiansudai.smswrapper.SmsTemplateCell;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JianZhouSmsWrapperClient {

    static Logger logger = Logger.getLogger(JianZhouSmsWrapperClient.class);

    private final static MediaType FORM = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8");

    private final static String SMS_TEXT_URL = "http://www.jianzhou.sh.cn/JianzhouSMSWSServer/http/sendBatchMessage";

    private final static String SMS_VOICE_URL = "http://www.jianzhou.sh.cn/JianzhouSMSWSServer/http/sendAudio";

    private final static String SMS_SIGN = "【拓天速贷】";

    private final static String ACCOUNT = "sdk_bjttwy";

    private final static String PASSWORD = "667789887";

    protected OkHttpClient okHttpClient;

    public JianZhouSmsWrapperClient(){
        this.okHttpClient = new OkHttpClient();
        this.okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        this.okHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);
        this.okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        OkHttpLoggingInterceptor loggingInterceptor = new OkHttpLoggingInterceptor(message -> logger.info(message));
        okHttpClient.interceptors().add(loggingInterceptor);
    }

    public void sendSms(boolean isVoice, List<String> mobileList, SmsTemplateCell template, List<String> paramList){
        String msgText = "测试开始" + SMS_SIGN;
        String destmobile = String.join(";", mobileList);
        String content = "account=" + ACCOUNT + "&" + "password=" + PASSWORD + "&" + "sendDateTime=" + "" + "&" + "destmobile=" + destmobile + "&"
                + "msgText=" + msgText;
        this.syncExecute(isVoice, content);
    }

    private String syncExecute(boolean isVoice, String requestData){
        String url = isVoice ? SMS_VOICE_URL : SMS_TEXT_URL;
        RequestBody requestBody = RequestBody.create(FORM, requestData);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = this.okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                return response.body().string();
            }

        }catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public static void main(String[] args) {
        new JianZhouSmsWrapperClient().sendSms(false, Lists.newArrayList("18895730992", "18310701649"), null, null);
    }
}
