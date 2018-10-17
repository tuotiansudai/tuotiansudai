package com.tuotiansudai.mq.consumer.sms.client;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.enums.JianZhouSmsTemplate;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.mq.consumer.sms.repository.mapper.JianZhouSmsHistoryMapper;
import com.tuotiansudai.mq.consumer.sms.repository.model.JianZhouSmsHistoryModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private Environment environment = Environment.valueOf(ETCDConfigReader.getReader().getValue("common.environment"));

    private int second = Integer.parseInt(ETCDConfigReader.getReader().getValue("sms.interval.seconds"));

    private List<String> antiCoolDownIpList = Arrays.asList(ETCDConfigReader.getReader().getValue("sms.antiCooldown.ipList").split("\\|"));

    private final static int QA_DAILY_LIMIT = 100;

    private final static String SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE = "sms_ip_restricted:{0}";

    private final static String QA_SEND_COUNT_PER_DAY = "qa_send_count_per_day:{0}";

    @Autowired
    private JianZhouSmsHistoryMapper jianZhouSmsHistoryMapper;

    public JianZhouSmsClient(){
        this.okHttpClient = new OkHttpClient();
        this.okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        this.okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        this.okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
    }

    public void sendSms(List<String> mobileList, JianZhouSmsTemplate template, boolean isVoice, List<String> paramList, String restrictedIP){

        if (Lists.newArrayList(Environment.SMOKE, Environment.DEV).contains(environment)) {
            logger.info(MessageFormat.format("sms sending ignored in {0} environment", environment));
            return;
        }

        if (hasExceedQALimit(mobileList)) {
            logger.info(MessageFormat.format("已超出QA环境当日发送限额{0}", QA_DAILY_LIMIT));
            return;
        }

        if (isInCoolDown(restrictedIP)) {
            logger.info(MessageFormat.format("IP: {0} 受限", restrictedIP));
            return;
        }

        String msgText = null;
        try {
            msgText = URLEncoder.encode(template.generateContent(isVoice, paramList) + SMS_SIGN, "UTF-8");
        } catch (Exception e) {
            logger.error(MessageFormat.format("{0} send sms fail", template.generateContent(isVoice, paramList)));
            return;
        }
        String mobiles = String.join(";", mobileList);
        String content = MessageFormat.format("account={0}&password={1}&sendDateTime={2}&destmobile={3}&msgText={4}", ACCOUNT, PASSWORD, "", mobiles, msgText);

        List<JianZhouSmsHistoryModel> models = createSmsHistory(mobileList, template, paramList, isVoice);
        String response = this.syncExecute(isVoice, content);
        updateSmsHistory(models, response);

        if(response == null || Long.parseLong(response) < 0){
            logger.info("短信网关返回失败");
            return;
        }

        this.setIntoCoolDown(restrictedIP);
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


    private boolean hasExceedQALimit(List<String> mobileList) {
        String hKey = DateTime.now().toString("yyyyMMdd");
        String redisValue = redisWrapperClient.hget(QA_SEND_COUNT_PER_DAY, hKey);

        int smsSendSize = Strings.isNullOrEmpty(redisValue) ? 0 : Integer.parseInt(redisValue);
        boolean exceed = smsSendSize + mobileList.size() >= QA_DAILY_LIMIT;
        if (!exceed) {
            smsSendSize += mobileList.size();
            redisWrapperClient.hset(QA_SEND_COUNT_PER_DAY, hKey, String.valueOf(smsSendSize), 172800);
        }
        return Lists.newArrayList(Environment.QA1, Environment.QA2, Environment.QA3, Environment.QA4, Environment.QA5).contains(environment) && exceed;
    }

    private boolean isInCoolDown(String ip) {
        String redisKey = MessageFormat.format(SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE, ip);
        return redisWrapperClient.exists(redisKey);
    }

    private void setIntoCoolDown(String ip) {
        if (!Strings.isNullOrEmpty(ip) && !antiCoolDownIpList.contains(ip)) {
            String redisKey = MessageFormat.format(SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE, ip);
            redisWrapperClient.setex(redisKey, second, ip);
        }
    }

    private List<JianZhouSmsHistoryModel> createSmsHistory(List<String> mobileList, JianZhouSmsTemplate template, List<String> paramList, boolean isVoice) {
        return mobileList.stream().map(mobile -> {
            JianZhouSmsHistoryModel model = new JianZhouSmsHistoryModel(
                    mobile,
                    template.generateContent(isVoice, paramList),
                    isVoice);
            jianZhouSmsHistoryMapper.create(model);
            return model;
        }).collect(Collectors.toList());
    }

    private void updateSmsHistory(List<JianZhouSmsHistoryModel> models, String response){
        models.forEach(model -> {
            model.setResponse(response);
            jianZhouSmsHistoryMapper.update(model);
        });
    }
}
