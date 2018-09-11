package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.enums.JianZhouSmsTemplate;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.LoanRepayNotifyModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LoanRepayNotifyScheduler {
    static Logger logger = LoggerFactory.getLogger(LoanRepayNotifyScheduler.class);
    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Scheduled(cron = "0 0 14 * * ?", zone = "Asia/Shanghai")
    public void loanRepayNotify() {
        String today = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        List<LoanRepayNotifyModel> loanRepayNotifyModelList = loanRepayMapper.findLoanRepayNotifyToday(today);
        Map<String, Long> repayNotify = loanRepayNotifyModelList.stream()
                .collect(Collectors.groupingBy(LoanRepayNotifyModel::getMobile, Collectors.summingLong(LoanRepayNotifyModel::getRepayAmount)));

        for (Map.Entry entry : repayNotify.entrySet()) {
            long amount = (Long) entry.getValue();
            logger.info("will send loanRepay notify, mobile: " + entry.getKey() + ", amount: " + amount);
            if (amount > 0) {
                logger.info("sent loan repay notify sms message to " + entry.getKey() + ", money:" + entry.getValue());
                mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_LOAN_REPAY_NOTIFY_TEMPLATE, Lists.newArrayList(((String) entry.getKey()).trim()), Lists.newArrayList(AmountConverter.convertCentToString(amount))));
            }
        }
    }
}
