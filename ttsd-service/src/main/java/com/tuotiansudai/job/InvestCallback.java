package com.tuotiansudai.job;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.SmsInvestFatalNotifyDto;
import com.tuotiansudai.repository.model.Environment;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;
import java.util.List;

public class InvestCallback implements Job {

    static Logger logger = Logger.getLogger(InvestCallback.class);

    public static final int RUN_INTERVAL_SECONDS = 2;

    public static final String JOB_GROUP = "umpay";

    public static final String JOB_NAME = "invest_call_back";

    public static final String JOB_TRIGGER_KEY = "job:invest:invest_callback_job_trigger";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Value("#{'${pay.invest.notify.fatal.mobile}'.split('\\|')}")
    private List<String> fatalNotifyMobiles;

    @Value("${common.environment}")
    private Environment environment;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String trigger = redisWrapperClient.get(JOB_TRIGGER_KEY);
        if (trigger != null && Integer.valueOf(trigger) > 0) {
            BaseDto<PayDataDto> bd = payWrapperClient.investCallback();
            if (!bd.isSuccess()) {
                PayDataDto pdd = bd.getData();
                String errMsg = "invest callback job execute fail. code:" + pdd.getCode() + ", status:" + pdd.getStatus()
                        + ", message:" + pdd.getMessage();
                sendSmsErrNotify(fatalNotifyMobiles, MessageFormat.format("{0},{1}", environment, errMsg));
            }
        }
    }

    private void sendSmsErrNotify(List<String> mobiles, String errMsg) {
        for (String mobile : mobiles) {
            logger.info("sent invest callback job fatal sms message to " + mobile);

            SmsInvestFatalNotifyDto dto = new SmsInvestFatalNotifyDto();
            dto.setMobile(mobile);
            dto.setErrMsg(errMsg);
            smsWrapperClient.sendInvestFatalNotify(dto);
        }
    }

}
