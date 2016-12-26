package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.sms.InvestSmsNotifyDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.LoanOutMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestNotifyInfo;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Component
public class LoanOutSuccessNotifyForLoanOutMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessNotifyForLoanOutMessageConsumer.class);

    private final static String LOAN_OUT_IDEMPOTENT_CHECK_TEMPLATE = "LOAN_OUT_IDEMPOTENT_CHECK:{0}";

    private final static String SMS_AND_EMAIL = "SMS_AND_EMAIL";

    private final static String FAILURE = "FAILURE";

    private final static String SUCCESS = "SUCCESS";

    private final static String SENT = "SENT";

    @Autowired
    private LoanMapper loanMapper;

    @Value("${common.environment}")
    private Environment environment;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_SmsMessage;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            LoanOutMessage loanOutMessage;
            try {
                loanOutMessage = JsonConverter.readValue(message, LoanOutMessage.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            long loanId = loanOutMessage.getLoanId();
            List<String> fatalSmsList = Lists.newArrayList();

            logger.info("[标的放款]：发送放款短信通知，标的ID:" + loanId);
            if (!processNotifyForLoanOut(loanId)) {
                fatalSmsList.add("发送放款短信通知失败");
                logger.error(MessageFormat.format("[标的放款]:发送放款短信通知失败 (loanId = {0})", String.valueOf(loanId)));
            }

            if (CollectionUtils.isNotEmpty(fatalSmsList)) {
                fatalSmsList.add(MessageFormat.format("标的ID:{0}", loanId));
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(Joiner.on(",").join(fatalSmsList)));
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess_SmsMessage fail sms sending. loanId:{0}", String.valueOf(loanId)));
            }

            logger.info("[MQ] consume LoanOutSuccess_SmsMessage success.");
        }
    }

    private boolean processNotifyForLoanOut(long loanId) {
        String redisKey = MessageFormat.format(LOAN_OUT_IDEMPOTENT_CHECK_TEMPLATE, String.valueOf(loanId));
        String statusString = redisWrapperClient.hget(redisKey, SMS_AND_EMAIL);
        if (Strings.isNullOrEmpty(statusString) || statusString.equals(FAILURE)) {
            try {
                redisWrapperClient.hset(redisKey, SMS_AND_EMAIL, SENT);
                List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);
                logger.debug(MessageFormat.format("[标的放款]:标的: {0} 放款短信通知", loanId));
                notifyInvestorsLoanOutSuccessfulBySMS(investModels);
                redisWrapperClient.hset(redisKey, SMS_AND_EMAIL, SUCCESS);
            } catch (Exception e) {
                redisWrapperClient.hset(redisKey, SMS_AND_EMAIL, FAILURE);
                logger.error(MessageFormat.format("[标的放款]:放款短信邮件通知失败 (loanId = {0})", String.valueOf(loanId)), e);
                return false;
            }
        } else {
            logger.info(MessageFormat.format("[标的放款]:重复发送放款短信邮件通知,标的ID : {0}", String.valueOf(loanId)));
        }
        return true;
    }

    private void notifyInvestorsLoanOutSuccessfulBySMS(List<InvestModel> investModels) {
        for (InvestModel investModel : investModels) {
            UserModel userModel = userMapper.findByLoginName(investModel.getLoginName());
            LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
            InvestNotifyInfo notifyInfo = new InvestNotifyInfo(investModel, loanModel, userModel);
            InvestSmsNotifyDto dto = new InvestSmsNotifyDto();
            dto.setLoanName(notifyInfo.getLoanName());
            dto.setMobile(notifyInfo.getMobile());
            dto.setAmount(AmountConverter.convertCentToString(notifyInfo.getAmount()));
            smsWrapperClient.sendInvestNotify(dto);
        }
    }
}
