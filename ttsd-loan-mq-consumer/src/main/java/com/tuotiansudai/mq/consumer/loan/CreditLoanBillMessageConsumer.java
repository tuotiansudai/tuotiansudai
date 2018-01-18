package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.job.DelayMessageDeliveryJobCreator;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.CreditLoanBillMapper;
import com.tuotiansudai.repository.model.CreditLoanBillModel;
import com.tuotiansudai.repository.model.CreditLoanBillOperationType;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class CreditLoanBillMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(CreditLoanBillMessageConsumer.class);

    private static final long CREDIT_LOAN_BALANCE_ALERT = 50000 * 100;

    @Autowired
    private CreditLoanBillMapper creditLoanBillMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private JobManager jobManager;

    private RedisWrapperClient redis = RedisWrapperClient.getInstance();

    private static final String CREDIT_LOAN_BALANCE_ALERT_KEY = "CREDIT_LOAN_BALANCE_ALERT_KEY:{}";

    @Value(value = "${credit.loan.balance.alert.startTime}")
    private String startTimeStr;

    @Value(value = "${credit.loan.balance.alert.endTime}")
    private String endTimeStr;


    @Override
    public MessageQueue queue() {
        return MessageQueue.CreditLoanBill;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            CreditLoanBillModel billModel;
            try {
                billModel = JsonConverter.readValue(message, CreditLoanBillModel.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            CreditLoanBillModel billInDb = creditLoanBillMapper.findByOrderIdAndBusinessType(billModel.getOrderId(), billModel.getBusinessType());
            if (billInDb != null) {
                logger.info("[MQ] ready to consume message to save credit loan bill, but bill has already saved. message:{}", message);
            } else {
                logger.info("[MQ] ready to consume message to save credit loan bill. message:{}", message);
                long balance = creditLoanBillMapper.findBalance();
                billModel.setBalance(billModel.getOperationType() == CreditLoanBillOperationType.IN ? balance + billModel.getAmount() : balance - billModel.getAmount());
                creditLoanBillMapper.create(billModel);
                if (billModel.getBalance() < CREDIT_LOAN_BALANCE_ALERT) {
                    sendCreditLoanBalanceAlert();
                }
            }
            logger.info("[MQ] consume message success.");
        }
    }

    private void sendCreditLoanBalanceAlert() {

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String today = sdfDate.format(now);
        String key = MessageFormat.format(CREDIT_LOAN_BALANCE_ALERT_KEY, today);

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeStr.split(":")[0]));
        startTime.set(Calendar.MINUTE, Integer.parseInt(startTimeStr.split(":")[1]));
        startTime.set(Calendar.SECOND, 0);

        Calendar endTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimeStr.split(":")[0]));
        startTime.set(Calendar.MINUTE, Integer.parseInt(endTimeStr.split(":")[1]));
        startTime.set(Calendar.SECOND, 0);

        if (redis.setnx(key, "1")) {
            if (now.after(startTime.getTime()) && now.before(endTime.getTime())) {
                smsWrapperClient.sendCreditLoanBalanceAlert();
            } else {
                DelayMessageDeliveryJobCreator.createOrReplaceCreditLoanBalanceAlertDelayJob(jobManager, getNext9am(now));
            }
        }
        redis.expire(key, 3600 * 25);
    }

    private Date getNext9am(Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);

        if (c.get(Calendar.HOUR_OF_DAY) >= 9) {
            c.add(Calendar.DATE, 1);
        }

        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }
}
