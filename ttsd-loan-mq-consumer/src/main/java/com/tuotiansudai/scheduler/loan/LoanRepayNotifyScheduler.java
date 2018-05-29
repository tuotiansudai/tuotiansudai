package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.JianZhouSmsTemplate;
import com.tuotiansudai.dto.sms.SmsDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.LoanRepayNotifyModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoanRepayNotifyScheduler {
    static Logger logger = LoggerFactory.getLogger(LoanRepayNotifyScheduler.class);
    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("#{'${repay.remind.mobileList}'.split('\\|')}")
    private List<String> repayRemindMobileList;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Scheduled(cron = "0 0 14 * * ?", zone = "Asia/Shanghai")
    public void loanRepayNotify() {
        String today = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        try{
            List<LoanRepayNotifyModel> loanRepayNotifyModelList = loanRepayMapper.findLoanRepayNotifyToday(today);

            Map<String, Long> notifyMap = new HashMap<>();
            for (String mobile : repayRemindMobileList) {
                notifyMap.put(mobile, 0L);
            }

            for (LoanRepayNotifyModel model : loanRepayNotifyModelList) {
                try {
                    BaseDto<PayDataDto> response = payWrapperClient.autoRepay(model.getId());
                    if (response.isSuccess() && response.getData().getStatus()) {
                        logger.info("auto repay success, loanRepayId: " + model.getId() + ", continue to next.");
                        continue;
                    }
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                    continue;
                }
                for (String mobile : repayRemindMobileList) {
                    notifyMap.put(mobile, notifyMap.get(mobile) + model.getRepayAmount());
                }
                if (notifyMap.get(model.getMobile()) == null) {
                    notifyMap.put(model.getMobile(), model.getRepayAmount());
                } else {
                    notifyMap.put(model.getMobile(), notifyMap.get(model.getMobile()) + model.getRepayAmount());
                }
                logger.info("notify count: " + notifyMap.size());
            }

            if (loanRepayNotifyModelList.size() > 0) {
                for (Map.Entry entry : notifyMap.entrySet()) {
                    long amount = (Long) entry.getValue();
                    logger.info("will send loanRepay notify, mobile: " + entry.getKey() + ", amount: " + amount);
                    if (amount > 0) {
                        logger.info("sent loan repay notify sms message to " + entry.getKey() + ", money:" + entry.getValue());
                        mqWrapperClient.sendMessage(MessageQueue.UserSms, new SmsDto(JianZhouSmsTemplate.SMS_LOAN_REPAY_NOTIFY_TEMPLATE, Lists.newArrayList(((String) entry.getKey()).trim()), Lists.newArrayList(AmountConverter.convertCentToString(amount))));

                    }
                }
            }
        }catch (Exception e){
            logger.error("[LoanRepayNotifyScheduler:] job execution is failed.", e);
        }

    }

}
