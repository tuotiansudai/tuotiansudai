package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.LoanOutMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.paywrapper.coupon.service.CouponRepayService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.extrarate.service.LoanOutInvestCalculationService;
import com.tuotiansudai.paywrapper.service.LoanService;
import com.tuotiansudai.paywrapper.service.RepayGeneratorService;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Component
public class LoanOutSuccessGenerateRepayMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessGenerateRepayMessageConsumer.class);


    @Autowired
    private RepayGeneratorService repayGeneratorService;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private CouponRepayService couponRepayService;

    @Autowired
    private LoanOutInvestCalculationService investExtraRateService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_GenerateRepay;
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

            logger.info("[MQ] ready to consume message: generateRepay is execute, loanId:{0}", loanId);
            try {
                repayGeneratorService.generateRepay(loanId);
            } catch (PayException e) {
                fatalSmsList.add("生成标的回款计划失败");
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess generateRepay is fail. loanId:{0}", String.valueOf(loanId)));
            }

            logger.info(MessageFormat.format("[MQ] generateCouponRepay is execute , (loanId : {0}) ", String.valueOf(loanId)));
            try {
                couponRepayService.generateCouponRepay(loanId);
            } catch (Exception e) {
                fatalSmsList.add("生成优惠券回款计划失败");
                logger.error(MessageFormat.format("loan out : generate coupon payment fail, (loanId : {0})", String.valueOf(loanId)), e);
                return;
            }

            logger.info("[MQ] ready to consume message: rateIncreases is execute, loanId:{0}", loanId);
            if (!investExtraRateService.rateIncreases(loanId)) {
                fatalSmsList.add("阶梯加息错误");
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess rateIncreases is fail. loanId:{0}", String.valueOf(loanId)));
            }


            if(CollectionUtils.isNotEmpty(fatalSmsList)){
                fatalSmsList.add(MessageFormat.format("标的ID:{0}",loanId));
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(Joiner.on(",").join(fatalSmsList)));
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess_GenerateRepay fail sms sending. loanId:{0}", String.valueOf(loanId)));
            }

            logger.info("[MQ] consume LoanOutSuccess_GenerateRepay success.");
        }
    }
}
