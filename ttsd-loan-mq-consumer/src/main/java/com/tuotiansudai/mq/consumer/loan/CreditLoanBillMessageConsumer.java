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
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
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

    private static final String CREDIT_LOAN_BALANCE_ALERT_KEY_FORMAT = "CREDIT_LOAN_BALANCE_ALERT_KEY:{0}";

    @Value(value = "${credit.loan.balance.alert.start.hour}")
    private int startHour;

    @Value(value = "${credit.loan.balance.alert.end.hour}")
    private int endHour;

    @Value(value = "${credit.loan.balance.alert.send.hour}")
    private int sendHour;


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

        DateTime now = new DateTime();
        String today = now.toString("yyyy-MM-dd");
        String key = MessageFormat.format(CREDIT_LOAN_BALANCE_ALERT_KEY_FORMAT, today);

        DateTime startTime = new DateTime().withTimeAtStartOfDay().withHourOfDay(startHour);
        DateTime endTime = new DateTime().withTimeAtStartOfDay().withHourOfDay(endHour);

        if (redis.setnx(key, "1")) {
            if (now.isAfter(startTime) && now.isBefore(endTime)) {
                logger.debug("send credit loan balance alert immediately.");
                smsWrapperClient.sendCreditLoanBalanceAlert();
            } else {
                logger.debug("send credit loan balance alert delay.");
                DelayMessageDeliveryJobCreator.createOrReplaceCreditLoanBalanceAlertDelayJob(jobManager, getNextSendTime());
            }
        }
        redis.expire(key, 3600 * 25);
    }

    private Date getNextSendTime() {
        DateTime d = new DateTime();

        if (d.getHourOfDay() >= sendHour)
            d.plusDays(1);

        DateTime nextSendTime = d.withTimeAtStartOfDay().withHourOfDay(19).withMinuteOfHour(15);
        logger.debug("get next send time : {}", nextSendTime.toString("yyyy-MM-dd HH:mm:ss"));
        return nextSendTime.toDate();
    }
}
