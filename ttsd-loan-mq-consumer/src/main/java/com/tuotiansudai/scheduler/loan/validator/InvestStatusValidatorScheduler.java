package com.tuotiansudai.scheduler.loan.validator;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
public class InvestStatusValidatorScheduler {

    private final static String INVEST_CHECK_QUEUE_KEY = "invest:check:queue";

    private final static Logger logger = Logger.getLogger(InvestStatusValidatorScheduler.class);

    private final static RedisWrapperClient client = RedisWrapperClient.getInstance();

    private final PayWrapperClient payWrapperClient;

    @Autowired
    public InvestStatusValidatorScheduler(PayWrapperClient payWrapperClient) {
        this.payWrapperClient = payWrapperClient;
    }

    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 60, zone = "Asia/Shanghai")
    public void run() {
        logger.info("[Invest Status Validator Scheduler] starting...");

        List<String> unsureOrderIds = Lists.newArrayList();

        String orderId;

        do {
            orderId = client.rpop(INVEST_CHECK_QUEUE_KEY);

            if (orderId != null) {
                try {
                    BaseDto<PayDataDto> dto = payWrapperClient.investStatusValidate(MessageFormat.format("/transaction-status/invest/{0}", orderId));
                    logger.info(MessageFormat.format("[Invest Status Validator Scheduler] invest {0} status is {1}",
                            orderId,
                            dto.getData().getExtraValues().getOrDefault("transferStatus", "")));

                    if (!dto.getData().getStatus()) {
                        unsureOrderIds.add(orderId);
                    }
                } catch (Exception e) {
                    unsureOrderIds.add(orderId);
                    logger.warn(e.getLocalizedMessage(), e);
                }
            }
        } while (orderId != null);

        for (String unsureOrderId : unsureOrderIds) {
            client.lpush(INVEST_CHECK_QUEUE_KEY, unsureOrderId);
        }
    }
}
