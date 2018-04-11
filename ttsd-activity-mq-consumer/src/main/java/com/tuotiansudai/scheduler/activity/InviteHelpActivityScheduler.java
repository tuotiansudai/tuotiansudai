package com.tuotiansudai.scheduler.activity;

import com.tuotiansudai.activity.repository.mapper.WeChatHelpMapper;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class InviteHelpActivityScheduler {
    static Logger logger = LoggerFactory.getLogger(InviteHelpActivityScheduler.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private WeChatHelpMapper weChatHelpMapper;

    public static final String INVEST_HELP_WAIT_SEND_CASH = "INVEST_HELP_WAIT_SEND_CASH";

    public static final String EVERYONE_HELP_WAIT_SEND_CASH = "EVERYONE_HELP_WAIT_SEND_CASH";


    @Scheduled(cron = "0 * * * * ?", zone = "Asia/Shanghai")
    public void sendCash(){


    }

    public void sendInvestHelpCash(){
        Map<String, String> investHelps = redisWrapperClient.hgetAll(INVEST_HELP_WAIT_SEND_CASH);

        for (Map.Entry<String, String> entry : investHelps.entrySet()){
            long loanId = Long.parseLong(entry.getKey());
            Date sendTime = DateTime.parse(entry.getValue(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            if (new Date().after(sendTime)){

                TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(IdGenerator.generate()), String.valueOf(sendCash),
                        UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.CASH_START_WORK_DETAIL_TEMPLATE);

                payWrapperClient.transferCash(transferCashDto);

            }

        }


    }

}
