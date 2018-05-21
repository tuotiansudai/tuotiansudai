package com.tuotiansudai.smswrapper.client;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.squareup.okhttp.*;
import com.tuotiansudai.client.OkHttpLoggingInterceptor;
import com.tuotiansudai.smswrapper.JianZhouSmsTemplate;
import com.tuotiansudai.util.WeChatClient;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JianZhouSmsClient {

    static Logger logger = Logger.getLogger(JianZhouSmsClient.class);

    private final static JianZhouSmsClient instance = new JianZhouSmsClient();

    private final static MediaType FORM = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8");

    private final static String SMS_TEXT_URL = "http://www.jianzhou.sh.cn/JianzhouSMSWSServer/http/sendBatchMessage";

    private final static String SMS_VOICE_URL = "http://www.jianzhou.sh.cn/JianzhouSMSWSServer/http/sendAudio";

    private final static String SMS_SIGN = "【拓天速贷】";

    private final static String ACCOUNT = "sdk_bjttwy";

    private final static String PASSWORD = "667789887";

    private OkHttpClient okHttpClient;

    public static JianZhouSmsClient getClient() {
        return instance;
    }

    public JianZhouSmsClient(){
        this.okHttpClient = new OkHttpClient();
        this.okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        this.okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        this.okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        OkHttpLoggingInterceptor loggingInterceptor = new OkHttpLoggingInterceptor(message -> logger.info(message));
        okHttpClient.interceptors().add(loggingInterceptor);
    }

    public String sendSms(boolean isVoice, List<String> mobileList, JianZhouSmsTemplate template, List<String> paramList, String sendDateTime){
        String msgText = template.generateContent(isVoice, paramList) + SMS_SIGN;
        String mobiles = String.join(";", mobileList);
        sendDateTime = Strings.isNullOrEmpty(sendDateTime) ? "" : sendDateTime;
        String content = MessageFormat.format("account={0}&password={1}&sendDateTime={2}&destmobile={3}&msgText={4}", ACCOUNT, PASSWORD, sendDateTime, mobiles, msgText);
        return this.syncExecute(isVoice, content);
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

        }catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public static void main(String[] args) {
        new JianZhouSmsClient().sendSms(true, Lists.newArrayList("18895730992", "13671079909"), JianZhouSmsTemplate.SMS_FATAL_NOTIFY_TEMPLATE, null, null);
    }
}
